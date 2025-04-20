package data_access_object;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import database.JDBCUtil;
import model.Employee;

public class EmployeeDAO {
	private static EmployeeDAO instance;
	
	public static EmployeeDAO getInstance() {
		if(instance == null) {
			instance = new EmployeeDAO();
		}
		return instance;
	}
	
	public EmployeeDAO() {
		
	}
	
	public ArrayList<Employee> loadEmployee() {
		ArrayList<Employee> list = new ArrayList<>();
		try {
			// Bước 1: tạo kết nối đến CSDL
			Connection con = JDBCUtil.getConnection();
			
			// Bước 2: tạo ra đối tượng statement
			String sql = "SELECT * FROM employee";
			PreparedStatement st = con.prepareStatement(sql);
			
			// Bước 3: thực thi câu lệnh SQL
			System.out.println(sql);
			ResultSet rs = st.executeQuery(); //
			
			// Bước 4:
			while(rs.next()) {
				String employee_id = rs.getString(1);
				String employee_name = rs.getString(2);
				String gender = rs.getString(3);
				String phone_num = rs.getString(4);
				String positon = rs.getString(5);
				
				Employee employee = new Employee(employee_id, employee_name, gender, phone_num, positon);
				
				list.add(employee);
			}
			
			// Bước 5:
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return list;
	}
	
	public static boolean addEmployee(Employee employee) {
	    int ketQua = 0;
	    Connection con = null;
	    PreparedStatement st = null;
	    
	    try {
	        // Bước 1: tạo kết nối đến CSDL
	        con = JDBCUtil.getConnection();
	        
	        // Bước 2: tạo ra câu lệnh SQL
	        String sql = "INSERT INTO employee (employee_id, employee_name, gender, phone_num, position) VALUES (?, ?, ?, ?, ?)";
	        
	        // Bước 3: tạo đối tượng PreparedStatement và gán giá trị
	        st = con.prepareStatement(sql);
	        st.setString(1, employee.getEmployee_id());
	        st.setString(2, employee.getEmployee_name());
	        st.setString(3, employee.getGender());
	        st.setString(4, employee.getPhone_num());
	        st.setString(5, employee.getPosition());
	        
	        // Bước 4: thực thi câu lệnh SQL
	        ketQua = st.executeUpdate(); // trả về số lượng dòng thay đổi trong database
	        
	        if (ketQua > 0) {
	            JOptionPane.showMessageDialog(null, "Đã thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Thêm nhân viên thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
	    } finally {
	        // Đảm bảo đóng tài nguyên kết nối sau khi hoàn thành
	        JDBCUtil.closeConnection(con);
	        JDBCUtil.closeStatement(st);
	    }
	    
	    return ketQua > 0;
	}

	
	public Employee getEmployee(String id) {
		Employee employee = null;
		try {
			// Bước 1: tạo kết nối đến CSDL
			Connection con = JDBCUtil.getConnection();
			
			// Bước 2: tạo ra đối tượng statement
			String sql = "SELECT * FROM employee Where employee_id=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, id);
			
			// Bước 3: thực thi câu lệnh SQL
			System.out.println(sql);
			ResultSet rs = st.executeQuery(); //
			
			// Bước 4:
			while(rs.next()) {
				String employee_id = rs.getString(1);
				String employee_name = rs.getString(2);
				String gender = rs.getString(3);
				String phone_num = rs.getString(4);
				String position = rs.getString(6);
				
				employee = new Employee(employee_id, employee_name, gender, phone_num, position);
			}
			// Bước 5:
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return employee;
	}
	
	public static boolean updateEmployee(Employee employee) {
		int ketQua = 0;
		try {
			// Bước 1: tạo kết nối đến CSDL
			Connection con = JDBCUtil.getConnection();
			
			// Bước 2: tạo ra đối tượng statement
			String sql = "UPDATE employee SET "
					+ "employee_name=?, gender=?, phone_num=?, position=? "
					+ " Where employee_id=?";
			
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, employee.getEmployee_name());
			st.setString(2, employee.getGender());
			st.setString(3, employee.getPhone_num());
			st.setString(4, employee.getPosition());
			st.setString(5, employee.getEmployee_id());
			
			// Bước 3: thực thi câu lệnh SQL
			ketQua = st.executeUpdate(); // trả về số lượng dòng thay đổi trong database
			
			JOptionPane.showMessageDialog(null, "Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			
			// Bước 4: ngắt kết nối
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cập nhật nhân viên thất bại XXX", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
		
		return ketQua > 0;
	}
	
	public static boolean deleteEmployee(String id) {
		int ketQua = 0;
		try {
			// Bước 1: tạo kết nối đến CSDL
			Connection con = JDBCUtil.getConnection();
			
			// Bước 2: tạo ra đối tượng statement
			String sql = "DELETE FROM employee "
					+ " Where employee_id=?";
			
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, id);
			
			// Bước 3: thực thi câu lệnh SQL
			ketQua = st.executeUpdate(); // trả về số lượng dòng thay đổi trong database
			
			JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			
			// Bước 4: ngắt kết nối
			JDBCUtil.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Xóa nhân viên thất bại XXX", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		}
		
		return ketQua > 0;
	}
	
	public static List<String> getEmployeeNames() {
        List<String> employeeList = new ArrayList<>();
        employeeList.add("Tất cả"); // Tùy chọn gửi cho tất cả nhân viên

        try (Connection con = JDBCUtil.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM Employee")) {

            while (rs.next()) {
                employeeList.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeList;
    }

}
