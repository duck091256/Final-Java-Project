package data_access_object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
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
    public static ArrayList<Dish> originalList; // Lưu trạng thái ban đầu của danh sách

    public static void loadData() {
        map = new HashMap<>();
        list = new ArrayList<>();

        String sql = "SELECT * FROM dish";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Dish dish = new Dish(
                        rs.getString("dishID"),
                        rs.getString("dishName"),
                        rs.getDouble("dishPrice"),
                        rs.getString("dishCategory"),
                        rs.getString("dishImage")
                );
                map.put(dish.getDishID(), dish);
                list.add(dish);
                
                CloneData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void CloneData() {
        originalList = new ArrayList<>();

        String sql = "SELECT * FROM dish";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Dish dish = new Dish(
                        rs.getString("dishID"),
                        rs.getString("dishName"),
                        rs.getDouble("dishPrice"),
                        rs.getString("dishCategory"),
                        rs.getString("dishImage")
                );
                originalList.add(dish);
                Collections.shuffle(originalList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Dish> handleSort(boolean isChecked) {
    	if (isChecked) {
            System.out.println("Sắp xếp theo A -> Z");
            // Sắp xếp list theo DishID
            list.sort(Comparator.comparing(Dish::getDishID));
        } else {
            System.out.println("Khôi phục lại sắp xếp");
            // Khôi phục danh sách từ bản sao gốc
            list.clear();
            for (Dish dish : originalList) {
                list.add(dish);
            }
        }
        return list;
    }
    
    public static Dish accessDish(int index) {
    	Dish dish = list.get(index);
    	return dish;
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
	
	public static void updateDishImage(String relativeImagePath, String dishID) {
		Connection con = JDBCUtil.getConnection();
		String query = "UPDATE dish "
				+ "SET dishImage = ? "
				+ "WHERE dishID =  ?;";
		try {
	        PreparedStatement pstmt = con.prepareStatement(query);
	        pstmt.setString(1, relativeImagePath);
	        pstmt.setString(2, dishID);
	        int rowsAffected = pstmt.executeUpdate();

	        if (rowsAffected > 0) {
	            System.out.println("Dish image updated successfully.");
	        } else {
	            System.out.println("No dish found with the specified ID.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        JDBCUtil.closeConnection(con);
	    }
	}
	
	public static String getDishImage(String dishID) {
	    String imagePath = null;
	    Connection conn = JDBCUtil.getConnection();
	    String query = "SELECT dishImage FROM dish WHERE dishID = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setString(1, dishID);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            imagePath = rs.getString("dishImage"); // Lấy đường dẫn hình ảnh từ CSDL
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return imagePath;
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
		String sql = "INSERT INTO dish (dishID, dishName, dishPrice, dishCategory, dishImage) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			for (Dish dish : map.values()) {
				stmt.setString(1, dish.getDishID());
				stmt.setString(2, dish.getDishName());
				stmt.setDouble(3, dish.getDishPrice());
				stmt.setString(4, dish.getDishCategory());
				stmt.setString(5, dish.getDishImage());
				
				stmt.executeUpdate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int countRows() {
		int rowCount = 0;
		try {
            // Kết nối cơ sở dữ liệu
            Connection conn = JDBCUtil.getConnection();

            // Câu lệnh SQL
            String query = "SELECT COUNT(*) AS total_rows FROM dish";

            // Thực thi câu lệnh
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Đọc kết quả
            if (rs.next()) {
                rowCount = rs.getInt("total_rows");
                System.out.println("Số dòng trong bảng 'users': " + rowCount);
            }

            // Đóng kết nối
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return rowCount;
	}
}
