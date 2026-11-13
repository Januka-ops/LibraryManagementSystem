
# 📚 Smart Library Management System

A desktop-based Library Management System developed using **Java Swing and MySQL**. This application is designed to simplify library operations by providing an efficient way to manage books, members, book borrowing, returns, and library records.

## 📌 Project Overview

The Smart Library Management System provides a user-friendly graphical interface for managing daily library activities. It includes secure administrator authentication and database connectivity to store and manage library information.
The system helps librarians maintain accurate records, track issued books, monitor overdue books, and manage library resources efficiently.

## ✨ Features

### 🔐 Authentication
- First-time administrator registration
- Secure administrator login
- Change password functionality
- Logout functionality
- Password hashing for improved security

### 📖 Book Management
- Add new books
- Update existing book details
- Delete books
- Search books
- View available book quantities

### 👥 Member Management
- Register new library members
- Update member information
- Delete member records
- Search and view members

### 🔄 Issue & Return Management
- Issue books to registered members
- Record book return dates
- Automatically update available book quantities
- Track issued and overdue books

### 📊 Reports
- View library records and statistics
- Monitor issued and overdue books
- Access book and member information

### ⚙️ Library Settings
- Update library information
- Manage contact details
- Configure borrowing period

### 📈 Dashboard
- Total books
- Total members
- Issued books
- Overdue books
- Recent books
- Quick action buttons

## 🛠️ Technologies Used

| Technology | Purpose |

| Java | Application development |
| Java Swing | Graphical User Interface |
| MySQL | Database management |
| JDBC | Database connectivity |
| Apache Maven | Dependency management and build |
| NetBeans IDE | Development environment |

## 🗂️ Project Structure

```text
LibraryManagementSystem/
│
├── src/
│   └── main/
│       └── java/
│           └── com/mycompany/librarymanagementsystem/
│               ├── database/
│               ├── dao/
│               ├── model/
│               ├── security/
│               ├── ui/
│               └── LibraryManagementSystem.java
│
├── pom.xml
├── .gitignore
└── README.md
```

## 🗄️ Database

The application uses **MySQL** as its database.

Database name:

```sql
library_management
```

Main database tables:

- `admins`
- `books`
- `members`
- `book_issues`
- `library_settings`

### Database Configuration

1. Install and start MySQL Server.
2. Create a database named `library_management`.
3. Configure your database connection in `DBConnection.java`.
4. Ensure the database username and password are correctly configured.

**Note:** Keep your database credentials private. Do not commit actual passwords to GitHub.

## 🚀 How to Run the Project

### Prerequisites

- JDK 26 (or the compatible JDK configured for this project)
- NetBeans IDE
- MySQL Server
- Git (optional)

## 🔒 Security

- Administrator passwords are stored using salted PBKDF2 password hashing.
- Database operations use JDBC.
- Prepared statements are used for database queries.
- Database credentials should be configured locally and kept out of version control.

## 🎯 Learning Outcomes

Through this project, I gained practical experience in:

- Object-Oriented Programming (OOP) with Java
- Java Swing GUI development
- MySQL database design and management
- JDBC connectivity
- CRUD operations
- Authentication and password hashing
- DAO and model-based application structure
- Maven project management
- Git and GitHub version control

## 👨‍💻 Developer

Januka Kaveen Nandasiri,
Developed as a practical software development project to improve my knowledge of Java application development and database management.

## 📄 License

This project is available for educational and learning purposes.
