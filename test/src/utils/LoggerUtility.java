/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author datth
 */
import java.util.logging.Logger;

public class LoggerUtility {

    // Phương thức tạo logger cho lớp động
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }

    // Phương thức ghi log với mức độ thông tin (INFO)
    public static void info(Class<?> clazz, String message) {
        Logger logger = getLogger(clazz);
        logger.info(message);
    }

    // Phương thức ghi log với mức độ cảnh báo (WARNING)
    public static void warn(Class<?> clazz, String message) {
        Logger logger = getLogger(clazz);
        logger.warning(message);
    }

    // Phương thức ghi log với mức độ lỗi (SEVERE)
    public static void error(Class<?> clazz, String message) {
        Logger logger = getLogger(clazz);
        logger.severe(message);
    }
}
