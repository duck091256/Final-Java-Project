package view;

import java.awt.EventQueue;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import data_access_object.BillDAO;
import data_access_object.DishDAO;
import data_access_object.StaffDAO;
import data_access_object.TableDAO;
import fx.*;
import model.Bill;
import model.Dish;
import model.RankingStaff;
import model.Staff;
import model.Table;
import service.RatingCalculation;

import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;

public class ManagementSystem extends JFrame {

    private static final long serialVersionUID = 1L;
    private boolean isPanelResized_2 = false; // Trạng thái kích thước của lbl_down_up_2
    private boolean isPanelResized_1 = false; // Trạng thái kích thước của lbl_down_up_1
    private JLabel dishImage, lbl_table_image;
    private JPanel panel_setting;
    private JPanel floorPanel, panel_image;
    public static Map<Integer, Integer> tableCount = TableDAO.numOfTableByFloor();
    public static int tableCountFloor1 = tableCount.getOrDefault(1, 0), tableCountFloor2 = tableCount.getOrDefault(2, 0), dishCount = DishDAO.countRows();
    private JPanel contentPane;
    private JPanel grip_mode_show_table_floor1, grip_mode_show_table_floor2, grip_mode_show_menu;
    private RoundedLabelEffect lbl_switch_table_floor1, lbl_switch_table_floor2;
    private Boolean checkFloor1 = false, checkFloor2 = true;
    private JScrollPane table_mode_show_table, table_mode_show_table_for_menu;
    private JPanel menu, floor1, floor2;
    private RoundedLabel lbl_switch_grip_for_menu, lbl_switch_table_for_menu, lbl_switch_table, lbl_switch_grip;
    private Boolean overallCheckStatus = false, dishCheckStatus = true, tableCheckStatus = true, billCheckStatus = true, employeeCheckStatus = true;
    private JLabel lbl_overall, lbl_dish, lbl_table, lbl_bill, lbl_employee;
    private CardLayout cardLayout, switch_CardLayout_for_menu, switch_CardLayout;
    private JPanel panel_contain_CardLayout, panel_contain_switch_CardLayout, panel_contain_switch_CardLayout_for_menu;
    private JTable DishTable, FloorTable, ReceiptTable, StaffTable;
    private DefaultTableModel Dish_table_model, Floor_table_model, Receipt_table_mode, Emp_table_model;
    private JTextField tf_staff_id;
    private JTextField tf_staff_name;
    private JTextField tf_gender;
    private JTextField tf_phone_num;
    private JTextField tf_position;
    private String DishSelected, FloorSelected, ReceiptSelected, EmpSelected;
    private JTextField tf_dish_id;
    private JTextField tf_dish_name;
    private JTextField tf_dish_price;
    private JTextField tf_dish_category;
    private JTextField tf_status;
    private JTextField tf_floorStay;
    private JTextField tf_tableNum;
    private JTextField tf_Respond;
    private JTextField tf_clientNum;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ManagementSystem frame = new ManagementSystem();
                frame.setVisible(true);

                // Vị trí ban đầu của cửa sổ
                Point initialLocation = frame.getLocation();

                // Ghi đè sự kiện di chuyển cửa sổ
                frame.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentMoved(ComponentEvent e) {
                        // Đặt lại vị trí nếu cửa sổ bị di chuyển
                        frame.setLocation(initialLocation);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public ManagementSystem() {
    	Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    		DishDAO.storeData();
    		TableDAO.storeData();
    		StaffDAO.storeDate();
        }));
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Đặt cửa sổ full màn hình
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);

        // Đặt kích thước và vị trí mặc định
        setBounds(100, 100, 1366, 768);
        setLocationRelativeTo(null);

        // Tạo contentPane
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 240, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        JPanel panel_contain_logo_and_task_bar = new JPanel();
        panel_contain_logo_and_task_bar.setBounds(0, 0, 1283, 154);
        contentPane.add(panel_contain_logo_and_task_bar);
        panel_contain_logo_and_task_bar.setLayout(null);
        
        JPanel panel_down = new JPanel();
        panel_down.setBackground(new Color(74, 100, 127));
        panel_down.setBounds(0, 77, 1283, 77);
        panel_contain_logo_and_task_bar.add(panel_down);
        panel_down.setLayout(null);
        
        lbl_overall = new JLabel("Tổng quan");
        lbl_overall.setFont(new Font("Arial", Font.BOLD, 18));
        lbl_overall.setForeground(new Color(255, 255, 255));
        lbl_overall.setBounds(47, 28, 95, 21);
        panel_down.add(lbl_overall);
        
        // Sự kiện di chuột đến, đi và nhấn
        lbl_overall.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lbl_overall.setFont(new Font("Arial", Font.BOLD, 18));
                lbl_overall.setForeground(new Color(255, 255, 255));
        	}
        	
        	@Override
        	public void mouseExited(MouseEvent e) {
        		if (overallCheckStatus) {
        			lbl_overall.setFont(new Font("Arial", Font.PLAIN, 18));
                    lbl_overall.setForeground(new Color(255, 255, 255));
        		}
        	}
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		cardLayout.show(panel_contain_CardLayout, "Overall");
        		changeBoldToPlain(lbl_overall, lbl_dish, lbl_table, lbl_bill, lbl_employee);
        		lbl_overall.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_overall.setForeground(new Color(255, 255, 255));
        		overallCheckStatus = false;
        		dishCheckStatus = true;
        		tableCheckStatus = true;
        		billCheckStatus = true;
        		employeeCheckStatus = true;
        	}
        });
        
        lbl_dish = new JLabel("Thực đơn");
        lbl_dish.setForeground(Color.WHITE);
        lbl_dish.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl_dish.setBounds(174, 28, 89, 21);
        panel_down.add(lbl_dish);
        
        // Sự kiện di chuột đến, đi và nhấn
        lbl_dish.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lbl_dish.setFont(new Font("Arial", Font.BOLD, 18));
                lbl_dish.setForeground(new Color(255, 255, 255));
        	}
        	
        	@Override
        	public void mouseExited(MouseEvent e) {
        		if (dishCheckStatus) {
        			lbl_dish.setFont(new Font("Arial", Font.PLAIN, 18));
                    lbl_dish.setForeground(new Color(255, 255, 255));
        		}
        	}
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		cardLayout.show(panel_contain_CardLayout, "Menu");
        		switch_CardLayout_for_menu.show(panel_contain_switch_CardLayout_for_menu, "TableModeForMenu");
        		changeBoldToPlain(lbl_overall, lbl_dish, lbl_table, lbl_bill, lbl_employee);
        		lbl_dish.setFont(new Font("Arial", Font.BOLD, 18));
                lbl_dish.setForeground(new Color(255, 255, 255));
                overallCheckStatus = true;
                dishCheckStatus = false;
        		tableCheckStatus = true;
        		billCheckStatus = true;
        		employeeCheckStatus = true;
        	}
        });
        
        lbl_table = new JLabel("Phòng bàn");
        lbl_table.setForeground(Color.WHITE);
        lbl_table.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl_table.setBounds(291, 28, 108, 21);
        panel_down.add(lbl_table);
        
        // Sự kiện di chuột đến, đi và nhấn
        lbl_table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lbl_table.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_table.setForeground(new Color(255, 255, 255));
        	}
        	
        	@Override
        	public void mouseExited(MouseEvent e) {
        		if (tableCheckStatus) {
        			lbl_table.setFont(new Font("Arial", Font.PLAIN, 18));
        			lbl_table.setForeground(new Color(255, 255, 255));
        		}
        	}
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		cardLayout.show(panel_contain_CardLayout, "Floor");
        		switch_CardLayout.show(panel_contain_switch_CardLayout, "TableMode");
        		changeBoldToPlain(lbl_overall, lbl_dish, lbl_table, lbl_bill, lbl_employee);
        		lbl_table.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_table.setForeground(new Color(255, 255, 255));
                overallCheckStatus = true;
                dishCheckStatus = true;
        		tableCheckStatus = false;
        		billCheckStatus = true;
        		employeeCheckStatus = true;
        	}
        });
        
        lbl_bill = new JLabel("Hóa đơn");
        lbl_bill.setForeground(Color.WHITE);
        lbl_bill.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl_bill.setBounds(415, 28, 89, 21);
        panel_down.add(lbl_bill);
        
        // Sự kiện di chuột đến, đi và nhấn
        lbl_bill.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lbl_bill.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_bill.setForeground(new Color(255, 255, 255));
        	}
        	
        	@Override
        	public void mouseExited(MouseEvent e) {
        		if (billCheckStatus) {
        			lbl_bill.setFont(new Font("Arial", Font.PLAIN, 18));
        			lbl_bill.setForeground(new Color(255, 255, 255));
        		}
        	}
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		cardLayout.show(panel_contain_CardLayout, "Receipt");
        		changeBoldToPlain(lbl_overall, lbl_dish, lbl_table, lbl_bill, lbl_employee);
        		lbl_bill.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_bill.setForeground(new Color(255, 255, 255));
                overallCheckStatus = true;
                dishCheckStatus = true;
        		tableCheckStatus = true;
        		billCheckStatus = false;
        		employeeCheckStatus = true;
        	}
        });
        
        lbl_employee = new JLabel("Nhân viên");
        lbl_employee.setForeground(Color.WHITE);
        lbl_employee.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl_employee.setBounds(526, 28, 89, 21);
        panel_down.add(lbl_employee);
        
        // Sự kiện di chuột đến, đi và nhấn
        lbl_employee.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lbl_employee.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_employee.setForeground(new Color(255, 255, 255));
        	}
        	
        	@Override
        	public void mouseExited(MouseEvent e) {
        		if (employeeCheckStatus) {
        			lbl_employee.setFont(new Font("Arial", Font.PLAIN, 18));
        			lbl_employee.setForeground(new Color(255, 255, 255));
        		}
        	}
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		cardLayout.show(panel_contain_CardLayout, "Employee");
        		changeBoldToPlain(lbl_overall, lbl_dish, lbl_table, lbl_bill, lbl_employee);
        		lbl_employee.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_employee.setForeground(new Color(255, 255, 255));
                overallCheckStatus = true;
                dishCheckStatus = true;
        		tableCheckStatus = true;
        		billCheckStatus = true;
        		employeeCheckStatus = false;
        	}
        });
        
        JPanel panel_up = new JPanel();
        panel_up.setBackground(new Color(255, 255, 255));
        panel_up.setBounds(0, 0, 1283, 77);
        panel_contain_logo_and_task_bar.add(panel_up);
        panel_up.setLayout(null);
        
        JLabel lbl_nameStall = new JLabel("UMI IZAKAYA");
        lbl_nameStall.setForeground(new Color(45, 59, 73));
        lbl_nameStall.setFont(new Font("Arial", Font.BOLD, 20));
        lbl_nameStall.setBounds(79, 27, 129, 24);
        panel_up.add(lbl_nameStall);
        
        JLabel lbl_iconStall = new JLabel("");
        lbl_iconStall.setIcon(new ImageIcon(ManagementSystem.class.getResource("/icon/icons8-kawaii-noodle-50.png")));
        lbl_iconStall.setBounds(19, 11, 50, 55);
        panel_up.add(lbl_iconStall);
        
        JLabel lbl_setting = new JLabel("Thiết lập");
        lbl_setting.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl_setting.setBounds(980, 25, 76, 31);
        panel_up.add(lbl_setting);
        
        JLabel lbl_inforEmployee = new JLabel("0905xxx000");
        lbl_inforEmployee.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl_inforEmployee.setBounds(1128, 25, 106, 31);
        panel_up.add(lbl_inforEmployee);
        
        JLabel lbl_barrier = new JLabel("|");
        lbl_barrier.setFont(new Font("Arial", Font.PLAIN, 26));
        lbl_barrier.setBounds(1087, 22, 17, 31);
        panel_up.add(lbl_barrier);
        
        cardLayout = new CardLayout();
        panel_contain_CardLayout = new JPanel(cardLayout);
        panel_contain_CardLayout.setBackground(new Color(255, 255, 255));
        panel_contain_CardLayout.setBounds(0, 154, 1283, 546);
        panel_contain_CardLayout.add(createOverviewPanel(), "Overall");
        panel_contain_CardLayout.add(createMenuPanel(), "Menu");
        panel_contain_CardLayout.add(createFloorPanel(), "Floor");
        panel_contain_CardLayout.add(createReceiptPanel(), "Receipt");
        panel_contain_CardLayout.add(createStaffPanel(), "Employee");
        
        contentPane.add(panel_contain_CardLayout);
    }
    
    private void changeBoldToPlain(JLabel lbl_overall,JLabel lbl_dish,JLabel lbl_table,JLabel lbl_bill,JLabel lbl_employee) {
    	// Đổi tất cả label thành PLAIN trước mới kiểm tra rồi chuyển thành BOLD
    	this.lbl_overall.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_overall.setForeground(new Color(255, 255, 255));
		this.lbl_dish.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_dish.setForeground(new Color(255, 255, 255));
		this.lbl_table.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_table.setForeground(new Color(255, 255, 255));
		this.lbl_bill.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_bill.setForeground(new Color(255, 255, 255));
		this.lbl_employee.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_employee.setForeground(new Color(255, 255, 255));
    	if (isBold(lbl_overall)) {
    		this.lbl_overall.setFont(new Font("Arial", Font.PLAIN, 18));
    		this.lbl_overall.setForeground(new Color(255, 255, 255));
    	} else if (isBold(lbl_dish)) {
    		this.lbl_dish.setFont(new Font("Arial", Font.PLAIN, 18));
    		this.lbl_dish.setForeground(new Color(255, 255, 255));
    	} else if (isBold(lbl_table)) {
    		this.lbl_table.setFont(new Font("Arial", Font.PLAIN, 18));
    		this.lbl_table.setForeground(new Color(255, 255, 255));
    	} else if (isBold(lbl_bill)) {
    		this.lbl_bill.setFont(new Font("Arial", Font.PLAIN, 18));
    		this.lbl_bill.setForeground(new Color(255, 255, 255));
    	} else if (isBold(lbl_employee)) {
    		this.lbl_employee.setFont(new Font("Arial", Font.PLAIN, 18));
    		this.lbl_employee.setForeground(new Color(255, 255, 255));
    	}
    }
    
    private static boolean isBold(JLabel label) {
        Font font = label.getFont();
        return (font.getStyle() & Font.BOLD) != 0;
    }
    
    private JPanel createOverviewPanel() {
    	JPanel overallPanel = new JPanel();
    	overallPanel.setBackground(new Color(255, 255, 255));
    	overallPanel.setLayout(null);
    	
    	JPanel panel_ranking_staff = new JPanel();
    	panel_ranking_staff.setLayout(null);
    	panel_ranking_staff.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_ranking_staff.setBackground(Color.WHITE);
    	panel_ranking_staff.setBounds(899, 33, 345, 486);
    	overallPanel.add(panel_ranking_staff);
    	
        JPanel staffPanel = createRankingStaffPanel();

        JScrollPane scrollPane = new JScrollPane(staffPanel);
        scrollPane.setBounds(10, 10, 325, 466);
        panel_ranking_staff.add(scrollPane);

    	
    	JPanel panel_hot_trend = new JPanel();
    	panel_hot_trend.setLayout(null);
    	panel_hot_trend.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_hot_trend.setBackground(Color.WHITE);
    	panel_hot_trend.setBounds(36, 221, 823, 298);
    	overallPanel.add(panel_hot_trend);
    	
    	JPanel panel_incomes = new JPanel();
    	panel_incomes.setLayout(null);
    	panel_incomes.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_incomes.setBackground(Color.WHITE);
    	panel_incomes.setBounds(36, 33, 823, 146);
    	overallPanel.add(panel_incomes);
    	
    	DefaultCategoryDataset dataset = createDataset();

    	JFreeChart barChart = ChartFactory.createBarChart(
    	        "Món ăn bán chạy",  
    	        "Tên món ăn",        
    	        "Số lượng bán",     
    	        dataset             
    	);

    	ChartPanel chartPanel = new ChartPanel(barChart);

    	int padding = 20;
    	chartPanel.setPreferredSize(new Dimension(
    	        panel_hot_trend.getWidth() - padding,
    	        panel_hot_trend.getHeight() - padding
    	));
    	chartPanel.setBounds(
    	        padding / 2,
    	        padding / 2,
    	        panel_hot_trend.getWidth() - padding,
    	        panel_hot_trend.getHeight() - padding
    	);

    	panel_hot_trend.setLayout(null);
    	panel_hot_trend.add(chartPanel);
    	panel_hot_trend.revalidate();
    	panel_hot_trend.repaint();
    	
        return overallPanel;
    }
    
    private JPanel createRankingStaffPanel() {
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
        } else {
            for (RankingStaff staff : staffList) {
                // Tạo JLabel cho từng nhân viên
                /* JLabel staffLabel = new JLabel(staff.getStaffName() + " - Total Sales: " + staff.getTotalSales());
                staffLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                panel.add(staffLabel); */
            }
        }

        return panel;
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<Dish> dishList = RatingCalculation.getListOfRatingDish();

        if (dishList != null && !dishList.isEmpty()) {
            for (Dish dish : dishList) {
                dataset.addValue(dish.getQuantity(), "Số lượng", dish.getDishName());
            }
        } else {
            dataset.addValue(0, "Số lượng", "Không có dữ liệu");
        }

        return dataset;
    }
    
    private JPanel createMenuPanel() {
    	JPanel menuPanel = new JPanel();
    	menuPanel.setBackground(new Color(255, 255, 255));
    	menuPanel.setLayout(null);
    	
    	lbl_switch_grip_for_menu = new RoundedLabel("Chế độ hiển thị lưới");
    	lbl_switch_grip_for_menu.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_switch_grip_for_menu.setForeground(Color.BLACK);
    	lbl_switch_grip_for_menu.setFont(new Font("Arial", Font.PLAIN, 14));
    	lbl_switch_grip_for_menu.setCornerRadius(10);
    	lbl_switch_grip_for_menu.setBackground(new Color(211, 211, 211));
    	lbl_switch_grip_for_menu.setBounds(1102, 186, 140, 32);
    	menuPanel.add(lbl_switch_grip_for_menu);
    	
    	lbl_switch_grip_for_menu.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_switch_grip_for_menu.setForeground(Color.WHITE);
    			lbl_switch_grip_for_menu.setBackground(new Color(169, 169, 169));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_switch_grip_for_menu.setForeground(Color.BLACK);
    			lbl_switch_grip_for_menu.setBackground(new Color(211, 211, 211));
    		}
    		
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			switch_CardLayout_for_menu.show(panel_contain_switch_CardLayout_for_menu, "GripModeForMenu");
    			lbl_switch_grip_for_menu.setVisible(false);
    	        lbl_switch_table_for_menu.setVisible(true);
       		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_switch_grip_for_menu.setForeground(Color.WHITE);
    			lbl_switch_grip_for_menu.setBackground(new Color(105, 105, 105));
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_switch_grip_for_menu.setForeground(Color.WHITE);
    			lbl_switch_grip_for_menu.setBackground(new Color(169, 169, 169));
    		}
		});
    	
    	lbl_switch_table_for_menu = new RoundedLabel("Chế độ hiển thị bảng");
    	lbl_switch_table_for_menu.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_switch_table_for_menu.setForeground(Color.BLACK);
    	lbl_switch_table_for_menu.setFont(new Font("Arial", Font.PLAIN, 14));
    	lbl_switch_table_for_menu.setCornerRadius(10);
    	lbl_switch_table_for_menu.setBackground(new Color(211, 211, 211));
    	lbl_switch_table_for_menu.setBounds(1102, 186, 140, 32);
    	menuPanel.add(lbl_switch_table_for_menu);
    	
    	lbl_switch_table_for_menu.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_switch_table_for_menu.setForeground(Color.WHITE);
    			lbl_switch_table_for_menu.setBackground(new Color(169, 169, 169));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_switch_table_for_menu.setForeground(Color.BLACK);
    			lbl_switch_table_for_menu.setBackground(new Color(211, 211, 211));
    		}
    		
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			switch_CardLayout_for_menu.show(panel_contain_switch_CardLayout_for_menu, "TableModeForMenu");
    			lbl_switch_table_for_menu.setVisible(false);
    			lbl_switch_grip_for_menu.setVisible(true);
       		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_switch_table_for_menu.setForeground(Color.WHITE);
    			lbl_switch_table_for_menu.setBackground(new Color(105, 105, 105));
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_switch_table_for_menu.setForeground(Color.WHITE);
    			lbl_switch_table_for_menu.setBackground(new Color(169, 169, 169));
    		}
		});
    	
    	JPanel panel_filter = new JPanel();
    	panel_filter.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_filter.setBounds(38, 36, 149, 486);
    	panel_filter.setBackground(Color.WHITE);
    	menuPanel.add(panel_filter);
    	panel_filter.setLayout(null);
    	
    	JCheckBox sortCheckBox = new JCheckBox("Sắp xếp theo tên");
    	sortCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));
    	sortCheckBox.setBackground(new Color(255, 255, 255));
    	sortCheckBox.setBounds(6, 23, 137, 23);
    	panel_filter.add(sortCheckBox);
    	
    	sortCheckBox.addItemListener(new ItemListener() {
    	    @Override
    	    public void itemStateChanged(ItemEvent e) {
    	        boolean isChecked = e.getStateChange() == ItemEvent.SELECTED;
    	        sortDish(DishDAO.handleSort(isChecked));
    	    }
    	});
    	
    	JPanel panel_setting = new JPanel();
    	panel_setting.setBounds(233, 34, 745, 143);
    	panel_setting.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_setting.setBackground(Color.white);
    	menuPanel.add(panel_setting);
    	panel_setting.setLayout(null);
    	
    	JLabel lbl_dish_id = new JLabel("Mã Món Ăn");
    	lbl_dish_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_dish_id.setBounds(30, 11, 140, 33);
    	panel_setting.add(lbl_dish_id);
    	
    	tf_dish_id = new JTextField();
    	tf_dish_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_dish_id.setColumns(10);
    	tf_dish_id.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_dish_id.setBounds(180, 11, 140, 33);
    	panel_setting.add(tf_dish_id);
    	
    	tf_dish_name = new JTextField();
    	tf_dish_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_dish_name.setColumns(10);
    	tf_dish_name.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_dish_name.setBounds(180, 55, 140, 33);
    	panel_setting.add(tf_dish_name);
    	
    	JLabel lbl_dish_name = new JLabel("Tên Món Ăn");
    	lbl_dish_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_dish_name.setBounds(30, 55, 140, 33);
    	panel_setting.add(lbl_dish_name);
    	
    	JLabel lbl_dish_category = new JLabel("Loại Món Ăn");
    	lbl_dish_category.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_dish_category.setBounds(428, 11, 121, 33);
    	panel_setting.add(lbl_dish_category);
    	
    	JLabel lbl_dish_price = new JLabel("Giá");
    	lbl_dish_price.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_dish_price.setBounds(30, 99, 140, 33);
    	panel_setting.add(lbl_dish_price);
    	
    	tf_dish_price = new JTextField();
    	tf_dish_price.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_dish_price.setColumns(10);
    	tf_dish_price.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_dish_price.setBounds(180, 99, 140, 33);
    	panel_setting.add(tf_dish_price);
    	
    	tf_dish_category = new JTextField();
    	tf_dish_category.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_dish_category.setColumns(10);
    	tf_dish_category.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_dish_category.setBounds(568, 11, 140, 33);
    	panel_setting.add(tf_dish_category);
    	
    	RoundedLabel lbl_add = new RoundedLabel("Thêm Món Ăn");
    	lbl_add.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_add.setForeground(Color.BLACK);
    	lbl_add.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_add.setCornerRadius(10);
    	lbl_add.setBackground(new Color(129, 199, 132));
    	lbl_add.setBounds(428, 55, 121, 33);
    	panel_setting.add(lbl_add);
    	
    	lbl_add.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_add.setForeground(Color.BLACK);
    			lbl_add.setBackground(new Color(129, 199, 132));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(33, 136, 56));
    			addDish();
    			addNewDish();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
		});
    	
    	RoundedLabel lbl_adjust = new RoundedLabel("Điều Chỉnh");
    	lbl_adjust.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_adjust.setForeground(Color.BLACK);
    	lbl_adjust.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_adjust.setCornerRadius(10);
    	lbl_adjust.setBackground(new Color(100, 181, 246));
    	lbl_adjust.setBounds(568, 55, 140, 33);
    	panel_setting.add(lbl_adjust);
    	
    	lbl_adjust.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    	    	lbl_adjust.setForeground(Color.WHITE);
    			lbl_adjust.setBackground(new Color(0, 123, 255));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_adjust.setForeground(Color.BLACK);
    			lbl_adjust.setBackground(new Color(100, 181, 246));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    	    	lbl_adjust.setForeground(Color.WHITE);
    			lbl_adjust.setBackground(new Color(0, 86, 179));
    			editDish();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    	    	lbl_adjust.setForeground(Color.WHITE);
    			lbl_adjust.setBackground(new Color(0, 123, 255));
    		}
		});
    	
    	RoundedLabel lbl_remove = new RoundedLabel("Xóa");
    	lbl_remove.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_remove.setForeground(Color.BLACK);
    	lbl_remove.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_remove.setCornerRadius(10);
    	lbl_remove.setBackground(new Color(229, 115, 115));
    	lbl_remove.setBounds(428, 99, 121, 33);
    	panel_setting.add(lbl_remove);
    	
    	lbl_remove.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_remove.setForeground(Color.WHITE);
    			lbl_remove.setBackground(new Color(220, 53, 69));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_remove.setForeground(Color.BLACK);
    			lbl_remove.setBackground(new Color(229, 115, 115));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_remove.setForeground(Color.WHITE);
    			lbl_remove.setBackground(new Color(176, 42, 55));
    			
    			Dish dish = DishDAO.getDish(DishSelected);
    			int selectedRow = DishTable.getSelectedRow();
    	        
    	        // Kiểm tra dòng được chọn
    	        if (selectedRow >= 0) {
    	        	int confirm = JOptionPane.showConfirmDialog(
    	                    null, 
    	                    "Bạn có chắc chắn muốn xóa món " + dish.getDishName() + "?", 
    	                    "Xác nhận xóa",
    	                    JOptionPane.YES_NO_OPTION
    	                );
    	                if (confirm == JOptionPane.YES_OPTION) {
    	                    removeDish(dish.getDishID());
    	                    deleteDish();
    	                }
    	        } else {
    	            JOptionPane.showMessageDialog(null, "Vui lòng chọn một món ăn để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
    	        }
    	        
                // Làm mới giao diện
                menu.revalidate();
                menu.repaint();	
    		}
    		
    		@Override
    	    public void mouseReleased(MouseEvent e) {
    			lbl_remove.setForeground(Color.WHITE);
    			lbl_remove.setBackground(new Color(220, 53, 69));
    	    }
		});
    	
    	RoundedLabel lbl_add_image = new RoundedLabel("Thêm Ảnh");
    	lbl_add_image.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_add_image.setForeground(Color.BLACK);
    	lbl_add_image.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_add_image.setCornerRadius(10);
    	lbl_add_image.setBackground(new Color(255, 253, 182));
    	lbl_add_image.setBounds(568, 99, 140, 33);
    	panel_setting.add(lbl_add_image);
    	
    	lbl_add_image.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_add_image.setForeground(Color.WHITE);
    			lbl_add_image.setBackground(new Color(255, 193, 7));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_add_image.setForeground(Color.BLACK);
    			lbl_add_image.setBackground(new Color(255, 253, 182));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_add_image.setForeground(Color.WHITE);
    			lbl_add_image.setBackground(new Color(140, 120, 80));
    			addImage();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_add_image.setForeground(Color.WHITE);
    			lbl_add_image.setBackground(new Color(255, 193, 7));
    		}
		});
    	
    	switch_CardLayout_for_menu = new CardLayout();
        panel_contain_switch_CardLayout_for_menu = new JPanel(switch_CardLayout_for_menu);
        panel_contain_switch_CardLayout_for_menu.setBackground(Color.WHITE);
        panel_contain_switch_CardLayout_for_menu.setBounds(233, 224, 1009, 298);
        panel_contain_switch_CardLayout_for_menu.add(switchTableModeForMenu(), "TableModeForMenu");
        panel_contain_switch_CardLayout_for_menu.add(switchGripModeForMenu(), "GripModeForMenu"); 
        
        menuPanel.add(panel_contain_switch_CardLayout_for_menu);
    	menuPanel.setLayout(null);
    	
    	dishImage = new JLabel("Ảnh Trống");
    	dishImage.setBounds(1033, 33, 209, 143);
    	menuPanel.add(dishImage);
    	dishImage.setHorizontalAlignment(SwingConstants.CENTER);
    	dishImage.setForeground(Color.BLACK);
    	dishImage.setFont(new Font("Arial", Font.PLAIN, 16));
    	dishImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    	
    	menuPanel.revalidate();
    	menuPanel.repaint();
    	
    	return menuPanel;
    }
    
	private JScrollPane switchTableModeForMenu() {

		// Chế độ bảng (Switch Mode)
		table_mode_show_table_for_menu = new JScrollPane();
		table_mode_show_table_for_menu.setBounds(38, 224, 1204, 298);

		Border roundedBorder = new LineBorder(Color.GRAY, 2, true);
		table_mode_show_table_for_menu.setBorder(roundedBorder);

		DishSelected = null;

		Dish_table_model = new DefaultTableModel(new Object[][] {},
				new String[] { "Mã Món Ăn", "Tên Món Ăn", "Giá (Nghìn VNĐ)", "Loại Món Ăn" });

		DishTable = new JTable();
		DishTable.setModel(Dish_table_model);
		DishTable.getTableHeader().setReorderingAllowed(false);
		DishTable.setFont(new Font("Arial", Font.PLAIN, 20));
		DishTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		DishTable.getColumnModel().getColumn(1).setPreferredWidth(320);
		DishTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		DishTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		Font headerFont = new Font("Arial", Font.BOLD, 18);
		DishTable.getTableHeader().setPreferredSize(new Dimension(DishTable.getTableHeader().getWidth(), 30));
		DishTable.getTableHeader().setFont(headerFont);
		DishTable.setRowHeight(30);

		DishTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectedRow = DishTable.getSelectedRow();
					if (selectedRow != -1) {
						DishSelected = DishTable.getValueAt(selectedRow, 0).toString();

						tf_dish_id.setText(DishTable.getValueAt(selectedRow, 0).toString());
						tf_dish_name.setText(DishTable.getValueAt(selectedRow, 1).toString());
						tf_dish_price.setText(DishTable.getValueAt(selectedRow, 2).toString());
						tf_dish_category.setText(DishTable.getValueAt(selectedRow, 3).toString());

						Dish tmpDish = DishDAO.getDish(DishSelected);
						String imagePath = tmpDish.getDishImage(); // Lấy đường dẫn ảnh từ đối tượng

						if (imagePath == null || imagePath.isEmpty()) {
							dishImage.setText("Chưa cập nhật ảnh");
							dishImage.setIcon(null);
						} else {
							// Lấy đường dẫn thư mục gốc của dự án
							String basePath = System.getProperty("user.dir");

							// Kết hợp thư mục gốc với đường dẫn tương đối từ cơ sở dữ liệu
							File imageFile = new File(basePath, imagePath);

							if (imageFile.exists()) {
								dishImage.setText("");
								ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
								dishImage.setIcon(imageIcon);
							} else {
								dishImage.setText("Ảnh không tồn tại");
							}
						}
					}
				}
			}
		});

		table_mode_show_table_for_menu.setViewportView(DishTable);
		loadDish();

		return table_mode_show_table_for_menu;
	}
    
	private void sortDish(List<Dish> dishes) {
		// Xóa tất cả dữ liệu hiện tại trong bảng
		Dish_table_model.setRowCount(0);

		// Thêm dữ liệu mới vào bảng
		for (Dish dish : dishes) {
			Dish_table_model.addRow(
					new Object[] { dish.getDishID(), dish.getDishName(), dish.getDishPrice(), dish.getDishCategory() });
		}

		// Thông báo thay đổi dữ liệu
		Dish_table_model.fireTableDataChanged();
	}

    private JPanel switchGripModeForMenu() {
    	
    	// Chế độ lưới (Switch Mode)
    	grip_mode_show_menu = new JPanel();
    	grip_mode_show_menu.setBackground(Color.WHITE);
    	grip_mode_show_menu.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	grip_mode_show_menu.setBounds(38, 224, 1022, 298);
    	grip_mode_show_menu.setLayout(null);
        
        menu = new JPanel();
        menu.setBackground(Color.WHITE);
        menu.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
        menu.setBounds(0, 0, 1022, 296);
        menu.setLayout(new GridLayout(0, 5, 10, 10));
        menu.setPreferredSize(new Dimension(500, 500));
        menu.setDoubleBuffered(true);
        
        loadMenu();
        
    	JScrollPane scrollPane = new JScrollPane(menu);
        scrollPane.setBounds(0, 0, 1009, 298);
        grip_mode_show_menu.add(scrollPane);
        
        menu.revalidate(); // Làm mới giao diện
        menu.repaint();
    	
    	return grip_mode_show_menu;
    }
    
    private Map<String, JPanel> dishPanels = new HashMap<>();
    
    private void loadMenu() {
        for (int i = 1; i <= dishCount; i++) {
        	Dish dishes = DishDAO.accessDish(i - 1);
        	addDishToMenu("Món " + dishes.getDishName(), dishes);
        }
    }
    
    private void updateDishName(String dishId) {
    	Dish dish = DishDAO.getDish(dishId);
    	String newName = dish.getDishName();
    	
        if (dishPanels.containsKey(dishId)) {
            JPanel dishPanel = dishPanels.get(dishId);
            
            for (Component component : dishPanel.getComponents()) {
                if (component instanceof JLabel) {
                    JLabel lblDishName = (JLabel) component;
                    
                    if (lblDishName.getText().startsWith("Món")) {
                        lblDishName.setText(newName);

                        // Làm mới giao diện
                        dishPanel.revalidate();
                        dishPanel.repaint();
                        return;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn để cập nhật tên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateDishImage(String dishId, String newImagePath) {
        if (dishPanels.containsKey(dishId)) {
            JPanel dishPanel = dishPanels.get(dishId);
            
            for (Component component : dishPanel.getComponents()) {
                if (component instanceof JLabel) {
                    JLabel lblDishImage = (JLabel) component;

                    if (lblDishImage.getIcon() != null || lblDishImage.getText().equals("Không có ảnh")) {
                        setDishImage(lblDishImage, newImagePath);
                        
                        // Làm mới giao diện
                        dishPanel.revalidate();
                        dishPanel.repaint();
                        return;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn cần cập nhật!");
        }
    }
    
	private void removeDish(String dishId) {
		if (dishPanels.containsKey(dishId)) {
			JPanel dishPanel = dishPanels.get(dishId);

			menu.remove(dishPanel);
			dishPanels.remove(dishId);
			dishCount--;

			menu.revalidate();
			menu.repaint();
			JOptionPane.showMessageDialog(null, "Món ăn đã được xóa!");
		} else {
			JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn để xóa.");
		}
	}
    
    private void addNewDish() {
        dishCount++;
        Dish dishes = DishDAO.accessDish(dishCount - 1);
        addDishToMenu("Món " + dishes.getDishName(), dishes);
        
        // Làm mới giao diện
        menu.revalidate(); 
        menu.repaint();
        
        addNewDishInMiniMenu();
    }
    
    private void showLargeImage(String imagePath) {
        // Sử dụng JWindow thay vì Window
        JWindow imageFrame = new JWindow();
        ImageIcon icon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(icon);

        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(700, 500, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImg));

        imageFrame.add(imageLabel);
        imageFrame.pack();
        imageFrame.setLocationRelativeTo(null);
        imageFrame.setVisible(true);

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                imageFrame.dispose();
            }
        });
    }

    private void addDishToMenu(String dishName, Dish dishes) {
        String tmp = dishes.getDishImage();
        
        JPanel JPdish = new JPanel();
        JPdish.setBackground(Color.WHITE);
        JPdish.setPreferredSize(new Dimension(100, 150));
        JPdish.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
        JPdish.setLayout(new BoxLayout(JPdish, BoxLayout.Y_AXIS));

        JLabel lblDishName = new JLabel(dishName, SwingConstants.CENTER);
        lblDishName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDishImage = new JLabel();
        lblDishImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        setDishImage(lblDishImage, tmp);
        
        JPdish.add(lblDishName);
        JPdish.add(lblDishImage);
        
        // Sự kiện khi nhấn
        JPdish.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	showLargeImage(tmp); 
            }
        });

        // Thêm món ăn vào danh sách và giao diện
        dishPanels.put(dishes.getDishID(), JPdish);
        menu.add(JPdish);

        // Làm mới giao diện
        menu.revalidate();
        menu.repaint();
    }
    
    private void setDishImage(JLabel lblDish, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            lblDish.setIcon(imageIcon);
        } else {
            lblDish.setText("Không có ảnh");
        }
    }

    private void loadDish() {
    	DishDAO.loadData();
		for(Dish dish : DishDAO.list) {
	        Object[] newRow = {dish.getDishID(), dish.getDishName(), dish.getDishPrice(), dish.getDishCategory()};
	        Dish_table_model.addRow(newRow);
		}
    }
    
    private void addDish() {
        JTextField tfDishID = new JTextField();
        JTextField tfDishName = new JTextField();
        JTextField tfPrice = new JTextField();
        JTextField tfCategory = new JTextField();
        JLabel lblImagePath = new JLabel("Chưa chọn hình ảnh");
        
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã Món Ăn:"));
        panel.add(tfDishID);
        panel.add(new JLabel("Tên Món Ăn:"));
        panel.add(tfDishName);
        panel.add(new JLabel("Giá Tiền:"));
        panel.add(tfPrice);
        panel.add(new JLabel("Loại Món Ăn:"));
        panel.add(tfCategory);
        panel.add(new JLabel("Chọn Hình Ảnh:"));
        panel.add(lblImagePath);

        int result = JOptionPane.showConfirmDialog(null, panel, "Thêm món mới", JOptionPane.OK_CANCEL_OPTION);

        // Xử lý nếu người dùng nhấn OK
        if (result == JOptionPane.OK_OPTION) {
            String dishID = tfDishID.getText();
            if (dishID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mã món ăn không được để trống!");
                return;
            }

            // Lấy thông tin, nếu trống thì chuyển thành "null"
            String dishName = tfDishName.getText().trim().isEmpty() ? null : tfDishName.getText().trim();
            Double price = tfPrice.getText().trim().isEmpty() ? null : Double.valueOf(tfPrice.getText().trim());
            String category = tfCategory.getText().trim().isEmpty() ? null : tfCategory.getText().trim();
            
            // Sử dụng phương thức chooseAndCopyImage để chọn và sao chép hình ảnh
            String relativeImagePath = null;
            relativeImagePath = chooseAndCopyImage();

            if (relativeImagePath == null) {
                JOptionPane.showMessageDialog(null, "Bạn chưa chọn hình ảnh!");
            }
            
            try {
                Dish dish = new Dish(dishID, dishName, price, category, relativeImagePath);
                DishDAO.addDish(dish);
                Object[] newRow = {dish.getDishID(), dish.getDishName(), dish.getDishPrice(), dish.getDishCategory()};
                Dish_table_model.addRow(newRow);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void editDish() {
        int selectedRow = DishTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một món ăn để sửa!");
            return;
        }

        // Lấy thông tin hiện tại từ hàng được chọn
        String currentDishID = Dish_table_model.getValueAt(selectedRow, 0).toString();
        
        // Tạo đối tượng hàng hóa hiện tại
        Dish currentDish = DishDAO.getDish(currentDishID);
        
        // Lấy thông tin của đối tượng
        String currentDishName = currentDish.getDishName();
        Double currentPrice = currentDish.getDishPrice();
        String currentCategory = currentDish.getDishCategory();
        String currentImage = currentDish.getDishImage();
        
        // Tạo các trường nhập liệu
        JTextField tfDishID = new JTextField(currentDishID);
        tfDishID.setEditable(false);
        JTextField tfDishName = new JTextField(currentDishName);
        JTextField tfPrice = new JTextField(currentPrice.toString());
        JTextField tfCategory = new JTextField(currentCategory);
        JLabel lblImagePath = new JLabel((currentImage != null && !currentImage.isEmpty()) ? currentImage : "Chưa chọn hình ảnh");
        
        // Tạo bảng nhập liệu
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã Món Ăn:"));
        panel.add(tfDishID);
        panel.add(new JLabel("Tên Món Ăn:"));
        panel.add(tfDishName);
        panel.add(new JLabel("Giá Tiền:"));
        panel.add(tfPrice);
        panel.add(new JLabel("Loại Món Ăn:"));
        panel.add(tfCategory);
        panel.add(new JLabel("Hình Ảnh:"));
        panel.add(lblImagePath);	

        int result = JOptionPane.showConfirmDialog(null, panel, "Sửa thông tin món ăn", JOptionPane.OK_CANCEL_OPTION);

        // Xử lý nếu người dùng nhấn OK
        if (result == JOptionPane.OK_OPTION) {
            String dishID = tfDishID.getText().trim();
            if (dishID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mã món ăn không được để trống!");
                return;
            }

            // Lấy thông tin, nếu trống thì chuyển thành "null"
            String name = tfDishName.getText().trim().isEmpty() ? null : tfDishName.getText().trim();
            Double price = tfPrice.getText().trim().isEmpty() ? null : Double.valueOf(tfPrice.getText().trim());
            String category = tfCategory.getText().trim().isEmpty() ? null : tfCategory.getText().trim();

            String image = currentDish.getDishImage();
			try {
				Dish newDish = new Dish(dishID, name, price, category, image);
				DishDAO.updateDish(currentDish, newDish);
				
				// Cập nhật tên cho ô hiển thị ở chế độ lưới nếu tên có sự thay đổi
				if (currentDishName != name) {
					updateDishName(dishID);
				}

				Dish_table_model.setValueAt(newDish.getDishName(), selectedRow, 1);
				Dish_table_model.setValueAt(newDish.getDishPrice(), selectedRow, 2);
				Dish_table_model.setValueAt(newDish.getDishCategory(), selectedRow, 3);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
			}
		}
	}
    
    // Phương thức xóa hàng trong bảng
    private void deleteDish() {
		int selectedRow = DishTable.getSelectedRow();

		String dishID = DishTable.getValueAt(selectedRow, 0).toString();

		// Gọi phương thức xóa món ăn trong Class DAO
		boolean success = DishDAO.deleteDish(dishID);

		if (success) {
			// Nếu xóa thành công, xóa dòng trong bảng
			Dish_table_model.removeRow(selectedRow);
		} else {
			JOptionPane.showMessageDialog(null, "Xóa món ăn thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
		}
	}
    
	private void addImage() {
        int selectedRow = DishTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một món ăn để thêm ảnh!");
            return;
        }

        // Lấy id từ hàng được chọn
        String currentDishID = Dish_table_model.getValueAt(selectedRow, 0).toString();

        // Lấy đối tượng Dish
        Dish currentDish = DishDAO.getDish(currentDishID);
        
        // Chọn và sao chép hình ảnh
        String newImage = chooseAndCopyImage();
        if (newImage == null || newImage.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không có hình ảnh nào được chọn!");
            return;
        }

        // Nếu người dùng không chọn ảnh mới, giữ nguyên ảnh cũ
        if (newImage.equals(currentDish.getDishImage())) {
            JOptionPane.showMessageDialog(null, "Hình ảnh không thay đổi.");
            return;
        }

        try {
            // Cập nhật hình ảnh mới vào cơ sở dữ liệu
            currentDish.setDishImage(newImage);
            updateDishImage(currentDishID, newImage);

            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(null, "Hình ảnh đã được cập nhật thành công cho món: " + currentDish.getDishImage());
        } catch (Exception e) {
            // Hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
        }
    }
    
    // Phương thức chooseAndCopyImage để chọn hình ảnh và sao chép vào thư mục image
    private String chooseAndCopyImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Hình ảnh (JPG, PNG)", "jpg", "png"));
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();

                // Sao chép hình ảnh vào package 'image'
                File destDir = new File("src/image"); // Đường dẫn tới package 'image'
                if (!destDir.exists()) {
                    destDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
                }

                File destFile = new File(destDir, selectedFile.getName());

                // Ghi đè file nếu nó đã tồn tại
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Trả về đường dẫn tương đối
                return "src/image/" + selectedFile.getName();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi sao chép hình ảnh: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        return null; // Trả về null nếu người dùng không chọn hình ảnh
    }
    
    private JPanel createFloorPanel() {
    	floorPanel = new JPanel();
    	floorPanel.setBackground(new Color(255, 255, 255));
    	floorPanel.setLayout(null);
    	
    	panel_image = new JPanel();
    	panel_image.setBackground(new Color(255, 255, 255));
    	panel_image.setBounds(38, 22, 160, 166);
    	panel_image.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	floorPanel.add(panel_image);
    	panel_image.setLayout(null);
    	
    	lbl_table_image = new JLabel("Trống");
    	lbl_table_image.setBounds(10, 11, 140, 144);
    	panel_image.add(lbl_table_image);
    	
    	lbl_table_image.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_table_image.setForeground(Color.BLACK);
    	lbl_table_image.setFont(new Font("Arial", Font.PLAIN, 16));
    	
    	lbl_switch_grip = new RoundedLabel("Chế độ hiển thị lưới");
    	lbl_switch_grip.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_switch_grip.setForeground(Color.BLACK);
    	lbl_switch_grip.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_switch_grip.setCornerRadius(10);
    	lbl_switch_grip.setBackground(new Color(211, 211, 211));
    	lbl_switch_grip.setBounds(1082, 180, 160, 40);
    	floorPanel.add(lbl_switch_grip);
    	
    	lbl_switch_grip.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_switch_grip.setForeground(Color.WHITE);
    			lbl_switch_grip.setBackground(new Color(169, 169, 169));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_switch_grip.setForeground(Color.BLACK);
    			lbl_switch_grip.setBackground(new Color(211, 211, 211));
    		}
    		
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			switch_CardLayout.show(panel_contain_switch_CardLayout, "GripModeFloor1");
    			lbl_switch_grip.setVisible(false);
    	        lbl_switch_table.setVisible(true);
    	        lbl_switch_table_floor1.setVisible(true);
    	        lbl_switch_table_floor2.setVisible(true);
       		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_switch_grip.setForeground(Color.WHITE);
    			lbl_switch_grip.setBackground(new Color(105, 105, 105));
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_switch_grip.setForeground(Color.WHITE);
    			lbl_switch_grip.setBackground(new Color(169, 169, 169));
    		}
		});
    	
    	lbl_switch_table = new RoundedLabel("Chế độ hiển thị bảng");
    	lbl_switch_table.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_switch_table.setForeground(Color.BLACK);
    	lbl_switch_table.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_switch_table.setCornerRadius(10);
    	lbl_switch_table.setBackground(new Color(211, 211, 211));
    	lbl_switch_table.setBounds(1082, 180, 160, 40);
    	floorPanel.add(lbl_switch_table);
    	
    	lbl_switch_table.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_switch_table.setForeground(Color.WHITE);
    			lbl_switch_table.setBackground(new Color(169, 169, 169));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_switch_table.setForeground(Color.BLACK);
    			lbl_switch_table.setBackground(new Color(211, 211, 211));
    		}
    		
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			switch_CardLayout.show(panel_contain_switch_CardLayout, "TableMode");
    			lbl_switch_table.setVisible(false);
    	        lbl_switch_grip.setVisible(true);
    	        lbl_switch_table_floor1.setVisible(false);
    	        lbl_switch_table_floor2.setVisible(false);
       		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_switch_table.setForeground(Color.WHITE);
    			lbl_switch_table.setBackground(new Color(105, 105, 105));
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_switch_table.setForeground(Color.WHITE);
    			lbl_switch_table.setBackground(new Color(169, 169, 169));
    		}
		});
    	
    	panel_setting = new JPanel();
    	panel_setting.setBackground(new Color(255, 255, 255));
    	panel_setting.setBounds(233, 34, 1009, 143);
    	panel_setting.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	floorPanel.add(panel_setting);
    	panel_setting.setLayout(null);
    	
    	RoundedLabel lbl_add = new RoundedLabel("Thêm Bàn");
    	lbl_add.setForeground(Color.BLACK);
    	lbl_add.setBackground(new Color(129, 199, 132));
    	lbl_add.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_add.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_add.setBounds(20, 99, 149, 33);
    	lbl_add.setCornerRadius(10);
    	panel_setting.add(lbl_add);
    	
    	lbl_add.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_add.setForeground(Color.BLACK);
    			lbl_add.setBackground(new Color(129, 199, 132));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(33, 136, 56));
    			addTable();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
		});
    	
    	RoundedLabel lbl_adjust = new RoundedLabel("Xem Order");
    	lbl_adjust.setForeground(Color.BLACK);
    	lbl_adjust.setBackground(new Color(100, 181, 246));
    	lbl_adjust.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_adjust.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_adjust.setCornerRadius(10);
    	lbl_adjust.setBounds(179, 99, 149, 33);
    	panel_setting.add(lbl_adjust);
    	
    	lbl_adjust.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_adjust.setForeground(Color.WHITE);
    			lbl_adjust.setBackground(new Color(0, 123, 255));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_adjust.setForeground(Color.BLACK);
    			lbl_adjust.setBackground(new Color(100, 181, 246));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_adjust.setForeground(Color.WHITE);
    			lbl_adjust.setBackground(new Color(0, 86, 179));
    			viewOrder();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_adjust.setForeground(Color.WHITE);
    			lbl_adjust.setBackground(new Color(0, 123, 255));
    		}
		});
    	
    	RoundedLabel lbl_remove = new RoundedLabel("Xóa Bàn");
    	lbl_remove.setForeground(Color.BLACK);
    	lbl_remove.setBackground(new Color(229, 115, 115));
    	lbl_remove.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_remove.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_remove.setCornerRadius(10);
    	lbl_remove.setBounds(338, 99, 149, 33);
    	panel_setting.add(lbl_remove);
    	
    	lbl_remove.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_remove.setForeground(Color.WHITE);
    			lbl_remove.setBackground(new Color(220, 53, 69));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_remove.setForeground(Color.BLACK);
    			lbl_remove.setBackground(new Color(229, 115, 115));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_remove.setForeground(Color.WHITE);
    			lbl_remove.setBackground(new Color(176, 42, 55));

    			Table table = TableDAO.getTable(FloorSelected);
    			int selectedRow = FloorTable.getSelectedRow();
    	        
    	        // Kiểm tra dòng được chọn
    	        if (selectedRow >= 0) {
    	        	int confirm = JOptionPane.showConfirmDialog(
    	                    null, 
    	                    "Bạn có chắc chắn muốn xóa bàn có mã  " + table.getTableID() + "?", 
    	                    "Xác nhận xóa",
    	                    JOptionPane.YES_NO_OPTION
    	                );
    	                if (confirm == JOptionPane.YES_OPTION) {
    	                    deleteTable();
    	                }
    	        } else {
    	            JOptionPane.showMessageDialog(null, "Vui lòng chọn một bàn để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
    	        }
    	        
                // Làm mới giao diện
                floorPanel.revalidate();
                floorPanel.repaint();	
    		}
    		
    		@Override
    	    public void mouseReleased(MouseEvent e) {
    			lbl_remove.setForeground(Color.WHITE);
    			lbl_remove.setBackground(new Color(220, 53, 69));
    	    }
		});
    	
    	RoundedLabel lbl_update_status = new RoundedLabel("Cập Nhật Trạng Thái");
    	lbl_update_status.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_update_status.setForeground(Color.BLACK);
    	lbl_update_status.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_update_status.setCornerRadius(10);
    	lbl_update_status.setBackground(new Color(255, 253, 182));
    	lbl_update_status.setBounds(497, 99, 218, 33);
    	panel_setting.add(lbl_update_status);
    	
    	lbl_update_status.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_update_status.setForeground(Color.WHITE);
    			lbl_update_status.setBackground(new Color(255, 193, 7));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_update_status.setForeground(Color.BLACK);
    			lbl_update_status.setBackground(new Color(255, 253, 182));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_update_status.setForeground(Color.WHITE);
    			lbl_update_status.setBackground(new Color(140, 120, 80));
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_update_status.setForeground(Color.WHITE);
    			lbl_update_status.setBackground(new Color(255, 193, 7));
    		}
		});

    	RoundedLabel lbl_assign_staff = new RoundedLabel("Thay Đổi Nhân Viên Phụ Trách");
    	lbl_assign_staff.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_assign_staff.setForeground(Color.BLACK);
    	lbl_assign_staff.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_assign_staff.setCornerRadius(10);
    	lbl_assign_staff.setBackground(new Color(211, 211, 211));
    	lbl_assign_staff.setBounds(725, 99, 261, 33);
    	panel_setting.add(lbl_assign_staff);
    	
    	lbl_assign_staff.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_assign_staff.setForeground(Color.WHITE);
    			lbl_assign_staff.setBackground(new Color(169, 169, 169));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_assign_staff.setForeground(Color.BLACK);
    			lbl_assign_staff.setBackground(new Color(211, 211, 211));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_assign_staff.setForeground(Color.WHITE);
    			lbl_assign_staff.setBackground(new Color(105, 105, 105));
    			changeStaffRes();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_assign_staff.setForeground(Color.WHITE);
    			lbl_assign_staff.setBackground(new Color(169, 169, 169));
    		}
		});

    	tf_status = new JTextField();
    	tf_status.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_status.setColumns(10);
    	tf_status.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_status.setBounds(818, 11, 168, 33);
    	panel_setting.add(tf_status);
    	
    	tf_floorStay = new JTextField();
    	tf_floorStay.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_floorStay.setColumns(10);
    	tf_floorStay.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_floorStay.setBounds(483, 11, 168, 33);
    	panel_setting.add(tf_floorStay);
    	
    	tf_tableNum = new JTextField();
    	tf_tableNum.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_tableNum.setColumns(10);
    	tf_tableNum.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_tableNum.setBounds(121, 11, 168, 33);
    	panel_setting.add(tf_tableNum);
    	
    	JLabel lbl_table_id = new JLabel("Số Bàn");
    	lbl_table_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_table_id.setBounds(10, 11, 121, 33);
    	panel_setting.add(lbl_table_id);
    	
    	JLabel lbl_floor_stay = new JLabel("Tầng Hoạt Động");
    	lbl_floor_stay.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_floor_stay.setBounds(335, 11, 121, 33);
    	panel_setting.add(lbl_floor_stay);
    	
    	JLabel lbl_operating_status = new JLabel("Trạng Thái");
    	lbl_operating_status.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_operating_status.setBounds(691, 11, 102, 33);
    	panel_setting.add(lbl_operating_status);
    	
    	tf_Respond = new JTextField();
    	tf_Respond.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_Respond.setColumns(10);
    	tf_Respond.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_Respond.setBounds(121, 55, 168, 33);
    	panel_setting.add(tf_Respond);
    	
    	JLabel lbl_respond = new JLabel("Phụ Trách Bởi");
    	lbl_respond.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_respond.setBounds(10, 55, 121, 33);
    	panel_setting.add(lbl_respond);
    	
    	JLabel lbl_client_num = new JLabel("Số Người");
    	lbl_client_num.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_client_num.setBounds(335, 55, 121, 33);
    	panel_setting.add(lbl_client_num);
    	
    	tf_clientNum = new JTextField();
    	tf_clientNum.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_clientNum.setColumns(10);
    	tf_clientNum.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_clientNum.setBounds(483, 55, 168, 33);
    	panel_setting.add(tf_clientNum);
    	
        switch_CardLayout = new CardLayout();
        panel_contain_switch_CardLayout = new JPanel(switch_CardLayout);
        panel_contain_switch_CardLayout.setBackground(Color.WHITE);
        panel_contain_switch_CardLayout.setBounds(38, 224, 1204, 298);
        panel_contain_switch_CardLayout.add(switchTableMode(), "TableMode");
        panel_contain_switch_CardLayout.add(switchGripModeFloor1(), "GripModeFloor1"); 
        panel_contain_switch_CardLayout.add(switchGripModeFloor2(), "GripModeFloor2"); 
        
        floorPanel.add(panel_contain_switch_CardLayout);
    	floorPanel.setLayout(null);
    	
    	lbl_switch_table_floor1 = new RoundedLabelEffect("Tầng 1");
    	lbl_switch_table_floor1.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_switch_table_floor1.setForeground(Color.BLACK);
    	lbl_switch_table_floor1.setFont(new Font("Arial", Font.PLAIN, 14));
    	lbl_switch_table_floor1.setCornerRadius(10);
    	lbl_switch_table_floor1.setBackground(new Color(211, 211, 211));
    	lbl_switch_table_floor1.setBounds(38, 199, 80, 25);
    	lbl_switch_table_floor1.setVisible(false);
    	floorPanel.add(lbl_switch_table_floor1);
    	
    	lbl_switch_table_floor1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
				switch_CardLayout.show(panel_contain_switch_CardLayout, "GripModeFloor1");
            }

			@Override
			public void mouseEntered(MouseEvent e) {
				lbl_switch_table_floor1.setBackground(new Color(169, 169, 169));
				lbl_switch_table_floor1.setForeground(Color.WHITE);
				lbl_switch_table_floor1.setFont(new Font("Arial", Font.PLAIN, 14));
			}

			@Override
    		public void mouseExited(MouseEvent e) {
				lbl_switch_table_floor1.setForeground(Color.BLACK);
				lbl_switch_table_floor1.setBackground(new Color(211, 211, 211));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_switch_table_floor1.setForeground(Color.WHITE);
    			lbl_switch_table_floor1.setBackground(new Color(105, 105, 105));
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_switch_table_floor1.setForeground(Color.WHITE);
    			lbl_switch_table_floor1.setBackground(new Color(169, 169, 169));
    		}
		});
    	
    	lbl_switch_table_floor2 = new RoundedLabelEffect("Tầng 2");
    	lbl_switch_table_floor2.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_switch_table_floor2.setForeground(Color.BLACK);
    	lbl_switch_table_floor2.setFont(new Font("Arial", Font.PLAIN, 14));
    	lbl_switch_table_floor2.setCornerRadius(10);
    	lbl_switch_table_floor2.setBackground(new Color(211, 211, 211));
    	lbl_switch_table_floor2.setBounds(118, 199, 80, 25);
    	lbl_switch_table_floor2.setVisible(false);
    	floorPanel.add(lbl_switch_table_floor2);
    	
    	lbl_switch_table_floor2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	switch_CardLayout.show(panel_contain_switch_CardLayout, "GripModeFloor2");
            }

            @Override
			public void mouseEntered(MouseEvent e) {
            	lbl_switch_table_floor2.setBackground(new Color(169, 169, 169));
            	lbl_switch_table_floor2.setForeground(Color.WHITE);
            	lbl_switch_table_floor2.setFont(new Font("Arial", Font.PLAIN, 14));
			}

			@Override
    		public void mouseExited(MouseEvent e) {
				lbl_switch_table_floor2.setForeground(Color.BLACK);
				lbl_switch_table_floor2.setBackground(new Color(211, 211, 211));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_switch_table_floor2.setForeground(Color.WHITE);
    			lbl_switch_table_floor2.setBackground(new Color(105, 105, 105));
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_switch_table_floor2.setForeground(Color.WHITE);
    			lbl_switch_table_floor2.setBackground(new Color(169, 169, 169));
    		}
		});
    	
    	floorPanel.revalidate();
    	floorPanel.repaint();
        
    	return floorPanel;
    }
    
    private JScrollPane switchTableMode() {
    	
    	// Chế độ bảng (Switch Mode)
    	table_mode_show_table = new JScrollPane();
    	table_mode_show_table.setBounds(38, 224, 1204, 298);
    	
    	Border roundedBorder = new LineBorder(Color.GRAY, 2, true);
    	table_mode_show_table.setBorder(roundedBorder);
        
    	FloorSelected = null;
    	
    	Floor_table_model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Số Bàn", "Tầng Hoạt Động", "Trạng Thái", "Phụ Trách Bởi", "Số Người"
				}
			);
		
    	FloorTable = new JTable();
    	FloorTable.setModel(Floor_table_model);
    	FloorTable.getTableHeader().setReorderingAllowed(false);
    	FloorTable.setFont(new Font("Arial", Font.PLAIN, 20));
    	FloorTable.getColumnModel().getColumn(0).setPreferredWidth(100);
    	FloorTable.getColumnModel().getColumn(1).setPreferredWidth(250);
    	FloorTable.getColumnModel().getColumn(2).setPreferredWidth(200);
    	FloorTable.getColumnModel().getColumn(3).setPreferredWidth(150);
    	FloorTable.getColumnModel().getColumn(4).setPreferredWidth(200);
		Font headerFont = new Font("Arial", Font.BOLD, 18);
		FloorTable.getTableHeader().setPreferredSize(new Dimension(FloorTable.getTableHeader().getWidth(), 30));
		FloorTable.getTableHeader().setFont(headerFont);
		FloorTable.setRowHeight(30);
    	
		FloorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    @Override
		    public void valueChanged(ListSelectionEvent e) {
		        if (!e.getValueIsAdjusting()) {
		            int selectedRow = FloorTable.getSelectedRow();
		            if (selectedRow != -1) {
		                // Lấy dữ liệu từ bảng
		                tf_tableNum.setText(FloorTable.getValueAt(selectedRow, 0).toString());
		                tf_floorStay.setText(FloorTable.getValueAt(selectedRow, 1).toString());
		                tf_status.setText(FloorTable.getValueAt(selectedRow, 2).toString());
		                tf_Respond.setText(FloorTable.getValueAt(selectedRow, 3) != null ? FloorTable.getValueAt(selectedRow, 3).toString() : "");
		                tf_clientNum.setText(FloorTable.getValueAt(selectedRow, 4).toString());

		                String tableID = FloorTable.getValueAt(selectedRow, 0).toString();
		                String tableStatus = FloorTable.getValueAt(selectedRow, 2).toString();

		                String imagePath = "src/icon/icons8-table-100.png";
		                String basePath = System.getProperty("user.dir");
		                File imageFile = new File(basePath, imagePath);

		                panel_image.removeAll();

		                JPanel innerPanel = new JPanel();
		                innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		                innerPanel.setBackground(panel_image.getBackground());

		                JLabel lblTableID = new JLabel("Mã bàn: " + tableID, SwingConstants.CENTER);
		                lblTableID.setFont(new Font("Arial", Font.BOLD, 16));

		                JLabel lblTableStatus = new JLabel(tableStatus, SwingConstants.CENTER);
		                lblTableStatus.setFont(new Font("Arial", Font.PLAIN, 15));
		                lblTableStatus.setForeground(new Color(40, 167, 69));

		                if (imageFile.exists()) {
		                    lbl_table_image.setText("");
		                    ImageIcon imageIcon = new ImageIcon(imageFile.getAbsolutePath());
		                    lbl_table_image.setIcon(imageIcon);
		                    lbl_table_image.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa hình ảnh
		                } else {
		                    lbl_table_image.setText("Ảnh không tồn tại");
		                    lbl_table_image.setIcon(null);
		                    lbl_table_image.setHorizontalAlignment(SwingConstants.CENTER); // Căn giữa thông báo
		                }

		                lblTableID.setAlignmentX(Component.CENTER_ALIGNMENT);
		                lbl_table_image.setAlignmentX(Component.CENTER_ALIGNMENT);
		                lblTableStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

		                innerPanel.add(lblTableID);   // Mã bàn ở trên
		                innerPanel.add(Box.createVerticalStrut(10)); // Khoảng cách giữa các thành phần
		                innerPanel.add(lbl_table_image); // Hình ảnh ở giữa
		                innerPanel.add(Box.createVerticalStrut(10)); // Khoảng cách giữa hình ảnh và trạng thái
		                innerPanel.add(lblTableStatus);  // Trạng thái ở dưới

		                // Thiết lập kích thước cho innerPanel
		                panel_image.setLayout(null); // Sử dụng layout null để điều chỉnh kích thước tự do
		                int padding = 5; // Kích thước thu nhỏ
		                innerPanel.setBounds(
		                    padding, 
		                    padding, 
		                    panel_image.getWidth() - 2 * padding, 
		                    panel_image.getHeight() - 2 * padding
		                );
		                
		                panel_image.add(innerPanel);

		                // Làm mới giao diện
		                panel_image.revalidate();
		                panel_image.repaint();

		                // Lưu mã bàn đã chọn
		                FloorSelected = tableID;
		            }
		        }
		    }
		});

		
		table_mode_show_table.setViewportView(FloorTable);
		loadFloor();
		
		return table_mode_show_table;
    }
    
    private JPanel switchGripModeFloor1() {
    	
    	// Chế độ lưới (Switch Mode)
    	grip_mode_show_table_floor1 = new JPanel();
    	grip_mode_show_table_floor1.setBackground(Color.WHITE);
    	grip_mode_show_table_floor1.setBorder(BorderFactory.createLineBorder(new Color(45, 61, 75), 1));
    	grip_mode_show_table_floor1.setBounds(38, 224, 1204, 298);
    	grip_mode_show_table_floor1.setLayout(null);
        
        floor1 = new JPanel();
        floor1.setBackground(Color.WHITE);
        floor1.setBounds(0, 0, 1202, 296);
        floor1.setLayout(new GridLayout(0, 4, 10, 10));
    	
        loadTablesFloor1();
        
    	JScrollPane scrollPane1 = new JScrollPane(floor1);
    	scrollPane1.setBackground(Color.WHITE);
        scrollPane1.setBounds(0, 0, 1204, 298);
        scrollPane1.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createLineBorder(Color.BLACK, 1), // Đường viền ngoài (tùy chọn)
        	    new EmptyBorder(5, 5, 5, 5) // Khoảng cách bên trong: trên, trái, dưới, phải
        	));
        grip_mode_show_table_floor1.add(scrollPane1);
    	
    	return grip_mode_show_table_floor1;
    }
    
    private void loadTablesFloor1() {
    	if (tableCountFloor1 == 0) {
    		JLabel notion = new JLabel("Hiện tầng này chưa có bàn nào");
    		notion.setForeground(Color.BLACK);
    		notion.setBackground(Color.WHITE);
    		notion.setHorizontalAlignment(SwingConstants.CENTER);
    		notion.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
            notion.setBounds(0, 0, 1202, 296);
            
            
    	} else {
	        for (int i = 1; i <= tableCountFloor1; i++) {
	            addTableToPanelFloor1("Bàn " + i, "Trống");
	        }
    	}
    }

    private void addTableFloor1() {
        tableCountFloor1++;
        addTableToPanelFloor1("Bàn " + tableCountFloor1, "Trống");
        floor1.revalidate(); // Làm mới giao diện
        floor1.repaint();
    }

    private void removeTableFloor1() {
        if (tableCountFloor1 > 0) {
            tableCountFloor1--;
            refreshTablesFloor1();
        } else {
            JOptionPane.showMessageDialog(null, "Không còn bàn nào để xóa!");
        }
    }
    
    private void refreshTablesFloor1() {
        floor1.removeAll(); // Xóa tất cả các bàn
        for (int i = 1; i <= tableCountFloor1; i++) {
            addTableToPanelFloor1("Bàn " + i, "Trống");
        }
        floor1.revalidate();
        floor1.repaint();
    }
    
    private Map<String, JLabel> tableStatusMap = new HashMap<>();
    
    private void addTableToPanelFloor1(String tableName, String status) {
        JPanel Table = new JPanel();
        Table.setBackground(Color.WHITE);
        Table.setPreferredSize(new Dimension(100, 100));
        Table.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
        Table.setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setOpaque(false);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTableName = new JLabel(tableName);
        lblTableName.setFont(new Font("Arial", Font.BOLD, 16));
        lblTableName.setForeground(Color.BLACK);
        lblTableName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblStatus = new JLabel(status);
        lblStatus.setFont(new Font("Arial", Font.PLAIN, 15));
        lblStatus.setForeground(Color.GRAY);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Lưu nhãn trạng thái vào HashMap để dễ cập nhật sau này
        tableStatusMap.put(tableName, lblStatus);

        labelPanel.add(lblTableName);
        labelPanel.add(Box.createVerticalStrut(5)); // Khoảng cách giữa tableName và status
        labelPanel.add(lblStatus);

        Table.add(labelPanel, BorderLayout.CENTER);

        Table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chooseAndUpdateTableStatus(tableName); // Cho phép chọn trạng thái mới
            }
        });

        floor1.add(Table);
    }
    
    private void updateTableStatus(String tableName, String newStatus) {
        // Kiểm tra nếu bàn tồn tại trong HashMap
        if (tableStatusMap.containsKey(tableName)) {
            JLabel lblStatus = tableStatusMap.get(tableName);
            lblStatus.setText(newStatus); // Cập nhật trạng thái
            lblStatus.setForeground(Color.BLUE); // Thay đổi màu sắc nếu cần
        } else {
            JOptionPane.showMessageDialog(null, "Bàn " + tableName + " không tồn tại!");
        }
    }

    private void chooseAndUpdateTableStatus(String tableName) {
        // Kiểm tra nếu bàn tồn tại
        if (!tableStatusMap.containsKey(tableName)) {
            JOptionPane.showMessageDialog(null, "Bàn " + tableName + " không tồn tại!");
            return;
        }

        // Danh sách trạng thái có thể chọn
        String[] statuses = {"Trống", "Đang sử dụng", "Đã đặt trước", "Bảo trì"};
        
        // Hiển thị hộp thoại để người dùng chọn trạng thái
        String newStatus = (String) JOptionPane.showInputDialog(
            null,
            "Chọn trạng thái mới cho " + tableName + ":",
            "Cập nhật trạng thái",
            JOptionPane.QUESTION_MESSAGE,
            null, // Không có icon
            statuses, // Danh sách trạng thái
            statuses[0] // Trạng thái mặc định
        );

        // Nếu người dùng chọn trạng thái
        if (newStatus != null) {
            // Cập nhật trạng thái của bàn
            updateTableStatus(tableName, newStatus);
            JOptionPane.showMessageDialog(null, "Cập nhật trạng thái thành công!");
        }
    }
    
    private JPanel switchGripModeFloor2() {
    	
    	// Chế độ lưới (Switch Mode)
    	grip_mode_show_table_floor2 = new JPanel();
    	grip_mode_show_table_floor2.setBackground(Color.WHITE);
    	grip_mode_show_table_floor2.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	grip_mode_show_table_floor2.setBounds(38, 224, 1204, 298);
    	grip_mode_show_table_floor2.setLayout(null);
        
        floor2 = new JPanel();
        floor2.setBackground(Color.WHITE);
        floor2.setBounds(0, 0, 1202, 296);
        floor2.setLayout(new GridLayout(0, 4, 10, 10));
    	
        loadTablesFloor2();
        
    	JScrollPane scrollPane2 = new JScrollPane(floor2);
    	scrollPane2.setBackground(Color.WHITE);
    	scrollPane2.setBounds(0, 0, 1204, 298);
    	scrollPane2.setBorder(BorderFactory.createCompoundBorder(
        	    BorderFactory.createLineBorder(Color.BLACK, 1), // Đường viền ngoài (tùy chọn)
        	    new EmptyBorder(5, 5, 5, 5) // Khoảng cách bên trong: trên, trái, dưới, phải
        	));
        
        grip_mode_show_table_floor2.add(scrollPane2);
    	
    	return grip_mode_show_table_floor2;
    }
    
    private void loadTablesFloor2() {
    	if (tableCountFloor2 == 0) {
    		JLabel notion = new JLabel("Hiện tầng này chưa có bàn nào");
    		notion.setForeground(Color.BLACK);
    		notion.setBackground(Color.WHITE);
    		notion.setHorizontalAlignment(SwingConstants.CENTER);
    		notion.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
            notion.setBounds(0, 0, 1202, 296);
            
            
    	} else {
	        for (int i = 1; i <= tableCountFloor2; i++) {
	            addTableToPanelFloor2("Bàn " + i, "Trống");
	        }
	    }
    }
    
    private void addTableFloor2() {
        tableCountFloor2++;
        addTableToPanelFloor2("Bàn " + tableCountFloor2, "Trống");
        floor2.revalidate(); // Làm mới giao diện
        floor2.repaint();
    }
    
    private void removeTableFloor2() {
        if (tableCountFloor2 > 0) {
            tableCountFloor2--;
            refreshTablesFloor2();
        } else {
            JOptionPane.showMessageDialog(null, "Không còn bàn nào để xóa!");
        }
    }
    
    private void refreshTablesFloor2() {
        floor2.removeAll(); // Xóa tất cả các bàn
        for (int i = 1; i <= tableCountFloor2; i++) {
            addTableToPanelFloor2("Bàn " + i, "Trống");
        }
        floor2.revalidate();
        floor2.repaint();
    }
    
    private void addTableToPanelFloor2(String tableName, String status) {
    	JPanel Table = new JPanel();
        Table.setBackground(Color.WHITE);
        Table.setPreferredSize(new Dimension(100, 100));
        Table.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
        JLabel lbl_table = new JLabel("<html>" + tableName + "<br>" + status + "</html>");
        Table.add(lbl_table);
        
        // Sự kiện khi nhấn vào bàn
        Table.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		JOptionPane.showMessageDialog(null, "Bạn đã chọn " + tableName);
        	}
		});
        floor2.add(Table);
    }
    
    private void loadFloor() {
    	TableDAO.loadData();
		for(Table table : TableDAO.list) {
	        Object[] newRow = {table.getTableID(), table.getFloorStay(), table.getOperatingStatus(), table.getResponsibleBy(), table.getClientNum()};
	        Floor_table_model.addRow(newRow);
		}
    }
    
    private JPanel containtMiniMenu, miniMenu;
    
    private JPanel MenuForTablePage() {
    	containtMiniMenu = new JPanel();
    	containtMiniMenu.setBackground(Color.WHITE);
    	containtMiniMenu.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	containtMiniMenu.setBounds(38, 224, 1022, 350);
    	containtMiniMenu.setLayout(null);
        
    	miniMenu = new JPanel();
    	miniMenu.setBackground(Color.WHITE);
    	miniMenu.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	miniMenu.setBounds(0, 0, 1022, 350);
    	miniMenu.setLayout(new GridLayout(0, 5, 10, 10));
    	miniMenu.setPreferredSize(new Dimension(500, 500));
    	miniMenu.setDoubleBuffered(true);
        
        loadMiniMenu();
        
    	JScrollPane scrollPane = new JScrollPane(miniMenu);
        scrollPane.setBounds(0, 0, 1009, 310);
        containtMiniMenu.add(scrollPane);
        
        miniMenu.revalidate(); // Làm mới giao diện
        miniMenu.repaint();
    	
    	return containtMiniMenu;
    }
    
    private void loadMiniMenu() {
        for (int i = 1; i <= dishCount; i++) {
        	Dish dishes = DishDAO.accessDish(i - 1);
        	addDishToMiniMenu("Món " + dishes.getDishName(), dishes);
        }
    }
    
    private void addNewDishInMiniMenu() {
        Dish dishes = DishDAO.accessDish(dishCount - 1);
        addDishToMiniMenu("Món " + dishes.getDishName(), dishes);
        
        // Làm mới giao diện
        miniMenu.revalidate(); 
        miniMenu.repaint();
    }
    
    private void addDishToMiniMenu(String dishName, Dish dishes) {
        String tmp = dishes.getDishImage();
        
        JPanel JPdish = new JPanel();
        JPdish.setBackground(Color.WHITE);
        JPdish.setPreferredSize(new Dimension(100, 150));
        JPdish.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
        JPdish.setLayout(new BoxLayout(JPdish, BoxLayout.Y_AXIS));

        JLabel lblDishName = new JLabel(dishName, SwingConstants.CENTER);
        lblDishName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDishImage = new JLabel();
        lblDishImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        setDishImage(lblDishImage, tmp);
        
        JPdish.add(lblDishName);
        JPdish.add(lblDishImage);
        
        // Sự kiện khi nhấn
        JPdish.addMouseListener(new MouseAdapter() {
        	//
        });

        // Thêm món ăn vào danh sách và giao diện
        miniMenu.add(JPdish);

        // Làm mới giao diện
        miniMenu.revalidate();
        miniMenu.repaint();
    }
    
    private JDialog dialog, menuDialog;
    
    private JDialog menuDialog() {
		JPanel switchPanel = MenuForTablePage();
		
        menuDialog = new JDialog(dialog, "Thêm Món", true);
        menuDialog.setSize(switchPanel.getPreferredSize());
        menuDialog.setLayout(new BorderLayout());
        menuDialog.add(switchPanel, BorderLayout.CENTER);
        menuDialog.setLocationRelativeTo(dialog);
        menuDialog.setVisible(true);
        
        return menuDialog;
    }
    
    private void viewOrder() {
        dialog = new JDialog((Frame) null, "Thông Tin Order", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.add(new JLabel("Nội dung cần hiển thị ở đây"));
        dialog.add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton addButton = new JButton("Thêm món");

        okButton.addMouseListener(new MouseAdapter() {
        	@Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
            }
        });

        addButton.addMouseListener(new MouseAdapter() {
        	@Override
            public void mouseClicked(MouseEvent e) {
        		menuDialog();
            }
        });

        buttonPanel.add(okButton);
        buttonPanel.add(addButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
    
    private void addTable() {
        JTextField tfTableID = new JTextField("#T");
        JTextField tfFloor = new JTextField();
        JTextField tfStatus = new JTextField("Sẵn sàng phục vụ");
        tfStatus.setBackground(Color.WHITE);
        tfStatus.setEditable(false);
        JTextField tfResBy = new JTextField();
        tfResBy.setBackground(Color.WHITE);
        tfResBy.setEditable(false);
        JTextField tfClientNum = new JTextField("0");
        tfClientNum.setBackground(Color.WHITE);
        tfClientNum.setEditable(false);

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã Bàn:"));
        panel.add(tfTableID);
        panel.add(new JLabel("Tầng Hoạt Động:"));
        panel.add(tfFloor);
        panel.add(new JLabel("Trạng Thái:"));
        panel.add(tfStatus);
        panel.add(new JLabel("Phục vụ bởi:"));
        panel.add(tfResBy);
        panel.add(new JLabel("Số Lượng Khách Hàng:"));
        panel.add(tfClientNum);

        int result = JOptionPane.showConfirmDialog(null, panel, "Thêm bàn mới", JOptionPane.OK_CANCEL_OPTION);

        // Xử lý nếu người dùng nhấn OK
        if (result == JOptionPane.OK_OPTION) {
            String tableID = tfTableID.getText();
            if (tableID.isEmpty() || tableID.equals("#T")) {
                JOptionPane.showMessageDialog(null, "Mã bàn không được để trống!");
                return;
            }

            // Lấy thông tin, nếu trống thì chuyển thành "null"
            String floor = tfFloor.getText().trim().isEmpty() ? null : tfFloor.getText().trim();
            String status = tfStatus.getText().trim().isEmpty() ? null : tfStatus.getText().trim();
            String resBy = tfResBy.getText().trim().isEmpty() ? null : tfResBy.getText().trim();
            String clientNum = tfClientNum.getText().trim().isEmpty() ? null : tfClientNum.getText().trim();

            if (!"1".equals(floor) && !"2".equals(floor)) {
            	JOptionPane.showMessageDialog(null, "Hiện tại chỉ có tầng 1 và tầng 2!");
            	return;
            }
            try {
                Table table = new Table(tableID, floor, status, resBy, clientNum);
                if(TableDAO.addTable(table)) {
	                Object[] newRow = {table.getTableID(), table.getFloorStay(), table.getOperatingStatus(), table.getResponsibleBy(), table.getClientNum()};
	                Floor_table_model.addRow(newRow);
	                
	                // Kiểm tra bàn được thêm ở tầng nào thêm vào tầng tương ứng
	                if ("1".equals(floor)) {
	                	addTableFloor1();
	                } else if ("2".equals(floor)) {
	                	addTableFloor2();
	                }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }
    
    // Phương thức xóa hàng trong bảng
 	private void deleteTable() {
 		int selectedRow = FloorTable.getSelectedRow();

 		String tableID = FloorTable.getValueAt(selectedRow, 0).toString();
 		
 		// Gọi đối tượng
 		Table tableNeedToRemove = TableDAO.getTable(tableID);
 		
 		// Lấy số tầng của bàn đó
 		String floorOfThatTable = tableNeedToRemove.getFloorStay();
 		
 		// Gọi phương thức xóa bàn trong Class DAO
 		boolean success = TableDAO.deleteTable(tableID);

 		if (success) {
 			// Nếu xóa thành công, xóa dòng trong bảng
 			Floor_table_model.removeRow(selectedRow);
 			
 			// Kiểm tra và xóa bàn khỏi tầng tương ứng
 			if ("1".equals(floorOfThatTable)) {
 				removeTableFloor1();
 			} else {
 				removeTableFloor2();
 			}
 		} else {
 			JOptionPane.showMessageDialog(null, "Xóa bàn thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
 		}
 	}
    
 	// Phương thức thay đổi nhân viên phụ trách
 	private void changeStaffRes() {
 	    // Kiểm tra người dùng có chọn hàng trong JTable hay không
 	    int selectedRow = FloorTable.getSelectedRow();
 	    if (selectedRow == -1) {
 	        JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng trong bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
 	        return;
 	    }

 	    // Tạo JDialog để chọn nhân viên
 	    JDialog dialog = new JDialog((Frame) null, "Chọn Nhân Viên", true);
 	    dialog.setLayout(new GridBagLayout());
 	    dialog.setSize(300, 200);
 	    dialog.setLocationRelativeTo(null); // Hiển thị ở giữa màn hình

 	    // Tạo JLabel cho hướng dẫn
 	    JLabel label = new JLabel("Chọn nhân viên:");
 	    label.setFont(new Font("Dialog", Font.PLAIN, 15));
 	    GridBagConstraints gbc = new GridBagConstraints();
 	    gbc.gridx = 0;
 	    gbc.gridy = 0;
 	    gbc.anchor = GridBagConstraints.WEST;
 	    gbc.insets = new Insets(10, 10, 5, 10);
 	    dialog.add(label, gbc);

 	    // Tạo JComboBox với các tùy chọn nhân viên
 	    String[] staffOptions = {"Nhân viên A", "Nhân viên B", "Nhân viên C"};
 	    JComboBox<String> comboBox = new JComboBox<>(staffOptions);
 	    comboBox.setFont(new Font("Dialog", Font.PLAIN, 15));
 	    comboBox.setBackground(Color.WHITE);
 	    comboBox.setForeground(Color.BLACK);
 	    comboBox.setSelectedItem(FloorTable.getValueAt(selectedRow, 3)); // Hiển thị giá trị hiện tại
 	    gbc.gridx = 0;
 	    gbc.gridy = 1;
 	    gbc.fill = GridBagConstraints.HORIZONTAL;
 	    gbc.insets = new Insets(5, 10, 10, 10);
 	    dialog.add(comboBox, gbc);

 	    // Tạo nút xác nhận
 	    JButton confirmButton = new JButton("Xác nhận");
 	    confirmButton.setFont(new Font("Dialog", Font.PLAIN, 15));
 	    gbc.gridx = 0;
 	    gbc.gridy = 2;
 	    gbc.fill = GridBagConstraints.NONE;
 	    gbc.insets = new Insets(10, 10, 10, 10);
 	    gbc.anchor = GridBagConstraints.CENTER;
 	    dialog.add(confirmButton, gbc);

 	    // Xử lý sự kiện khi nhấn nút xác nhận
 	    confirmButton.addActionListener(e -> {
 	        String selectedStaff = (String) comboBox.getSelectedItem();
 	        if (selectedStaff != null) {
 	            // Cập nhật giá trị vào JTable
 	            FloorTable.setValueAt(selectedStaff, selectedRow, 3); // Cột 3 là cột giá trị nhân viên
 	            tf_Respond.setText(selectedStaff); // Hiển thị giá trị mới trong TextField
 	        }
 	        dialog.dispose(); // Đóng dialog sau khi xác nhận
 	    });

 	    // Hiển thị dialog
 	    dialog.setVisible(true);
 	}
 	
    // Làm màu
	class FlippableLabel extends JLabel {
	    private boolean isFlipped = false;

	    public FlippableLabel(String text, int horizontalAlignment) {
	        super(text, horizontalAlignment);
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        Graphics2D g2d = (Graphics2D) g;
	        AffineTransform original = g2d.getTransform();

	        int offsetY = 15;

	        if (isFlipped) {
	            g2d.translate(getWidth() / 2, getHeight() / 2 - offsetY);
	            g2d.scale(1, -1);
	            g2d.translate(-getWidth() / 2, -getHeight() / 2);
	        }

	        super.paintComponent(g);
	        g2d.setTransform(original);
	    }

	    public void toggleFlip() {
	        isFlipped = !isFlipped;
	    }
	}
    
    private JPanel createReceiptPanel() {
    	JPanel receiptPanel = new JPanel();
    	receiptPanel.setBackground(new Color(255, 255, 255));
    	receiptPanel.setLayout(null);
    	
    	JPanel panel_filter = new JPanel();
    	panel_filter.setLayout(null);
    	panel_filter.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_filter.setBackground(Color.WHITE);
    	panel_filter.setBounds(34, 208, 222, 90);
    	receiptPanel.add(panel_filter);
    	
    	JLabel lbl_filter_title = new JLabel("Lọc");
    	lbl_filter_title.setFont(new Font("Dialog", Font.BOLD, 16));
    	lbl_filter_title.setBounds(10, 11, 43, 21);
    	panel_filter.add(lbl_filter_title);
    	
    	JCheckBox check_recent_payment = new JCheckBox("Thanh toán gần đây");
    	check_recent_payment.setFont(new Font("Dialog", Font.PLAIN, 15));
    	check_recent_payment.setOpaque(false); // Làm trong suốt
    	check_recent_payment.setBounds(20, 43, 167, 23);
    	panel_filter.add(check_recent_payment);

        // Tạo FlippableLabel 1
        FlippableLabel lbl_down_up_1 = new FlippableLabel("^", SwingConstants.CENTER);
        lbl_down_up_1.setBounds(10, 10, 50, 30);
        lbl_down_up_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lbl_down_up_1.toggleFlip();
                lbl_down_up_1.repaint();

                int targetHeight = isPanelResized_1 ? 90 : 43; // Kích thước mục tiêu
                AnimatedMoving.animateResize(panel_filter, panel_filter.getHeight(), targetHeight, (currentHeight) -> {
                    panel_filter.setBounds(34, isPanelResized_2 ? 136 : 208, 222, currentHeight);
                });

                isPanelResized_1 = !isPanelResized_1;
            }
        });
    	
    	lbl_down_up_1.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_down_up_1.setFont(new Font("Viner Hand ITC", Font.PLAIN, 19));
    	lbl_down_up_1.setBounds(173, 18, 49, 23);
    	panel_filter.add(lbl_down_up_1);
    	
    	JPanel panel_status = new JPanel();
    	panel_status.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_status.setBackground(Color.WHITE);
    	panel_status.setBounds(34, 69, 222, 113);
    	receiptPanel.add(panel_status);
    	panel_status.setLayout(null);
    	
    	JLabel lbl_status_title = new JLabel("Trạng thái");
    	lbl_status_title.setFont(new Font("Dialog", Font.BOLD, 16));
    	lbl_status_title.setBounds(10, 11, 84, 21);
    	panel_status.add(lbl_status_title);
    	
    	JCheckBox check_done = new JCheckBox("Hoàn thành");
    	check_done.setFont(new Font("Dialog", Font.PLAIN, 15));
    	check_done.setOpaque(false); // Làm trong suốt
    	check_done.setBounds(20, 43, 110, 23);
    	panel_status.add(check_done);
    	
    	JCheckBox check_not_done = new JCheckBox("Chưa hoàn thành");
    	check_not_done.setFont(new Font("Dialog", Font.PLAIN, 15));
    	check_not_done.setOpaque(false); // Làm trong suốt
    	check_not_done.setBounds(20, 69, 150, 23);
    	panel_status.add(check_not_done);
    	
        // Tạo FlippableLabel 2
        FlippableLabel lbl_down_up_2 = new FlippableLabel("^", SwingConstants.CENTER);
        lbl_down_up_2.setBounds(10, 50, 50, 30);
        lbl_down_up_2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lbl_down_up_2.toggleFlip();
                lbl_down_up_2.repaint();

                int targetHeight = isPanelResized_2 ? 113 : 43; // Kích thước mục tiêu
                int targetY = isPanelResized_2 ? 208 : 136;    // Vị trí Y mục tiêu của panel_filter

                AnimatedMoving.animateResize(panel_status, panel_status.getHeight(), targetHeight, (currentHeight) -> {
                    panel_status.setBounds(34, 69, 222, currentHeight);
                });

                AnimatedMoving.animateMove(panel_filter, panel_filter.getY(), targetY, (currentY) -> {
                    panel_filter.setBounds(34, currentY, 222, isPanelResized_1 ? 43 : 90);
                });

                isPanelResized_2 = !isPanelResized_2;
            }
        });
        
    	lbl_down_up_2.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_down_up_2.setFont(new Font("Viner Hand ITC", Font.PLAIN, 19));
    	lbl_down_up_2.setBounds(173, 18, 49, 23);
    	panel_status.add(lbl_down_up_2);

    	JLabel lbl_receipt = new JLabel("Hóa đơn");
    	lbl_receipt.setFont(new Font("Dialog", Font.BOLD, 23));
    	lbl_receipt.setBounds(34, 0, 107, 64);
    	receiptPanel.add(lbl_receipt);
    	
    	RoundedLabel lbl_export = new RoundedLabel("Xuất thành file Excel");
    	lbl_export.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_export.setForeground(Color.BLACK);
    	lbl_export.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_export.setCornerRadius(10);
    	lbl_export.setBackground(new Color(129, 199, 132));
    	lbl_export.setBounds(1080, 15, 164, 41);
    	receiptPanel.add(lbl_export);
    	
    	lbl_export.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_export.setForeground(Color.WHITE);
    			lbl_export.setBackground(new Color(40, 167, 69));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_export.setForeground(Color.BLACK);
    			lbl_export.setBackground(new Color(129, 199, 132));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_export.setForeground(Color.WHITE);
    			lbl_export.setBackground(new Color(33, 136, 56));
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_export.setForeground(Color.WHITE);
    			lbl_export.setBackground(new Color(40, 167, 69));
    		}
		});
    	
    	RoundedLabel lbl_add = new RoundedLabel("+  Thêm mới");
    	lbl_add.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_add.setForeground(Color.BLACK);
    	lbl_add.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_add.setCornerRadius(10);
    	lbl_add.setBackground(new Color(129, 199, 132));
    	lbl_add.setBounds(930, 15, 130, 41);
    	receiptPanel.add(lbl_add);
    	
    	lbl_add.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_add.setForeground(Color.BLACK);
    			lbl_add.setBackground(new Color(129, 199, 132));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(33, 136, 56));
    			addReceipt();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
		});
    	
    	lbl_add.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_add.setForeground(Color.BLACK);
    			lbl_add.setBackground(new Color(129, 199, 132));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(33, 136, 56));
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_add.setForeground(Color.WHITE);
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
		});
    	
    	JScrollPane scrollpane_show_table = new JScrollPane();
    	scrollpane_show_table.setBounds(286, 69, 962, 450);
    	receiptPanel.add(scrollpane_show_table);
    	
    	Border roundedBorder = new LineBorder(Color.GRAY, 2, true);
    	scrollpane_show_table.setBorder(roundedBorder);
    	
    	ReceiptSelected = null;
    	
    	Receipt_table_mode = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Mã Hoá Đơn", "Trạng Thái Thanh Toán", "Thời Gian", "Tổng Tiền"
				}
			);
		
    	ReceiptTable = new JTable();
    	ReceiptTable.setModel(Receipt_table_mode);
    	ReceiptTable.getTableHeader().setReorderingAllowed(false);
    	ReceiptTable.setFont(new Font("Arial", Font.PLAIN, 20));
    	ReceiptTable.getColumnModel().getColumn(0).setPreferredWidth(100);
    	ReceiptTable.getColumnModel().getColumn(1).setPreferredWidth(250);
    	ReceiptTable.getColumnModel().getColumn(2).setPreferredWidth(200);
    	ReceiptTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		Font headerFont = new Font("Arial", Font.BOLD, 18);
		ReceiptTable.getTableHeader().setPreferredSize(new Dimension(ReceiptTable.getTableHeader().getWidth(), 30));
		ReceiptTable.getTableHeader().setFont(headerFont);
		ReceiptTable.setRowHeight(30);
    	
//		ReceiptTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    int selectedRow = ReceiptTable.getSelectedRow();
//                    if (selectedRow != -1) {
//                        tf_employee_id.setText(ReceiptTable.getValueAt(selectedRow, 0).toString());
//                        tf_employee_name.setText(ReceiptTable.getValueAt(selectedRow, 1).toString());
//                        tf_gender.setText(ReceiptTable.getValueAt(selectedRow, 2).toString());
//                        tf_phone_num.setText(ReceiptTable.getValueAt(selectedRow, 3).toString());
//                        
//                        ReceiptSelected = ReceiptTable.getValueAt(selectedRow, 0).toString();
//                    }
//                }
//            }
//        });
		
		scrollpane_show_table.setViewportView(ReceiptTable);
		loadReceipt();
    	
        return receiptPanel;
    }

    private void loadReceipt() {
    	BillDAO.loadData();
		for(Bill bill : BillDAO.list) {
	        Object[] newRow = {bill.getBillID(), bill.isWasPay(), bill.getTime(), bill.getPayment()};
	        Receipt_table_mode.addRow(newRow);
		}
    }
    
    private void addReceipt() {
        JTextField tfBillID = new JTextField();
        JTextField tfStatus = new JTextField();
        JTextField tfDateTime = new JTextField();
        JTextField tfTotalPrice = new JTextField("0");
        
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã Hóa Đơn:"));
        panel.add(tfBillID);
        panel.add(new JLabel("Trạng Thái Thanh Toán:"));
        panel.add(tfStatus);
        panel.add(new JLabel("Thời Gian:"));
        panel.add(tfDateTime);
        panel.add(new JLabel("Tổng Tiền:"));
        panel.add(tfTotalPrice);

        int result = JOptionPane.showConfirmDialog(null, panel, "Thêm hóa đơn mới", JOptionPane.OK_CANCEL_OPTION);

        // Xử lý nếu người dùng nhấn OK
        if (result == JOptionPane.OK_OPTION) {
            String billID = tfBillID.getText();
            if (billID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mã hóa đơn không được để trống!");
                return;
            }

            // Lấy thông tin, nếu trống thì chuyển thành "null"
            Boolean status = tfStatus.getText().trim().isEmpty() ? null : Boolean.valueOf(tfStatus.getText().trim());
            Date dateTime = tfDateTime.getText().trim().isEmpty() ? null : Date.valueOf(tfDateTime.getText().trim());
            Double totalPrice = tfTotalPrice.getText().trim().isEmpty() ? null : Double.valueOf(tfTotalPrice.getText().trim());
            
            try {
                Bill bill = new Bill(billID, status, dateTime, totalPrice);
                if(BillDAO.addBill(bill)) {
	                Object[] newRow = {bill.getBillID(), bill.isWasPay(), bill.getTime(), bill.getPayment()};
	                Receipt_table_mode.addRow(newRow);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }
    
    private JPanel createStaffPanel() {
    	JPanel staffPanel = new JPanel();
    	staffPanel.setBackground(new Color(255, 255, 255));
    	staffPanel.setLayout(null);
    	
    	JPanel panel_filter = new JPanel();
    	panel_filter.setBackground(new Color(255, 255, 255));
    	panel_filter.setBounds(38, 34, 143, 143);
    	panel_filter.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	staffPanel.add(panel_filter);
    	panel_filter.setLayout(null);
    	
    	JPanel panel_setting = new JPanel();
    	panel_setting.setBackground(new Color(255, 255, 255));
    	panel_setting.setBounds(233, 34, 1009, 143);
    	panel_setting.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	staffPanel.add(panel_setting);
    	panel_setting.setLayout(null);
    	
    	JLabel lbl_staff_id = new JLabel("Mã Nhân Viên");
    	lbl_staff_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_staff_id.setBounds(24, 11, 121, 33);
    	panel_setting.add(lbl_staff_id);
    	
    	JLabel lbl_staff_name = new JLabel("Tên Nhân Viên");
    	lbl_staff_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_staff_name.setBounds(24, 55, 121, 33);
    	panel_setting.add(lbl_staff_name);
    	
    	tf_staff_id = new JTextField();
    	tf_staff_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_staff_id.setBounds(155, 11, 168, 33);
    	tf_staff_id.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	panel_setting.add(tf_staff_id);
    	tf_staff_id.setColumns(10);
    	
    	tf_staff_name = new JTextField();
    	tf_staff_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_staff_name.setColumns(10);
    	tf_staff_name.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_staff_name.setBounds(155, 55, 168, 33);
    	panel_setting.add(tf_staff_name);
    	
    	JLabel lbl_gender = new JLabel("Giới Tính");
    	lbl_gender.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_gender.setBounds(24, 99, 121, 33);
    	panel_setting.add(lbl_gender);
    	
    	tf_gender = new JTextField();
    	tf_gender.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_gender.setColumns(10);
    	tf_gender.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_gender.setBounds(155, 99, 168, 33);
    	panel_setting.add(tf_gender);
    	
    	JLabel lbl_phone_num = new JLabel("Số Điện Thoại");
    	lbl_phone_num.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_phone_num.setBounds(428, 11, 121, 33);
    	panel_setting.add(lbl_phone_num);
    	
    	tf_phone_num = new JTextField();
    	tf_phone_num.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_phone_num.setColumns(10);
    	tf_phone_num.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_phone_num.setBounds(561, 11, 168, 33);
    	panel_setting.add(tf_phone_num);
    	
    	tf_position = new JTextField();
    	tf_position.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_position.setColumns(10);
    	tf_position.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_position.setBounds(561, 55, 168, 33);
    	panel_setting.add(tf_position);
    	
    	JLabel lbl_position = new JLabel("Vị Trí");
    	lbl_position.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_position.setBounds(428, 55, 121, 33);
    	panel_setting.add(lbl_position);
    	
    	RoundedLabel lbl_add = new RoundedLabel("Thêm Nhân Viên");
    	lbl_add.setForeground(Color.WHITE);
    	lbl_add.setBackground(new Color(129, 199, 132));
    	lbl_add.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_add.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_add.setBounds(837, 11, 149, 33);
    	lbl_add.setCornerRadius(10);
    	panel_setting.add(lbl_add);
    	
    	lbl_add.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_add.setBackground(new Color(129, 199, 132));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_add.setBackground(new Color(33, 136, 56));
    			addEmployee(); // Gọi phương thức thêm nhân viên
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
		});
    	
    	RoundedLabel lbl_adjust = new RoundedLabel("Điều Chỉnh");
    	lbl_adjust.setForeground(Color.WHITE);
    	lbl_adjust.setBackground(new Color(100, 181, 246));
    	lbl_adjust.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_adjust.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_adjust.setCornerRadius(10);
    	lbl_adjust.setBounds(837, 55, 149, 33);
    	panel_setting.add(lbl_adjust);
    	
    	lbl_adjust.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_adjust.setBackground(new Color(0, 123, 255));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_adjust.setBackground(new Color(100, 181, 246));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_adjust.setBackground(new Color(0, 86, 179));
    			editEmployee();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_adjust.setBackground(new Color(0, 123, 255));
    		}
		});
    	
    	RoundedLabel lbl_remove = new RoundedLabel("Xóa");
    	lbl_remove.setForeground(Color.WHITE);
    	lbl_remove.setBackground(new Color(229, 115, 115));
    	lbl_remove.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_remove.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_remove.setCornerRadius(10);
    	lbl_remove.setBounds(837, 99, 149, 33);
    	panel_setting.add(lbl_remove);
    	
    	lbl_remove.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			lbl_remove.setBackground(new Color(220, 53, 69));
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			lbl_remove.setBackground(new Color(229, 115, 115));
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    			lbl_remove.setBackground(new Color(176, 42, 55));
    			deleteEmployee();
    		}
    		
    		@Override
    	    public void mouseReleased(MouseEvent e) {
    			lbl_remove.setBackground(new Color(220, 53, 69));
    	    }
		});
    	
    	JScrollPane scrollpane_show_table = new JScrollPane();
    	scrollpane_show_table.setBounds(38, 224, 1204, 298);
    	staffPanel.add(scrollpane_show_table);
    	
    	Border roundedBorder = new LineBorder(Color.GRAY, 2, true);
    	scrollpane_show_table.setBorder(roundedBorder);
    	
    	EmpSelected = null;
    	
    	Emp_table_model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Mã Nhân Viên", "Họ và tên", "Giới tính", "Số Điện Thoại", "Vị Trí"
				}
			);
		
    	StaffTable = new JTable();
    	StaffTable.setModel(Emp_table_model);
    	StaffTable.getTableHeader().setReorderingAllowed(false);
    	StaffTable.setFont(new Font("Arial", Font.PLAIN, 20));
    	StaffTable.getColumnModel().getColumn(0).setPreferredWidth(100);
    	StaffTable.getColumnModel().getColumn(1).setPreferredWidth(250);
    	StaffTable.getColumnModel().getColumn(2).setPreferredWidth(200);
    	StaffTable.getColumnModel().getColumn(3).setPreferredWidth(150);
    	StaffTable.getColumnModel().getColumn(4).setPreferredWidth(200);
		Font headerFont = new Font("Arial", Font.BOLD, 18);
		StaffTable.getTableHeader().setPreferredSize(new Dimension(StaffTable.getTableHeader().getWidth(), 30));
		StaffTable.getTableHeader().setFont(headerFont);
		StaffTable.setRowHeight(30);
    	
		StaffTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = StaffTable.getSelectedRow();
                    if (selectedRow != -1) {
                        tf_staff_id.setText(StaffTable.getValueAt(selectedRow, 0).toString());
                        tf_staff_name.setText(StaffTable.getValueAt(selectedRow, 1).toString());
                        tf_gender.setText(StaffTable.getValueAt(selectedRow, 2).toString());
                        tf_phone_num.setText(StaffTable.getValueAt(selectedRow, 3).toString());
                        tf_position.setText(StaffTable.getValueAt(selectedRow, 4).toString());
                        
                        EmpSelected = StaffTable.getValueAt(selectedRow, 0).toString();
                    }
                }
            }
        });
		
		scrollpane_show_table.setViewportView(StaffTable);
		loadStaff();
    	return staffPanel;
    }
    
    private void loadStaff() {
		StaffDAO.loadData();
		for(Staff staff : StaffDAO.list) {
	        Object[] newRow = {staff.getStaffID(), staff.getFullName(), staff.getSex(), staff.getPhone(), staff.getPosition()};
	        Emp_table_model.addRow(newRow);
		}
    }
    
    private void addEmployee() {
        JTextField tfEmpID = new JTextField("UA - ");
        JTextField tfEmpName = new JTextField();
        JTextField tfGender = new JTextField();
        JTextField tfPhoneNum = new JTextField();
        JTextField tfPos = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã Nhân Viên:"));
        panel.add(tfEmpID);
        panel.add(new JLabel("Họ Và Tên:"));
        panel.add(tfEmpName);
        panel.add(new JLabel("Giới Tính:"));
        panel.add(tfGender);
        panel.add(new JLabel("Số Điện Thoại:"));
        panel.add(tfPhoneNum);
        panel.add(new JLabel("Vị Trí:"));
        panel.add(tfPos);

        int result = JOptionPane.showConfirmDialog(null, panel, "Thêm nhân viên mới", JOptionPane.OK_CANCEL_OPTION);

        // Xử lý nếu người dùng nhấn OK
        if (result == JOptionPane.OK_OPTION) {
            String empID = tfEmpID.getText();
            if (empID.isEmpty() || empID.equals("UA - ")) {
                JOptionPane.showMessageDialog(null, "Mã nhân viên không được để trống!");
                return;
            }

            // Lấy thông tin, nếu trống thì chuyển thành "null"
            String empName = tfEmpName.getText().trim().isEmpty() ? null : tfEmpName.getText().trim();
            String gender = tfGender.getText().trim().isEmpty() ? null : tfGender.getText().trim();
            String phoneNum = tfPhoneNum.getText().trim().isEmpty() ? null : tfPhoneNum.getText().trim();
            String Pos = tfPos.getText().trim().isEmpty() ? null : tfPos.getText().trim();

            // Dữ liệu Demo
            String userName = "";
            String password = "";
            Time startShift = null;
            Time endShift = null;
            try {
                Staff staff = new Staff(empID, userName, password, empName, gender, phoneNum, Pos, startShift, endShift);
                StaffDAO.addStaff(staff);
                Object[] newRow = {staff.getStaffID(), staff.getFullName(), staff.getSex(), staff.getPhone(), staff.getPosition()};
                Emp_table_model.addRow(newRow);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void editEmployee() {
        int selectedRow = StaffTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhân viên để sửa!");
            return;
        }

        // Lấy thông tin hiện tại từ hàng được chọn
        String currentEmpID = Emp_table_model.getValueAt(selectedRow, 0).toString();
        String currentEmpName = Emp_table_model.getValueAt(selectedRow, 1) != null ? Emp_table_model.getValueAt(selectedRow, 1).toString() : "";
        String currentGender = Emp_table_model.getValueAt(selectedRow, 2) != null ? Emp_table_model.getValueAt(selectedRow, 2).toString() : "";
        String currentPhoneNum = Emp_table_model.getValueAt(selectedRow, 3) != null ? Emp_table_model.getValueAt(selectedRow, 3).toString() : "";
        String currentPos = Emp_table_model.getValueAt(selectedRow, 4) != null ? Emp_table_model.getValueAt(selectedRow, 4).toString() : "";

        // Tạo các trường nhập liệu
        JTextField tfEmpID = new JTextField(currentEmpID);
        tfEmpID.setEditable(false);
        JTextField tfEmpName = new JTextField(currentEmpName);
        JTextField tfGender = new JTextField(currentGender);
        JTextField tfPhoneNum = new JTextField(currentPhoneNum);
        JTextField tfPos = new JTextField(currentPos);

        // Tạo bảng nhập liệu
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã Nhân Viên:"));
        panel.add(tfEmpID);
        panel.add(new JLabel("Họ Và Tên:"));
        panel.add(tfEmpName);
        panel.add(new JLabel("Giới tính:"));
        panel.add(tfGender);
        panel.add(new JLabel("Số Điện Thoại:"));
        panel.add(tfPhoneNum);
        panel.add(new JLabel("Vị Trí:"));
        panel.add(tfPos);

        int result = JOptionPane.showConfirmDialog(null, panel, "Sửa thông tin nhân viên", JOptionPane.OK_CANCEL_OPTION);

        // Xử lý nếu người dùng nhấn OK
        if (result == JOptionPane.OK_OPTION) {
            String empID = tfEmpID.getText().trim();
            if (empID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mã nhân viên không được để trống!");
                return;
            }

            // Lấy thông tin, nếu trống thì chuyển thành "null"
            String name = tfEmpName.getText().trim().isEmpty() ? null : tfEmpName.getText().trim();
            String gender = tfGender.getText().trim().isEmpty() ? null : tfGender.getText().trim();
            String phone = tfPhoneNum.getText().trim().isEmpty() ? null : tfPhoneNum.getText().trim();
            String pos = tfPos.getText().trim().isEmpty() ? null : tfPos.getText().trim();

            // Dữ liệu Demo
            String userName = "";
            String password = "";
            Time startShift = null;
            Time endShift = null;
            
            Staff currentStaff = new Staff(currentEmpID, userName, password, currentEmpName, currentGender, currentPhoneNum, currentPos, startShift, endShift);
            try {
                Staff newStaff = new Staff(empID, userName, password, name, gender, phone, pos, startShift, endShift);
                if (StaffDAO.updateStaff(currentStaff, newStaff)) {
                	Emp_table_model.setValueAt(newStaff.getFullName(), selectedRow, 1);
                	Emp_table_model.setValueAt(newStaff.getSex(), selectedRow, 2);
                	Emp_table_model.setValueAt(newStaff.getPhone(), selectedRow, 3);
                	Emp_table_model.setValueAt(newStaff.getPosition(), selectedRow, 4);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void deleteEmployee() {
        int selectedRow = StaffTable.getSelectedRow();
        
        // Kiểm tra nếu có dòng được chọn
        if (selectedRow >= 0) {
            String empID = StaffTable.getValueAt(selectedRow, 0).toString(); // Lấy mã nhân viên từ cột đầu tiên
            
            // Gọi phương thức xóa nhân viên trong cơ sở dữ liệu
            boolean success = StaffDAO.deleteStaff(empID);
            
            if (success) {
                // Nếu xóa thành công, xóa dòng trong bảng
                Emp_table_model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "Xóa nhân viên thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhân viên để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
}