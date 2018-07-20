package com.android.icecreamapp.model;

import java.io.Serializable;

public class Product implements Serializable {
    public static int ID = 0;

    public int id;
    public String name;
    public double price;
    public String image;
    public String description;
    public int idType;

    public Product() {
    }

    private Product(int id, String name, double price, String image, String description, int idType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.idType = idType;
    }

    public static Product generateProduct(String name, double price, String image, String description, int idType) {
        return new Product(++ID, name, price, image, description, idType);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }
}
