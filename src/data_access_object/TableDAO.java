package data_access_object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JOptionPane;

import database.JDBCUtil;
import model.Dish;
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
                        rs.getString("responsibleBy")
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
		addTableToDatabase(table, JDBCUtil.getConnection());
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
	 * Cập nhật thông tin bàn trong danh sách.
	 *
	 * @param oldTable Bàn cũ (bàn cần cập nhật thông tin).
	 * @param newTable Bàn mới (bàn với thông tin cần cập nhật).
	 * @return true - nếu cập nhật thành công, false - nếu thất bại.
	 */
	public static boolean updateTable(Table oldTable, Table newTable) {
	    String oldID = oldTable.getTableID();
	    String newID = newTable.getTableID();

	    // Tìm bàn cũ trong danh sách
	    Table existingTable = null;
	    for (Table table : list) {
	        if (table.getTableID().equals(oldID)) {
	            existingTable = table;
	            break;
	        }
	    }

	    // Nếu không tìm thấy bàn cũ
	    if (existingTable == null) {
	        JOptionPane.showMessageDialog(null, "Không tìm thấy bàn cần cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
	        return false;
	    }

	    // Kiểm tra nếu ID mới đã tồn tại trong danh sách (và không phải của bàn hiện tại)
	    for (Table table : list) {
	        if (table.getTableID().equals(newID) && !table.equals(existingTable)) {
	            JOptionPane.showMessageDialog(null, "Cập nhật bàn thất bại, ID mới đã tồn tại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	            return false;
	        }
	    }

	    // Cập nhật thông tin bàn
	    updateTableToDatabase(existingTable, newTable, JDBCUtil.getConnection());
	    
	    existingTable.setTableID(newID);
	    existingTable.setAvailable(newTable.getAvailable());
	    JOptionPane.showMessageDialog(null, "Cập nhật bàn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

	    return true;
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
		updateResponsibleToData(tableID, staffID);
		return true;
	}
	
	public static void updateResponsibleToData(String tableID, String staffID) {
		String sqlString = "UPDATE dining_table SET responsibleBy = ? WHERE tableID = ?";
		
		try (Connection connection = JDBCUtil.getConnection(); 
				PreparedStatement stmtPreparedStatement = connection.prepareStatement(sqlString)) {
			stmtPreparedStatement.setString(1, staffID);
			stmtPreparedStatement.setString(2, tableID);
			
			stmtPreparedStatement.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
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
		try (Connection connection = JDBCUtil.getConnection()) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getTableID().equals(tableID)) {
					deleteTableToDatabase(list.get(i), connection);
					list.remove(i);
					return true;
				}
			}
			return false;	
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
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
		String sql = "INSERT INTO dining_table VALUES (?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			for (Table table : list) {
				stmt.setString(1, table.getTableID());
				stmt.setString(2, table.getFloorStay());
				stmt.setString(3, table.getOperatingStatus());
				stmt.setString(4, table.getResponsibleBy());
				
				stmt.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static Table accessTable(int index) {
    	Table table = list.get(index);
    	return table;
    }
	
	public static Map<Integer, Integer> numOfTableByFloor() {
	    Map<Integer, Integer> tableCountByFloor = new HashMap<>();
	    try {
	        // Kết nối cơ sở dữ liệu
	        Connection conn = JDBCUtil.getConnection();

	        // Câu lệnh SQL với GROUP BY
	        String query = "SELECT floorStay, COUNT(*) AS number_of_table FROM dining_table GROUP BY floorStay";

	        // Thực thi câu lệnh
	        Statement stmt = conn.createStatement();
	        ResultSet rs = stmt.executeQuery(query);

	        // Đọc kết quả
	        while (rs.next()) {
	            int floorStay = rs.getInt("floorStay");
	            int numberOfTable = rs.getInt("number_of_table");
	            tableCountByFloor.put(floorStay, numberOfTable);
	        }

	        // Đóng kết nối
	        conn.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return tableCountByFloor;
	}
	

	public static void addTableToDatabase(Table table, Connection conn) {
		String sql = "INSERT INTO dining_table VALUES (?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, table.getTableID());
			stmt.setString(2, table.getFloorStay());
			stmt.setString(3, table.getOperatingStatus());
			stmt.setString(4, table.getResponsibleBy());
			
			stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void deleteTableToDatabase(Table table, Connection conn) {
		String delete_sql = "DELETE FROM dining_table WHERE tableID = ?";
		
		try (PreparedStatement stmt = conn.prepareStatement(delete_sql)) {
			stmt.setString(1, table.getTableID());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateTableToDatabase(Table table, Table newTable, Connection conn) {
		deleteTableToDatabase(table, conn);
		addTableToDatabase(newTable, conn);
	}
}
