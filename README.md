# Bridgelabz Training Database Repository

A comprehensive repository containing Java database projects and SQL/DBMS practice queries.

This repository represents hands-on learning of database concepts, SQL, JDBC connectivity, PostgreSQL integration, and backend application development using Java.

---

# Overview

This repository is divided into two major sections:

- Database JDBC Projects
- SQL / DBMS Practice Queries

The objective is to build strong knowledge in:

- Database Management Systems
- SQL Queries
- JDBC Connectivity
- PostgreSQL
- CRUD Operations
- Backend Development

---

# Repository Structure

```bash
Database/
│
├── Databse_JDBC_Projects/
│   │
│   ├── Employee_Payroll_Application/
│   ├── Employee_Payroll_Application_Maven/
│   ├── greeting-jdbc-app/
│   ├── greeting-jdbc-template-app/
│   └── LMS_Project/
│
└── DBMS_SQL_Queries/
```

---

# Database JDBC Projects

This section contains Java applications integrated with databases using JDBC, PostgreSQL, Maven, and Spring JdbcTemplate.

---

## 1. Employee Payroll Application

A Java + JDBC based payroll management system.

### Features
- User Authentication  
- Employee Management  
- Salary Management  
- Payroll Audit  
- Department Payroll Calculation  

### Concepts Used
- Java  
- JDBC  
- PostgreSQL  
- SQL Functions  
- SQL Triggers  

---

## 2. Employee Payroll Application (Maven)

Maven-based payroll application with improved architecture.

### Features
- Employee Management  
- Payroll Processing  
- Role-Based Access  
- Database Connectivity  

### Concepts Used
- Maven  
- JDBC  
- PostgreSQL  

---

## 3. Greeting JDBC App

Basic greeting management system using JDBC.

### Features
- Greeting Storage  
- CRUD Operations  
- Database Integration  

---

## 4. Greeting JDBC Template App

Greeting application using Spring JdbcTemplate.

### Features
- CRUD Operations  
- Database Connectivity  
- Query Execution  

### Concepts Used
- Spring JdbcTemplate  
- PostgreSQL  
- Maven  

---

## 5. LMS Project

Learning / Library Management System.

### Features
- Record Management  
- CRUD Operations  
- Database Connectivity  

---

# SQL / DBMS Practice Queries

This section contains SQL practice queries covering core DBMS concepts.

---

## Topics Covered

### DDL Commands
- CREATE  
- ALTER  
- DROP  
- TRUNCATE  

---

### DML Commands
- INSERT  
- UPDATE  
- DELETE  

---

### DQL Commands
- SELECT  
- WHERE  
- ORDER BY  
- GROUP BY  
- HAVING  

---

### Joins
- INNER JOIN  
- LEFT JOIN  
- RIGHT JOIN  
- FULL JOIN  

---

### Keys and Constraints
- Primary Key  
- Foreign Key  
- Unique Key  
- Not Null  
- Check Constraint  

---

### Normalization
- 1NF  
- 2NF  
- 3NF  
- BCNF  

---

### Advanced SQL Concepts
- Subqueries  
- Views  
- Functions  
- Procedures  
- Triggers  
- Indexing  

---

# Technologies Used

- Java  
- JDBC  
- PostgreSQL  
- SQL  
- DBMS  
- Maven  
- Spring JdbcTemplate  
- IntelliJ IDEA  
- VS Code  

---

# Learning Objectives

- Build strong DBMS concepts  
- Learn SQL queries  
- Understand JDBC architecture  
- Perform CRUD operations  
- Work with PostgreSQL databases  
- Build real-world backend applications  
- Improve problem-solving skills  

---

# How to Run

## Clone Repository

```bash
git clone <repository-url>
```

---

## Database Setup

Create database in PostgreSQL:

```sql
CREATE DATABASE project_db;
```

Run schema files for respective projects.

---

## Configure Database Credentials

Update credentials in configuration files.

Example:

```java
URL=jdbc:postgresql://localhost:5432/project_db
USER=postgres
PASSWORD=your_password
```

---

## Run Java Application

Compile:

```bash
javac Main.java
```

Run:

```bash
java Main
```

For Maven Projects:

```bash
mvn clean install
mvn exec:java
```

---

# Key Concepts Covered

- SQL Queries  
- DBMS Concepts  
- JDBC Architecture  
- CRUD Operations  
- Connection Management  
- Database Design  
- Backend Development  

---

# Author

Sakshi Singh

Java | JDBC | PostgreSQL | SQL | DBMS | Backend Development
