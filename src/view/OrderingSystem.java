package view;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JComboBox;

public class OrderingSystem extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Point mouseClickPoint; // Lưu tọa độ khi nhấn chuột

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrderingSystem frame = new OrderingSystem();
                    frame.setUndecorated(true); // Ẩn thanh tiêu đề
                    frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public OrderingSystem() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 506);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		ImageIcon icon = new ImageIcon(getClass().getResource("/image/Order_Interface.png"));
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(900, 506, Image.SCALE_SMOOTH);
        contentPane.setLayout(null);
        
        JComboBox comboBox = new JComboBox();
        comboBox.setBackground(new Color(255, 255, 255));
        comboBox.setForeground(new Color(192, 192, 192));
        comboBox.setBounds(20, 53, 125, 22);
        comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					if(!comboBox.getSelectedItem().toString().equals("Vị trí trong nhà")) {
						JOptionPane.showMessageDialog(null, comboBox.getSelectedItem().toString() + " is selected");
					}
				}
			}
		});
		comboBox.setFont(new Font("Calibri", Font.PLAIN, 13));
		contentPane.add(comboBox);
		
		comboBox.addItem("Vị trí trong nhà") ;
		comboBox.addItem("Khu vực tầng 1");
		comboBox.addItem("Khu vực tầng 2");
        
        JLabel lblMenu = new JLabel("Thực đơn");
        lblMenu.setBounds(120, 11, 100, 31);
        lblMenu.setVerticalAlignment(SwingConstants.CENTER);
        lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setFont(new Font("Calibri", Font.PLAIN, 15));
        contentPane.add(lblMenu);
        
        JLabel lblTable = new JLabel("Phòng bàn");
        lblTable.setBounds(10, 11, 100, 31);
        lblTable.setForeground(new Color(255, 255, 255));
        lblTable.setFont(new Font("Calibri", Font.PLAIN, 15));
        lblTable.setHorizontalAlignment(SwingConstants.CENTER);
        lblTable.setVerticalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTable);
        
        JLabel label = new JLabel(new ImageIcon(scaledImage));
        label.setBounds(0, -1, 900, 506);
        contentPane.add(label);
        
        // Chức năng di chuyển cửa sổ
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseClickPoint = e.getPoint();
                setOpacity(0.6f);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setOpacity(1.0f);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = e.getLocationOnScreen();
                setLocation(currentPoint.x - mouseClickPoint.x, currentPoint.y - mouseClickPoint.y);
            }
        });
	}
}
