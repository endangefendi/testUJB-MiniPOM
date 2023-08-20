package com.efendi.minipom.utils;


import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Constants {

    public static String rupiahFormatter(String harga){
        NumberFormat formatter = new DecimalFormat("###,###,###");
        double myNumber = Double.parseDouble(harga);
        String finalString = formatter.format(myNumber);
        return "Rp. "+finalString;
    }
    public static String decimalFormatter(String harga){
        NumberFormat formatter = new DecimalFormat("###,###,###");
        if (harga.contains(",")){
            harga = harga.replace(",", "");
        }
        double myNumber = Double.parseDouble(harga);
        return formatter.format(myNumber);
    }

    public static String backString(String nominal){
        if (nominal.contains("Rp. ")){
            nominal = nominal.replace("Rp. ", "");
        }
        if (nominal.contains(",")){
            nominal = nominal.replace(",", "");
        }
        return nominal;
    }


    public static double doubleFormater(double value) {
//        return purchaseAmount / pricePerLiter;
        return Math.round(value * 100.0) / 100.0;
//        return value;
    }

}
