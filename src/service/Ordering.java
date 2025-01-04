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
        ArrayList<Dish> list = orderList.getOrDefault(table.getTableID(), new ArrayList<>());
        table.setAvailable(false);

        list.addAll(listDish);
        orderList.put(table.getTableID(), list);
        return true;
    }

}
