/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.librarymanagementsystem.model;

/**
 *
 * @author ACER
 */
public class LibrarySettings {

    private String libraryName;
    private String contactEmail;
    private String contactPhone;
    private int borrowingPeriodDays;

    public LibrarySettings() {
    }

    public LibrarySettings(String libraryName,
            String contactEmail,
            String contactPhone,
            int borrowingPeriodDays) {

        this.libraryName = libraryName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.borrowingPeriodDays = borrowingPeriodDays;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public int getBorrowingPeriodDays() {
        return borrowingPeriodDays;
    }

    public void setBorrowingPeriodDays(int borrowingPeriodDays) {
        this.borrowingPeriodDays = borrowingPeriodDays;
    }
}

