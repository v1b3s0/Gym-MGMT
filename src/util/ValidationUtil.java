package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ValidationUtil {
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static boolean isPositiveInteger(String text) {
        try {
            int number = Integer.parseInt(text.trim());
            return number > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{7,15}");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isPositiveDouble(String text) {
        try {
            double number = Double.parseDouble(text.trim());
            return number > 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date.trim());
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
