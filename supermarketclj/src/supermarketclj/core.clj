(ns supermarketclj.core
  (:gen-class :main true))

(defn make-item
  "An item is a map:
   - its name
   - its price
   - the offer (may be NONE)"
  [name price offer]
  {:name name
   :price price
   :offer offer})

(defn make-offer
  "An offer is a map:
   - name
   - a function of two arguments: quantity and unit price, returning
     the (-ve) discount amount to apply."
  [name offer-fn]
  {:name name
   :offer-fn offer-fn})

(defn make-receipt-entry
  "Make a receipt entry
   - item the item
   - qty the number of items
   Returns a map of
   - the item
   - quantity bought
   - the total price before discount
   - the (-ve) discount to be applied"
  [item qty]
  {:item item
   :qty qty
   :price (* qty (:price item))
   :offer ((get-in item [:offer :offer-fn]) qty (:price item))})

(def inventory {:apple (make-item "Apple" 0.2 (make-offer "BOGOF"
                                                          (fn [qty price]
                                                            (-> (int (/ qty 2))
                                                                (+ (mod qty 2))
                                                                (- qty)
                                                                (* price)))))
                :orange (make-item "Orange" 0.5 (make-offer "NONE" (constantly 0)))
                :watermelon (make-item "Watermelon" 0.8 (make-offer "Three for Two"
                                                                    (fn [qty price] (-> (int (/ qty 3))
                                                                            (* 2)
                                                                            (+ (mod qty 3))
                                                                            (- qty)
                                                                            (* price)))))})

(def shopping-items
  [(:watermelon inventory)
   (:orange inventory)
   (:watermelon inventory)
   (:apple inventory)
   (:watermelon inventory)
   (:watermelon inventory)
   (:apple inventory)
   (:orange inventory)
   (:watermelon inventory)
   (:watermelon inventory)
   (:apple inventory)
   (:apple inventory)
   (:orange inventory)])

(defn make-basket
  "Create the basket from an arbitrary list of items.
  Groups the items together to determine how many of each there are
  then makes a "
  [shopping-items]
  (->> (group-by :name shopping-items)
       (map (fn [[name item-list]]
              (make-receipt-entry (first item-list)
                                  (count item-list))))))

(defn print-receipt-entry
  [entry]
  (print (format "%-12s x%d    %.2f\n"
                 (get-in entry [:item :name])
                 (:qty entry)
                 (:price entry)))
  (when-not (zero? (:offer entry))
    (print (format "           %-15s %.2f\n"
                   (get-in entry [:item :offer :name]) (:offer entry)))))

(defn print-totals
  [[total discount-total]]
  (print (format "\nTOTAL     %.2f\n" total))
  (when-not (zero? discount-total)
    (do
      (print (format "\nOFFERS   %.2f\n" discount-total))
      (print (format "\n          %.2f\n" (+ total discount-total)))))
    (println "\nThank you for shopping with us"))


(defn print-receipt
  [basket]
  (println "\nSUPERMARKET plc\n")
  (print-totals
    (loop [total          0
           total-discount 0
           basket         basket]
      (if (seq basket)
        (let [entry (first basket)]
          (print-receipt-entry entry)
          (recur (+ total (:price entry))
                 (+ total-discount (:offer entry))
                 (rest basket)))
        [total total-discount]))))

; Alternative version (bit less functional)
(defn print-receipt-1
  []
  (let [total (atom 0)]
    (println "\nSUPERMARKET plc\n")
    (doseq [receipt-item (make-basket shopping-items)]
      (print (format "%-12s x%d    %.2f\n"
              (get-in receipt-item [:item :name])
              (:qty receipt-item)
              (:price receipt-item)))
      (when-not (zero? (:offer receipt-item))
        (print (format "           %-15s %.2f\n"
                       (get-in receipt-item [:item :offer :name]) (:offer receipt-item))))
      (swap! total + (:price receipt-item) (:offer receipt-item)))
    (print (format "\nTOTAL     %.2f\n" @total))
    (println "\nThank you for shopping with us")))


(defn -main [& args]
  (print-receipt (make-basket shopping-items)))
