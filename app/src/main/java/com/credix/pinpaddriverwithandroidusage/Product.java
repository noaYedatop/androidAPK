package com.credix.pinpaddriverwithandroidusage;
public class Product {
    private String name;
    private double quantity;
    private int index;
    private double price;
    private double amount;
    private double anacha;
    private double discount;
    private String discountName;
    private int videoResourceId; // Resource ID for the video


    public Product(String name, double quantity, double price,int index,double anacha,double discount,String discountName) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.anacha = anacha;
        this.discount = discount;
        this.discountName = discountName;
        this.index = index;
        this.videoResourceId = videoResourceId;

    }

    public String getName() {
        return name;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public int getIndex() {
        return index;
    }
    public double getPrice() { return price;}
    public double getAnacha() { return anacha; }
    public double getDiscount() { return discount; }
    public String getDiscountName() { return discountName; }

    public void setPrice(double price) {
        this.price = price;
    }
    public void setAnacha(double anacha) {
        this.anacha = anacha;
    }
    public void setDiscount(double discount) {
        this.discount = discount;
    }
    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }
    public int getVideoResourceId() {
        return videoResourceId;
    }
}