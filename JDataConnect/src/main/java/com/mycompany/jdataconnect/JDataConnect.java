package com.mycompany.jdataconnect;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class JDataConnect extends JFrame {
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField courseField;
    private JTextField ageField;
    private JTextField phoneField;
    private JTextField searchField;
    private JComboBox<String> searchFieldCombo;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JLabel statusLabel;
    private int selectedStudentId = -1;
    
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Color BG_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(222, 226, 230);

    public JDataConnect() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(BG_COLOR);
        setBackground(BG_COLOR);
        
        DatabaseManager.createTable();
        
        initializeUI();
        
        loadStudents();
    }
    
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        mainPanel.setOpaque(true);
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(20, 0));
        centerPanel.setBackground(BG_COLOR);
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = createFormPanel();
        JScrollPane formScrollPane = new JScrollPane(formPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        formScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        formScrollPane.getViewport().setBackground(CARD_COLOR);
        formScrollPane.setPreferredSize(new Dimension(400, 0));
        formScrollPane.setMinimumSize(new Dimension(400, 0));
        formScrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
        centerPanel.add(formScrollPane, BorderLayout.WEST);
        
        JPanel tablePanel = createTablePanel();
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        headerPanel.setOpaque(true);
        
        JLabel titleLabel = new JLabel("Student Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel countLabel = new JLabel("Total Students: 0");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        countLabel.setForeground(Color.WHITE);
        countLabel.setBorder(new EmptyBorder(5, 15, 5, 15));
        countLabel.setOpaque(true);
        countLabel.setBackground(new Color(255, 255, 255, 30));
        countLabel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 255, 255, 50), 1, true),
            new EmptyBorder(5, 15, 5, 15)
        ));
        headerPanel.add(countLabel, BorderLayout.EAST);
        
        SwingUtilities.invokeLater(() -> {
            int count = tableModel.getRowCount();
            countLabel.setText("Total Students: " + count);
        });
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension pref = super.getPreferredSize();
                pref.width = 400;
                int totalHeight = 0;
                for (Component comp : getComponents()) {
                    if (comp.isVisible()) {
                        Dimension compPref = comp.getPreferredSize();
                        totalHeight += compPref.height;
                    }
                }
                Insets insets = getInsets();
                totalHeight += insets.top + insets.bottom;
                pref.height = Math.max(pref.height, totalHeight);
                return pref;
            }
        };
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));
        formPanel.setMinimumSize(new Dimension(400, 0));
        formPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        
        JLabel sectionTitle = new JLabel("Student Information");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(PRIMARY_COLOR);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        formPanel.add(sectionTitle);
        
        formPanel.add(createFormField("Full Name", nameField = createTextField()));
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createFormField("Email Address", emailField = createTextField()));
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createFormField("Course", courseField = createTextField()));
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createFormField("Age", ageField = createTextField()));
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createFormField("Phone Number", phoneField = createPhoneField()));
        formPanel.add(Box.createVerticalStrut(20));
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 102));
        buttonPanel.setPreferredSize(new Dimension(0, 102));
        
        addButton = createStyledButton("Add Student", SUCCESS_COLOR);
        addButton.addActionListener(e -> addStudent());
        buttonPanel.add(addButton);
        
        updateButton = createStyledButton("Update", PRIMARY_COLOR);
        updateButton.addActionListener(e -> updateStudent());
        buttonPanel.add(updateButton);
        
        deleteButton = createStyledButton("Delete", DANGER_COLOR);
        deleteButton.addActionListener(e -> deleteStudent());
        buttonPanel.add(deleteButton);
        
        clearButton = createStyledButton("Clear", SECONDARY_COLOR);
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel);
        
        formPanel.add(Box.createVerticalStrut(10));
        
        return formPanel;
    }
    
    private JPanel createFormField(String labelText, JTextField field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(0, 8));
        fieldPanel.setBackground(CARD_COLOR);
        fieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(73, 80, 87));
        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);
        
        return fieldPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel sectionTitle = new JLabel("Students List");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        sectionTitle.setForeground(PRIMARY_COLOR);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        tablePanel.add(sectionTitle, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "Name", "Email", "Course", "Age", "Phone"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentTable.setRowHeight(40);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.setShowGrid(false);
        studentTable.setIntercellSpacing(new Dimension(0, 0));
        studentTable.setSelectionBackground(new Color(74, 144, 226));
        studentTable.setSelectionForeground(Color.WHITE);
        
        JTableHeader header = studentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(33, 37, 41));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);
        
        studentTable.setBackground(new Color(230, 240, 255));
        studentTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(230, 240, 255) : new Color(200, 220, 255));
                    c.setForeground(new Color(33, 37, 41));
                } else {
                    c.setBackground(new Color(74, 144, 226));
                    c.setForeground(Color.WHITE);
                }
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(new EmptyBorder(10, 15, 10, 15));
                }
                return c;
            }
        });
        
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    loadStudentToForm(selectedRow);
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(CARD_COLOR);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(230, 240, 255));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout(15, 0));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        bottomPanel.setOpaque(true);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(BG_COLOR);
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(searchLabel);
        
        String[] searchFields = {"All Fields", "Name", "Email", "Course", "Phone", "Age"};
        searchFieldCombo = new JComboBox<>(searchFields);
        searchFieldCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchFieldCombo.setPreferredSize(new Dimension(120, 45));
        searchFieldCombo.setBackground(Color.WHITE);
        searchFieldCombo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        searchPanel.add(searchFieldCombo);
        
        searchField = new JTextField(35);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        searchField.setPreferredSize(new Dimension(350, 45));
        searchField.addActionListener(e -> searchStudents());
        searchPanel.add(searchField);
        
        JButton searchButton = createStyledButton("Search", PRIMARY_COLOR);
        searchButton.setPreferredSize(new Dimension(120, 45));
        searchButton.addActionListener(e -> searchStudents());
        searchPanel.add(searchButton);
        
        JButton refreshButton = createStyledButton("Refresh", SECONDARY_COLOR);
        refreshButton.setPreferredSize(new Dimension(120, 45));
        refreshButton.addActionListener(e -> loadStudents());
        searchPanel.add(refreshButton);
        
        bottomPanel.add(searchPanel, BorderLayout.WEST);
        
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(SECONDARY_COLOR);
        statusLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
        statusLabel.setVisible(false);
        bottomPanel.add(statusLabel, BorderLayout.EAST);
        
        return bottomPanel;
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(0, 45));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2, true),
                    new EmptyBorder(11, 14, 11, 14)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JTextField createPhoneField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(0, 45));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                String newString = string.replaceAll("[^0-9]", "");
                if (fb.getDocument().getLength() + newString.length() <= 10) {
                    super.insertString(fb, offset, newString, attr);
                }
            }
            
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                String newString = text.replaceAll("[^0-9]", "");
                if (fb.getDocument().getLength() - length + newString.length() <= 10) {
                    super.replace(fb, offset, length, newString, attrs);
                }
            }
        });
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2, true),
                    new EmptyBorder(11, 14, 11, 14)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(12, 15, 12, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(0, 45));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darkenColor(color, 0.15f));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private Color darkenColor(Color color, float factor) {
        return new Color(
            Math.max((int)(color.getRed() * (1 - factor)), 0),
            Math.max((int)(color.getGreen() * (1 - factor)), 0),
            Math.max((int)(color.getBlue() * (1 - factor)), 0)
        );
    }
    
    private void loadStudents() {
        tableModel.setRowCount(0);
        List<Student> students = DatabaseManager.getAllStudents();
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCourse(),
                student.getAge(),
                student.getPhone()
            });
        }
        clearForm();
        updateStatus("Loaded " + students.size() + " student(s)");
    }
    
    private void loadStudentToForm(int row) {
        selectedStudentId = (Integer) tableModel.getValueAt(row, 0);
        nameField.setText((String) tableModel.getValueAt(row, 1));
        emailField.setText((String) tableModel.getValueAt(row, 2));
        courseField.setText((String) tableModel.getValueAt(row, 3));
        ageField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        phoneField.setText((String) tableModel.getValueAt(row, 5));
        updateStatus("Student selected: " + tableModel.getValueAt(row, 1));
        if (statusLabel != null) {
            statusLabel.setVisible(true);
        }
    }
    
    private void addStudent() {
        if (!validateForm()) {
            return;
        }
        
        Student student = new Student(
            nameField.getText().trim(),
            emailField.getText().trim(),
            courseField.getText().trim(),
            Integer.parseInt(ageField.getText().trim()),
            phoneField.getText().trim()
        );
        
        String result = DatabaseManager.addStudent(student);
        if ("SUCCESS".equals(result)) {
            JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadStudents();
            updateStatus("Student added successfully");
        } else if ("DUPLICATE_EMAIL".equals(result)) {
            JOptionPane.showMessageDialog(this, "Email already exists. Please use a different email address.", "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error: Email already exists");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student: " + result, "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error: " + result);
        }
    }
    
    private void updateStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateForm()) {
            return;
        }
        
        Student student = new Student(
            selectedStudentId,
            nameField.getText().trim(),
            emailField.getText().trim(),
            courseField.getText().trim(),
            Integer.parseInt(ageField.getText().trim()),
            phoneField.getText().trim()
        );
        
        if (DatabaseManager.updateStudent(student)) {
            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadStudents();
            updateStatus("Student updated successfully");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update student.", "Error", JOptionPane.ERROR_MESSAGE);
            updateStatus("Error: Failed to update student");
        }
    }
    
    private void deleteStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this student?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (DatabaseManager.deleteStudent(selectedStudentId)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
                updateStatus("Student deleted successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student.", "Error", JOptionPane.ERROR_MESSAGE);
                updateStatus("Error: Failed to delete student");
            }
        }
    }
    
    private void searchStudents() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadStudents();
            return;
        }
        
        String selectedField = (String) searchFieldCombo.getSelectedItem();
        tableModel.setRowCount(0);
        List<Student> students = DatabaseManager.searchStudents(searchTerm, selectedField);
        for (Student student : students) {
            tableModel.addRow(new Object[]{
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCourse(),
                student.getAge(),
                student.getPhone()
            });
        }
        String fieldText = "All Fields".equals(selectedField) ? "" : " in " + selectedField;
        updateStatus("Found " + students.size() + " student(s) matching '" + searchTerm + "'" + fieldText);
    }
    
    private void clearForm() {
        selectedStudentId = -1;
        nameField.setText("");
        emailField.setText("");
        courseField.setText("");
        ageField.setText("");
        phoneField.setText("");
        studentTable.clearSelection();
        if (statusLabel != null) {
            statusLabel.setVisible(false);
            statusLabel.setText("");
        }
    }
    
    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setVisible(selectedStudentId != -1);
        }
    }
    
    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address (e.g., name@example.com)!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (courseField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (ageField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Age is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            int age = Integer.parseInt(ageField.getText().trim());
            if (age < 1 || age > 150) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age (1-150)!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number is required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!phone.matches("^[0-9]{10}$")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            phoneField.requestFocus();
            return false;
        }
        return true;
    }
    
    private boolean isValidEmail(String email) {
        if (!email.contains("@")) {
            return false;
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        String localPart = parts[0];
        String domainPart = parts[1];
        if (localPart.isEmpty() || localPart.length() > 64) {
            return false;
        }
        if (domainPart.isEmpty() || !domainPart.contains(".")) {
            return false;
        }
        String[] domainParts = domainPart.split("\\.");
        if (domainParts.length < 2) {
            return false;
        }
        String tld = domainParts[domainParts.length - 1];
        if (tld.length() < 2) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new JDataConnect().setVisible(true);
        });
    }
}
