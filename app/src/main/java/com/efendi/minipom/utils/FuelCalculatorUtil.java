package com.efendi.minipom.utils;

public class FuelCalculatorUtil {
    private final double pricePerLiter;

    public FuelCalculatorUtil(double pricePerLiter) {
        this.pricePerLiter = pricePerLiter;
    }

    public double calculateLiters(double purchaseAmount) {
//        return purchaseAmount / pricePerLiter;
        double result = purchaseAmount / pricePerLiter;
        result = Math.round(result * 100.0) / 100.0;
        return result;
    }

}
