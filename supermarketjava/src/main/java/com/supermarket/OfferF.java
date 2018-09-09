package com.supermarket;

@FunctionalInterface
public interface OfferF
{
  public double applyOffer(long qty, double unitPrice);
}
