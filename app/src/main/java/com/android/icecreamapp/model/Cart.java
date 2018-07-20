package com.android.icecreamapp.model;

import java.util.ArrayList;

public class Cart {
    public static ArrayList<Cart> arrayCart = null;
    public static int ID = 0;

    public int id;
    private ArrayList<OrderLine> orderLinesList;
    private double totalPrice;

    public Cart() {
    }

    public static Cart generateCart() {
        Cart newCart = new Cart();
        newCart.id = ++ID;
        newCart.orderLinesList = new ArrayList<>();
        newCart.totalPrice = 0;
        return newCart;
    }


    public ArrayList<OrderLine> getOrderLinesList() {
        return orderLinesList;
    }

    public void setOrderLinesList(ArrayList<OrderLine> orderLinesList) {
        this.orderLinesList = orderLinesList;
    }

    public void addToCart(Product product, int qty) {
        if (orderLinesList.size() == 0) {
            OrderLine orderLine = OrderLine.generateOrderLine(product, qty, product.price * qty);
            orderLinesList.add(orderLine);
        } else {
            for (OrderLine orderLine : orderLinesList) {
                if (product.id == orderLine.getProduct().id &&
                        product.idType == orderLine.getProduct().idType) {
                    orderLine.setQuantity(orderLine.getQuantity() + qty);
                    orderLine.setTotalPrice(orderLine.getTotalPrice() + product.price * qty);
                }
            }
        }
    }
}
