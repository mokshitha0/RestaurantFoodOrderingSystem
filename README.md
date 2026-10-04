# Restaurant Food Ordering System

## Project Description

A console-based Restaurant Food Ordering System developed using Java, JDBC, and MySQL.

The application provides separate options for customers and restaurant owners to manage customers, menu items, orders, billing, and other restaurant operations.

## Technologies Used

- Java
- JDBC
- MySQL
- Eclipse IDE
- Git & GitHub

## Features

### Customer

- Customer Registration
- View Menu
- Place Order
- View Order History
- Generate Bill
- Cancel Order
- Delete Account

### Restaurant Owner

- Add Menu Item
- View Menu
- View Customers
- Update Menu Item
- Delete Menu Item
- Batch Add Menu Items

## Database Tables

- customers
- menu
- orders
- order_items

## JDBC Concepts Used

- JDBC Driver
- Connection
- PreparedStatement
- ResultSet
- CRUD Operations
- Transactions
- Commit
- Rollback
- Batch Processing
- Generated Keys

## Validation

The application validates:

- Empty fields
- Customer names
- Menu item names
- Categories
- Email
- Phone number
- Price
- Quantity

## Project Structure

```text
RestaurantFoodOrderingSystem
│
├── src
│   └── JDBC
│       ├── MainApplication.java
│       ├── DatabaseOperations.java
│       └── Validation.java
│
├── .gitignore
├── .project
└── README.md