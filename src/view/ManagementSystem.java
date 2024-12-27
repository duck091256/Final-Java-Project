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

import data_access_object.CargoDAO;
import data_access_object.EmployeeDAO;
import data_access_object.TableDAO;
import fx.RoundedBorderPanel;
import fx.RoundedLabel;
import fx.RoundedLabelEffect;
import model.Cargo;
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
    private int tableCountFloor1 = 12, tableCountFloor2 = 15;
    private JPanel contentPane;
    private JPanel grip_mode_show_table_floor1, grip_mode_show_table_floor2;
    private RoundedLabelEffect lbl_switch_table_floor1, lbl_switch_table_floor2;
    private Boolean checkFloor1 = false, checkFloor2 = true;
    private JScrollPane table_mode_show_table;
    private JPanel floor1, floor2;
    private RoundedLabel lbl_switch_table, lbl_switch_grip;
    private Boolean overallCheckStatus = false, cargoCheckStatus = true, tableCheckStatus = true, billCheckStatus = true, employeeCheckStatus = true;
    private JLabel lbl_overall, lbl_cargo, lbl_table, lbl_bill, lbl_employee;
    private CardLayout cardLayout, switch_CardLayout;
    private JPanel panel_contain_CardLayout, panel_contain_switch_CardLayout;
    private JTable CargoTable, FloorTable, StaffTable;
    private DefaultTableModel Cargo_table_model, Floor_table_model, Emp_table_model;
    private JTextField tf_employee_id;
    private JTextField tf_employee_name;
    private JTextField tf_gender;
    private JTextField tf_phone_num;
    private JTextField tf_position;
    private String CargoSelected, FloorSelected, EmpSelected;
    private JTextField tf_cargo_id;
    private JTextField tf_cargo_name;
    private JTextField tf_stock_quantity;
    private JTextField tf_price;
    private JTextField tf_suppiler;
    private JTextField tf_expiration_date;
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
    		CargoDAO.storeData();
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
        		changeBoldToPlain(lbl_overall, lbl_cargo, lbl_table, lbl_bill, lbl_employee);
        		lbl_overall.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_overall.setForeground(new Color(255, 255, 255));
        		overallCheckStatus = false;
        		cargoCheckStatus = true;
        		tableCheckStatus = true;
        		billCheckStatus = true;
        		employeeCheckStatus = true;
        	}
        });
        
        lbl_cargo = new JLabel("Hàng hóa");
        lbl_cargo.setForeground(Color.WHITE);
        lbl_cargo.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl_cargo.setBounds(174, 28, 89, 21);
        panel_down.add(lbl_cargo);
        
        // Sự kiện di chuột đến, đi và nhấn
        lbl_cargo.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseEntered(MouseEvent e) {
        		lbl_cargo.setFont(new Font("Arial", Font.BOLD, 18));
                lbl_cargo.setForeground(new Color(255, 255, 255));
        	}
        	
        	@Override
        	public void mouseExited(MouseEvent e) {
        		if (cargoCheckStatus) {
        			lbl_cargo.setFont(new Font("Arial", Font.PLAIN, 18));
                    lbl_cargo.setForeground(new Color(255, 255, 255));
        		}
        	}
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		cardLayout.show(panel_contain_CardLayout, "Cargo");
        		changeBoldToPlain(lbl_overall, lbl_cargo, lbl_table, lbl_bill, lbl_employee);
        		lbl_cargo.setFont(new Font("Arial", Font.BOLD, 18));
                lbl_cargo.setForeground(new Color(255, 255, 255));
                overallCheckStatus = true;
        		cargoCheckStatus = false;
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
        		changeBoldToPlain(lbl_overall, lbl_cargo, lbl_table, lbl_bill, lbl_employee);
        		lbl_table.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_table.setForeground(new Color(255, 255, 255));
                overallCheckStatus = true;
        		cargoCheckStatus = true;
        		tableCheckStatus = false;
        		billCheckStatus = true;
        		employeeCheckStatus = true;
        	}
        });
        
        lbl_bill = new JLabel("Giao dịch");
        lbl_bill.setForeground(Color.WHITE);
        lbl_bill.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl_bill.setBounds(413, 28, 89, 21);
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
        		changeBoldToPlain(lbl_overall, lbl_cargo, lbl_table, lbl_bill, lbl_employee);
        		lbl_bill.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_bill.setForeground(new Color(255, 255, 255));
                overallCheckStatus = true;
        		cargoCheckStatus = true;
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
        		changeBoldToPlain(lbl_overall, lbl_cargo, lbl_table, lbl_bill, lbl_employee);
        		lbl_employee.setFont(new Font("Arial", Font.BOLD, 18));
        		lbl_employee.setForeground(new Color(255, 255, 255));
                overallCheckStatus = true;
        		cargoCheckStatus = true;
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
        panel_contain_CardLayout.add(createCargoPanel(), "Cargo");
        panel_contain_CardLayout.add(createFloorPanel(), "Floor");
        panel_contain_CardLayout.add(createEmployeePanel(), "Employee");
        
        contentPane.add(panel_contain_CardLayout);
    }
    
    public void changeBoldToPlain(JLabel lbl_overall,JLabel lbl_cargo,JLabel lbl_table,JLabel lbl_bill,JLabel lbl_employee) {
    	// Đổi tất cả label thành PLAIN trước mới kiểm tra rồi chuyển thành BOLD
    	this.lbl_overall.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_overall.setForeground(new Color(255, 255, 255));
		this.lbl_cargo.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_cargo.setForeground(new Color(255, 255, 255));
		this.lbl_table.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_table.setForeground(new Color(255, 255, 255));
		this.lbl_bill.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_bill.setForeground(new Color(255, 255, 255));
		this.lbl_employee.setFont(new Font("Arial", Font.PLAIN, 18));
		this.lbl_employee.setForeground(new Color(255, 255, 255));
    	if (isBold(lbl_overall)) {
    		this.lbl_overall.setFont(new Font("Arial", Font.PLAIN, 18));
    		this.lbl_overall.setForeground(new Color(255, 255, 255));
    		System.out.println("Overall change");
    	} else if (isBold(lbl_cargo)) {
    		this.lbl_cargo.setFont(new Font("Arial", Font.PLAIN, 18));
    		this.lbl_cargo.setForeground(new Color(255, 255, 255));
    		System.out.println("Cargo change");
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
    
    private JPanel createCargoPanel() {
    	JPanel cargoPanel = new JPanel();
    	cargoPanel.setBackground(new Color(255, 255, 255));
    	cargoPanel.setLayout(null);
    	
    	JPanel panel_filter = new JPanel();
    	panel_filter.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_filter.setBounds(38, 34, 149, 488);
    	panel_filter.setBackground(Color.WHITE);
    	cargoPanel.add(panel_filter);
    	
    	JPanel panel_setting = new JPanel();
    	panel_setting.setBounds(233, 34, 1009, 143);
    	panel_setting.setBorder(new RoundedBorderPanel(15, new Color(45, 61, 75), 1));
    	panel_setting.setBackground(Color.white);
    	cargoPanel.add(panel_setting);
    	panel_setting.setLayout(null);
    	
    	JLabel lbl_cargo_id = new JLabel("Mã Hàng Hóa");
    	lbl_cargo_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_cargo_id.setBounds(38, 11, 121, 33);
    	panel_setting.add(lbl_cargo_id);
    	
    	tf_cargo_id = new JTextField();
    	tf_cargo_id.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_cargo_id.setColumns(10);
    	tf_cargo_id.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_cargo_id.setBounds(186, 11, 168, 33);
    	panel_setting.add(tf_cargo_id);
    	
    	tf_cargo_name = new JTextField();
    	tf_cargo_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_cargo_name.setColumns(10);
    	tf_cargo_name.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_cargo_name.setBounds(186, 55, 168, 33);
    	panel_setting.add(tf_cargo_name);
    	
    	JLabel lbl_cargo_name = new JLabel("Tên Hàng Hóa");
    	lbl_cargo_name.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_cargo_name.setBounds(38, 55, 121, 33);
    	panel_setting.add(lbl_cargo_name);
    	
    	JLabel lbl_stock_quantity = new JLabel("Số Lượng Tồn Kho");
    	lbl_stock_quantity.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_stock_quantity.setBounds(38, 99, 138, 33);
    	panel_setting.add(lbl_stock_quantity);
    	
    	tf_stock_quantity = new JTextField();
    	tf_stock_quantity.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_stock_quantity.setColumns(10);
    	tf_stock_quantity.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_stock_quantity.setBounds(186, 99, 168, 33);
    	panel_setting.add(tf_stock_quantity);
    	
    	JLabel lbl_suppiler = new JLabel("Ngày Nhập");
    	lbl_suppiler.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_suppiler.setBounds(442, 55, 121, 33);
    	panel_setting.add(lbl_suppiler);
    	
    	JLabel lbl_price = new JLabel("Giá");
    	lbl_price.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_price.setBounds(442, 11, 121, 33);
    	panel_setting.add(lbl_price);
    	
    	tf_price = new JTextField();
    	tf_price.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_price.setColumns(10);
    	tf_price.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_price.setBounds(551, 11, 168, 33);
    	panel_setting.add(tf_price);
    	
    	tf_suppiler = new JTextField();
    	tf_suppiler.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_suppiler.setColumns(10);
    	tf_suppiler.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_suppiler.setBounds(551, 55, 168, 33);
    	panel_setting.add(tf_suppiler);
    	
    	JLabel lbl_expiration_date = new JLabel("Ngày Hết Hạn");
    	lbl_expiration_date.setFont(new Font("Arial", Font.PLAIN, 16));
    	lbl_expiration_date.setBounds(442, 99, 121, 33);
    	panel_setting.add(lbl_expiration_date);
    	
    	tf_expiration_date = new JTextField();
    	tf_expiration_date.setFont(new Font("Arial", Font.PLAIN, 16));
    	tf_expiration_date.setColumns(10);
    	tf_expiration_date.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    	tf_expiration_date.setBounds(551, 99, 168, 33);
    	panel_setting.add(tf_expiration_date);
    	
    	RoundedLabel lbl_add = new RoundedLabel("Thêm Hàng Hóa");
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
    			addCargo();
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
    			editCargo();
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
    			deleteCargo();
    		}
    		
    		@Override
    	    public void mouseReleased(MouseEvent e) {
    			lbl_remove.setBackground(new Color(220, 53, 69));
    	    }
		});
    	
    	JScrollPane scrollpane_show_table = new JScrollPane();
    	scrollpane_show_table.setBounds(233, 217, 1009, 305);
    	cargoPanel.add(scrollpane_show_table);
    	
    	Border roundedBorder = new LineBorder(Color.GRAY, 2, true);
    	scrollpane_show_table.setBorder(roundedBorder);
    	
    	CargoSelected = null;
    	
    	Cargo_table_model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Mã Hàng Hóa", "Tên Hàng Hóa", "Số Lượng Tồn Kho", "Giá", "Ngày Nhập", "Ngày Hết Hạn"
				}
			);
		
		CargoTable = new JTable();
		CargoTable.setModel(Cargo_table_model);
		CargoTable.getTableHeader().setReorderingAllowed(false);
		CargoTable.setFont(new Font("Arial", Font.PLAIN, 20));
		CargoTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		CargoTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		CargoTable.getColumnModel().getColumn(2).setPreferredWidth(200);
		CargoTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		CargoTable.getColumnModel().getColumn(4).setPreferredWidth(150);
		CargoTable.getColumnModel().getColumn(5).setPreferredWidth(150);
		Font headerFont = new Font("Arial", Font.BOLD, 18);
		CargoTable.getTableHeader().setPreferredSize(new Dimension(CargoTable.getTableHeader().getWidth(), 30));
		CargoTable.getTableHeader().setFont(headerFont);
		CargoTable.setRowHeight(30);
    	
		CargoTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = CargoTable.getSelectedRow();
                    if (selectedRow != -1) {
                        tf_cargo_id.setText(CargoTable.getValueAt(selectedRow, 0).toString());
                        tf_cargo_name.setText(CargoTable.getValueAt(selectedRow, 1).toString());
                        tf_stock_quantity.setText(CargoTable.getValueAt(selectedRow, 2).toString());
                        tf_price.setText(CargoTable.getValueAt(selectedRow, 3).toString());
                        tf_suppiler.setText(CargoTable.getValueAt(selectedRow, 4).toString());
                        tf_expiration_date.setText(CargoTable.getValueAt(selectedRow, 5).toString());
                        
                        CargoSelected = CargoTable.getValueAt(selectedRow, 0).toString();
                    }
                }
            }
        });
		
		scrollpane_show_table.setViewportView(CargoTable);
    	loadCargo();
    	return cargoPanel;
    }
    
    public void loadCargo() {
    	CargoDAO.loadData();
		for(Cargo cargo : CargoDAO.map.values()) {
	        Object[] newRow = {cargo.getCargo_id(), cargo.getCargo_name(), cargo.getStock_quantity(), cargo.getPrice(), cargo.getSuppiler(), cargo.getExpiration_date()};
	        Cargo_table_model.addRow(newRow);
		}
    }
    
    public void sortByName() {
    	ArrayList<Cargo> ls = new ArrayList<>(CargoDAO.map.values());
    }
    
    public void addCargo() {
        JTextField tfCargoID = new JTextField();
        JTextField tfCargoName = new JTextField();
        JTextField tfQuantity = new JTextField();
        JTextField tfPrice = new JTextField();
        JTextField tfSuppiler = new JTextField();
        JTextField tfExpire = new JTextField();
        
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã Hàng Hóa:"));
        panel.add(tfCargoID);
        panel.add(new JLabel("Tên Hàng Hóa:"));
        panel.add(tfCargoName);
        panel.add(new JLabel("Số Lượng:"));
        panel.add(tfQuantity);
        panel.add(new JLabel("Giá Tiền:"));
        panel.add(tfPrice);
        panel.add(new JLabel("Ngày Nhập Hàng:"));
        panel.add(tfSuppiler);
        panel.add(new JLabel("Ngày Hết Hạn:"));
        panel.add(tfExpire);

        int result = JOptionPane.showConfirmDialog(null, panel, "Thêm hàng mới", JOptionPane.OK_CANCEL_OPTION);

        // Xử lý nếu người dùng nhấn OK
        if (result == JOptionPane.OK_OPTION) {
            String cargoID = tfCargoID.getText();
            if (cargoID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mã hàng hóa không được để trống!");
                return;
            }

            // Lấy thông tin, nếu trống thì chuyển thành "null"
            String cargoName = tfCargoName.getText().trim().isEmpty() ? null : tfCargoName.getText().trim();
            String quantity = tfQuantity.getText().trim().isEmpty() ? null : tfQuantity.getText().trim();
            String price = tfPrice.getText().trim().isEmpty() ? null : tfPrice.getText().trim();
            Date suppiler = tfSuppiler.getText().trim().isEmpty() ? null : Date.valueOf(tfSuppiler.getText().trim());
            Date expire = tfExpire.getText().trim().isEmpty() ? null : Date.valueOf(tfExpire.getText().trim());
            
            try {
                Cargo cargo = new Cargo(cargoID, cargoName, quantity, price, suppiler, expire);
                CargoDAO.addCargo(cargo);
                Object[] newRow = {cargo.getCargo_id(), cargo.getCargo_name(), cargo.getStock_quantity(), cargo.getPrice(), cargo.getSuppiler(), cargo.getExpiration_date()};
                Cargo_table_model.addRow(newRow);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }

    public void editCargo() {
        int selectedRow = CargoTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng hóa để sửa!");
            return;
        }

        // Lấy thông tin hiện tại từ hàng được chọn
        String currentCargoID = Cargo_table_model.getValueAt(selectedRow, 0).toString();
        String currentCargoName = Cargo_table_model.getValueAt(selectedRow, 1) != null ? Cargo_table_model.getValueAt(selectedRow, 1).toString() : "";
        String currentQuantity = Cargo_table_model.getValueAt(selectedRow, 2) != null ? Cargo_table_model.getValueAt(selectedRow, 2).toString() : "";
        String currentPrice = Cargo_table_model.getValueAt(selectedRow, 3) != null ? Cargo_table_model.getValueAt(selectedRow, 3).toString() : "";
        Date currentSuppiler = Cargo_table_model.getValueAt(selectedRow, 4) != null ? Date.valueOf(Cargo_table_model.getValueAt(selectedRow, 4).toString()) : null;
        Date currentExpire = Cargo_table_model.getValueAt(selectedRow, 5) != null ? Date.valueOf(Cargo_table_model.getValueAt(selectedRow, 5).toString()) : null;

        
        // Tạo đối tượng hàng hóa hiện tại
        Cargo currentCargo = new Cargo(currentCargoID, currentCargoName, currentQuantity, currentPrice, currentSuppiler, currentExpire);
        
        // Tạo các trường nhập liệu
        JTextField tfCargoID = new JTextField(currentCargoID);
        tfCargoID.setEditable(false);
        JTextField tfCargoName = new JTextField(currentCargoName);
        JTextField tfQuantity = new JTextField(currentQuantity);
        JTextField tfPrice = new JTextField(currentPrice);
        JTextField tfSuppiler = new JTextField(currentSuppiler.toString());
        JTextField tfExpire = new JTextField(currentExpire.toString());

        // Tạo bảng nhập liệu
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Mã Hàng Hóa:"));
        panel.add(tfCargoID);
        panel.add(new JLabel("Tên Hàng Hóa:"));
        panel.add(tfCargoName);
        panel.add(new JLabel("Số Lượng:"));
        panel.add(tfQuantity);
        panel.add(new JLabel("Giá Tiền:"));
        panel.add(tfPrice);
        panel.add(new JLabel("Ngày Nhập Hàng:"));
        panel.add(tfSuppiler);
        panel.add(new JLabel("Ngày Hết Hạn:"));
        panel.add(tfExpire);

        int result = JOptionPane.showConfirmDialog(null, panel, "Sửa thông tin hàng hóa", JOptionPane.OK_CANCEL_OPTION);

        // Xử lý nếu người dùng nhấn OK
        if (result == JOptionPane.OK_OPTION) {
            String cargoID = tfCargoID.getText().trim();
            if (cargoID.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Mã hàng hóa không được để trống!");
                return;
            }

            // Lấy thông tin, nếu trống thì chuyển thành "null"
            String name = tfCargoName.getText().trim().isEmpty() ? null : tfCargoName.getText().trim();
            String quantity = tfQuantity.getText().trim().isEmpty() ? null : tfQuantity.getText().trim();
            String price = tfPrice.getText().trim().isEmpty() ? null : tfPrice.getText().trim();
            Date suppiler = tfSuppiler.getText().trim().isEmpty() ? null : Date.valueOf(tfSuppiler.getText().trim());
            Date expire = tfExpire.getText().trim().isEmpty() ? null : Date.valueOf(tfExpire.getText().trim());

            try {
                Cargo newCargo = new Cargo(cargoID, name, quantity, price, suppiler, expire);
                CargoDAO.updateCargo(currentCargo, newCargo);
                Cargo_table_model.setValueAt(newCargo.getCargo_name(), selectedRow, 1);
                Cargo_table_model.setValueAt(newCargo.getStock_quantity(), selectedRow, 2);
                Cargo_table_model.setValueAt(newCargo.getPrice(), selectedRow, 3);
                Cargo_table_model.setValueAt(newCargo.getSuppiler(), selectedRow, 4);
                Cargo_table_model.setValueAt(newCargo.getExpiration_date(), selectedRow, 5);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage());
            }
        }
    }

    public void deleteCargo() {
        int selectedRow = CargoTable.getSelectedRow();
        
        // Kiểm tra nếu có dòng được chọn
        if (selectedRow >= 0) {
            String cargoID = CargoTable.getValueAt(selectedRow, 0).toString();
            
            // Gọi phương thức xóa hàng hóa trong cơ sở dữ liệu
            boolean success = CargoDAO.deleteCargo(cargoID);
            
            if (success) {
                // Nếu xóa thành công, xóa dòng trong bảng
                Cargo_table_model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "Xóa hàng hóa thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng hóa để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
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
    	lbl_switch_grip.setBounds(1065, 180, 166, 40);
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
    	lbl_switch_table.setBounds(1065, 180, 166, 40);
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