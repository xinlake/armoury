package armoury.common;

import android.util.Patterns;

public class Validator {
    public static boolean isPort(int port) {
        return (port > 0) && (port < 65536);
    }

    public static boolean isIpAddress(String address) {
        return Patterns.IP_ADDRESS.matcher(address).matches();
    }

    /**
     * Check if email string is valid format.
     * @param email input string
     * @return boolean email format validation
     */
    public static boolean isEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Check if string only contains alpha dash numeric underscore and hyphen.
     * @param value input string
     * @return boolean
     */
    public static boolean isAlphaDash(String value) {
        return value.matches("^[a-zA-Z0-9-_]*$");
    }

    /**
     * Check if string only contain alpha dash.
     * @param value input string
     * @return boolean
     */
    public static boolean isAlphaNumeric(String value) {
        return value.matches("^[a-zA-Z0-9]*$");
    }

    /**
     * Check if input string only contain signed numeric.
     * @param value input string
     * @return boolean
     */
    public static boolean isNumeric(Object value) {
        return isNumeric(value, false);
    }

    /**
     * Check if input string only contain numeric signed and unsigned number depends on parameter.
     * @param value        input string
     * @param isSignedOnly is allow signed number only or both
     * @return boolean
     */
    public static boolean isNumeric(Object value, boolean isSignedOnly) {
        try {
            int result = Integer.parseInt(value.toString());
            return !isSignedOnly || result >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if string is not member of collection data.
     * @param value   input string
     * @param dataSet collection of string data
     * @return boolean
     */
    public static boolean isUnique(String value, String[] dataSet) {
        for (String data : dataSet) {
            if (data.equals(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if integer is not member of collection data.
     * @param value   input integer
     * @param dataSet collection of integer data
     * @return boolean
     */
    public static boolean isUnique(int value, int[] dataSet) {
        for (int data : dataSet) {
            if (data == value) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a string member of data set, ignoring number of data matched.
     * @param value   input string
     * @param dataSet collection of string data
     * @return boolean
     */
    public static boolean isMemberOf(String value, String[] dataSet) {
        for (String data : dataSet) {
            if (data.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a integer member of data set, ignoring number of data matched.
     * @param value   input string
     * @param dataSet collection of integer data
     * @return boolean
     */
    public static boolean isMemberOf(int value, int[] dataSet) {
        for (int data : dataSet) {
            if (data == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Build slug from string title like "The beautiful day in 1992" turns "the-beautiful-day-in-1992"
     * or "Super massive black hole O'creaz MO on July" turns "super-massive-black-hole-o-creaz-mo-on-july"
     * @param title article title
     * @return slug string
     */
    public static String createSlug(String title) {
        String trimmed = title.trim();
        String slug = trimmed
            .replaceAll("[^a-zA-Z0-9-]", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-$", "");
        return slug.toLowerCase();
    }
}
