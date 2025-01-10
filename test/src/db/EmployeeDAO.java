/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.EmployeeCheckData;

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
public static List queryDatabase(String manv) {
        List list = new ArrayList<>();
        Connection con = DatabaseConnection.connect();
        try {
            String sqlQuery = "SELECT * FROM account WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sqlQuery);
            ps.setString(1, manv);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String publicKey = rs.getString("public_key");
                String checkIn = rs.getString("checkin");
                String checkOut = rs.getString("checkout");
                list.add(manv);
                list.add(publicKey);
                list.add(checkIn);
                list.add(checkOut);
            }
            ps.close();
        } catch (Exception e) {
            System.out.println("Lỗi khi truy vấn dữ liệu từ database: " + e);
        }
        return list;
    }

public static int setTimeCheck(boolean isCheckIn, String manv, String time) {
        Connection con = DatabaseConnection.connect();
        try {
            String query = isCheckIn ? "UPDATE account set checkin = ? WHERE id = ?" : "UPDATE account set checkout = ? WHERE id = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, time);
            pst.setString(2, manv);
            pst.execute();
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

public static List<EmployeeCheckData> getAllCheckData() {
        List<EmployeeCheckData> dataList = new ArrayList<>();
         Connection con = DatabaseConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
             String query = "SELECT id, checkin, checkout FROM account";
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String employeeId = rs.getString("id");
                String checkIn = rs.getString("checkin");
                String checkOut = rs.getString("checkout");

                dataList.add(new EmployeeCheckData(employeeId, checkIn, checkOut));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }
public static List<EmployeeCheckData> getCheckDataByEmployeeId(String employeeCode) {
        List<EmployeeCheckData> dataList = new ArrayList<>();
         Connection con = DatabaseConnection.connect();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
        String query = "SELECT id, checkin, checkout FROM account WHERE id = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1, employeeCode);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String code = rs.getString("id");
                String checkIn = rs.getString("checkin");
                String checkOut = rs.getString("checkout");

                dataList.add(new EmployeeCheckData(code, checkIn, checkOut));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }
}
