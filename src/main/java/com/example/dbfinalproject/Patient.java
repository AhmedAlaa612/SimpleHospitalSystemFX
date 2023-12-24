package com.example.dbfinalproject;

import java.time.LocalDate;

public class Patient {
    private int id;
    private String fname;
    private String lname;
    private String phone;
    private String gender;
    private LocalDate birthdate;
    private int age;

    public Patient(int id, String fname, String lname, String phone, String gender, LocalDate birthdate, int age) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.gender = gender;
        this.birthdate = birthdate;
        this.age = age;
    }
    @Override
    public String toString() {
        return fname + " " + lname;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }
}
