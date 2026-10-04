package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class MainApplication {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/restaurant_db";
        String username = "root";
        String password = "Mokshitha@123";

        try {

            // 1. Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Create connection only once
            Connection con = DriverManager.getConnection(
                    url,
                    username,
                    password
            );

            System.out.println(
                    "Database connected successfully!"
            );

            // 3. Create tables before choices
            DatabaseOperations.createTables(con);

            Scanner sc = new Scanner(System.in);

            boolean running = true;

            while (running) {

                System.out.println(
                        "\n========== RESTAURANT FOOD ORDERING SYSTEM =========="
                );

                System.out.println("1. Customer");
                System.out.println("2. Restaurant Owner");
                System.out.println("3. Exit");

                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                case 1:

                    boolean customerRunning = true;

                    while (customerRunning) {

                        System.out.println(
                                "\n========== CUSTOMER =========="
                        );

                        System.out.println("1. Customer Registration");
                        System.out.println("2. View Menu");
                        System.out.println("3. Place Order");
                        System.out.println("4. Order History");
                        System.out.println("5. Bill Generation");
                        System.out.println("6. Cancel Order");
                        System.out.println("7. Delete Account");
                        System.out.println("8. Back");

                        System.out.print("Enter your choice: ");
                        int customerChoice = sc.nextInt();
                        sc.nextLine();

                        switch (customerChoice) {

                            case 1:
                                DatabaseOperations.addCustomer(con, sc);
                                break;

                            case 2:
                                DatabaseOperations.viewMenu(con);
                                break;

                            case 3:
                                DatabaseOperations.placeOrder(con, sc);
                                break;

                            case 4:
                                DatabaseOperations.viewOrderHistory(con, sc);
                                break;

                            case 5:
                                DatabaseOperations.generateBill(con, sc);
                                break;

                            case 6:
                                DatabaseOperations.cancelOrder(con, sc);
                                break;

                            case 7:
                                DatabaseOperations.deleteAccount(con, sc);
                                break;

                            case 8:
                                customerRunning = false;
                                System.out.println("Returning to main menu...");
                                break;
                                
                        }
                    }

                    break;
                case 2:

                    boolean ownerRunning = true;

                    while (ownerRunning) {

                        System.out.println(
                                "\n========== RESTAURANT OWNER =========="
                        );

                        System.out.println("1. Add Menu Item");
                        System.out.println("2. View Menu");
                        System.out.println("3. View Customers");
                        System.out.println("4. Update Menu Item");
                        System.out.println("5. Delete Menu Item");
                        System.out.println("6. Batch Add Menu Items");
                        System.out.println("7. Back");

                        System.out.print("Enter your choice: ");
                        int ownerChoice = sc.nextInt();
                        sc.nextLine();

                        switch (ownerChoice) {
                        case 1:
                            DatabaseOperations.addMenuItem(con, sc);
                            break;

                        case 2:
                            DatabaseOperations.viewMenu(con);
                            break;

                        case 3:
                            DatabaseOperations.viewCustomers(con);
                            break;

                        case 4:
                            DatabaseOperations.updateMenuItem(con, sc);
                            break;

                        case 5:
                            DatabaseOperations.deleteMenuItem(con, sc);
                            break;

                        case 6:
                            DatabaseOperations.batchAddMenuItems(con, sc);
                            break;

                        case 7:
                            ownerRunning = false;
                            System.out.println("Returning to main menu...");
                            break;
                            default:
                                System.out.println(
                                        "Invalid choice. Please try again."
                                );
                        }
                    }

                    break;

                    case 3:
                        running = false;
                        System.out.println(
                                "Thank you for using the system!"
                        );
                        break;
                }
            }

            sc.close();
            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}