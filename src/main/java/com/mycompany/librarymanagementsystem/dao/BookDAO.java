/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.librarymanagementsystem.dao;

import com.mycompany.librarymanagementsystem.database.DBConnection;
import com.mycompany.librarymanagementsystem.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // 1. Add a new book
    public boolean addBook(Book book) {

        String sql = "INSERT INTO books "
                + "(title, author, category, isbn, quantity, available_quantity) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.setString(3, book.getCategory());
            pst.setString(4, book.getIsbn());
            pst.setInt(5, book.getQuantity());
            pst.setInt(6, book.getAvailableQuantity());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Get all books
    public List<Book> getAllBooks() {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books ORDER BY book_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Book book = new Book();

                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategory(rs.getString("category"));
                book.setIsbn(rs.getString("isbn"));
                book.setQuantity(rs.getInt("quantity"));
                book.setAvailableQuantity(
                        rs.getInt("available_quantity"));

                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // 3. Search books by title, author, or ISBN
    public List<Book> searchBooks(String keyword) {

        List<Book> books = new ArrayList<>();

        String sql = "SELECT * FROM books "
                + "WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            String searchValue = "%" + keyword + "%";

            pst.setString(1, searchValue);
            pst.setString(2, searchValue);
            pst.setString(3, searchValue);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    Book book = new Book();

                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setCategory(rs.getString("category"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setQuantity(rs.getInt("quantity"));
                    book.setAvailableQuantity(
                            rs.getInt("available_quantity"));

                    books.add(book);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // 4. Update an existing book
    public boolean updateBook(Book book) {

        String sql = "UPDATE books SET title=?, author=?, "
                + "category=?, isbn=?, quantity=?, available_quantity=? "
                + "WHERE book_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, book.getTitle());
            pst.setString(2, book.getAuthor());
            pst.setString(3, book.getCategory());
            pst.setString(4, book.getIsbn());
            pst.setInt(5, book.getQuantity());
            pst.setInt(6, book.getAvailableQuantity());
            pst.setInt(7, book.getBookId());

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Delete a book
    public boolean deleteBook(int bookId) {

        String sql = "DELETE FROM books WHERE book_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, bookId);

            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
