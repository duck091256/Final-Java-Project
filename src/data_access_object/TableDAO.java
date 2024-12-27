package data_access_object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import database.JDBCUtil;
import model.Table;

public class TableDAO {
	
	private static TableDAO instance;
	
	public static TableDAO getInstance() {
		if(instance == null) {
			instance = new TableDAO();
		}
		return instance;
	}
	
	public static HashMap<String, Table> map;
	public static ArrayList<Table> list;

	/**
	 * Tải tất dữ liệu của nhân viên từ database vào list và map của chương trình
	 * 
	 * Sử dụng JDBC để thực hiện các câu lệnh truy vấn, close database sau khi hoàn tất.
	 *
	 */
	public static void loadData() {
		map = new HashMap<>();
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
				map.put(table.getTableID(), table);
				list.add(table);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Thêm hàng hóa vào HashMap và List hiện tại của chương trình
	 * 
	 * Hàm sẽ kiểm tra PRIMARY KEY đã tồn tại hay chưa?
	 * 
	 * @param cargo nhận từ input người dùng nhập
	 * @return true nếu thành công, false nếu thất bại
	 */
	public static boolean addTable(Table table) {
		
		if (TableDAO.map.containsKey(table.getTableID())) {
	        JOptionPane.showMessageDialog(null, "Thêm bàn thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		TableDAO.list.add(table);
		TableDAO.map.put(table.getTableID(), table);
        JOptionPane.showMessageDialog(null, "Đã thêm bàn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	/**
	 * Tìm kiếm và trả về hàng hóa từ HashMap và List hiện tại của chương trình
	 * 
	 * Hàm sẽ kiểm tra PRIMARY KEY đã tồn tại hay chưa?
	 * 
	 * @param cargo_id - Nhận vào ID của hàng hóa để tìm kiếm
	 * @return trả về Cargo được sử dụng để load lại vào giao diện hoặc null
	 */
	public static Table getTable(String tableID) {
		return TableDAO.map.getOrDefault(tableID, null);
	}
	
	/**
	 * Cập nhật hàng hóa đã có trong HashMap và list hiện tại của chương trình
	 * 
	 * Hàm sẽ kiểm tra PRIMARY KEY đã tồn tại hay chưa?
	 * Cần truyền vào cargo trước khi update, là cargo được click vào để update
	 * 
	 * @param cargo là cargo được chọn để update, các thông tin nhận từ input người dùng nhập
	 * @return true nếu thành công, false nếu thất bại
	 */
	public static boolean checkOrder(Table table, Table newTable) {
		
		/* Nếu không cập nhật ID của Staff **/
		if (newTable.getTableID().equals(table.getTableID())) {
			TableDAO.map.put(table.getTableID(), newTable);
			JOptionPane.showMessageDialog(null, "Cập nhật hàng hóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		/* Nếu cập nhật ID của Staff */
		// Nếu ID mới đã tồn tại
		if (TableDAO.map.containsKey(newTable.getTableID())) {
			JOptionPane.showMessageDialog(null, "Cập nhật hàng hóa thất bại XXX", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// Nếu ID mới chưa tồn tại
		TableDAO.map.remove(table.getTableID());
		TableDAO.map.put(newTable.getTableID(), newTable);
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
	public static boolean deleteCargo(String id) {
		if (!TableDAO.map.containsKey(id)) {
			JOptionPane.showMessageDialog(null, "Xóa hàng hóa thất bại XXX", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		Table table = TableDAO.map.get(id);
		CargoDAO.map.remove(id);
		list.remove(table);
		JOptionPane.showMessageDialog(null, "Xóa hàng hóa thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}
	
	/**
	 * Lưu trữ tất dữ liệu của nhân viên vào database
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
			for (Table table : map.values()) {
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
