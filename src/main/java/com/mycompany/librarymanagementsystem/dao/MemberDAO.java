package com.mycompany.librarymanagementsystem.dao;

import com.mycompany.librarymanagementsystem.database.DBConnection;
import com.mycompany.librarymanagementsystem.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    // Add a new member
    public boolean addMember(Member member) throws SQLException {

        String query = "INSERT INTO members "
                + "(full_name, email, phone, address, registration_date) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, member.getFullName());
            pst.setString(2, member.getEmail());
            pst.setString(3, member.getPhone());
            pst.setString(4, member.getAddress());
            pst.setDate(5, member.getRegistrationDate());

            return pst.executeUpdate() > 0;
        }
    }

    // Retrieve all members
    public List<Member> getAllMembers() throws SQLException {

        List<Member> members = new ArrayList<>();

        String query = "SELECT * FROM members ORDER BY member_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Member member = new Member();

                member.setMemberId(rs.getInt("member_id"));
                member.setFullName(rs.getString("full_name"));
                member.setEmail(rs.getString("email"));
                member.setPhone(rs.getString("phone"));
                member.setAddress(rs.getString("address"));
                member.setRegistrationDate(
                        rs.getDate("registration_date")
                );

                members.add(member);
            }
        }

        return members;
    }

    // Search members by name, email or phone
    public List<Member> searchMembers(String keyword)
            throws SQLException {

        List<Member> members = new ArrayList<>();

        String query = "SELECT * FROM members "
                + "WHERE full_name LIKE ? "
                + "OR email LIKE ? "
                + "OR phone LIKE ? "
                + "ORDER BY member_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            String searchValue = "%" + keyword + "%";

            pst.setString(1, searchValue);
            pst.setString(2, searchValue);
            pst.setString(3, searchValue);

            try (ResultSet rs = pst.executeQuery()) {

                while (rs.next()) {

                    Member member = new Member();

                    member.setMemberId(rs.getInt("member_id"));
                    member.setFullName(rs.getString("full_name"));
                    member.setEmail(rs.getString("email"));
                    member.setPhone(rs.getString("phone"));
                    member.setAddress(rs.getString("address"));
                    member.setRegistrationDate(
                            rs.getDate("registration_date")
                    );

                    members.add(member);
                }
            }
        }

        return members;
    }

    // Update an existing member
    public boolean updateMember(Member member) throws SQLException {

        String query = "UPDATE members SET "
                + "full_name = ?, email = ?, phone = ?, address = ? "
                + "WHERE member_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, member.getFullName());
            pst.setString(2, member.getEmail());
            pst.setString(3, member.getPhone());
            pst.setString(4, member.getAddress());
            pst.setInt(5, member.getMemberId());

            return pst.executeUpdate() > 0;
        }
    }

    // Delete a member
    public boolean deleteMember(int memberId) throws SQLException {

        String query = "DELETE FROM members WHERE member_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, memberId);

            return pst.executeUpdate() > 0;
        }
    }
}