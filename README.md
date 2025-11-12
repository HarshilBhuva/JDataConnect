# JDataConnect - Student Management System
# Made By Harshil Bhuva & Jay Gorasiya

A Java Swing desktop application for managing student records with MySQL database integration.

## Features

- **Add Students**: Create new student records with validation
- **Update Students**: Modify existing student information
- **Delete Students**: Remove student records with confirmation
- **Search Students**: Search by name, email, course, phone, age, or all fields
- **View All Students**: Display all students in a table format
- **Form Validation**: Email format validation, phone number validation (10 digits), age validation (1-150)

## Technology Stack

- **Java**: 24
- **Build Tool**: Maven
- **Database**: MySQL (via XAMPP)
- **UI Framework**: Java Swing
- **Database Driver**: MySQL Connector/J 8.3.0
- **IDE**: NetBeans

## Prerequisites

- NetBeans IDE (latest version recommended)
- Java 24 or higher (JDK must be installed)
- Maven (bundled with NetBeans)
- XAMPP (includes Apache and MySQL Server)

## Setup Instructions for NetBeans and XAMPP

### Step 1: Install and Start XAMPP

1. **Download XAMPP**:
   - Download XAMPP from https://www.apachefriends.org/
   - Choose the version compatible with your operating system
   - Install XAMPP (default installation path is usually `C:\xampp`)

2. **Start MySQL in XAMPP**:
   - Open XAMPP Control Panel
   - Click **"Start"** button next to **MySQL**
   - Wait until MySQL status shows as "Running" (green)
   - **Note**: Apache is not required for this project, only MySQL needs to be running

3. **Verify MySQL is Running**:
   - MySQL should be running on `localhost:3306`
   - Default username: `root`
   - Default password: (empty/blank)

### Step 2: Open Project in NetBeans

1. **Launch NetBeans IDE**

2. **Open the Project**:
   - Go to `File` → `Open Project...`
   - Navigate to the `JDataConnect` folder
   - Select the folder (it should show as a Maven project)
   - Click `Open Project`

3. **Verify Project Structure**:
   - In the Projects window, you should see:
     ```
     JDataConnect
     ├── Source Packages
     │   └── com.mycompany.jdataconnect
     │       ├── DatabaseManager.java
     │       ├── JDataConnect.java
     │       └── Student.java
     ├── Test Packages
     └── Dependencies
     ```

### Step 3: Configure Database Connection

1. **Check Database Settings**:
   - Open `DatabaseManager.java` in NetBeans
   - Locate these lines (around line 8-12):
     ```java
     private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
     private static final String DATABASE_NAME = "JDataConnect";
     private static final String USERNAME = "root";
     private static final String PASSWORD = "";
     ```

2. **Default XAMPP Settings**:
   - The default settings work with XAMPP's MySQL:
     - Username: `root`
     - Password: (empty/blank)
     - Port: `3306`
   - **No changes needed** if using default XAMPP installation

3. **If You Changed MySQL Password in XAMPP**:
   - Update the `PASSWORD` variable in `DatabaseManager.java` with your MySQL password
   - Save the file (`Ctrl + S`)

### Step 4: Build the Project

1. **Clean and Build**:
   - Right-click on the project name in Projects window
   - Select `Clean and Build`
   - Or use `Shift + F11`
   - Wait for "BUILD SUCCESS" message in the Output window

2. **Check for Errors**:
   - If there are any compilation errors, they will be shown in red
   - Fix any errors before proceeding

### Step 5: Run the Application

1. **Ensure MySQL is Running in XAMPP**:
   - Check XAMPP Control Panel
   - MySQL should show as "Running" (green)

2. **Run the Project**:
   - Right-click on the project name in Projects window
   - Select `Run`
   - Or press `F6`
   - Or click the green Play button in the toolbar

3. **First Run**:
   - The application will automatically:
     - Create the `JDataConnect` database (if it doesn't exist)
     - Create the `students` table (if it doesn't exist)
   - The Student Management System window should open maximized

### Step 6: Verify Database Connection

1. **Test the Application**:
   - Try adding a student record
   - If successful, the database connection is working
   - If you get an error, check:
     - MySQL is running in XAMPP Control Panel
     - XAMPP MySQL port is 3306 (default)
     - Database credentials in `DatabaseManager.java` match your XAMPP MySQL settings

2. **Optional: Verify Database in phpMyAdmin**:
   - Open XAMPP Control Panel
   - Click **"Admin"** button next to MySQL (opens phpMyAdmin)
   - You should see the `JDataConnect` database
   - Click on it to see the `students` table

## Project Structure

```
JDataConnect/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── mycompany/
                    └── jdataconnect/
                        ├── JDataConnect.java      # Main GUI application
                        ├── DatabaseManager.java   # Database operations
                        └── Student.java           # Student model class
```

## Usage Guide

### Adding a Student
1. Fill in all fields in the form:
   - **Full Name**: Student's full name
   - **Email Address**: Valid email (e.g., student@example.com)
   - **Course**: Course name
   - **Age**: Number between 1-150
   - **Phone Number**: Exactly 10 digits
2. Click **"Add Student"** button
3. A success message will appear if the student is added

### Updating a Student
1. Click on a student row in the table to select it
2. The form will be populated with student details
3. Modify the fields as needed
4. Click **"Update"** button
5. Changes will be saved to the database

### Deleting a Student
1. Click on a student row in the table to select it
2. Click **"Delete"** button
3. Confirm the deletion in the dialog box
4. The student will be removed from the database

### Searching Students
1. Select a search field from the dropdown:
   - **All Fields**: Search across all columns
   - **Name**: Search by name only
   - **Email**: Search by email only
   - **Course**: Search by course only
   - **Phone**: Search by phone number only
   - **Age**: Search by age only
2. Enter your search term in the search box
3. Click **"Search"** button
4. Matching results will be displayed in the table

### Refreshing the List
- Click **"Refresh"** button to reload all students from the database

## Validation Rules

- **Name**: Required, cannot be empty
- **Email**: Required, must be valid email format (e.g., name@example.com)
- **Course**: Required, cannot be empty
- **Age**: Required, must be a number between 1 and 150
- **Phone**: Required, must be exactly 10 digits (numbers only)

