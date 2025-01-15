package service;

import data_access_object.DishDAO;
import database.JDBCUtil;
import model.DetailReceipt;
import model.Dish;
import model.RankingStaff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

public class RatingCalculation {

    public static ArrayList<RankingStaff> getListOfRankingStaff() {
        ArrayList<RankingStaff> ans = new ArrayList<>();

        ResultSet rs = loadDataRankingStaff();

        try {

            while(rs.next()) {
                RankingStaff rankingStaff = new RankingStaff(rs.getString("staffID"), rs.getInt("rating"));
                ans.add(rankingStaff);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        ans.sort(Comparator.comparing(RankingStaff::getRating));
        return ans;
    };

    /**
     * Hàm xử lí thống kê món ăn bán chạy trong tuần.
     *
     * Hàm truy vấn database và trả về danh sách các món ăn được sắp xếp giảm dần
     * theo số lượng bán được trong tuần (kể từ thứ 2).
     * @return 1 ArrayList là kết quả truy vấn
     */
    public static ArrayList<Dish> getListOfRatingDish() {
        ArrayList<Dish> ans = new ArrayList<>();
        ArrayList<DetailReceipt> detailReceiptArrayList = new ArrayList<>();
        Connection connection = JDBCUtil.getConnection();

        try (ResultSet rs = loadDataDishThisWeek(connection)) {  // Ensure ResultSet is managed properly
            if (rs == null) return null;

            while (rs.next()) {
                detailReceiptArrayList.add(new DetailReceipt(
                        rs.getString("dishID"),
                        rs.getString("billID"),
                        rs.getInt("dishQuantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // Sort and process data
        detailReceiptArrayList.sort(Comparator.comparing(DetailReceipt::getDishID));

        if (detailReceiptArrayList.isEmpty()) return ans;

        int quan = detailReceiptArrayList.get(0).getDishQuantity();

        for (int i = 1; i < detailReceiptArrayList.size(); i++) {
            DetailReceipt curDish = detailReceiptArrayList.get(i);
            DetailReceipt preDish = detailReceiptArrayList.get(i - 1);

            if (!curDish.getDishID().equals(preDish.getDishID())) {
                Dish tmp = DishDAO.map.get(preDish.getDishID());
                Dish rankingDish = new Dish(tmp.getDishID(), tmp.getDishName(), tmp.getDishPrice(), tmp.getDishCategory(), tmp.getDishImage());
                rankingDish.setQuantity(quan);
                ans.add(rankingDish);
                quan = curDish.getDishQuantity();
            } else {
                quan += curDish.getDishQuantity();
            }
        }

        DetailReceipt lastDish = detailReceiptArrayList.get(detailReceiptArrayList.size() - 1);
        Dish tmp = DishDAO.map.get(lastDish.getDishID());
        Dish rankingDish = new Dish(tmp.getDishID(), tmp.getDishName(), tmp.getDishPrice(), tmp.getDishCategory(), tmp.getDishImage());
        rankingDish.setQuantity(quan);
        ans.add(rankingDish);

        ans.sort((o1, o2) -> Integer.compare(o2.getQuantity(), o1.getQuantity()));

        return ans;
    }


    /**
     * Hàm xử lí thống kế nhân viên bán được nhiều món ăn trong năm.
     *
     * Hàm truy vấn database và trả về danh sách nhân viên sắp xếp giảm dần tổng số tiền bán được của những món ăn trong năm
     * vừa qua.
     *
     * @return 1 ArrayList là kết quả truy vấn
     */

    public static ArrayList<RankingStaff> getListOfRatingStaff() {return null;}

    /**
     * Hàm truy vấn database để lấy các món ăn trong detail_receipt và đảm bảo chúng được bán trong tuần này
     *
     * @return 1 ResultSet là kết quả của truy vấn database
     */
    private static ResultSet loadDataDishThisWeek(Connection conn) {
    	String sql = "SELECT * " +
                "FROM detail_receipt " +
                "INNER JOIN bill " +
                "ON bill.billID = detail_receipt.billID " +
                "WHERE bill.time >= DATE_ADD(CURDATE(), " +
                "    INTERVAL " +
                "        CASE " +
                "            WHEN DAYOFWEEK(NOW()) = 1 THEN -6 " +
                "            ELSE 2 - DAYOFWEEK(NOW()) " +
                "        END DAY " +
                ") " +
                "AND bill.time <= NOW();";

        try (
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ResultSet loadDataRankingStaff() {
        String sql = "SELECT staffID, rating FROM ranking_staff";

        try(Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Hàm tính tổng bill bán được trong tuần.
     *
     * @return Tổng bill bán được trong tuần hoặc -1 nếu xảy ra lỗi
     */
    public static double getTotalBillThisWeek() {
    	String sql = "SELECT SUM(payment) AS totalSales " +
                "FROM bill " +
                "WHERE bill.time >= DATE_ADD(CURDATE(), " +
                "INTERVAL " +
                "CASE " +
                "WHEN DAYOFWEEK(NOW()) = 1 THEN -6 " +
                "ELSE 2 - DAYOFWEEK(NOW()) " +
                "END DAY) " +
                "AND bill.time <= NOW();";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ) {
        	ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("totalSales");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
