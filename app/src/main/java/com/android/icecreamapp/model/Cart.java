package com.android.icecreamapp.model;

import java.util.ArrayList;

public class Cart {
//    public static ArrayList<Cart> arrayCart = new ArrayList<>();
    public static int ID = 0;

    public int id;
    public static ArrayList<OrderLine> orderLinesList = new ArrayList<>();
    public static double totalPrice = 0;

    public Cart() {
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
