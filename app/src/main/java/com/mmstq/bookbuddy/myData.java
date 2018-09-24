package com.mmstq.bookbuddy;

public class myData {
    private String book, address,description , semester,price , phone,time,category;


    public myData(){}

    public myData(String book,String address,String description,String semester,String price,String phone,String time, String category) {
        this.book = book;
        this.address = address;
        this.description = description;
        this.semester = semester;
        this.price = price;
        this.phone = phone;
        this.time = time;
        this.category= category;

    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
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
}
