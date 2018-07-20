package com.android.icecreamapp.model;

public class OrderLine {
    private static int ID = 0;

    private int id;
    private Product product;
    private int quantity;
    private double totalPrice;

    private OrderLine(int id, Product product, int quantity, double totalPrice) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public static OrderLine generateOrderLine(Product product, int qty, double totalPrice) {
        return new OrderLine(++ID, product, qty, totalPrice);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
