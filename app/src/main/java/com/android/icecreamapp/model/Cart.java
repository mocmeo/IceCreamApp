package com.android.icecreamapp.model;

import java.util.ArrayList;

public class Cart {

    public static String id;
    public static ArrayList<OrderLine> orderLinesList = new ArrayList<>();
    public static String uId;
    public static boolean statusConfirm;

    public static int countIcecream() {
        int qty = 0;
        for (OrderLine orderline : orderLinesList) {
            qty += orderline.getQuantity();
        }
        return qty;
    }

    public Cart() {
    }

}
