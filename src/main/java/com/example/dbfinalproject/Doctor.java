package com.example.dbfinalproject;

public class Doctor {
    private int id;
    private String fname;
    private String lname;
    private String spec;
    private int specId;
    private String phone;
    private int salary;

    public Doctor(int id, String fname, String lname, String spec, int specId, String phone, int salary){
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.spec = spec;
        this.specId = specId;
        this.phone = phone;
        this.salary = salary;
    }

    public Doctor(int id, String fname, String lname) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
    }

    @Override
    public String toString() {
        return fname + " " + lname + " - " + spec;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
