package data_access_object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import database.JDBCUtil;
import model.Dish;

public class DishDAO {
	
	private static DishDAO instance;
	
	public static DishDAO getInstance() {
		if(instance == null) {
			instance = new DishDAO();
		}
		return instance;
	}
	public static HashMap<String, Dish> map;
	public static ArrayList<Dish> list;

	public static void loadData() {
		map = new HashMap<>();
		list = new ArrayList<>();
		
		String sql = "SELECT * FROM dish";
		
		try (Connection conn = JDBCUtil.getConnection(); 	// Đổi lại hệ quản trị sau nhé
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				Dish dish = new Dish(
						rs.getString("dishID"),
                        rs.getString("dishName"),
                        rs.getString("dishCategory"),
                        rs.getDouble("dishPrice")
				);
				map.put(dish.getDishID(), dish);
				list.add(dish);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean addDish(Dish dish) {
		
		if (DishDAO.map.containsKey(dish.getDishID())) {
	        JOptionPane.showMessageDialog(null, "Thêm món ăn thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		DishDAO.list.add(dish);
		DishDAO.map.put(dish.getDishID(), dish);
        JOptionPane.showMessageDialog(null, "Đã thêm món ăn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	public static Dish getDish(String dishID) {
		return DishDAO.map.getOrDefault(dishID, null);
	}

	public static boolean updateDish(Dish dish, Dish newDish) {
		
		/* Nếu không cập nhật ID của Staff **/
		if (newDish.getDishID().equals(dish.getDishID())) {
			DishDAO.map.put(dish.getDishID(), newDish);
			JOptionPane.showMessageDialog(null, "Cập nhật món ăn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return true;
		}
		/* Nếu cập nhật ID của Staff */
		// Nếu ID mới đã tồn tại
		if (DishDAO.map.containsKey(newDish.getDishID())) {
			JOptionPane.showMessageDialog(null, "Cập nhật món ăn thất bại XXX", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		// Nếu ID mới chưa tồn tại
		DishDAO.map.remove(dish.getDishID());
		DishDAO.map.put(newDish.getDishID(), newDish);
		return true;
	}

	public static boolean deleteDish(String id) {
		if (!DishDAO.map.containsKey(id)) {
			JOptionPane.showMessageDialog(null, "Xóa món ăn thất bại XXX", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		Dish dish = DishDAO.map.get(id);
		DishDAO.map.remove(id);
		list.remove(dish);
		JOptionPane.showMessageDialog(null, "Xóa món ăn thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	public static void storeData() {
		try (Connection conn = JDBCUtil.getConnection()) {

			clearTable(conn);
			insertData(conn);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void clearTable(Connection conn) {
		String delete_sql = "DELETE FROM dish";
		
		try (PreparedStatement stmt = conn.prepareStatement(delete_sql)) {
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void insertData(Connection conn) {
		String sql = "INSERT INTO dish VALUES (?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			for (Dish dish : map.values()) {
				stmt.setString(1, dish.getDishID());
				stmt.setString(2, dish.getDishName());
				stmt.setString(3, dish.getDishCategory());
				stmt.setDouble(4, dish.getDishPrice());

				stmt.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
