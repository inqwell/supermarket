package com.supermarket;

public class ReceiptEntry
{
  public final Item item;
  public final long qty;

  // The non-discounted price
  public final double price;

  // Any offer discount. -ve or zero when no offer is in effect
  public final double offer;

  public ReceiptEntry(Item item, long qty) {
    this(item, qty, 0);
  }

  public ReceiptEntry(Item item, long qty, double offer) {
    this.item = item;
    this.qty = qty;
    this.price = qty * item.price;
    this.offer = offer;
  }

  public String toString() {
    String ret = item.name + " " + qty + " " + price;

    if (offer != 0)
      ret += " " + item.offer.offerName + " " + offer;

    return ret;
  }
}
