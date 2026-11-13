package com.mycompany.librarymanagementsystem.ui;

import com.mycompany.librarymanagementsystem.dao.MemberDAO;
import com.mycompany.librarymanagementsystem.model.Member;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class MemberManagement extends JPanel {

    // Dashboard theme colors
    private final Color NAVY = new Color(17, 48, 85);
    private final Color BLUE = new Color(35, 125, 235);
    private final Color BG = new Color(245, 249, 255);
    private final Color TEXT = new Color(20, 48, 91);

    // Form fields
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtAddress;

    // Search field
    private JTextField txtSearch;

    // Table
    private JTable memberTable;
    private DefaultTableModel tableModel;

    // DAO
    private MemberDAO memberDAO;

    // Selected member ID
    private int selectedMemberId = -1;

    public MemberManagement() {

        memberDAO = new MemberDAO();

        setBackground(BG);
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Main heading
        JLabel heading = new JLabel("Member Management");
        heading.setFont(new Font("SansSerif", Font.BOLD, 27));
        heading.setForeground(TEXT);

        JLabel subtitle = new JLabel(
                "Register, search and manage library members."
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(75, 112, 165));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        titlePanel.add(heading);
        titlePanel.add(Box.createVerticalStrut(7));
        titlePanel.add(subtitle);

        // Form and table
        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.setOpaque(false);

        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(createMemberForm(), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadMembers();
    }

    // ---------- MEMBER FORM ----------

    private JPanel createMemberForm() {

        JPanel form = new JPanel(new BorderLayout(10, 15));
        form.setBackground(Color.WHITE);

        form.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 232, 248)
                        ),
                        new EmptyBorder(18, 20, 18, 20)
                )
        );

        JLabel formTitle = new JLabel("Member Details");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        formTitle.setForeground(TEXT);

        JPanel fields = new JPanel(new GridLayout(2, 4, 15, 12));
        fields.setOpaque(false);

        txtName = createTextField();
        txtEmail = createTextField();
        txtPhone = createTextField();
        txtAddress = createTextField();

        addFormField(fields, "Full Name", txtName);
        addFormField(fields, "Email", txtEmail);
        addFormField(fields, "Phone", txtPhone);
        addFormField(fields, "Address", txtAddress);

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 25)
        );
        buttons.setOpaque(false);

        JButton btnAdd = createButton("Add Member", BLUE);
        JButton btnUpdate = createButton(
                "Update", new Color(16, 145, 105)
        );
        JButton btnDelete = createButton(
                "Delete", new Color(220, 65, 75)
        );
        JButton btnClear = createButton(
                "Clear", new Color(100, 116, 139)
        );

        btnAdd.addActionListener(e -> addMember());
        btnUpdate.addActionListener(e -> updateMember());
        btnDelete.addActionListener(e -> deleteMember());
        btnClear.addActionListener(e -> clearFields());

        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnClear);

        JPanel formBottom = new JPanel(new BorderLayout());
        formBottom.setOpaque(false);
        formBottom.add(fields, BorderLayout.CENTER);
        formBottom.add(buttons, BorderLayout.SOUTH);

        form.add(formTitle, BorderLayout.NORTH);
        form.add(formBottom, BorderLayout.CENTER);

        return form;
    }

    // ---------- TABLE PANEL ----------

    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 232, 248)
                        ),
                        new EmptyBorder(15, 18, 15, 18)
                )
        );

        // Table heading
        JLabel tableTitle = new JLabel("Registered Members");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        tableTitle.setForeground(TEXT);

        // Search section
        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        searchPanel.setOpaque(false);

        txtSearch = createTextField();
        txtSearch.setPreferredSize(new Dimension(230, 36));

        JButton btnSearch = createButton("Search", BLUE);
        JButton btnShowAll = createButton(
                "Show All", NAVY
        );

        btnSearch.addActionListener(e -> searchMembers());
        btnShowAll.addActionListener(e -> {
            txtSearch.setText("");
            loadMembers();
        });

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setOpaque(false);
        tableHeader.add(tableTitle, BorderLayout.WEST);
        tableHeader.add(searchPanel, BorderLayout.EAST);

        // Table columns
        String[] columns = {
            "ID",
            "Full Name",
            "Email",
            "Phone",
            "Address",
            "Registration Date"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        memberTable = new JTable(tableModel);

        memberTable.setRowHeight(38);
        memberTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        memberTable.setForeground(TEXT);
        memberTable.setGridColor(new Color(230, 238, 248));
        memberTable.setSelectionBackground(
                new Color(220, 235, 255)
        );
        memberTable.setSelectionForeground(TEXT);
        memberTable.setShowVerticalLines(false);

        memberTable.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );
        memberTable.getTableHeader().setBackground(
                new Color(235, 243, 253)
        );
        memberTable.getTableHeader().setForeground(TEXT);
        memberTable.getTableHeader().setPreferredSize(
                new Dimension(0, 38)
        );

        // Select a row to load its details into the form
        memberTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && memberTable.getSelectedRow() != -1) {

                int row = memberTable.getSelectedRow();

                selectedMemberId = Integer.parseInt(
                        tableModel.getValueAt(row, 0).toString()
                );

                txtName.setText(
                        tableModel.getValueAt(row, 1).toString()
                );

                txtEmail.setText(
                        tableModel.getValueAt(row, 2) == null
                        ? "" : tableModel.getValueAt(row, 2).toString()
                );

                txtPhone.setText(
                        tableModel.getValueAt(row, 3) == null
                        ? "" : tableModel.getValueAt(row, 3).toString()
                );

                txtAddress.setText(
                        tableModel.getValueAt(row, 4) == null
                        ? "" : tableModel.getValueAt(row, 4).toString()
                );
            }
        });

        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(tableHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ---------- REUSABLE COMPONENTS ----------

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(180, 36));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(210, 225, 243)
                        ),
                        new EmptyBorder(7, 9, 7, 9)
                )
        );

        return field;
    }

    private void addFormField(
            JPanel panel,
            String labelText,
            JTextField field
    ) {

        JPanel fieldPanel = new JPanel(new BorderLayout(0, 6));
        fieldPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXT);

        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        panel.add(fieldPanel);
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(115, 37));

        return button;
    }

    // ---------- ADD MEMBER ----------

    private void addMember() {

        if (!validateFields()) {
            return;
        }

        Member member = getMemberFromFields();

        // Automatically set today's registration date
        member.setRegistrationDate(
                new Date(System.currentTimeMillis())
        );

        try {

            if (memberDAO.addMember(member)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Member registered successfully!"
                );

                loadMembers();
                clearFields();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to add member:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ---------- UPDATE MEMBER ----------

    private void updateMember() {

        if (selectedMemberId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a member from the table."
            );

            return;
        }

        if (!validateFields()) {
            return;
        }

        Member member = getMemberFromFields();
        member.setMemberId(selectedMemberId);

        try {

            if (memberDAO.updateMember(member)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Member updated successfully!"
                );

                loadMembers();
                clearFields();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update member:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ---------- DELETE MEMBER ----------

    private void deleteMember() {

        if (selectedMemberId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a member to delete."
            );

            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this member?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            if (memberDAO.deleteMember(selectedMemberId)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Member deleted successfully!"
                );

                loadMembers();
                clearFields();
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete member:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ---------- LOAD MEMBERS ----------

    private void loadMembers() {

        try {

            List<Member> members = memberDAO.getAllMembers();

            displayMembers(members);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load members:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ---------- SEARCH MEMBERS ----------

    private void searchMembers() {

        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadMembers();
            return;
        }

        try {

            List<Member> members =
                    memberDAO.searchMembers(keyword);

            displayMembers(members);

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Search failed:\n" + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ---------- DISPLAY MEMBERS ----------

    private void displayMembers(List<Member> members) {

        tableModel.setRowCount(0);

        for (Member member : members) {

            tableModel.addRow(new Object[]{
                member.getMemberId(),
                member.getFullName(),
                member.getEmail(),
                member.getPhone(),
                member.getAddress(),
                member.getRegistrationDate()
            });
        }
    }

    // ---------- VALIDATE FIELDS ----------

    private boolean validateFields() {

        if (txtName.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the member's full name."
            );

            txtName.requestFocus();
            return false;
        }

        String email = txtEmail.getText().trim();

        if (!email.isEmpty()
                && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address."
            );

            txtEmail.requestFocus();
            return false;
        }

        String phone = txtPhone.getText().trim();

        if (!phone.isEmpty()
                && !phone.matches("[0-9+() -]+")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid phone number."
            );

            txtPhone.requestFocus();
            return false;
        }

        return true;
    }

    // ---------- GET MEMBER FROM FORM ----------

    private Member getMemberFromFields() {

        Member member = new Member();

        member.setFullName(txtName.getText().trim());
        member.setEmail(emptyToNull(txtEmail.getText()));
        member.setPhone(emptyToNull(txtPhone.getText()));
        member.setAddress(emptyToNull(txtAddress.getText()));

        return member;
    }

    // Convert empty optional fields to SQL NULL
    private String emptyToNull(String value) {

        value = value.trim();

        return value.isEmpty() ? null : value;
    }

    // ---------- CLEAR FIELDS ----------

    private void clearFields() {

        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");

        selectedMemberId = -1;

        memberTable.clearSelection();
        txtName.requestFocus();
    }
}