package data_access_object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JOptionPane;

import database.JDBCUtil;
import model.Staff;
import model.Table;

public class TableDAO {
	
	private static TableDAO instance;
	
	public static TableDAO getInstance() {
		if(instance == null) {
			instance = new TableDAO();
		}
		return instance;
	}
	public static ArrayList<Table> list;

	/**
	 * Tải tất dữ liệu của nhân viên từ database vào list và canOrder của chương trình
	 * 
	 * Sử dụng JDBC để thực hiện các câu lệnh truy vấn, close database sau khi hoàn tất.
	 *
	 */
	public static void loadData() {
		list = new ArrayList<>();
		
		String sql = "SELECT * FROM dining_table";
		
		try (Connection conn = JDBCUtil.getConnection(); 	// Đổi lại hệ quản trị sau nhé
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Table table = new Table(
						rs.getString("tableID"),
                        rs.getString("floorStay"),
                        rs.getString("operatingStatus"),
                        rs.getString("responsibleBy"),
                        rs.getString("clientNum")
				);
				list.add(table);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Thêm table vào list
	 *
	 * Hàm sẽ kiểm tra PRIMARY KEY đã tồn tại hay chưa?
	 * 
	 * @param table nhận từ input người dùng nhập
	 * @return true nếu thành công, false nếu thất bại
	 */
	public static boolean addTable(Table table) {

		if (getTable(table.getTableID()) != null) {
			JOptionPane.showMessageDialog(null, "Thêm bàn thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		TableDAO.list.add(table);
        JOptionPane.showMessageDialog(null, "Đã thêm bàn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	/**
	 * Tìm kiếm và trả về table từ tableID tử người dùng nhập
	 *
	 * Hàm sẽ kiểm tra PRIMARY KEY đã tồn tại hay chưa?
	 * 
	 * @param tableID - Nhận vào ID của hàng hóa để tìm kiếm
	 * @return trả về Table được sử dụng để load lại vào giao diện hoặc null
	 */
	public static Table getTable(String tableID) {
		Optional<Table> result = list.stream().filter(new Predicate<Table>() {
			@Override
			public boolean test(Table table) {
				return table.getTableID().equals(tableID);
			}
		}).findFirst();

		return result.orElse(null);
	}
	
	/**
	 * Thay đổi người phụ trách của bàn
	 * 
	 * Hàm kiểm tra người phụ trách mới có tồn tại không?
	 *
	 * @param tableID ID của bàn cần thay đổi
	 * @param staffID ID của người phụ trách mới
	 * @return trả về true nếu thành công, false nếu thất bại
	 */
	public static boolean updateResponsible(String tableID, String staffID) {
		Table table = getTable(tableID);
		Staff staff = StaffDAO.getStaff(staffID);

		if (table == null || staff == null) return false;

		table.setResponsibleBy(staffID);
		return true;
	}

	/**
	 * Xóa table dựa vào tableID
	 *
	 * Hàm sẽ kiểm tra tableID có tồn tại không và thực hiện xóa bàn
	 *
	 * @param tableID id của bàn cần xóa
	 * @return true nếu thành công, false nếu thất bại
	 */
	public static boolean deleteTable(String tableID) {
		int preSize = list.size();
		list.removeIf(new Predicate<Table>() {
			@Override
			public boolean test(Table table) {
				return table.getTableID().equals(tableID);
			}
		});

		return preSize != list.size();
	}
	
	/**
	 * Lưu trữ tất dữ liệu của table vào database
	 * 
	 * Sử dụng JDBC để thực hiện các câu lệnh truy vấn, close database sau khi hoàn tất.
	 *
	 */
	public static void storeData() {
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
		String delete_sql = "DELETE FROM dining_table";
		
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
		String sql = "INSERT INTO dining_table VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			for (Table table : list) {
				stmt.setString(1, table.getTableID());
				stmt.setString(2, table.getFloorStay());
				stmt.setString(3, table.getOperatingStatus());
				stmt.setString(4, table.getResponsibleBy());
				stmt.setString(5, table.getClientNum());
				
				stmt.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
