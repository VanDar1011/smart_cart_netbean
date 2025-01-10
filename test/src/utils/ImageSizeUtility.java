/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author datth
 */
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageSizeUtility {

    public static void printImageSize(String imagePath) {
        try {
            // Load the image from the specified path
            File imgFile = new File(imagePath);
            if (!imgFile.exists()) {
                System.out.println("File not found at " + imagePath);
                return;
            }

            // Read the image
            BufferedImage bufferedImage = ImageIO.read(imgFile);

            // Convert BufferedImage to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);  // You can change "png" to other formats like "jpg"

            // Get the byte array
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            int sizeInBytes = imageBytes.length;  // Image size in bytes
            int sizeInBits = sizeInBytes * 8;    // Convert bytes to bits

            // Print the size of the image
            System.out.println("Image Size: " + sizeInBytes + " bytes");
            System.out.println("Image Size: " + sizeInBits + " bits");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading the image file.");
        }
    }
}
