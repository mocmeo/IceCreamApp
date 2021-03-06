package com.android.icecreamapp.firebase;


public class FirebaseUserEntity {

    private String uId;

    private String email;

    private String name;

    private String phone;

    private String address;

    private String imageUrl;

    private String coverUrl;

    public FirebaseUserEntity() {

    }

    public FirebaseUserEntity(String uId, String name) {
        this.uId = uId;
        this.name = name;
    }

    public FirebaseUserEntity(String uId, String email, String name, String phone, String address, String imageUrl, String coverUrl) {
        this.uId = uId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.imageUrl = imageUrl;
        this.coverUrl = coverUrl;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getuId() {

        return uId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
}
