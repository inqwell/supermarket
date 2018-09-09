package com.supermarket;

/**
 * Name and function for an offer. The function returns the price reduction to
 * apply at checkout
 */
public enum Offer
{
  NONE ("", (long qty, double price) -> 0), // No offer
  BOGOF ("BOGOF", (long qty, double price) -> (qty / 2 + qty % 2 - qty) * price),
  THREEFORTWO ("Three for Two", (long qty, double price) -> (qty / 3 * 2 + qty % 3 - qty) * price);

  public final String offerName;
  public final OfferF offerF;

  Offer(String name, OfferF offerF) {
    this.offerName = name;
    this.offerF = offerF;
  }

  @Override
  public String toString()
  {
    return offerName;
  }
}
