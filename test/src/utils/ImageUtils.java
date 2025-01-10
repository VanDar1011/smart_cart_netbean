package utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author datth
 */
public class ImageUtils {

    public static byte[] imageToBytes(Image image, String format) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }

        // Chuyển Image thành BufferedImage
        BufferedImage bufferedImage = toBufferedImage(image);

        // Ghi BufferedImage vào ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, byteArrayOutputStream);

        // Trả về mảng byte
        return byteArrayOutputStream.toByteArray();
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bufferedImage = new BufferedImage(
                img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(img, 0, 0, null);
        return bufferedImage;
    }

    public static String imageToHex(Image img) {
        // Convert Image to BufferedImage
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(img, 0, 0, null);

        // Convert BufferedImage to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // Write the image as PNG to the ByteArrayOutputStream
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert byte array to Hexadecimal String
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        StringBuilder hexString = new StringBuilder();
        for (byte b : imageBytes) {
            // Convert each byte to hex and append to the StringBuilder
            hexString.append(String.format("%02X", b));
        }

        return hexString.toString();
    }

    // Convert Hexadecimal String to Image
    public static Image hexToImage(String hexString) {
        try {
            // Convert Hex String to byte array
            byte[] imageBytes = StringUtils.hexStringToByteArray(hexString);

            // Convert byte array to Image
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            return ImageIO.read(byteArrayInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;  // Return null if there's an error
    }

    // Helper method to convert hex string to byte array
//    private static byte[] hexStringToByteArray(String hexString) {
//        int len = hexString.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
//                    + Character.digit(hexString.charAt(i + 1), 16));
//        }
//        return data;
//    }
    public static void printImageSize(Image img) {
        if (img == null) {
            System.out.println("Image is null");
            return;
        }

        try {
            // Chuyển đổi Image thành BufferedImage
            BufferedImage bufferedImage = (BufferedImage) img;

            // Chuyển đổi BufferedImage thành mảng byte
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);  // Bạn có thể thay đổi định dạng nếu cần

            byte[] imageBytes = byteArrayOutputStream.toByteArray();  // Chuyển đổi thành mảng byte
            int sizeInBytes = imageBytes.length;  // Kích thước hình ảnh tính theo byte
            int sizeInBits = sizeInBytes * 8;    // Chuyển đổi sang bit

            // In kích thước ra console
            System.out.println("Image Size: " + sizeInBytes + " bytes");
            System.out.println("Image Size: " + sizeInBits + " bits");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayImage(byte[] imageData, JLabel label) {
        try {
            // Chuyển đổi byte[] thành BufferedImage
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));

            // Lấy kích thước gốc của ảnh
            int originalWidth = bufferedImage.getWidth();
            int originalHeight = bufferedImage.getHeight();

            // Lấy kích thước của JLabel
            int labelWidth = label.getWidth();
            int labelHeight = label.getHeight();

            // Tính tỷ lệ chiều rộng và chiều cao
            double widthRatio = (double) labelWidth / originalWidth;
            double heightRatio = (double) labelHeight / originalHeight;

            // Chọn tỷ lệ nhỏ nhất để đảm bảo ảnh vừa với JLabel mà không bị biến dạng
            double ratio = Math.min(widthRatio, heightRatio);

            // Tính toán kích thước mới cho ảnh
            int newWidth = (int) (originalWidth * ratio);
            int newHeight = (int) (originalHeight * ratio);

            // Thay đổi kích thước ảnh để vừa vặn với JLabel nhưng vẫn giữ tỷ lệ
            Image scaledImage = bufferedImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            // Tạo ImageIcon từ ảnh đã thay đổi kích thước
            ImageIcon icon = new ImageIcon(scaledImage);

            // Đặt ImageIcon cho JLabel
            label.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
