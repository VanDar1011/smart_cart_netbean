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

public class ImageSelectorComponent {

    public static Image selectedImage = null;

    // Phương thức để tạo thành phần chọn ảnh
    public void openImageSelector(JFrame parentFrame, JLabel labelToUpdate) {
        // Tạo JFileChooser để hiển thị hộp thoại chọn ảnh
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn ảnh");
        File defaultDirectory = new File("C:\\Users\\datth\\OneDrive\\Máy tính\\avt"); // Thay đường dẫn này bằng đường dẫn thư mục bạn muốn
        fileChooser.setCurrentDirectory(defaultDirectory);

        // Lọc file ảnh (JPG, PNG, GIF)
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Ảnh (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        // Hiển thị hộp thoại chọn file
        int result = fileChooser.showOpenDialog(parentFrame);

        // Nếu người dùng chọn file
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Cập nhật ảnh vào JLabel
                Image image = ImageIO.read(selectedFile); // Đọc ảnh từ file
                image = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Thay đổi kích thước ảnh
                labelToUpdate.setIcon(new ImageIcon(image)); // Cập nhật JLabel bằng ảnh đã chọn
                // Lưu vào biến static để sử dụng sau này
                selectedImage = ImageIO.read(selectedFile);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
