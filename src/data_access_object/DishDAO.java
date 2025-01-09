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
		
		if (getDish(dish.getDishID()) != null) {
			JOptionPane.showMessageDialog(null, "Thêm món thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		DishDAO.list.add(dish);
		DishDAO.map.put(dish.getDishID(), dish);
		DishDAO.originalList.add(dish);
        JOptionPane.showMessageDialog(null, "Đã món mới thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

	public static Dish getDish(String dishID) {
		Optional<Dish> result = list.stream().filter(new Predicate<Dish>() {
			@Override
			public boolean test(Dish dish) {
				return dish.getDishID().equals(dishID);
			}
		}).findFirst();

		return result.orElse(null);
	}

	public static boolean updateDish(Dish dish, Dish newDish) {
	    // Tìm món ăn hiện tại trong danh sách
	    for (int i = 0; i < DishDAO.list.size(); i++) {
	        if (DishDAO.list.get(i).getDishID().equals(dish.getDishID())) {
	            // Nếu không cập nhật ID của Dish
	            if (newDish.getDishID().equals(dish.getDishID())) {
	                DishDAO.list.set(i, newDish);

	                // Đồng bộ với originalList
	                for (int j = 0; j < DishDAO.originalList.size(); j++) {
	                    if (DishDAO.originalList.get(j).getDishID().equals(dish.getDishID())) {
	                        DishDAO.originalList.set(j, newDish);
	                        break;
	                    }
	                }

	                JOptionPane.showMessageDialog(null, "Cập nhật món ăn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	                return true;
	            }

	            // Nếu cập nhật ID của Dish
	            // Kiểm tra ID mới đã tồn tại hay chưa
	            for (Dish d : DishDAO.list) {
	                if (d.getDishID().equals(newDish.getDishID())) {
	                    JOptionPane.showMessageDialog(null, "Cập nhật món ăn thất bại, ID đã tồn tại!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	                    return false;
	                }
	            }

	            // Nếu ID mới chưa tồn tại
	            DishDAO.list.remove(i);
	            DishDAO.list.add(newDish);

	            // Đồng bộ với originalList
	            for (int j = 0; j < DishDAO.originalList.size(); j++) {
	                if (DishDAO.originalList.get(j).getDishID().equals(dish.getDishID())) {
	                    DishDAO.originalList.remove(j);
	                    DishDAO.originalList.add(newDish);
	                    break;
	                }
	            }

	            JOptionPane.showMessageDialog(null, "Cập nhật món ăn thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	            return true;
	        }
	    }

	    // Nếu không tìm thấy món ăn cần cập nhật
	    JOptionPane.showMessageDialog(null, "Không tìm thấy món ăn cần cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
	    return false;
	}

	public static boolean deleteDish(String id) {
	    // Lưu lại kích thước ban đầu của cả list và originalList
	    int initialSizeList = list.size();
	    int initialSizeOriginalList = originalList.size();
	    
	    // Xóa món ăn khỏi list
	    boolean isDeletedFromList = list.removeIf(dish -> dish.getDishID().equals(id));
	    
	    // Nếu món ăn đã bị xóa trong list, xóa nó khỏi originalList
	    if (isDeletedFromList) {
	        originalList.removeIf(dish -> dish.getDishID().equals(id));
	    }
	    
	    // Kiểm tra xem list và originalList đã thay đổi chưa
	    return initialSizeList != list.size() && initialSizeOriginalList != originalList.size();
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
