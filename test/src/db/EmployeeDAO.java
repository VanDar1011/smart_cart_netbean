/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


/**
 *
 * @author datth
 */
public class EmployeeDAO {

    public static String getAccountById(String id) throws SQLException {
        Connection con = DatabaseConnection.connect();
        String query = "SELECT * FROM account WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String publicKey = "";

        try {
            // Tạo Statement và thực hiện truy vấn
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Assuming 'account' table has columns 'id' and 'public_key'
                String accountId = rs.getString("id");
                publicKey = rs.getString("public_key");
                System.out.println("ID: " + accountId + ", publickey: " + publicKey);
            } else {
                System.out.println("Account not found.");
            }
        } finally {
            // Ensure ResultSet, PreparedStatement, and Connection are closed
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }

            DatabaseConnection.disconnect(); // Disconnect after the operation
        }

        return publicKey;
    }

    public static void updatePublicKey(String publicKey, int id) {
        Connection con = DatabaseConnection.connect();
        String query = "UPDATE account SET public_key = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, publicKey); // Set the public key
            pstmt.setInt(2, id); // Set the account ID
            int rowsUpdated = pstmt.executeUpdate(); // Execute the update query

            if (rowsUpdated > 0) {
                System.out.println("Public key updated successfully for account ID: " + id);
            } else {
                System.out.println("No rows were updated. Account ID not found: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close(); // Close the connection
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void createAccount(String id, String publicKey) {
        Connection con = DatabaseConnection.connect();
        String query = "INSERT INTO account (id, public_key) VALUES (?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            // Gán giá trị id (kiểu String) vào tham số đầu tiên
            pstmt.setString(1, id);

            // Gán giá trị publicKey (kiểu String) vào tham số thứ hai
            pstmt.setString(2, publicKey);

            int rowsInserted = pstmt.executeUpdate(); // Thực hiện câu lệnh insert

            if (rowsInserted > 0) {
                System.out.println("Account created successfully with ID: " + id);
            } else {
                System.out.println("Failed to create account.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi SQL
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close(); // Đảm bảo đóng kết nối sau khi hoàn thành
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    


    public static boolean checkin(String accountId, String customDateTime) {
        String queryCheckExisting = "SELECT * FROM checkin_checkout WHERE id = ? AND date = CAST(? AS DATE)";
        String insertCheckin = "INSERT INTO checkin_checkout (id, checkin_time, date) VALUES (?, CAST(? AS TIMESTAMP), CAST(? AS DATE))";

        try (Connection conn = DatabaseConnection.connect(); PreparedStatement checkStmt = conn.prepareStatement(queryCheckExisting); PreparedStatement insertStmt = conn.prepareStatement(insertCheckin)) {

            // Kiểm tra đã check-in chưa
            checkStmt.setString(1, accountId);
            checkStmt.setString(2, customDateTime.substring(0, 10)); // Lấy phần ngày (yyyy-MM-dd)
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "Bạn đã check-in hôm nay.");
                return false; // Đã check-in
            }

            // Thực hiện check-in với thời gian custom
            insertStmt.setString(1, accountId);
            insertStmt.setString(2, customDateTime); // Chuyển toàn bộ customDateTime thành TIMESTAMP
            insertStmt.setString(3, customDateTime.substring(0, 10)); // Chỉ lấy ngày
            insertStmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Check-in thành công!");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkout(String accountId, String customDateTime) {
        String queryCheckExisting = "SELECT * FROM checkin_checkout WHERE id = ? AND date = ?";
        String queryCheckCheckedOut = "SELECT checkout_time FROM checkin_checkout WHERE id = ? AND date = ?";
        String updateCheckout = "UPDATE checkin_checkout SET checkout_time = ? WHERE id = ? AND date = ?";

        try (
                Connection conn = DatabaseConnection.connect(); PreparedStatement checkStmt = conn.prepareStatement(queryCheckExisting); PreparedStatement checkOutStmt = conn.prepareStatement(queryCheckCheckedOut); PreparedStatement updateStmt = conn.prepareStatement(updateCheckout)) {
            // Chuyển đổi customDateTime thành Date (yyyy-MM-dd)
            String datePart = customDateTime.substring(0, 10);  // Lấy phần ngày từ chuỗi (yyyy-MM-dd)
            Date date = Date.valueOf(datePart);  // Chuyển thành đối tượng Date

            // Kiểm tra đã check-in hôm nay chưa bằng customDateTime
            checkStmt.setString(1, accountId);
            checkStmt.setDate(2, date);  // Sử dụng Date để so sánh
            ResultSet rsCheckIn = checkStmt.executeQuery();

            if (!rsCheckIn.next()) {
                JOptionPane.showMessageDialog(null, "Bạn chưa check-in hôm nay.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return false; // Chưa check-in
            }

            // Kiểm tra đã check-out hôm nay chưa
            checkOutStmt.setString(1, accountId);
            checkOutStmt.setDate(2, date);  // Sử dụng Date để so sánh
            ResultSet rsCheckOut = checkOutStmt.executeQuery();
            if (rsCheckOut.next()) {
                Timestamp checkoutTime = rsCheckOut.getTimestamp("checkout_time");
                if (checkoutTime != null) {
                    JOptionPane.showMessageDialog(null, "Bạn đã check-out rồi: ", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return false; // Đã check-out trước đó
                }
            }

            // Chuyển đổi customDateTime thành Timestamp (bao gồm cả thời gian)
            Timestamp timestamp = Timestamp.valueOf(customDateTime);  // Chuyển đổi toàn bộ chuỗi (yyyy-MM-dd HH:mm:ss)

            // Thực hiện check-out với customDateTime
            updateStmt.setTimestamp(1, timestamp);
            updateStmt.setString(2, accountId);
            updateStmt.setDate(3, date);  // Sử dụng Date để so sánh
            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Check-out thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Không thể thực hiện check-out. Hãy thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

}
