package view;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import data_access_object.DishDAO;
import data_access_object.EmployeeDAO;
import data_access_object.TableDAO;
import fx.RoundedBorderPanel;
import fx.RoundedLabel;
import fx.RoundedLabelEffect;
import model.Dish;
import model.Employee;
import model.Table;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.border.*;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.CardLayout;

public class ManagementSystem extends JFrame {

    private static final long serialVersionUID = 1L;
    private int tableCountFloor1 = 12, tableCountFloor2 = 15, dishCount = 10;
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
    private JTable DishTable, FloorTable, StaffTable;
    private DefaultTableModel Dish_table_model, Floor_table_model, Emp_table_model;
    private JTextField tf_employee_id;
    private JTextField tf_employee_name;
    private JTextField tf_gender;
    private JTextField tf_phone_num;
    private JTextField tf_position;
    private String DishSelected, FloorSelected, EmpSelected;
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
        panel_contain_CardLayout.add(createEmployeePanel(), "Employee");
        
        contentPane.add(panel_contain_CardLayout);
    }
    
    public void changeBoldToPlain(JLabel lbl_overall,JLabel lbl_dish,JLabel lbl_table,JLabel lbl_bill,JLabel lbl_employee) {
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
    
    public static boolean isBold(JLabel label) {
        Font font = label.getFont();
        return (font.getStyle() & Font.BOLD) != 0;
    }
    
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Trang Tổng quan"));
        return panel;
    }
    
    private JPanel createMenuPanel() {
    	JPanel menuPanel = new JPanel();
    	menuPanel.setBackground(new Color(255, 255, 255));
    	menuPanel.setLayout(null);
    	
    	lbl_switch_grip_for_menu = new RoundedLabel("Chế độ hiển thị lưới");
    	lbl_switch_grip_for_menu.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_switch_grip_for_menu.setForeground(Color.BLACK);
    	lbl_switch_grip_for_menu.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_switch_grip_for_menu.setCornerRadius(10);
    	lbl_switch_grip_for_menu.setBackground(new Color(211, 211, 211));
    	lbl_switch_grip_for_menu.setBounds(1082, 180, 160, 40);
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
    	lbl_switch_table_for_menu.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_switch_table_for_menu.setCornerRadius(10);
    	lbl_switch_table_for_menu.setBackground(new Color(211, 211, 211));
    	lbl_switch_table_for_menu.setBounds(1082, 180, 160, 40);
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
    	panel_filter.setBounds(38, 34, 149, 488);
    	panel_filter.setBackground(Color.WHITE);
    	menuPanel.add(panel_filter);
    	
    	JPanel panel_setting = new JPanel();
    	panel_setting.setBounds(233, 34, 1009, 143);
    	panel_setting.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_setting.setBackground(Color.white);
    	menuPanel.add(panel_setting);
    	panel_setting.setLayout(null);
    	
    	JLabel lbl_dish_id = new JLabel("Mã Món Ăn");
    	lbl_dish_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_dish_id.setBounds(39, 31, 121, 33);
    	panel_setting.add(lbl_dish_id);
    	
    	tf_dish_id = new JTextField();
    	tf_dish_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_dish_id.setColumns(10);
    	tf_dish_id.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_dish_id.setBounds(187, 31, 168, 33);
    	panel_setting.add(tf_dish_id);
    	
    	tf_dish_name = new JTextField();
    	tf_dish_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_dish_name.setColumns(10);
    	tf_dish_name.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_dish_name.setBounds(187, 75, 168, 33);
    	panel_setting.add(tf_dish_name);
    	
    	JLabel lbl_dish_name = new JLabel("Tên Món Ăn");
    	lbl_dish_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_dish_name.setBounds(39, 75, 121, 33);
    	panel_setting.add(lbl_dish_name);
    	
    	JLabel lbl_dish_category = new JLabel("Loại Món Ăn");
    	lbl_dish_category.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_dish_category.setBounds(443, 75, 121, 33);
    	panel_setting.add(lbl_dish_category);
    	
    	JLabel lbl_dish_price = new JLabel("Giá");
    	lbl_dish_price.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_dish_price.setBounds(443, 31, 121, 33);
    	panel_setting.add(lbl_dish_price);
    	
    	tf_dish_price = new JTextField();
    	tf_dish_price.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_dish_price.setColumns(10);
    	tf_dish_price.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_dish_price.setBounds(590, 31, 168, 33);
    	panel_setting.add(tf_dish_price);
    	
    	tf_dish_category = new JTextField();
    	tf_dish_category.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_dish_category.setColumns(10);
    	tf_dish_category.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_dish_category.setBounds(590, 75, 168, 33);
    	panel_setting.add(tf_dish_category);
    	
    	RoundedLabel lbl_add = new RoundedLabel("Thêm Món Ăn");
    	lbl_add.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_add.setForeground(Color.WHITE);
    	lbl_add.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_add.setCornerRadius(10);
    	lbl_add.setBackground(new Color(129, 199, 132));
    	lbl_add.setBounds(828, 11, 149, 33);
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
    			addDish();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_add.setBackground(new Color(40, 167, 69));
    		}
		});
    	
    	RoundedLabel lbl_adjust = new RoundedLabel("Điều Chỉnh");
    	lbl_adjust.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_adjust.setForeground(Color.WHITE);
    	lbl_adjust.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_adjust.setCornerRadius(10);
    	lbl_adjust.setBackground(new Color(100, 181, 246));
    	lbl_adjust.setBounds(828, 55, 149, 33);
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
    			editDish();
    		}
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    			lbl_adjust.setBackground(new Color(0, 123, 255));
    		}
		});
    	
    	RoundedLabel lbl_remove = new RoundedLabel("Xóa");
    	lbl_remove.setHorizontalAlignment(SwingConstants.CENTER);
    	lbl_remove.setForeground(Color.WHITE);
    	lbl_remove.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_remove.setCornerRadius(10);
    	lbl_remove.setBackground(new Color(229, 115, 115));
    	lbl_remove.setBounds(828, 99, 149, 33);
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
    			deleteDish();
    		}
    		
    		@Override
    	    public void mouseReleased(MouseEvent e) {
    			lbl_remove.setBackground(new Color(220, 53, 69));
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
    	
    	Dish_table_model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Mã Món Ăn", "Tên Món Ăn", "Giá", "Loại Món Ăn"
				}
			);
		
		DishTable = new JTable();
		DishTable.setModel(Dish_table_model);
		DishTable.getTableHeader().setReorderingAllowed(false);
		DishTable.setFont(new Font("Arial", Font.PLAIN, 20));
		DishTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		DishTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		DishTable.getColumnModel().getColumn(2).setPreferredWidth(200);
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
                        tf_dish_id.setText(DishTable.getValueAt(selectedRow, 0).toString());
                        tf_dish_name.setText(DishTable.getValueAt(selectedRow, 1).toString());
                        tf_dish_price.setText(DishTable.getValueAt(selectedRow, 2).toString());
                        tf_dish_category.setText(DishTable.getValueAt(selectedRow, 3).toString());
                        
                        DishSelected = DishTable.getValueAt(selectedRow, 0).toString();
                    }
                }
            }
        });
		
		table_mode_show_table_for_menu.setViewportView(DishTable);
    	loadDish();
		
		return table_mode_show_table_for_menu;
    }
    
    private JPanel switchGripModeForMenu() {
    	
    	// Chế độ lưới (Switch Mode)
    	grip_mode_show_menu = new JPanel();
    	grip_mode_show_menu.setBackground(Color.WHITE);
    	grip_mode_show_menu.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	grip_mode_show_menu.setBounds(38, 224, 1204, 298);
    	grip_mode_show_menu.setLayout(null);
        
        menu = new JPanel();
        menu.setBackground(Color.WHITE);
        menu.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
        menu.setBounds(0, 0, 1202, 296);
        menu.setLayout(new GridLayout(0, 5, 10, 10));
    	
        loadMenu();
        
    	JScrollPane scrollPane = new JScrollPane(menu);
        scrollPane.setBounds(0, 0, 1009, 298);
        grip_mode_show_menu.add(scrollPane);
    	
    	return grip_mode_show_menu;
    }
    
    private void loadMenu() {
        for (int i = 1; i <= dishCount; i++) {
        	addDishToMenu("Món " + i);
        }
    }
    
    private void addNewDish() {
        dishCount++;
        addDishToMenu("Món " + dishCount);
        menu.revalidate(); // Làm mới giao diện
        menu.repaint();
    }
    
    private void addDishToMenu(String dish) {
    	JPanel JPmenu = new JPanel();
    	JPmenu.setBackground(Color.WHITE);
    	JPmenu.setPreferredSize(new Dimension(100, 100));
    	JPmenu.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
        JLabel lbl_dish = new JLabel("<html>" + dish + "<br>");
        JPmenu.add(lbl_dish);
        
        // Sự kiện khi nhấn
        JPmenu.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		JOptionPane.showMessageDialog(null, "Bạn đã chọn " + dish);
        	}
		});
        menu.add(JPmenu);
    }
    
    public void loadDish() {
    	DishDAO.loadData();
		for(Dish dish : DishDAO.map.values()) {
	        Object[] newRow = {dish.getDishID(), dish.getDishName(), dish.getDishPrice(), dish.getDishCategory()};
	        Dish_table_model.addRow(newRow);
		}
    }
    
    public void sortByName() {
    	ArrayList<Dish> ls = new ArrayList<>(DishDAO.map.values());
    }
    
    public void addDish() {
        JTextField tfDishID = new JTextField();
        JTextField tfDishName = new JTextField();
        JTextField tfPrice = new JTextField();
        JTextField tfCategory = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã Món Ăn:"));
        panel.add(tfDishID);
        panel.add(new JLabel("Tên Món Ăn:"));
        panel.add(tfDishName);
        panel.add(new JLabel("Giá Tiền:"));
        panel.add(tfPrice);
        panel.add(new JLabel("Loại Món Ăn:"));
        panel.add(tfCategory);

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
            String price = tfPrice.getText().trim().isEmpty() ? null : tfPrice.getText().trim();
            Double category = tfCategory.getText().trim().isEmpty() ? null : Double.valueOf(tfCategory.getText().trim());
            
            try {
                Dish dish = new Dish(dishID, dishName, price, category);
                DishDAO.addDish(dish);
                Object[] newRow = {dish.getDishID(), dish.getDishName(), dish.getDishPrice(), dish.getDishCategory()};
                Dish_table_model.addRow(newRow);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }

    public void editDish() {
        int selectedRow = DishTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng hóa để sửa!");
            return;
        }

        // Lấy thông tin hiện tại từ hàng được chọn
        String currentDishID = Dish_table_model.getValueAt(selectedRow, 0).toString();
        String currentDishName = Dish_table_model.getValueAt(selectedRow, 1) != null ? Dish_table_model.getValueAt(selectedRow, 1).toString() : "";
        String currentPrice = Dish_table_model.getValueAt(selectedRow, 2) != null ? Dish_table_model.getValueAt(selectedRow, 2).toString() : "";
        Double currentCategory = Dish_table_model.getValueAt(selectedRow, 3) != null ? Double.valueOf(Dish_table_model.getValueAt(selectedRow, 3).toString()) : null;
        
        // Tạo đối tượng hàng hóa hiện tại
        Dish currentDish = new Dish(currentDishID, currentDishName, currentPrice, currentCategory);
        
        // Tạo các trường nhập liệu
        JTextField tfDishID = new JTextField(currentDishID);
        tfDishID.setEditable(false);
        JTextField tfDishName = new JTextField(currentDishName);
        JTextField tfPrice = new JTextField(currentPrice);
        JTextField tfCategory = new JTextField(currentCategory.toString());
        
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
            String price = tfPrice.getText().trim().isEmpty() ? null : tfPrice.getText().trim();
            Double category = tfCategory.getText().trim().isEmpty() ? null : Double.valueOf(tfCategory.getText().trim());

            try {
                Dish newDish = new Dish(dishID, name, price, category);
                DishDAO.updateDish(currentDish, newDish);
                Dish_table_model.setValueAt(newDish.getDishName(), selectedRow, 1);
                Dish_table_model.setValueAt(newDish.getDishPrice(), selectedRow, 2);
                Dish_table_model.setValueAt(newDish.getDishCategory(), selectedRow, 3);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }

    public void deleteDish() {
        int selectedRow = DishTable.getSelectedRow();
        
        // Kiểm tra nếu có dòng được chọn
        if (selectedRow >= 0) {
            String dishID = DishTable.getValueAt(selectedRow, 0).toString();
            
            // Gọi phương thức xóa hàng hóa trong cơ sở dữ liệu
            boolean success = DishDAO.deleteDish(dishID);
            
            if (success) {
                // Nếu xóa thành công, xóa dòng trong bảng
            	Dish_table_model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "Xóa món ăn thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một món ăn để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private JPanel createFloorPanel() {
    	JPanel floorPanel = new JPanel();
    	floorPanel.setBackground(new Color(255, 255, 255));
    	floorPanel.setLayout(null);
    	
    	JPanel panel_filter = new JPanel();
    	panel_filter.setBackground(new Color(255, 255, 255));
    	panel_filter.setBounds(38, 22, 160, 166);
    	panel_filter.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	floorPanel.add(panel_filter);
    	panel_filter.setLayout(null);
    	
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
    	
    	JPanel panel_setting = new JPanel();
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
                        tf_tableNum.setText(FloorTable.getValueAt(selectedRow, 0).toString());
                        tf_floorStay.setText(FloorTable.getValueAt(selectedRow, 1).toString());
                        tf_status.setText(FloorTable.getValueAt(selectedRow, 2).toString());
                        tf_Respond.setText(FloorTable.getValueAt(selectedRow, 3).toString());
                        tf_clientNum.setText(FloorTable.getValueAt(selectedRow, 4).toString());
                        
                        FloorSelected = FloorTable.getValueAt(selectedRow, 0).toString();
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
    	grip_mode_show_table_floor1.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	grip_mode_show_table_floor1.setBounds(38, 224, 1204, 298);
    	grip_mode_show_table_floor1.setLayout(null);
        
        floor1 = new JPanel();
        floor1.setBackground(Color.WHITE);
        floor1.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
        floor1.setBounds(0, 0, 1202, 296);
        floor1.setLayout(new GridLayout(0, 4, 10, 10));
    	
        loadTablesFloor1();
        
    	JScrollPane scrollPane1 = new JScrollPane(floor1);
        scrollPane1.setBounds(0, 0, 1204, 298);
        grip_mode_show_table_floor1.add(scrollPane1);
    	
    	return grip_mode_show_table_floor1;
    }
    
    private void loadTablesFloor1() {
        for (int i = 1; i <= tableCountFloor1; i++) {
            addTableToPanelFloor1("Bàn " + i, "Trống");
        }
    }
    
    private void addTableFloor1() {
        tableCountFloor1++;
        addTableToPanelFloor1("Bàn " + tableCountFloor1, "Trống");
        floor1.revalidate(); // Làm mới giao diện
        floor1.repaint();
    }
    
    private void addTableToPanelFloor1(String tableName, String status) {
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
        floor1.add(Table);
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
        floor2.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
        floor2.setBounds(0, 0, 1202, 296);
        floor2.setLayout(new GridLayout(0, 4, 10, 10));
    	
        loadTablesFloor2();
        
    	JScrollPane scrollPane2 = new JScrollPane(floor2);
        scrollPane2.setBounds(0, 0, 1204, 298);
        grip_mode_show_table_floor2.add(scrollPane2);
    	
    	return grip_mode_show_table_floor2;
    }
    
    private void loadTablesFloor2() {
        for (int i = 1; i <= tableCountFloor2; i++) {
            addTableToPanelFloor2("Bàn " + i, "Trống");
        }
    }
    
    private void addTableFloor2() {
        tableCountFloor2++;
        addTableToPanelFloor2("Bàn " + tableCountFloor2, "Trống");
        floor2.revalidate(); // Làm mới giao diện
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
    
    public void loadFloor() {
    	TableDAO.loadData();
		for(Table table : TableDAO.map.values()) {
	        Object[] newRow = {table.getTableID(), table.getFloorStay(), table.getOperatingStatus(), table.getResponsibleBy(), table.getClientNum()};
	        Floor_table_model.addRow(newRow);
		}
    }
    
    private JPanel createEmployeePanel() {
    	JPanel employeePanel = new JPanel();
    	employeePanel.setBackground(new Color(255, 255, 255));
    	employeePanel.setLayout(null);
    	
    	JPanel panel_filter = new JPanel();
    	panel_filter.setBackground(new Color(255, 255, 255));
    	panel_filter.setBounds(38, 34, 143, 143);
    	panel_filter.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	employeePanel.add(panel_filter);
    	panel_filter.setLayout(null);
    	
    	JPanel panel_setting = new JPanel();
    	panel_setting.setBackground(new Color(255, 255, 255));
    	panel_setting.setBounds(233, 34, 1009, 143);
    	panel_setting.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	employeePanel.add(panel_setting);
    	panel_setting.setLayout(null);
    	
    	JLabel lbl_employee_id = new JLabel("Mã Nhân Viên");
    	lbl_employee_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_employee_id.setBounds(24, 11, 121, 33);
    	panel_setting.add(lbl_employee_id);
    	
    	JLabel lbl_employee_name = new JLabel("Tên Nhân Viên");
    	lbl_employee_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_employee_name.setBounds(24, 55, 121, 33);
    	panel_setting.add(lbl_employee_name);
    	
    	tf_employee_id = new JTextField();
    	tf_employee_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_employee_id.setBounds(155, 11, 168, 33);
    	tf_employee_id.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	panel_setting.add(tf_employee_id);
    	tf_employee_id.setColumns(10);
    	
    	tf_employee_name = new JTextField();
    	tf_employee_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_employee_name.setColumns(10);
    	tf_employee_name.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_employee_name.setBounds(155, 55, 168, 33);
    	panel_setting.add(tf_employee_name);
    	
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
    	employeePanel.add(scrollpane_show_table);
    	
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
                        tf_employee_id.setText(StaffTable.getValueAt(selectedRow, 0).toString());
                        tf_employee_name.setText(StaffTable.getValueAt(selectedRow, 1).toString());
                        tf_gender.setText(StaffTable.getValueAt(selectedRow, 2).toString());
                        tf_phone_num.setText(StaffTable.getValueAt(selectedRow, 3).toString());
                        tf_position.setText(StaffTable.getValueAt(selectedRow, 4).toString());
                        
                        EmpSelected = StaffTable.getValueAt(selectedRow, 0).toString();
                    }
                }
            }
        });
		
		scrollpane_show_table.setViewportView(StaffTable);
		loadEmployee();
    	return employeePanel;
    }
    
    public void loadEmployee() {
		ArrayList<Employee> list = EmployeeDAO.getInstance().loadEmployee();
		Emp_table_model.setRowCount(0);
		for(Employee employee : list) {
	        Object[] newRow = {employee.getEmployee_id(), employee.getEmployee_name(), employee.getGender(), employee.getPhone_num(), employee.getPosition()};
	        Emp_table_model.addRow(newRow);
		}
    }
    
    public void addEmployee() {
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

            try {
                Employee employee = new Employee(empID, empName, gender, phoneNum, Pos);
                EmployeeDAO.addEmployee(employee);
                Object[] newRow = {employee.getEmployee_id(), employee.getEmployee_name(), employee.getGender(), employee.getPhone_num(), employee.getPosition()};
                Emp_table_model.addRow(newRow);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }

    public void editEmployee() {
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

            try {
                Employee employee = new Employee(empID, name, gender, phone, pos);
                EmployeeDAO.updateEmployee(employee);
                Emp_table_model.setValueAt(employee.getEmployee_name(), selectedRow, 1);
                Emp_table_model.setValueAt(employee.getGender(), selectedRow, 2);
                Emp_table_model.setValueAt(employee.getPhone_num(), selectedRow, 3);
                Emp_table_model.setValueAt(employee.getPosition(), selectedRow, 4);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }

    public void deleteEmployee() {
        int selectedRow = StaffTable.getSelectedRow();
        
        // Kiểm tra nếu có dòng được chọn
        if (selectedRow >= 0) {
            String empID = StaffTable.getValueAt(selectedRow, 0).toString(); // Lấy mã nhân viên từ cột đầu tiên
            
            // Gọi phương thức xóa nhân viên trong cơ sở dữ liệu
            boolean success = EmployeeDAO.deleteEmployee(empID);
            
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