/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;
import java.awt.*;
import javax.swing.JButton;
/**
 *
 * @author ADMIN
 */
public class RoundedButton extends JButton {
    private int cornerRadius;

    public RoundedButton(String text, int radius) {
        super(text);
        this.cornerRadius = radius;
        setFocusPainted(false); // Không hiển thị viền khi nút được chọn
        setContentAreaFilled(false); // Không tô màu cho khu vực nội dung
        setBorderPainted(false); // Tắt viền mặc định
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(Color.GRAY); // Màu khi nút được nhấn
        } else {
            g.setColor(getBackground()); // Màu khi nút ở trạng thái bình thường
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius); // Bo tròn góc
        super.paintComponent(g); // Vẽ lại nội dung của nút
    }
}
