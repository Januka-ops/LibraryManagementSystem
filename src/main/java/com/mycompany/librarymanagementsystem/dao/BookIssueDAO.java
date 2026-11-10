package com.mycompany.librarymanagementsystem.dao;

import com.mycompany.librarymanagementsystem.database.DBConnection;
import com.mycompany.librarymanagementsystem.model.BookIssue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookIssueDAO {

    // Issue a book
    public void issueBook(int bookId, int memberId,
                          Date issueDate, Date dueDate)
            throws SQLException {

        String checkBook = "SELECT available_quantity FROM books "
                         + "WHERE book_id = ? FOR UPDATE";

        String updateBook = "UPDATE books "
                          + "SET available_quantity = available_quantity - 1 "
                          + "WHERE book_id = ? AND available_quantity > 0";

        String insertIssue = "INSERT INTO book_issues "
                           + "(book_id, member_id, issue_date, due_date, status) "
                           + "VALUES (?, ?, ?, ?, 'ISSUED')";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                // Check book availability
                try (PreparedStatement ps = con.prepareStatement(checkBook)) {
                    ps.setInt(1, bookId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || rs.getInt("available_quantity") <= 0) {
                            throw new SQLException("This book is not available.");
                        }
                    }
                }

                // Decrease available quantity
                try (PreparedStatement ps = con.prepareStatement(updateBook)) {
                    ps.setInt(1, bookId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Could not update book quantity.");
                    }
                }

                // Insert issue record
                try (PreparedStatement ps = con.prepareStatement(insertIssue)) {
                    ps.setInt(1, bookId);
                    ps.setInt(2, memberId);
                    ps.setDate(3, issueDate);
                    ps.setDate(4, dueDate);
                    ps.executeUpdate();
                }

                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    // Return a book
    public void returnBook(int issueId, Date returnDate)
            throws SQLException {

        String findIssue = "SELECT book_id, status, return_date "
                         + "FROM book_issues WHERE issue_id = ? FOR UPDATE";

        String updateIssue = "UPDATE book_issues "
                           + "SET return_date = ?, status = 'RETURNED' "
                           + "WHERE issue_id = ? AND status = 'ISSUED' "
                           + "AND return_date IS NULL";

        String updateBook = "UPDATE books "
                          + "SET available_quantity = available_quantity + 1 "
                          + "WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                int bookId;

                // Find the issue record
                try (PreparedStatement ps = con.prepareStatement(findIssue)) {
                    ps.setInt(1, issueId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Issue record not found.");
                        }

                        if (!"ISSUED".equals(rs.getString("status"))
                                || rs.getDate("return_date") != null) {
                            throw new SQLException("This book has already been returned.");
                        }

                        bookId = rs.getInt("book_id");
                    }
                }

                // Update issue record
                try (PreparedStatement ps = con.prepareStatement(updateIssue)) {
                    ps.setDate(1, returnDate);
                    ps.setInt(2, issueId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Could not update return record.");
                    }
                }

                // Increase available book quantity
                try (PreparedStatement ps = con.prepareStatement(updateBook)) {
                    ps.setInt(1, bookId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException("Could not update book quantity.");
                    }
                }

                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    // Get all issue and return records
    public List<BookIssue> getAllIssues(String keyword)
            throws SQLException {

        List<BookIssue> list = new ArrayList<>();

        String sql = "SELECT bi.*, b.title, m.full_name "
                   + "FROM book_issues bi "
                   + "JOIN books b ON bi.book_id = b.book_id "
                   + "JOIN members m ON bi.member_id = m.member_id "
                   + "WHERE b.title LIKE ? OR m.full_name LIKE ? "
                   + "OR bi.status LIKE ? "
                   + "ORDER BY bi.issue_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    BookIssue issue = new BookIssue();

                    issue.setIssueId(rs.getInt("issue_id"));
                    issue.setBookId(rs.getInt("book_id"));
                    issue.setMemberId(rs.getInt("member_id"));
                    issue.setBookTitle(rs.getString("title"));
                    issue.setMemberName(rs.getString("full_name"));
                    issue.setIssueDate(rs.getDate("issue_date"));
                    issue.setDueDate(rs.getDate("due_date"));
                    issue.setReturnDate(rs.getDate("return_date"));
                    issue.setStatus(rs.getString("status"));

                    list.add(issue);
                }
            }
        }

        return list;
    }
}