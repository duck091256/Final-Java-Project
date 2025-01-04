package service;

import database.JDBCUtil;
import model.DetailReceipt;
import model.Dish;
import model.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

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

        ArrayList<Dish> list = Ordering.orderList.getOrDefault(table.getTableID(), new ArrayList<>());
        ArrayList<DetailReceipt> detailBill = new ArrayList<>();

        double totalPrice = 0;

        list.sort(new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return o1.getDishName().compareTo(o2.getDishName());
            }
        });

        totalPrice += list.get(0).getDishPrice();
        int sub = 1;
        for (int i = 1; i < list.size(); i++) {
            Dish curDish = list.get(i);
            Dish preDish = list.get(i - 1);

            totalPrice += list.get(i).getDishPrice();

            if (!curDish.getDishName().equals(preDish.getDishName())) {
                detailBill.add(new DetailReceipt(preDish.getDishID(), billID, sub));
                sub = 0;
            }
            else sub++;
        }

        totalPrice += list.get(list.size() - 1).getDishPrice();
        detailBill.add(new DetailReceipt(list.get(list.size() - 1).getDishID(), billID, sub));
        table.setAvailable(true);
        Ordering.orderList.remove(table.getTableID());

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

    private static void storeDetailReceipt(Connection conn, ArrayList<DetailReceipt> list) {
        String sql = "INSERT INTO detail_receipt VALUES(?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (DetailReceipt d : list) {
                stmt.setString(1, d.getDishID());
                stmt.setString(2, d.getBillID());
                stmt.setInt(3, d.getDishQuantity());
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void storeSession(Connection conn, String sessionID, String staffID, String tableID, String billID) {
        String sql = "INSERT INTO Session VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sessionID);
            stmt.setString(1, staffID);
            stmt.setString(1, tableID);
            stmt.setString(1, billID);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void storeBill(Connection conn, String billID, LocalDateTime time, double totalPrice) {
        String sql = "INSERT INTO bill VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, billID);
            stmt.setInt(2, 1);
            stmt.setTimestamp(3, Timestamp.valueOf(time));
            stmt.setDouble(4, totalPrice);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getBillID(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH:mm:ss");
        return time.format(formatter);
    }
}
