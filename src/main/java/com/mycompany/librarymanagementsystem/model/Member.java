/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.librarymanagementsystem.model;

import java.sql.Date;

/**
 *
 * @author ACER
 */
public class Member {
    
    private int memberId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Date registrationDate;
    
    public Member(){
    }

    public Member(int memeberId, String fullName, String email, String phone, String address, Date registrationDate) {
        this.memberId = memeberId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = registrationDate;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memeberId) {
        this.memberId = memeberId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    
}
