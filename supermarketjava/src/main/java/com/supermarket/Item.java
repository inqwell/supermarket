package com.supermarket;

public class Item
{
  public final String name;
  public final double price;
  public final Offer offer;

  public Item(String name, double price, Offer offer){
    this.name = name;
    this.price = price;
    this.offer = offer;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString()
  {
    return name + " " + price + " " + offer.offerName;
  }
}
