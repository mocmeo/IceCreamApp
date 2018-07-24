package com.android.icecreamapp.model;

public class OrderLine {
    private static int ID = 0;

    private int id;
    private Product product;
    private int quantity;
    private long totalPrice;

    private OrderLine(int id, Product product, int quantity, long totalPrice) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public static OrderLine generateOrderLine(Product product, int qty, long totalPrice) {
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

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
