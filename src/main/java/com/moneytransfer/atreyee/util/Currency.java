package com.moneytransfer.atreyee.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Currency {
    private static Map<String, HashMap<String, Double>> currencyConversionRates;

    private static void loadCurrencyValues() {

        currencyConversionRates = new HashMap<String, HashMap<String, Double>>();
        //rates wrt euro
        HashMap<String, Double> euroRates = new HashMap<String, Double>();
        euroRates.put("USD", 1.1);
        euroRates.put("GBP", 0.86);
        euroRates.put("INR", 78.9);


        currencyConversionRates.put("EUR", euroRates);

        //rates wrt dollar
        HashMap<String, Double> usDollarRates = new HashMap<String, Double>();

        usDollarRates.put("EUR", 0.90);
        usDollarRates.put("GBP", 0.78);
        usDollarRates.put("INR", 71.0);


        currencyConversionRates.put("USD", usDollarRates);

        //rates wrt inr
        HashMap<String, Double> indianRupeeRates = new HashMap<String, Double>();
        indianRupeeRates.put("USD", 0.014);
        indianRupeeRates.put("EUR", 0.013);
        indianRupeeRates.put("GBP", 0.011);


        currencyConversionRates.put("INR", indianRupeeRates);

        //rates wrt GBP
        HashMap<String, Double> gbpRates = new HashMap<String, Double>();
        gbpRates.put("USD", 1.29);
        gbpRates.put("EUR", 1.16);
        gbpRates.put("INR", 91.43);


        currencyConversionRates.put("GBP", gbpRates);
    }

    public static Map<String, HashMap<String, Double>> getCurrentConversionValues() {
        if (currencyConversionRates == null) {
            loadCurrencyValues();
        }
        return new HashMap<String, HashMap<String, Double>>(currencyConversionRates);
    }

    // Convert a currency to another
    public static Double convert(Double amount, Double exchangeValue) {
        Double price;
        price = amount * exchangeValue;
        price = Math.round(price * 100d) / 100d;

        return price;
    }

}
