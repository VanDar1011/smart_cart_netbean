/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author datth
 */
public class StringUtils {

    /**
     * Checks if a string is null.
     *
     * @param str the string to check
     * @return true if the string is null, false otherwise
     */
    public static boolean isNull(String str) {
        return str == null;
    }

    /**
     * Checks if a string is empty (""), but not null.
     *
     * @param str the string to check
     * @return true if the string is empty, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str != null && str.isEmpty();
    }

    /**
     * Checks if a string is blank (null, empty, or only whitespace).
     *
     * @param str the string to check
     * @return true if the string is blank, false otherwise
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Checks if a string is not null and not empty.
     *
     * @param str the string to check
     * @return true if the string is not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * Checks if a string is not blank (not null, not empty, and not only
     * whitespace).
     *
     * @param str the string to check
     * @return true if the string is not blank, false otherwise
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param byteArray The byte array to convert.
     * @return The hexadecimal string.
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            // Chuyển mỗi byte thành chuỗi hex và thêm vào StringBuilder
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }

    /**
     * Converts a hex string to a byte array.
     *
     * @param hexString The hex string to convert.
     * @return The corresponding byte array.
     */
    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
     public static String getRandomString(int length) {
        // Các ký tự có thể có trong chuỗi ngẫu nhiên
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder randomString = new StringBuilder(length);

        // Tạo chuỗi ngẫu nhiên dựa trên độ dài yêu cầu
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            randomString.append(characters.charAt(randomIndex));
        }

        return randomString.toString();
    }
    /**
     * Splits the hex byte array by the semicolon (0x3B) byte.
     *
     * @param hexa The byte array to split.
     * @return A list of byte arrays, each representing a part.
     */
    public static List<byte[]> splitHexArrayBySemicolon(byte[] hexa) {
        List<byte[]> parts = new ArrayList<>();
        List<Byte> currentPart = new ArrayList<>();

        for (byte b : hexa) {
            if (b == 0x3B) {  // 0x3B is the semicolon byte
                // Add the current part to the list and reset for the next part
                parts.add(toByteArray(currentPart));
                currentPart.clear();
            } else {
                currentPart.add(b);
            }
        }

        // Add the last part (after the final semicolon)
        if (!currentPart.isEmpty()) {
            parts.add(toByteArray(currentPart));
        }

        return parts;
    }

    /**
     * Converts a list of Byte objects into a byte array.
     *
     * @param byteList The list of Byte objects.
     * @return A byte array.
     */
    private static byte[] toByteArray(List<Byte> byteList) {
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }

    /**
     * Converts a byte array to a hexArrayToText
     *
     * @param bytes The byte array to convert.
     * @return The text like ASCII or UTF-8
     */
    public static String hexArrayToText(byte[] bytes) {
    // Convert the hex string to a byte array
    return new String(bytes);
}
}
