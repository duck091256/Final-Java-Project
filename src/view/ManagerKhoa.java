package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManagerKhoa extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Connection connection;

    public ManagerKhoa() {
        // Kết nối cơ sở dữ liệu
        connectToDatabase();

        setTitle("Quản lý dữ liệu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tạo các trang
        mainPanel.add(createOverviewPanel(), "Tổng quan");
        mainPanel.add(createGoodsPanel(), "Hàng hoá");
        mainPanel.add(createRoomsPanel(), "Phòng bàn");
        mainPanel.add(createTransactionsPanel(), "Giao dịch");
        mainPanel.add(createEmployeesPanel(), "Nhân viên");

        add(createMenuPanel(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/umi_izakaya", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Kết nối cơ sở dữ liệu thất bại: " + e.getMessage());
        }
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();

        JPanel pnlOverview = new JPanel();
        pnlOverview.add(new JLabel("Tổng quan"));
        pnlOverview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Tổng quan");
            }
        });

        JPanel pnlGoods = new JPanel();
        pnlGoods.add(new JLabel("Hàng hoá"));
        pnlGoods.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Hàng hoá");
            }
        });

        JPanel pnlRooms = new JPanel();
        pnlRooms.add(new JLabel("Phòng bàn"));
        pnlRooms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Phòng bàn");
            }
        });

        JPanel pnlTransactions = new JPanel();
        pnlTransactions.add(new JLabel("Giao dịch"));
        pnlTransactions.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Giao dịch");
            }
        });

        JPanel pnlEmployees = new JPanel();
        pnlEmployees.add(new JLabel("Nhân viên"));
        pnlEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "Nhân viên");
            }
        });

        menuPanel.add(pnlOverview);
        menuPanel.add(pnlGoods);
        menuPanel.add(pnlRooms);
        menuPanel.add(pnlTransactions);
        menuPanel.add(pnlEmployees);

        return menuPanel;
    }

    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Trang Tổng quan"));
        return panel;
    }

    private JPanel createGoodsPanel() {
        return createCRUDPanel("MONAN", new String[]{"id_monan", "name_monan", "type_monan", "price_monan"});
    }

    private JPanel createRoomsPanel() {
        return createCRUDPanel("PHONGBAN", new String[]{"id_phongban", "name_phongban", "type_phongban"});
    }

    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Trang Giao dịch"));
        return panel;
    }

    private JPanel createEmployeesPanel() {
        return createCRUDPanel("NHANVIEN", new String[]{"id_nhanvien", "name_nhanvien", "type_nhanvien", "phonenumber_nhanvien", "adress_nhanvien", "position_nhanvien"});
    }

    private JPanel createCRUDPanel(String tableName, String[] attributes) {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel formPanel = new JPanel(new GridLayout(attributes.length, 2));
        JTextField[] textFields = new JTextField[attributes.length];
        for (int i = 0; i < attributes.length; i++) {
            formPanel.add(new JLabel(attributes[i] + ":"));
            textFields[i] = new JTextField();
            formPanel.add(textFields[i]);
        }

        JPanel buttonPanel = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xoá");

        btnAdd.addActionListener(e -> {
            try {
                StringBuilder query = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
                for (int i = 0; i < attributes.length; i++) {
                    query.append("?");
                    if (i < attributes.length - 1) query.append(", ");
                }
                query.append(")");
                PreparedStatement ps = connection.prepareStatement(query.toString());
                for (int i = 0; i < attributes.length; i++) {
                    ps.setString(i + 1, textFields[i].getText());
                }
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Thêm thành công!");
                loadTableData(textArea, tableName, attributes);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                StringBuilder query = new StringBuilder("UPDATE " + tableName + " SET ");
                for (int i = 1; i < attributes.length; i++) {
                    query.append(attributes[i]).append(" = ?");
                    if (i < attributes.length - 1) query.append(", ");
                }
                query.append(" WHERE ").append(attributes[0]).append(" = ?");

                PreparedStatement ps = connection.prepareStatement(query.toString());
                for (int i = 1; i < attributes.length; i++) {
                    ps.setString(i, textFields[i].getText());
                }
                ps.setString(attributes.length, textFields[0].getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Sửa thành công!");
                loadTableData(textArea, tableName, attributes);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
            }
        });

        btnDelete.addActionListener(e -> {
            try {
                String query = "DELETE FROM " + tableName + " WHERE " + attributes[0] + " = ?";
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, textFields[0].getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(null, "Xoá thành công!");
                loadTableData(textArea, tableName, attributes);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
            }
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        loadTableData(textArea, tableName, attributes);

        return panel;
    }

    private void loadTableData(JTextArea textArea, String tableName, String[] attributes) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);
            textArea.setText("");
            while (rs.next()) {
                for (String attr : attributes) {
                    textArea.append(rs.getString(attr) + "\t");
                }
                textArea.append("\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManagerKhoa::new);
    }
}