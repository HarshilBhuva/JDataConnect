package com.mycompany.jdataconnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "JDataConnect";
    private static final String URL = BASE_URL + DATABASE_NAME;
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    
    public static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(BASE_URL, USERNAME, PASSWORD);
             Statement stmt = conn.createStatement()) {
            // Create database if it doesn't exist
            String createDbSql = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            stmt.executeUpdate(createDbSql);
        } catch (SQLException e) {
            System.err.println("Error creating database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void createTable() {
        // First ensure database exists
        createDatabase();
        
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "course VARCHAR(50) NOT NULL, " +
                    "age INT NOT NULL, " +
                    "phone VARCHAR(20) NOT NULL" +
                    ")";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static String addStudent(Student student) {
        String sql = "INSERT INTO students (name, email, course, age, phone) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getCourse());
            pstmt.setInt(4, student.getAge());
            pstmt.setString(5, student.getPhone());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                return "SUCCESS";
            } else {
                return "Failed to add student.";
            }
        } catch (SQLException e) {
            // Check for duplicate entry error (MySQL error code 1062)
            if (e.getErrorCode() == 1062) {
                return "DUPLICATE_EMAIL";
            }
            e.printStackTrace();
            return "Database error: " + e.getMessage();
        }
    }
    
    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("course"),
                    rs.getInt("age"),
                    rs.getString("phone")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return students;
    }
    
    public static boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, email = ?, course = ?, age = ?, phone = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getCourse());
            pstmt.setInt(4, student.getAge());
            pstmt.setString(5, student.getPhone());
            pstmt.setInt(6, student.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<Student> searchStudents(String searchTerm) {
        return searchStudents(searchTerm, "All Fields");
    }
    
    public static List<Student> searchStudents(String searchTerm, String searchField) {
        List<Student> students = new ArrayList<>();
        String sql;
        
        try (Connection conn = getConnection()) {
            String searchPattern = "%" + searchTerm + "%";
            PreparedStatement pstmt = null;
            
            if ("All Fields".equals(searchField)) {
                // Search across all fields
                sql = "SELECT * FROM students WHERE name LIKE ? OR email LIKE ? OR course LIKE ? OR phone LIKE ? OR CAST(age AS CHAR) LIKE ? ORDER BY id DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, searchPattern);
                pstmt.setString(2, searchPattern);
                pstmt.setString(3, searchPattern);
                pstmt.setString(4, searchPattern);
                pstmt.setString(5, searchPattern);
            } else if ("Name".equals(searchField)) {
                sql = "SELECT * FROM students WHERE name LIKE ? ORDER BY id DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, searchPattern);
            } else if ("Email".equals(searchField)) {
                sql = "SELECT * FROM students WHERE email LIKE ? ORDER BY id DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, searchPattern);
            } else if ("Course".equals(searchField)) {
                sql = "SELECT * FROM students WHERE course LIKE ? ORDER BY id DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, searchPattern);
            } else if ("Phone".equals(searchField)) {
                sql = "SELECT * FROM students WHERE phone LIKE ? ORDER BY id DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, searchPattern);
            } else if ("Age".equals(searchField)) {
                // For age, try to match exact number or partial match
                try {
                    int ageValue = Integer.parseInt(searchTerm);
                    sql = "SELECT * FROM students WHERE age = ? ORDER BY id DESC";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, ageValue);
                } catch (NumberFormatException e) {
                    // If not a number, search as string
                    sql = "SELECT * FROM students WHERE CAST(age AS CHAR) LIKE ? ORDER BY id DESC";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, searchPattern);
                }
            } else {
                // Default to all fields
                sql = "SELECT * FROM students WHERE name LIKE ? OR email LIKE ? OR course LIKE ? OR phone LIKE ? OR CAST(age AS CHAR) LIKE ? ORDER BY id DESC";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, searchPattern);
                pstmt.setString(2, searchPattern);
                pstmt.setString(3, searchPattern);
                pstmt.setString(4, searchPattern);
                pstmt.setString(5, searchPattern);
            }
            
            if (pstmt != null) {
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Student student = new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("course"),
                            rs.getInt("age"),
                            rs.getString("phone")
                        );
                        students.add(student);
                    }
                }
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return students;
    }
}

