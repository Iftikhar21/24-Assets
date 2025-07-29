package com.example.application.model;


import java.time.LocalDateTime;

// Asset data class
public class Asset {
    private String id;
    private LocalDateTime takeTime;
    private LocalDateTime returnTime;
    private String status;
    private String pin;
    private String note;
    private Location location;
    private Products[] products;
    private String role;
    private String name;
    private String className;
    private Integer quantity;

    public Asset(String id, LocalDateTime takeTime, LocalDateTime returnTime, String status, String pin, String note, Location location, Products[] products, String role, String name, String className) {
        this.id = id;
        this.takeTime = takeTime;
        this.returnTime = returnTime;
        this.status = status;
        this.pin = pin;
        this.note = note;
        this.location = location;
        this.products = products;
        this.role = role;
        this.name = name;
        this.className = className;
    }

    public Asset() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(LocalDateTime takeTime) {
        this.takeTime = takeTime;
    }

    public LocalDateTime getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(LocalDateTime returnTime) {
        this.returnTime = returnTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Products[] getProducts() {
        return products;
    }

    public void setProducts(Products[] products) {
        this.products = products;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
