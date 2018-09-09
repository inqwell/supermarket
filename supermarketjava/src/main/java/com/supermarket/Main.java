package com.supermarket;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

  // What we sell
  static Map<String, Item> inventory = new HashMap<>();

  // This list represents the items bought. The idea is the items could be
  // in any order and total any number. Once through the "checkout" and we
  // know what they all are, we can apply the offers.
  static List<Item> shoppingItems = new ArrayList<>();

  static {
    //           "barcode" :)
    //               |
    //               v
    inventory.put("Apple", new Item("Apple", 0.2, Offer.BOGOF));
    inventory.put("Orange", new Item("Orange", 0.5, Offer.NONE));
    inventory.put("Watermelon", new Item("Watermelon", 0.8, Offer.THREEFORTWO));

    // As per the spec - 4 apples; 3 oranges; 6 watermellons (in some random scanning order)
    shoppingItems.add(inventory.get("Watermelon"));
    shoppingItems.add(inventory.get("Orange"));
    shoppingItems.add(inventory.get("Watermelon"));
    shoppingItems.add(inventory.get("Apple"));
    shoppingItems.add(inventory.get("Watermelon"));
    shoppingItems.add(inventory.get("Watermelon"));
    shoppingItems.add(inventory.get("Apple"));
    shoppingItems.add(inventory.get("Orange"));
    shoppingItems.add(inventory.get("Watermelon"));
    shoppingItems.add(inventory.get("Watermelon"));
    shoppingItems.add(inventory.get("Apple"));
    shoppingItems.add(inventory.get("Apple"));
    shoppingItems.add(inventory.get("Orange"));

  }

  public static Map<String, Long> checkoutQtys(List<Item> items) {
    // As we checkout, group items, counting them, so we can apply any offers
    return items.stream()
            .collect(Collectors.groupingBy(Item::getName, LinkedHashMap<String, Long> ::new, Collectors.counting()));

  }

  public static List<ReceiptEntry> generateReceipt(Map<String, Long> qtys) {
    // With the grouped "barcodes" and associated quantities, work out any
    // offer discounts and create the receipt.
    return qtys.entrySet().stream()
            .map(entry -> {Item item = inventory.get(entry.getKey());
                           return new ReceiptEntry(item,
                                                   entry.getValue(),
                                                   item.offer.offerF.applyOffer(entry.getValue(),item.price));})
            .collect(Collectors.toList());
  }

  public static double calcTotal(List<ReceiptEntry> items) {
    // Calculate the receipt total
    return items.stream()
            .mapToDouble(entry -> entry.price + entry.offer).sum();
  }

  public static void printReceipt(List<ReceiptEntry> items) {
    // Print the receipt, and the total amount inc offers
    System.out.println("\nSUPERMARKET plc\n");

    // This is a bit hacky - the idea is to process the items sequence only
    // once, printing each item and making the total as we go.
    double total = items.stream()
            .mapToDouble(entry ->
            {
              System.out.printf("%-12s x%d    %.2f\n", entry.item.name, entry.qty, entry.price);
              if (entry.offer != 0)
                System.out.printf("           %-15s %.2f\n", entry.item.offer.offerName, entry.offer);
              return entry.price + entry.offer;
            })
            .sum();
    System.out.printf("\nTOTAL     %.2f\n", total);
    System.out.println("\nThank you for shopping with us");
  }

  public static void main(String[] args)
  {
    Map<String, Long> checkout = checkoutQtys(shoppingItems);

    List<ReceiptEntry> receipt = generateReceipt(checkout);

    printReceipt(receipt);

  }
}
