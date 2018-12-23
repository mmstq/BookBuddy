package com.mmstq.bookbuddy;

import android.support.annotation.Keep;

@Keep
public class myData {
    private String book, address,description , semester,price , phone,category, image;
    private long time;

    public myData(){}

    public myData(String book,String address,String description,String semester,String price,String phone,long time, String category,String image) {
        this.book = book;
        this.address = address;
        this.description = description;
        this.semester = semester;
        this.price = price;
        this.phone = phone;
        this.time = time;
        this.category= category;
        this.image=image;

    }

    public void setImage(String image) {
        this.image = image;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public void setBook(String book) {
        this.book = book;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setSemester(String semester) {
        this.semester = semester;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBook() {
        return book;
    }
    public String getAddress() {
        return address;
    }
    public String getDescription() {
        return description;
    }
    public String getSemester() {
        return semester;
    }
    public String getPrice() {
        return price;
    }
    public String getPhone() {
        return phone;
    }
    public String getCategory() {
        return category;
    }
    public String getImage() {
        return image;
    }
    public long getTime() {
        return time;
    }
}
