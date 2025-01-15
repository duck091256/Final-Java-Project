package service;

import model.Dish;
import model.Table;

import java.util.ArrayList;
import java.util.HashMap;

public class Ordering {
	public static HashMap<String, ArrayList<Dish>> orderList = new HashMap<>();

	/**
	 * Gọi thêm món vào danh sách các món hiện tại của bàn hiện tại
	 *
	 * @param table bàn order món ăn
	 * @param listDish Các món ăn được gọi thêm
	 * @return true - nếu thành công, false - nếu thất bại
	 */
	public static boolean order(Table table, ArrayList<Dish> listDish) {

	    try {
	        // Lấy danh sách món hiện tại hoặc tạo mới nếu chưa có
	        ArrayList<Dish> list = orderList.getOrDefault(table.getTableID(), new ArrayList<>());
	        
	        // Thêm các món mới vào danh sách
	        list.addAll(listDish);
	        
	        // Cập nhật lại danh sách món ăn vào orderList
	        orderList.put(table.getTableID(), list);
	        
	        // Đánh dấu bàn đã có khách
	        table.setAvailable(false);
	        
	        return true;
	    } catch (Exception e) {
	        System.out.println("Lỗi khi thêm món: " + e.getMessage());
	        return false;
	    }
	}

    /**
     * Trả về danh sách các món được gọi của bàn hiện tại
     *
     * @param table bàn order món ăn
     * @return ArrayList<Dish> danh sách những món table đang order
     */
    public static ArrayList<Dish> getOrderingFromTable(Table table) {
        return orderList.getOrDefault(table.getTableID(), new ArrayList<>());
    }
    
    /**
     * Thêm một món ăn vào danh sách món của một bàn cụ thể.
     *
     * @param table Bàn đích để thêm món ăn
     * @param dish  Món ăn cần thêm
     * @return true - nếu thêm thành công, false - nếu xảy ra lỗi
     */
    public static boolean addDishToTable(Table table, Dish dish) {
        try {
            // Lấy danh sách món hiện tại của bàn hoặc tạo mới nếu chưa có
            ArrayList<Dish> list = orderList.getOrDefault(table.getTableID(), new ArrayList<>());

            // Thêm món ăn vào danh sách
            list.add(dish);

            // Cập nhật lại danh sách món ăn vào orderList
            orderList.put(table.getTableID(), list);

            // Đánh dấu bàn đã có khách
            table.setAvailable(false);

            return true;
        } catch (Exception e) {
            System.out.println("Lỗi khi thêm món vào bàn: " + e.getMessage());
            return false;
        }
    }
}
