package ui;

import db.DatabaseConnection;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Statistical extends JPanel {

    public Statistical() {
        setLayout(new BorderLayout());  // Sử dụng BorderLayout cho panel chính

        // Tạo bảng và model
        String[] columnNames = {"Mã nhân viên", "Ngày làm việc", "Thời gian check-in", "Thời gian check-out", "Tổng giờ làm việc"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);  // Đảm bảo bảng chiếm toàn bộ chiều cao
        table.setRowHeight(30);  // Thiết lập chiều cao mỗi dòng
        table.setSelectionBackground(new Color(85, 153, 255));  // Màu nền khi chọn dòng

        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(model);
        table.setRowSorter(rowSorter);

        // Tạo các ô tìm kiếm
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 240, 240));  // Màu nền cho panel tìm kiếm
        JLabel labelSearch = new JLabel("Tìm kiếm:");
        JTextField searchField = new JTextField(30);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterData(searchField.getText(), rowSorter);
            }

            public void removeUpdate(DocumentEvent e) {
                filterData(searchField.getText(), rowSorter);
            }

            public void changedUpdate(DocumentEvent e) {
                filterData(searchField.getText(), rowSorter);
            }
        });

        searchPanel.add(labelSearch);
        searchPanel.add(searchField);

        // Tạo các JComboBox cho tháng và năm
        JPanel dateFilterPanel = new JPanel();
        dateFilterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel labelMonth = new JLabel("Tháng:");
        JComboBox<String> monthComboBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            monthComboBox.addItem(String.format("%02d", i));  // Thêm các tháng từ 01 đến 12
        }

        JLabel labelYear = new JLabel("Năm:");
        JComboBox<String> yearComboBox = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 5; i <= currentYear; i++) {
            yearComboBox.addItem(String.valueOf(i));  // Thêm năm từ hiện tại trừ đi 5 năm
        }

        yearComboBox.setSelectedItem(String.valueOf(currentYear));

        dateFilterPanel.add(labelMonth);
        dateFilterPanel.add(monthComboBox);
        dateFilterPanel.add(labelYear);
        dateFilterPanel.add(yearComboBox);

        // Lắng nghe sự kiện thay đổi tháng và năm để lọc dữ liệu
        monthComboBox.addActionListener(e -> filterByDate(rowSorter, monthComboBox, yearComboBox));
        yearComboBox.addActionListener(e -> filterByDate(rowSorter, monthComboBox, yearComboBox));

        // Tạo JScrollPane để hiển thị bảng
        JScrollPane scrollPane = new JScrollPane(table);

        // Thêm các thành phần vào panel chính
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(dateFilterPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);  // Thêm topPanel vào phần trên của BorderLayout
        add(scrollPane, BorderLayout.CENTER);  // Thêm bảng vào phần giữa của BorderLayout

        // Kết nối và truy xuất dữ liệu từ cơ sở dữ liệu
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT id, date, checkin_time, checkout_time FROM checkin_checkout";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                Date date = rs.getDate("date");
                Timestamp checkinTime = rs.getTimestamp("checkin_time");
                Timestamp checkoutTime = rs.getTimestamp("checkout_time");

                // Tính tổng giờ làm việc
                String workHours = "";
                if (checkinTime != null && checkoutTime != null) {
                    long diffInMillis = checkoutTime.getTime() - checkinTime.getTime();
                    long hours = diffInMillis / (1000 * 60 * 60);
                    long minutes = (diffInMillis % (1000 * 60 * 60)) / (1000 * 60);
                    workHours = hours + " giờ " + minutes + " phút";
                } else if (checkinTime != null && checkoutTime == null) {
                    workHours = "0 giờ 0 phút";
                } else {
                    workHours = "Chưa chấm công";
                }

                Object[] row = {
                    id,
                    new SimpleDateFormat("dd/MM/yyyy").format(date),
                    checkinTime != null ? new SimpleDateFormat("HH:mm").format(checkinTime) : "Chưa check-in",
                    checkoutTime != null ? new SimpleDateFormat("HH:mm").format(checkoutTime) : "Chưa check-out",
                    workHours
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi truy xuất dữ liệu", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hàm lọc dữ liệu theo tháng và năm
    private static void filterByDate(TableRowSorter<DefaultTableModel> rowSorter, JComboBox<String> monthComboBox, JComboBox<String> yearComboBox) {
        String selectedMonth = (String) monthComboBox.getSelectedItem();
        String selectedYear = (String) yearComboBox.getSelectedItem();

        if (selectedMonth == null || selectedYear == null) {
            return;
        }

        RowFilter<DefaultTableModel, Object> monthYearFilter = new RowFilter<DefaultTableModel, Object>() {
            public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
                String dateValue = entry.getStringValue(1);  // Cột ngày (index 1)

                String[] dateParts = dateValue.split("/");
                String month = dateParts[1];
                String year = dateParts[2];

                return year.equals(selectedYear) && month.equals(selectedMonth);
            }
        };

        rowSorter.setRowFilter(monthYearFilter);
    }

    // Hàm lọc dữ liệu trong bảng
    private static void filterData(String searchText, TableRowSorter<DefaultTableModel> rowSorter) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter("(?i)" + searchText);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        rowSorter.setRowFilter(rf);
    }
}
