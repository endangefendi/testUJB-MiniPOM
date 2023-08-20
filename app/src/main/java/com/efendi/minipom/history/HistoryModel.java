package com.efendi.minipom.history;

public class HistoryModel {
    String id;
    String amount;
    String time;
    String type;
    String amount_liter;
    String price_per_liter;

    public HistoryModel(){

    }

    public HistoryModel(String id, String amount, String time, String type, String amount_liter, String price_per_liter){
        this.id = id;
        this.amount = amount;
        this.amount_liter = amount_liter;
        this.time = time;
        this.type = type;
        this.price_per_liter = price_per_liter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount_liter() {
        return amount_liter;
    }

    public void setAmount_liter(String amount_liter) {
        this.amount_liter = amount_liter;
    }

    public String getPrice_per_liter() {
        return price_per_liter;
    }

    public void setPrice_per_liter(String price_per_liter) {
        this.price_per_liter = price_per_liter;
    }
}
