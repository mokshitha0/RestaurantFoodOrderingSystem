package JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

    public static void createTables(Connection con) {

        try {
            
            Statement stmt = con.createStatement();

            // 1. Customers table
            String customersTable = """
                    CREATE TABLE IF NOT EXISTS customers (
                        customer_id INT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(100) NOT NULL,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        phone VARCHAR(10) NOT NULL
                    )
                    """;

            stmt.executeUpdate(customersTable);

            // 2. Menu table
            String menuTable = """
                    CREATE TABLE IF NOT EXISTS menu (
                        menu_id INT PRIMARY KEY AUTO_INCREMENT,
                        item_name VARCHAR(100) NOT NULL,
                        price DECIMAL(10,2) NOT NULL CHECK (price > 0),
                        category VARCHAR(50) NOT NULL,
                        available BOOLEAN DEFAULT TRUE
                    )
                    """;

            stmt.executeUpdate(menuTable);

            // 3. Orders table
            String ordersTable = """
                    CREATE TABLE IF NOT EXISTS orders (
                        order_id INT PRIMARY KEY AUTO_INCREMENT,
                        customer_id INT NOT NULL,
                        order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        total_amount DECIMAL(10,2) DEFAULT 0.00
                            CHECK (total_amount >= 0),

                        FOREIGN KEY (customer_id)
                            REFERENCES customers(customer_id)
                    )
                    """;
            stmt.executeUpdate(ordersTable);
            String alterOrdersTable =
                    "ALTER TABLE orders " +
                    "ADD COLUMN status VARCHAR(20) DEFAULT 'PLACED'";

            try {
                stmt.executeUpdate(alterOrdersTable);
            } catch (Exception e) {
                // Column already exists, so no action needed
            }

            // 4. Order Items table
            String orderItemsTable = """
                    CREATE TABLE IF NOT EXISTS order_items (
                        order_item_id INT PRIMARY KEY AUTO_INCREMENT,
                        order_id INT NOT NULL,
                        menu_id INT NOT NULL,
                        quantity INT NOT NULL CHECK (quantity > 0),
                        price DECIMAL(10,2) NOT NULL CHECK (price > 0),

                        FOREIGN KEY (order_id)
                            REFERENCES orders(order_id),

                        FOREIGN KEY (menu_id)
                            REFERENCES menu(menu_id)
                    )
                    """;
            stmt.executeUpdate(orderItemsTable);

            System.out.println("All tables created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
      public static void addCustomer(Connection con, Scanner sc) {

            try {

                // Name validation
                String name;

                while (true) {

                    System.out.print("Enter customer name: ");
                    name = sc.nextLine();

                    if (!Validation.isNotEmpty(name)) {
                        System.out.println("Name cannot be empty.");
                    }
                    else if (!Validation.containsOnlyAlphabets(name)) {
                        System.out.println("Name should contain alphabets only.");
                    }
                    else {
                        break;
                    }
                }

                // Email validation
                String email;

                while (true) {

                    System.out.print("Enter email: ");
                    email = sc.nextLine();

                    if (!Validation.isNotEmpty(email)) {
                        System.out.println("Email cannot be empty.");
                    }
                    else if (!Validation.isValidEmail(email)) {
                        System.out.println("Enter a valid email.");
                    }
                    else {
                        break;
                    }
                }

                // Phone validation
                String phone;

                while (true) {

                    System.out.print("Enter phone number: ");
                    phone = sc.nextLine();

                    if (!Validation.isNotEmpty(phone)) {
                        System.out.println("Phone number cannot be empty.");
                    }
                    else if (!Validation.isValidPhone(phone)) {
                        System.out.println("Enter a valid 10-digit phone number.");
                    }
                    else {
                        break;
                    }
                }

                String sql =
                        "INSERT INTO customers(name, email, phone) VALUES (?, ?, ?)";

                PreparedStatement ps = con.prepareStatement(sql);

                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, phone);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Customer registered successfully!");
                }

            } catch (SQLIntegrityConstraintViolationException e) {

                System.out.println("Email already exists.");

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
      public static void viewCustomers(Connection con) {

    	    String sql = "SELECT * FROM customers";

    	    try {

    	        PreparedStatement ps = con.prepareStatement(sql);

    	        ResultSet rs = ps.executeQuery();

    	        System.out.println("\n========== CUSTOMER DETAILS ==========");

    	        while (rs.next()) {

    	            displayCustomer(rs);
    	        }

    	    } catch (Exception e) {

    	        e.printStackTrace();
    	    }
    	}
      public static void displayCustomer(ResultSet rs) throws Exception {

    	    System.out.println("--------------------------------------");

    	    System.out.println(
    	            "Customer ID : " + rs.getInt("customer_id")
    	    );

    	    System.out.println(
    	            "Name        : " + rs.getString("name")
    	    );

    	    System.out.println(
    	            "Email       : " + rs.getString("email")
    	    );

    	    System.out.println(
    	            "Phone       : " + rs.getString("phone")
    	    );
    	}
     
      public static void updateCustomer(Connection con, Scanner sc) {

    	    System.out.print("Enter customer ID to update: ");
    	    int customerId = sc.nextInt();
    	    sc.nextLine();

    	    System.out.println("\nWhat do you want to update?");
    	    System.out.println("1. Name");
    	    System.out.println("2. Email");
    	    System.out.println("3. Phone");
    	    System.out.println("4. Cancel");

    	    System.out.print("Enter your choice: ");
    	    int choice = sc.nextInt();
    	    sc.nextLine();

    	    String sql = "";

    	    try {

    	        switch (choice) {

    	            case 1:

    	                String name;

    	                while (true) {

    	                    System.out.print("Enter new name: ");
    	                    name = sc.nextLine();

    	                    if (!Validation.isNotEmpty(name)) {
    	                        System.out.println("Name cannot be empty.");
    	                    }
    	                    else if (!Validation.containsOnlyAlphabets(name)) {
    	                        System.out.println(
    	                            "Name should contain alphabets only."
    	                        );
    	                    }
    	                    else {
    	                        break;
    	                    }
    	                }

    	                sql = "UPDATE customers SET name = ? WHERE customer_id = ?";

    	                PreparedStatement namePs =
    	                        con.prepareStatement(sql);

    	                namePs.setString(1, name);
    	                namePs.setInt(2, customerId);

    	                int nameRows = namePs.executeUpdate();

    	                if (nameRows > 0) {
    	                    System.out.println("Name updated successfully!");
    	                } else {
    	                    System.out.println("Customer ID not found.");
    	                }

    	                break;


    	            case 2:

    	                String email;

    	                while (true) {

    	                    System.out.print("Enter new email: ");
    	                    email = sc.nextLine();

    	                    if (!Validation.isNotEmpty(email)) {
    	                        System.out.println("Email cannot be empty.");
    	                    }
    	                    else if (!Validation.isValidEmail(email)) {
    	                        System.out.println("Enter a valid email.");
    	                    }
    	                    else {
    	                        break;
    	                    }
    	                }

    	                sql = "UPDATE customers SET email = ? WHERE customer_id = ?";

    	                PreparedStatement emailPs =
    	                        con.prepareStatement(sql);

    	                emailPs.setString(1, email);
    	                emailPs.setInt(2, customerId);

    	                int emailRows = emailPs.executeUpdate();

    	                if (emailRows > 0) {
    	                    System.out.println("Email updated successfully!");
    	                } else {
    	                    System.out.println("Customer ID not found.");
    	                }

    	                break;


    	            case 3:

    	                String phone;

    	                while (true) {

    	                    System.out.print("Enter new phone number: ");
    	                    phone = sc.nextLine();

    	                    if (!Validation.isNotEmpty(phone)) {
    	                        System.out.println(
    	                            "Phone number cannot be empty."
    	                        );
    	                    }
    	                    else if (!Validation.isValidPhone(phone)) {
    	                        System.out.println(
    	                            "Enter a valid 10-digit phone number."
    	                        );
    	                    }
    	                    else {
    	                        break;
    	                    }
    	                }

    	                sql = "UPDATE customers SET phone = ? WHERE customer_id = ?";

    	                PreparedStatement phonePs =
    	                        con.prepareStatement(sql);

    	                phonePs.setString(1, phone);
    	                phonePs.setInt(2, customerId);

    	                int phoneRows = phonePs.executeUpdate();

    	                if (phoneRows > 0) {
    	                    System.out.println("Phone updated successfully!");
    	                } else {
    	                    System.out.println("Customer ID not found.");
    	                }

    	                break;


    	            case 4:

    	                System.out.println("Update cancelled.");
    	                break;


    	            default:

    	                System.out.println("Invalid choice.");
    	        }

    	    } catch (SQLIntegrityConstraintViolationException e) {

    	        System.out.println("Email already exists.");

    	    } catch (Exception e) {

    	        e.printStackTrace();
    	    }
      }
      public static void cancelOrder(Connection con, Scanner sc) {

    	    try {

    	        System.out.print("Enter order ID to cancel: ");
    	        int orderId = sc.nextInt();
    	        sc.nextLine();

    	        String sql =
    	                "UPDATE orders " +
    	                "SET status = 'CANCELLED' " +
    	                "WHERE order_id = ? AND status = 'PLACED'";

    	        PreparedStatement ps = con.prepareStatement(sql);

    	        ps.setInt(1, orderId);

    	        int rows = ps.executeUpdate();

    	        if (rows > 0) {
    	            System.out.println("Order cancelled successfully!");
    	        } else {
    	            System.out.println(
    	                "Order not found or order is already cancelled."
    	            );
    	        }

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}
      public static void deleteAccount(Connection con, Scanner sc) {

    	    try {
    	        System.out.print("Enter customer ID: ");
    	        int customerId = sc.nextInt();
    	        sc.nextLine();

    	        String sql = "DELETE FROM customers WHERE customer_id = ?";

    	        PreparedStatement ps = con.prepareStatement(sql);
    	        ps.setInt(1, customerId);

    	        int rows = ps.executeUpdate();

    	        if (rows > 0) {
    	            System.out.println("Account deleted successfully!");
    	        } else {
    	            System.out.println("Customer not found.");
    	        }

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}
      public static void addMenuItem(Connection con, Scanner sc) {

    	    try {

    	        String itemName;

    	        // Item name validation
    	        while (true) {

    	            System.out.print("Enter item name: ");
    	            itemName = sc.nextLine();

    	            if (!Validation.isNotEmpty(itemName)) {
    	                System.out.println("Item name cannot be empty.");
    	            }
    	            else if (!Validation.isValidItemName(itemName)) {
    	                System.out.println(
    	                    "Item name should contain alphabets, numbers and spaces only."
    	                );
    	            }
    	            else {
    	                break;
    	            }
    	        }

    	        String price;

    	        // Price validation
    	        while (true) {

    	            System.out.print("Enter price: ");
    	            price = sc.nextLine();

    	            if (!Validation.isNotEmpty(price)) {
    	                System.out.println("Price cannot be empty.");
    	            }
    	            else if (!Validation.isValidPrice(price)) {
    	                System.out.println("Price should be greater than 0.");
    	            }
    	            else {
    	                break;
    	            }
    	        }

    	        String category;

    	        // Category validation
    	        while (true) {

    	            System.out.print("Enter category: ");
    	            category = sc.nextLine();

    	            if (!Validation.isNotEmpty(category)) {
    	                System.out.println("Category cannot be empty.");
    	            }
    	            else if (!Validation.containsOnlyAlphabets(category)) {
    	                System.out.println("Category should contain alphabets only.");
    	            }
    	            else {
    	                break;
    	            }
    	        }

    	        String sql =
    	                "INSERT INTO menu(item_name, price, category) " +
    	                "VALUES (?, ?, ?)";

    	        PreparedStatement ps = con.prepareStatement(sql);

    	        ps.setString(1, itemName);
    	        ps.setDouble(2, Double.parseDouble(price));
    	        ps.setString(3, category);

    	        int rows = ps.executeUpdate();

    	        if (rows > 0) {
    	            System.out.println("Menu item added successfully!");
    	        }

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}
      
      public static void viewMenu(Connection con) {

    	    String sql = "SELECT * FROM menu";

    	    try {

    	        PreparedStatement ps = con.prepareStatement(sql);
    	        ResultSet rs = ps.executeQuery();

    	        System.out.println("\n========== MENU DETAILS ==========");

    	        while (rs.next()) {
    	            displayMenu(rs);
    	        }

    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	}
      public static void displayMenu(ResultSet rs) throws Exception {

    	    System.out.println("--------------------------------------");

    	    System.out.println(
    	            "Menu ID     : " + rs.getInt("menu_id")
    	    );

    	    System.out.println(
    	            "Item Name   : " + rs.getString("item_name")
    	    );

    	    System.out.println(
    	            "Price       : " + rs.getDouble("price")
    	    );

    	    System.out.println(
    	            "Category    : " + rs.getString("category")
    	    );

    	    System.out.println(
    	            "Available   : " + rs.getBoolean("available")
    	    );
    	}
      public static void showAvailableMenu(Connection con) {

    	    String sql = "SELECT * FROM menu WHERE available = TRUE";

    	    try {

    	        PreparedStatement ps = con.prepareStatement(sql);
    	        ResultSet rs = ps.executeQuery();

    	        System.out.println("\n========== AVAILABLE MENU ==========");

    	        while (rs.next()) {
    	            displayMenu(rs);
    	        }

    	    } catch (Exception e) {

    	        e.printStackTrace();
    	    }
    	}
      public static void updateMenuItem(Connection con, Scanner sc) {

    	    System.out.print("Enter menu ID to update: ");
    	    int menuId = sc.nextInt();
    	    sc.nextLine();

    	    System.out.println("\nWhat do you want to update?");
    	    System.out.println("1. Item Name");
    	    System.out.println("2. Price");
    	    System.out.println("3. Category");
    	    System.out.println("4. Availability");
    	    System.out.println("5. Cancel");

    	    System.out.print("Enter your choice: ");
    	    int choice = sc.nextInt();
    	    sc.nextLine();

    	    try {

    	        switch (choice) {

    	            case 1:

    	                String itemName;

    	                while (true) {

    	                    System.out.print("Enter new item name: ");
    	                    itemName = sc.nextLine();

    	                    if (!Validation.isNotEmpty(itemName)) {
    	                        System.out.println("Item name cannot be empty.");
    	                    }
    	                    else if (!Validation.isValidItemName(itemName)) {
    	                        System.out.println(
    	                            "Item name should contain alphabets, numbers and spaces only."
    	                        );
    	                    
    	                    
    	                    }
    	                    else {
    	                        break;
    	                    }
    	                }

    	                String nameSql =
    	                        "UPDATE menu SET item_name = ? WHERE menu_id = ?";

    	                PreparedStatement namePs =
    	                        con.prepareStatement(nameSql);

    	                namePs.setString(1, itemName);
    	                namePs.setInt(2, menuId);

    	                int nameRows = namePs.executeUpdate();

    	                if (nameRows > 0) {
    	                    System.out.println("Item name updated successfully!");
    	                } else {
    	                    System.out.println("Menu ID not found.");
    	                }

    	                break;

    	            case 2:

    	                String price;

    	                while (true) {

    	                    System.out.print("Enter new price: ");
    	                    price = sc.nextLine();

    	                    if (!Validation.isNotEmpty(price)) {
    	                        System.out.println("Price cannot be empty.");
    	                    }
    	                    else if (!Validation.isValidPrice(price)) {
    	                        System.out.println("Price should be greater than 0.");
    	                    }
    	                    else {
    	                        break;
    	                    }
    	                }

    	                String priceSql =
    	                        "UPDATE menu SET price = ? WHERE menu_id = ?";

    	                PreparedStatement pricePs =
    	                        con.prepareStatement(priceSql);

    	                pricePs.setDouble(1, Double.parseDouble(price));
    	                pricePs.setInt(2, menuId);

    	                int priceRows = pricePs.executeUpdate();

    	                if (priceRows > 0) {
    	                    System.out.println("Price updated successfully!");
    	                } else {
    	                    System.out.println("Menu ID not found.");
    	                }

    	                break;

    	            case 3:

    	                String category;

    	                while (true) {

    	                    System.out.print("Enter new category: ");
    	                    category = sc.nextLine();

    	                    if (!Validation.isNotEmpty(category)) {
    	                        System.out.println("Category cannot be empty.");
    	                    }
    	                    else if (!Validation.containsOnlyAlphabets(category)) {
    	                        System.out.println(
    	                            "Category should contain alphabets only."
    	                        );
    	                    }
    	                    else {
    	                        break;
    	                    }
    	                }

    	                String categorySql =
    	                        "UPDATE menu SET category = ? WHERE menu_id = ?";

    	                PreparedStatement categoryPs =
    	                        con.prepareStatement(categorySql);

    	                categoryPs.setString(1, category);
    	                categoryPs.setInt(2, menuId);

    	                int categoryRows = categoryPs.executeUpdate();

    	                if (categoryRows > 0) {
    	                    System.out.println("Category updated successfully!");
    	                } else {
    	                    System.out.println("Menu ID not found.");
    	                }

    	                break;

    	            case 4:

    	                System.out.print(
    	                        "Is the item available? (true/false): "
    	                );

    	                boolean available = sc.nextBoolean();
    	                sc.nextLine();

    	                String availableSql =
    	                        "UPDATE menu SET available = ? WHERE menu_id = ?";

    	                PreparedStatement availablePs =
    	                        con.prepareStatement(availableSql);

    	                availablePs.setBoolean(1, available);
    	                availablePs.setInt(2, menuId);

    	                int availableRows = availablePs.executeUpdate();

    	                if (availableRows > 0) {
    	                    System.out.println(
    	                            "Availability updated successfully!"
    	                    );
    	                } else {
    	                    System.out.println("Menu ID not found.");
    	                }

    	                break;

    	            case 5:

    	                System.out.println("Update cancelled.");
    	                break;

    	            default:

    	                System.out.println("Invalid choice.");
    	        }

    	    } catch (Exception e) {

    	        e.printStackTrace();
    	    }
    	}
      public static void deleteMenuItem(Connection con, Scanner sc) {

    	    try {

    	        System.out.print("Enter menu ID to delete: ");
    	        int menuId = sc.nextInt();
    	        sc.nextLine();

    	        String sql =
    	                "DELETE FROM menu WHERE menu_id = ?";

    	        PreparedStatement ps = con.prepareStatement(sql);

    	        ps.setInt(1, menuId);

    	        int rows = ps.executeUpdate();

    	        if (rows > 0) {

    	            System.out.println("Menu item deleted successfully!");

    	        } else {

    	            System.out.println("Menu ID not found.");
    	        }

    	    } catch (SQLIntegrityConstraintViolationException e) {

    	        System.out.println(
    	            "Menu item cannot be deleted because it is used in an order."
    	        );

    	    } catch (Exception e) {

    	        e.printStackTrace();
    	    }
    	}
      public static void batchAddMenuItems(Connection con, Scanner sc) {

    	    try {

    	        System.out.print("How many menu items do you want to add? ");
    	        int count = sc.nextInt();
    	        sc.nextLine();

    	        String sql =
    	                "INSERT INTO menu(item_name, price, category) " +
    	                "VALUES (?, ?, ?)";

    	        PreparedStatement ps = con.prepareStatement(sql);

    	        for (int i = 1; i <= count; i++) {

    	            System.out.println("\nEnter details for menu item " + i);

    	            String itemName;

    	            while (true) {

    	                System.out.print("Enter item name: ");
    	                itemName = sc.nextLine();

    	                if (!Validation.isNotEmpty(itemName)) {
    	                    System.out.println("Item name cannot be empty.");
    	                }
    	                else if (!Validation.isValidItemName(itemName)) {
    	                    System.out.println(
    	                        "Item name should contain alphabets only."
    	                    );
    	                }
    	                else {
    	                    break;
    	                }
    	            }

    	            String price;

    	            while (true) {

    	                System.out.print("Enter price: ");
    	                price = sc.nextLine();

    	                if (!Validation.isNotEmpty(price)) {
    	                    System.out.println("Price cannot be empty.");
    	                }
    	                else if (!Validation.isValidPrice(price)) {
    	                    System.out.println("Price should be greater than 0.");
    	                }
    	                else {
    	                    break;
    	                }
    	            }

    	            String category;

    	          

    	            while (true) {

    	                System.out.print("Enter category: ");
    	                category = sc.nextLine();

    	                if (!Validation.isNotEmpty(category)) {
    	                    System.out.println("Category cannot be empty.");
    	                }
    	                else if (!Validation.containsOnlyAlphabets(category)) {
    	                    System.out.println(
    	                        "Category should contain alphabets only."
    	                    );
    	                }
    	                else {
    	                    break;
    	                }
    
    	            }

    	            ps.setString(1, itemName);
    	            ps.setDouble(2, Double.parseDouble(price));
    	            ps.setString(3, category);

    	            ps.addBatch();
    	        }

    	        int[] results = ps.executeBatch();

    	        System.out.println(
    	                results.length + " menu items added successfully!"
    	        );

    	    } catch (Exception e) {

    	        e.printStackTrace();
    	    }
    	}
     
      public static void placeOrder(Connection con, Scanner sc) {

    	    List<Integer> menuIds = new ArrayList<>();
    	    List<Integer> quantities = new ArrayList<>();
    	    List<Double> prices = new ArrayList<>();

    	    try {

    	        // 1. Get customer ID
    	        System.out.print("Enter customer ID: ");
    	        int customerId = sc.nextInt();
    	        sc.nextLine();

    	        // 2. Check whether customer exists
    	        String customerSql =
    	                "SELECT customer_id FROM customers WHERE customer_id = ?";

    	        PreparedStatement customerPs =
    	                con.prepareStatement(customerSql);

    	        customerPs.setInt(1, customerId);

    	        ResultSet customerRs = customerPs.executeQuery();

    	        if (!customerRs.next()) {

    	            System.out.println("Customer ID not found.");
    	            return;
    	        }

    	        System.out.println("Customer found.");

    	        // 3. Select food items
    	        while (true) {

    	            // Show only available menu items
    	            showAvailableMenu(con);
    	           // 4. Select menu item
    	            System.out.print("Enter menu ID: ");
    	            int menuId = sc.nextInt();
    	            sc.nextLine();

    	            // 5. Enter quantity
    	            System.out.print("Enter quantity: ");
    	            String quantityInput = sc.nextLine();

    	            if (!Validation.isValidQuantity(quantityInput)) {
    	                System.out.println(
    	                        "Quantity must be greater than 0."
    	                );
    	                continue;
    	            }

    	            int quantity = Integer.parseInt(quantityInput);
    	            

    	            // 6. Validate quantity
    	            if (quantity <= 0) {

    	                System.out.println(
    	                        "Quantity must be greater than 0."
    	                );

    	                continue;
    	            }

    	            // 7. Check whether menu item exists and is available
    	            String menuSql =
    	                    "SELECT menu_id, item_name, price " +
    	                    "FROM menu " +
    	                    "WHERE menu_id = ? AND available = TRUE";

    	            PreparedStatement menuPs =
    	                    con.prepareStatement(menuSql);

    	            menuPs.setInt(1, menuId);

    	            ResultSet menuRs = menuPs.executeQuery();

    	            if (!menuRs.next()) {

    	                System.out.println(
    	                        "Invalid menu ID or item is not available."
    	                );

    	                continue;
    	            }

    	            // 8. Get menu details
    	            String itemName =
    	                    menuRs.getString("item_name");

    	            double price =
    	                    menuRs.getDouble("price");

    	            // 9. Calculate item total
    	            double itemTotal = price * quantity;

    	            System.out.println("\nSelected Item : " + itemName);
    	            System.out.println("Price         : " + price);
    	            System.out.println("Quantity      : " + quantity);
    	            System.out.println("Item Total    : " + itemTotal);

    	            // 10. Store selected item
    	            menuIds.add(menuId);
    	            quantities.add(quantity);
    	            prices.add(price);

    	            // 11. Ask whether customer wants another item
    	            System.out.print(
    	                    "\nDo you want to add another item? (yes/no): "
    	            );

    	            String choice = sc.nextLine();

    	            if (choice.equalsIgnoreCase("no")) {
    	                break;
    	            }
    	        }

    	        // 12. Calculate total order amount
    	        double totalAmount = 0;

    	        for (int i = 0; i < menuIds.size(); i++) {

    	            totalAmount =
    	                    totalAmount +
    	                    (prices.get(i) * quantities.get(i));
    	        }

    	        System.out.println("\n========== ORDER SUMMARY ==========");

    	        for (int i = 0; i < menuIds.size(); i++) {

    	            System.out.println(
    	                    "Menu ID : " + menuIds.get(i) +
    	                    " | Quantity : " + quantities.get(i) +
    	                    " | Price : " + prices.get(i)
    	            );
    	        }

    	        System.out.println(
    	                "Total Amount : " + totalAmount
    	        );
    	     // Start transaction
    	        con.setAutoCommit(false);

    	        try {

    	            // 1. Insert order into orders table
    	            String orderSql =
    	                    "INSERT INTO orders(customer_id, total_amount, status) " +
    	                    "VALUES (?, ?, 'PLACED')";

    	            PreparedStatement orderPs =
    	                    con.prepareStatement(
    	                            orderSql,
    	                            Statement.RETURN_GENERATED_KEYS
    	                    );

    	            orderPs.setInt(1, customerId);
    	            orderPs.setDouble(2, totalAmount);

    	            orderPs.executeUpdate();

    	            // 2. Get generated order ID
    	            ResultSet generatedKeys =
    	                    orderPs.getGeneratedKeys();

    	            if (!generatedKeys.next()) {

    	                throw new Exception("Order ID could not be generated.");
    	            }

    	            int orderId =
    	                    generatedKeys.getInt(1);

    	            // 3. Insert order items
    	            String itemSql =
    	                    "INSERT INTO order_items " +
    	                    "(order_id, menu_id, quantity, price) " +
    	                    "VALUES (?, ?, ?, ?)";

    	            PreparedStatement itemPs =
    	                    con.prepareStatement(itemSql);

    	            for (int i = 0; i < menuIds.size(); i++) {

    	                itemPs.setInt(1, orderId);
    	                itemPs.setInt(2, menuIds.get(i));
    	                itemPs.setInt(3, quantities.get(i));
    	                itemPs.setDouble(4, prices.get(i));

    	                itemPs.addBatch();
    	            }

    	            itemPs.executeBatch();

    	            // 4. Commit transaction
    	            con.commit();

    	            System.out.println(
    	                    "\nOrder placed successfully!"
    	            );

    	            System.out.println(
    	                    "Order ID : " + orderId
    	            );

    	            System.out.println(
    	                    "Total Amount : " + totalAmount
    	            );

    	        } catch (Exception e) {

    	            // 5. Rollback if anything fails
    	            con.rollback();

    	            System.out.println(
    	                    "Order could not be placed. Transaction rolled back."
    	            );

    	            e.printStackTrace();

    	        } finally {

    	            // Return connection to normal mode
    	            con.setAutoCommit(true);
    	        }

    	    } catch (Exception e) {

    	        e.printStackTrace();
    	    }
    	}
      public static void viewOrderHistory(Connection con, Scanner sc) {

    	    try {

    	        System.out.print("Enter customer ID: ");
    	        int customerId = sc.nextInt();
    	        sc.nextLine();

    	        String sql =
    	                "SELECT order_id, order_date, total_amount, status " +
    	                "FROM orders " +
    	                "WHERE customer_id = ? " +
    	                "ORDER BY order_date DESC";

    	        PreparedStatement ps =
    	                con.prepareStatement(sql);

    	        ps.setInt(1, customerId);

    	        ResultSet rs = ps.executeQuery();

    	        boolean found = false;

    	        System.out.println("\n========== ORDER HISTORY ==========");

    	        while (rs.next()) {

    	            found = true;

    	            displayOrderHistory(rs);
    	        }

    	        if (!found) {

    	            System.out.println(
    	                    "No orders found for this customer."
    	            );
    	        }

    	    } catch (Exception e) {

    	        e.printStackTrace();
    	    }
    	}
      public static void displayOrderHistory(ResultSet rs) throws Exception {

    	    System.out.println("--------------------------------------");

    	    System.out.println(
    	            "Order ID     : " + rs.getInt("order_id")
    	    );

    	    System.out.println(
    	            "Order Date   : " + rs.getTimestamp("order_date")
    	    );

    	    System.out.println(
    	            "Total Amount : " + rs.getDouble("total_amount")
    	    );

    	    System.out.println(
    	            "Status       : " + rs.getString("status")
    	    );
    	}
      public static void generateBill(Connection con, Scanner sc) {

    	    try {

    	        System.out.print("Enter order ID: ");
    	        int orderId = sc.nextInt();
    	        sc.nextLine();

    	        String sql =
    	                "SELECT o.order_id, o.customer_id, " +
    	                "o.order_date, o.total_amount, o.status, " +
    	                "m.item_name, oi.quantity, oi.price " +
    	                "FROM orders o " +
    	                "JOIN order_items oi " +
    	                "ON o.order_id = oi.order_id " +
    	                "JOIN menu m " +
    	                "ON oi.menu_id = m.menu_id " +
    	                "WHERE o.order_id = ?";

    	        PreparedStatement ps =
    	                con.prepareStatement(sql);

    	        ps.setInt(1, orderId);

    	        ResultSet rs = ps.executeQuery();

    	        boolean found = false;

    	        double totalAmount = 0;

    	        while (rs.next()) {

    	            if (!found) {

    	                found = true;

    	                displayBillHeader(rs);

    	                totalAmount =
    	                        rs.getDouble("total_amount");
    	            }

    	            displayBillItem(rs);
    	        }

    	        if (found) {

    	            System.out.println("--------------------------------------");

    	            System.out.println(
    	                    "Total Amount : " + totalAmount
    	            );

    	            System.out.println(
    	                    "======================================"
    	            );

    	        } else {

    	            System.out.println("Order ID not found.");
    	        }

    	    } catch (Exception e) {

    	        e.printStackTrace();
    	    }
    	
    	}
      public static void displayBillHeader(ResultSet rs) throws Exception {

    	    System.out.println("--------------------------------------");

    	    System.out.println(
    	            "Order ID      : " + rs.getInt("order_id")
    	    );

    	    System.out.println(
    	            "Customer ID   : " + rs.getInt("customer_id")
    	    );

    	    System.out.println(
    	            "Order Date    : " + rs.getTimestamp("order_date")
    	    );

    	    System.out.println(
    	            "Status        : " + rs.getString("status")
    	    );

    	    System.out.println("--------------------------------------");
    	}
      public static void displayBillItem(ResultSet rs) throws Exception {

    	    int quantity = rs.getInt("quantity");
    	    double price = rs.getDouble("price");

    	    double itemTotal = quantity * price;

    	    System.out.println(
    	            "Item : " + rs.getString("item_name") +
    	            " | Quantity : " + quantity +
    	            " | Price : " + price +
    	            " | Total : " + itemTotal
    	    );
    	}
      
    }
