/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author datth
 */
public class Employee {

    String employee_code;
    String name;
    String birthday;
    String position;
    Image avt;
    String pin_code;

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public Image getAvt() {
        return avt;
    }

    public void setAvt(Image avt) {
        this.avt = avt;
    }

    public Employee() {
    }

    public Employee(String employee_code, String name, String birthday, String position, Image avt) {
        this.employee_code = employee_code;
        this.name = name;
        this.birthday = birthday;
        this.position = position;
        this.avt = avt;
    }

    public String getEmployee_code() {
        return employee_code;
    }

    public void setEmployee_code(String employee_code) {
        this.employee_code = employee_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

//    public byte[] toBytes() throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//
//        // Nối các trường String với dấu phân cách ";"
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(employee_code).append(";");
//        stringBuilder.append(name).append(";");
//        stringBuilder.append(birthday).append(";");
//        stringBuilder.append(position).append(";");
//
//        // Chuyển chuỗi thành byte và ghi vào ByteArrayOutputStream
//        
//
//        // Chuyển đổi hình ảnh thành byte nếu có
//        if (avt != null) {
//            System.out.println("Value of avt : " + avt);
//            byteArrayOutputStream.write(stringBuilder.toString().getBytes());
//            ByteArrayOutputStream imageByteArrayOutputStream = new ByteArrayOutputStream();
//            ImageIO.write((java.awt.image.BufferedImage) avt, "png", imageByteArrayOutputStream);  // Chuyển ảnh thành byte
//            byteArrayOutputStream.write(imageByteArrayOutputStream.toByteArray());
//        } else {
//            String s = "abcd";
//            stringBuilder.append(s);
//            byteArrayOutputStream.write(stringBuilder.toString().getBytes());
//        }
//        
//        return byteArrayOutputStream.toByteArray();
//    }
    public byte[] toBytes() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    // Build the string with fields separated by ";"
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(employee_code).append(";");
    stringBuilder.append(name).append(";");
    stringBuilder.append(birthday).append(";");
    stringBuilder.append(position).append(";");

    // Write the string fields to the output stream
    byteArrayOutputStream.write(stringBuilder.toString().getBytes());

    // Handle the image field (avt)
    if (avt != null) {
        System.out.println("Value of avt: " + avt);

        // Assuming avt is a BufferedImage, convert it to byte[] and write it to the stream
        if (avt instanceof java.awt.image.BufferedImage) {
            ByteArrayOutputStream imageByteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write((java.awt.image.BufferedImage) avt, "png", imageByteArrayOutputStream);  // Convert image to bytes
            byteArrayOutputStream.write(imageByteArrayOutputStream.toByteArray());
        } else {
            throw new IOException("avt is not a BufferedImage.");
        }
    } else {
        // If avt is null, append a placeholder or handle accordingly
        byteArrayOutputStream.write("No image data".getBytes());
    }

    // Return the final byte array
    return byteArrayOutputStream.toByteArray();
}


    public static String bytesToHex(byte[] byteArray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : byteArray) {
            sb.append(String.format("%02X ", b));  // Format each byte as a 2-digit hex value
        }
        return sb.toString().trim();  // Remove trailing space
    }

}
