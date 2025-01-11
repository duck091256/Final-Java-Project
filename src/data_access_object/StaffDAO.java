package data_access_object;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;

import javax.swing.JOptionPane;

import database.JDBCUtil;
import model.*;
import util.*;

public class StaffDAO {
	
	private static StaffDAO instance;
	
	public static StaffDAO getInstance() {
		if(instance == null) {
			instance = new StaffDAO();
		}
		return instance;
	}
	public static HashMap<String, Staff> map;
	public static ArrayList<Staff> list;

	/**
	 * Tải tất dữ liệu của nhân viên từ database vào list và map của chương trình
	 * 
	 * Sử dụng JDBC để thực hiện các câu lệnh truy vấn, close database sau khi hoàn tất.
	 *
	 */
	public static void loadData() {
		map = new HashMap<>();
		list = new ArrayList<>();
		
		String sql = "SELECT * FROM STAFF";
		
		try (Connection conn = JDBCUtil.getConnection(); 
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Staff staff = new Staff(
						rs.getString("staffID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("fullName"),
                        rs.getString("phone"),
                        rs.getString("position"),
                        rs.getString("sex"),
                        rs.getTime("startShift"),
                        rs.getTime("endShift")
				);
				map.put(staff.getStaffID(), staff);
				list.add(staff);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Thêm nhân viên vào HashMap và List hiện tại của chương trình
	 * 
	 * Hàm sẽ kiểm tra PRIMARY KEY đã tồn tại hay chưa?
	 * 
	 * @param staff nhận từ input người dùng nhập
	 * @return true nếu thành công, false nếu thất bại
	 */
	public static boolean addStaff(Staff staff) {
		
		if (StaffDAO.map.containsKey(staff.getStaffID())) {
	        JOptionPane.showMessageDialog(null, "Thêm nhân viên thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		StaffDAO.list.add(staff);
		StaffDAO.map.put(staff.getStaffID(), staff);
        JOptionPane.showMessageDialog(null, "Đã thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	/**
	 * Tìm kiếm và trả về nhân viên từ HashMap và List hiện tại của chương trình
	 * 
	 * Hàm sẽ kiểm tra PRIMARY KEY đã tồn tại hay chưa?
	 * 
	 * @param staffID - Nhận vào ID của nhân viên để tìm kiếm
	 * @return trả về Staff được sử dụng để load lại vào giao diện hoặc null
	 */
	public static Staff getStaff(String staffID) {
		return StaffDAO.map.getOrDefault(staffID, null);
	}
	
	/**
	 * Cập nhật nhân viên đã có trong HashMap và list hiện tại của chương trình
	 * 
	 * Hàm sẽ kiểm tra PRIMARY KEY đã tồn tại hay chưa?
	 * Cần truyền vào staff trước khi update, là staff được click vào để update
	 * 
	 * @param staff là staff được chọn để update, các thông tin nhận từ input người dùng nhập
	 * @return true nếu thành công, false nếu thất bại
	 */
	public static boolean updateStaff(Staff staff, Staff newStaff) {
		
		/* Nếu không cập nhật ID của Staff **/
		if (newStaff.getStaffID().equals(staff.getStaffID())) {
			staff.updateInfo(newStaff);
			JOptionPane.showMessageDialog(null, "Cập nhật nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		/* Nếu cập nhật ID của Staff */
		// Nếu ID mới đã tồn tại
		if (StaffDAO.map.containsKey(newStaff.getStaffID())) {
			JOptionPane.showMessageDialog(null, "Cập nhật nhân viên thất bại XXX", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// Nếu ID mới chưa tồn tại
		StaffDAO.map.remove(staff.getStaffID());
		staff.updateInfo(newStaff);
		StaffDAO.map.put(staff.getStaffID(), staff);
		return true;
	}

	/**
	 * Cập nhật nhân viên đã có trong HashMap và list hiện tại của chương trình
	 *
	 * Hàm sẽ kiểm tra PRIMARY KEY đã tồn tại hay chưa?
	 * Cần truyền vào staff trước khi update, là staff được click vào để update
	 *
	 * @param id là staff được chọn để update, các thông tin nhận từ input người dùng nhập
	 * @return true nếu thành công, false nếu thất bại
	 */
	public static boolean deleteStaff(String id) {
		if (!StaffDAO.map.containsKey(id)) {
			JOptionPane.showMessageDialog(null, "Xóa nhân viên thất bại XXX", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		Staff staff = StaffDAO.map.get(id);
		StaffDAO.map.remove(id);
		list.remove(staff);
		JOptionPane.showMessageDialog(null, "Xóa nhân viên thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}
	
	/**
	 * Lưu trữ tất dữ liệu của nhân viên vào database
	 * 
	 * Sử dụng JDBC để thực hiện các câu lệnh truy vấn, close database sau khi hoàn tất.
	 *
	 */
	public static void storeDate() {
		try (Connection conn = JDBCUtil.getConnection()) {

			clearTable(conn);
			insertData(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Xóa tất cả các dữ liệu cũ khỏi database
	 * 
	 * Sử dụng JDBC để thực hiện các câu lệnh truy vấn, close database sau khi hoàn tất.
	 * 
	 * @param conn - Connection đã được kết nối với database
	 */
	private static void clearTable(Connection conn) {
		String delete_sql = "DELETE FROM STAFF";
		
		try (PreparedStatement stmt = conn.prepareStatement(delete_sql)) {
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Thêm dữ liệu từ list vào database
	 *  
	 * Sử dụng JDBC để thực hiện các câu lệnh truy vấn, close database sau khi hoàn tất.
	 * 
	 * @param conn - Connection đã được kết nối với database
	 */
	private static void insertData(Connection conn) {
		String sql = "INSERT INTO STAFF VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			for (Staff staff : list) {
				stmt.setString(1, staff.getStaffID());
				stmt.setString(2, staff.getUserName());
				stmt.setString(3, staff.getPassword());
				stmt.setString(4, staff.getFullName());
				stmt.setString(5, staff.getPhone());
				stmt.setString(6, staff.getPosition());
				stmt.setString(7, staff.getSex());
				stmt.setTime(8, staff.getStartShift());
				stmt.setTime(9,  staff.getEndShift());
				
				stmt.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
