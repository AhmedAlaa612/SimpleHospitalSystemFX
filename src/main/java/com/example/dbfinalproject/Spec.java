package com.example.dbfinalproject;

public class Spec {
    private String name;
    private int id;
    public Spec(int id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
