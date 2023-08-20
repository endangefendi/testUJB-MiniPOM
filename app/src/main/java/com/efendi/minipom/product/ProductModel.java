package com.efendi.minipom.product;

public class ProductModel {

    private String product_id;
    private String name;
    private long cost;
    private String amount_liter;

    public ProductModel() {
    }

    public ProductModel(String product_id, String name, long cost, String amount_liter) {
        this.product_id = product_id;
        this.name = name;
        this.cost = cost;
        this.amount_liter = amount_liter;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getAmount_liter() {
        return amount_liter;
    }

    public void setAmount_liter(String amount_liter) {
        this.amount_liter = amount_liter;
    }
}