package ui;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private Image backgroundImage;

    // Constructor nhận đường dẫn tới hình ảnh
    public ImagePanel(String imagePath) {
        // Đọc hình ảnh từ tài nguyên
        backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Vẽ hình ảnh làm background
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
