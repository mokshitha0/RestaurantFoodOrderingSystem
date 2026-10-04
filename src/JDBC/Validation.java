package JDBC;

public class Validation {

    // Check whether input is empty
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    // Check whether input contains only alphabets and spaces
    public static boolean containsOnlyAlphabets(String value) {
        return value != null && value.matches("[A-Za-z ]+");
    }
 // Check whether input contains alphabets, numbers and spaces
    public static boolean isValidItemName(String value) {
        return value != null && value.matches("[A-Za-z0-9 ]+");
    }

    // Check whether input contains only numbers
    public static boolean containsOnlyNumbers(String value) {
        return value != null && value.matches("\\d+");
    }

    // Validate email
    public static boolean isValidEmail(String email) {
        return email != null &&
               email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Validate phone number
    public static boolean isValidPhone(String phone) {
        return phone != null &&
               phone.matches("[6-9][0-9]{9}");
    }

    // Validate positive integer
    public static boolean isValidQuantity(String value) {

        if (!containsOnlyNumbers(value)) {
            return false;
        }

        int quantity = Integer.parseInt(value);

        return quantity > 0;
    }

    // Validate positive price
    public static boolean isValidPrice(String value) {

        try {
            double price = Double.parseDouble(value);

            return price > 0;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}