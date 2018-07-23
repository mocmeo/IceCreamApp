package com.android.icecreamapp.model;

public class IceCream {

    private int id;
    private String description;
    private String name;
    private double price;
    private String image;
    private int idType;

    public IceCream() {
    }

    public IceCream(int id, String description, String name, double price, String image, int idType) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.price = price;
        this.image = image;
        this.idType = idType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public int getId() {

        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getIdType() {
        return idType;
    }
}
