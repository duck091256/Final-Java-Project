package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Test {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Label Interaction");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new FlowLayout());

        // Tạo các JLabel
        JLabel label1 = new JLabel("Tổng quan");
        JLabel label2 = new JLabel("Hàng hóa");
        JLabel label3 = new JLabel("Phòng bàn");
        JLabel label4 = new JLabel("Giao dịch");
        JLabel label5 = new JLabel("Nhân viên");

        // Thiết lập font mặc định cho tất cả các JLabel
        Font plainFont = new Font("Arial", Font.PLAIN, 20);
        Font boldFont = new Font("Arial", Font.BOLD, 20);

        label1.setFont(plainFont);
        label2.setFont(plainFont);
        label3.setFont(plainFont);
        label4.setFont(plainFont);
        label5.setFont(plainFont);

        // Thêm các label vào JFrame
        frame.add(label1);
        frame.add(label2);
        frame.add(label3);
        frame.add(label4);
        frame.add(label5);

        // Danh sách các JLabel để dễ dàng thay đổi kiểu chữ
        JLabel[] labels = { label1, label2, label3, label4, label5 };

        // Thêm MouseListener cho các JLabel
        addMouseListenerToLabels(labels, plainFont, boldFont);

        frame.setVisible(true);
    }

    private static void addMouseListenerToLabels(JLabel[] labels, Font plainFont, Font boldFont) {
        for (JLabel label : labels) {
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Khi chuột vào, nếu chưa nhấn vào, đổi kiểu chữ thành BOLD
                    if (!label.getFont().equals(boldFont)) {
                        label.setFont(boldFont);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Khi chuột rời, nếu chưa nhấn vào, quay lại kiểu chữ PLAIN
                    if (label.getFont().equals(boldFont) && label.isEnabled()) {
                        label.setFont(plainFont);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // Khi nhấn vào label, đổi tất cả các label về PLAIN trước
                    for (JLabel l : labels) {
                        l.setFont(plainFont); // Đổi tất cả các label khác thành PLAIN
                    }

                    // Đổi label hiện tại thành BOLD
                    label.setFont(boldFont);
                    label.setEnabled(false);  // Khóa lại để không thay đổi khi chuột rời đi
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // Không thay đổi gì khi nhả chuột
                }
            });
        }
    }
}
