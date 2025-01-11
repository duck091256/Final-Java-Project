package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import model.RankingStaff;
import service.RatingCalculation;

public class Test {

    public static JPanel createStaffPanel() {
        // Tạo JPanel để chứa các JLabel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Lấy danh sách nhân viên từ service
        ArrayList<RankingStaff> staffList = RatingCalculation.getListOfRatingStaff();

        if (staffList == null || staffList.isEmpty()) {
            JLabel noDataLabel = new JLabel("No staff data available.");
            noDataLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            noDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(noDataLabel);
        } /* else {
            for (RankingStaff staff : staffList) {
                // Tạo JLabel cho từng nhân viên
                JLabel staffLabel = new JLabel(staff.getStaffName() + " - Total Sales: " + staff.getTotalSales());
                staffLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                panel.add(staffLabel);
            }
        }*/

        return panel;
    }

    public static void main(String[] args) {
        // Tạo JFrame chính
        JFrame frame = new JFrame("Staff Ratings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        // Tạo JPanel chính
        JPanel overallPanel = new JPanel();
        overallPanel.setLayout(null);

        // Tạo JPanel panel_rush_hour
        JPanel panel_rush_hour = new JPanel();
        panel_rush_hour.setLayout(null);
        panel_rush_hour.setBorder(BorderFactory.createLineBorder(new Color(45, 61, 75), 1));
        panel_rush_hour.setBackground(Color.WHITE);
        panel_rush_hour.setBounds(899, 33, 345, 486);

        // Lấy panel hiển thị danh sách nhân viên
        JPanel staffPanel = createStaffPanel();

        // Thêm JScrollPane để cuộn nếu danh sách quá dài
        JScrollPane scrollPane = new JScrollPane(staffPanel);
        scrollPane.setBounds(10, 10, 325, 466); // Set vị trí và kích thước của scrollPane trong panel_rush_hour
        panel_rush_hour.add(scrollPane);

        // Thêm panel_rush_hour vào overallPanel
        overallPanel.add(panel_rush_hour);

        // Thêm overallPanel vào frame
        frame.add(overallPanel);

        // Hiển thị JFrame
        frame.setVisible(true);
    }
}