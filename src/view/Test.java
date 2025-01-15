package view;

import javax.swing.*;
import java.awt.*;
import service.RatingCalculation;

public class Test {

    public static JPanel createIncomePanel() {
        // Tạo JPanel để hiển thị tổng bill
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(BorderFactory.createLineBorder(new Color(45, 61, 75), 1));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Total Bill Sales: ");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBounds(20, 20, 300, 30);
        panel.add(label);

        // Tính tổng bill bán được trong tuần
        double totalBill = RatingCalculation.getTotalBillThisWeek();

        JLabel totalLabel = new JLabel(totalBill >= 0 ? String.valueOf(totalBill) : "Error calculating total");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(totalBill >= 0 ? Color.BLUE : Color.RED);
        totalLabel.setBounds(320, 20, 200, 30);
        panel.add(totalLabel);

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

        // Tạo JPanel panel_incomes
        JPanel panel_incomes = new JPanel();
        panel_incomes.setLayout(null);
        panel_incomes.setBorder(BorderFactory.createLineBorder(new Color(45, 61, 75), 1));
        panel_incomes.setBackground(Color.WHITE);
        panel_incomes.setBounds(36, 33, 823, 146);

        // Thêm chức năng hiển thị tổng bill vào panel_incomes
        JPanel incomePanel = createIncomePanel();
        incomePanel.setBounds(10, 10, 803, 126);
        panel_incomes.add(incomePanel);

        overallPanel.add(panel_incomes);

        // Thêm overallPanel vào frame
        frame.add(overallPanel);

        // Hiển thị JFrame
        frame.setVisible(true);
    }
}