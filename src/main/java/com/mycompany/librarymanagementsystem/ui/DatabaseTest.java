/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.librarymanagementsystem.ui;

import com.mycompany.librarymanagementsystem.database.DBConnection;
import java.sql.Connection;

/**
 *
 * @author ACER
 */
public class DatabaseTest {
    public static void main(String[] args){
        try(Connection con = DBConnection.getConnection()){
            System.out.println("Database connected successfully!");
        }
        catch(Exception e){
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}
