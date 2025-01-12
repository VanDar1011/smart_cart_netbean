package ui;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author datth
 */
import javax.swing.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ImageSelectorComponent {

    public static Image selectedImage = null;

    // Phương thức để tạo thành phần chọn ảnh
    public void openImageSelector(JFrame parentFrame, JLabel labelToUpdate) {
        // Tạo JFileChooser để hiển thị hộp thoại chọn ảnh
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh");
//        File defaultDirectory = new File("C:\\Users\\datth\\OneDrive\\Máy tính\\avt"); // Thay đường dẫn này bằng đường dẫn thư mục bạn muốn
        File defaultDirectory = new File("./avt");
        fileChooser.setCurrentDirectory(defaultDirectory);
        // Lọc file ảnh (JPG, PNG, GIF)
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Ảnh (JPG, PNG)", "jpg", "jpeg", "png"));

        // Hiển thị hộp thoại chọn file
        int result = fileChooser.showOpenDialog(parentFrame);

        // Nếu người dùng chọn file
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String extension = getFileExtension(selectedFile);
                if ("jpg".equalsIgnoreCase(extension) || "jpeg".equalsIgnoreCase(extension)) {
                    BufferedImage image = ImageIO.read(selectedFile);
                    // Chuyển ảnh sang PNG và lưu
                    // File outputFile = new File("./avt/" + selectedFile.getName().replaceAll("\\.jpg|\\.jpeg", ".png"));
                    File outputFile = new File("./avt/convert_image.png");

                    ImageIO.write(image, "PNG", outputFile);  // Lưu ảnh ở định dạng PNG

                    // Đọc lại ảnh đã chuyển sang PNG
                    selectedImage = ImageIO.read(outputFile);
                    // Log to debug
                    System.out.println("Ảnh đã được chuyển sang định dạng PNG!");
                }
                selectedImage = ImageIO.read(selectedFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Phương thức để lấy phần mở rộng của file
    private String getFileExtension(File file) {
        String extension = "";
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }
}
