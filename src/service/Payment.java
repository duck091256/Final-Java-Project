package service;

import database.JDBCUtil;
import model.DetailReceipt;
import model.Dish;
import model.Table;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import static data_access_object.BillDAO.storeBill;
import static data_access_object.DeltailReceiptDAO.storeDetailReceipt;
import static data_access_object.SessionDAO.storeSession;
import static service.Ordering.orderList;

public class Payment {

    /**
     * Hàm xử lí dịch vụ thanh toán.
     *
     * Thanh toán dựa trên bàn đã order:
     * tính tổng số tiền đã thanh toán,
     * tạo giá trị billID và sessionID dựa trên ngày tháng hiện tại,
     * Lấy người phụ trách hiện tại dựa vào table, sau đó
     * lần lượt ghi dữ liệu vào database (bill, session, detail_receipt)
     *
     * @param table bàn được yêu cầu thanh toán
     * @return true - nếu thành công, false - nếu thất bại
     */
    public static boolean payment(Table table) {
        if (table.getAvailable()) return false;
        LocalDateTime time = LocalDateTime.now();
        String billID = getBillID(time);

        ArrayList<Dish> list = orderList.getOrDefault(table.getTableID(), new ArrayList<>());
        ArrayList<DetailReceipt> detailBill = new ArrayList<>();

        double totalPrice = 0;

        list.sort(new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return o1.getDishName().compareTo(o2.getDishName());
            }
        });

        totalPrice += list.get(0).getDishPrice();
        int sub = list.get(0).getQuantity();
        for (int i = 1; i < list.size(); i++) {
            Dish curDish = list.get(i);
            Dish preDish = list.get(i - 1);

            totalPrice += list.get(i).getDishPrice();

            if (!curDish.getDishName().equals(preDish.getDishName())) {
                detailBill.add(new DetailReceipt(preDish.getDishID(), billID, sub));
                sub = curDish.getQuantity();
            }
            else {
                sub += preDish.getQuantity();
            }
        }

        totalPrice += list.get(list.size() - 1).getDishPrice();
        detailBill.add(new DetailReceipt(list.get(list.size() - 1).getDishID(), billID, sub));
        table.setAvailable(true);
        orderList.remove(table.getTableID());

        // Lưu vào database
        try (Connection conn = JDBCUtil.getConnection()) {

            // Stored Bill
            storeBill(conn, billID, time, totalPrice);

            // Stored Session
            String sessionID = billID + table.getResponsibleBy();
            storeSession(conn, sessionID, table.getResponsibleBy(), table.getTableID(), billID);

            // Stored detail receipt
            storeDetailReceipt(conn, detailBill);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Hàm tạo bill ID theo thời gian thực.
     *
     * Sử dụng ngày tháng năm và thời gian hiện tại để tạo bill ID -> tránh bị trùng lặp
     *
     * @param time thời gian hiện tại
     * @return chuỗi billID được định dạng dựa theo thời gian thực
     */
    private static String getBillID(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH:mm:ss");
        return time.format(formatter);
    }
}
