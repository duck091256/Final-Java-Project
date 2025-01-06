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

        ResultSet rs = loadDataThisWeek();
        if (rs == null) return null;

        try {
            while(rs.next()) {
                detailReceiptArrayList.add(new DetailReceipt(
                        rs.getString("dishID"),
                        rs.getString("billID"),
                        rs.getInt("dishQuantity")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        detailReceiptArrayList.sort(new Comparator<DetailReceipt>() {
            @Override
            public int compare(DetailReceipt o1, DetailReceipt o2) {
                return o1.getDishID().compareTo(o2.getDishID());
            }
        });

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
            }
            else {
                quan += curDish.getDishQuantity();
            }
        }

        DetailReceipt lastDish = detailReceiptArrayList.get(detailReceiptArrayList.size() - 1);
        Dish tmp = DishDAO.map.get(lastDish.getDishID());
        quan += lastDish.getDishQuantity();
        Dish rankingDish = new Dish(tmp.getDishID(), tmp.getDishName(), tmp.getDishPrice(), tmp.getDishCategory(), tmp.getDishImage());
        rankingDish.setQuantity(quan);
        ans.add(rankingDish);

        ans.sort(new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o2.getQuantity(), o1.getQuantity());
            }
        });

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
    private static ResultSet loadDataThisWeek() {
        String sql = "SELECT * " +
                "FROM detail_receipt " +
                "INNER JOIN " +
                "bill " +
                "ON " +
                "bill.billID = detail_receipt.billID " +
                "WHERE bill.time >= DATEADD(DAY, " +
                    "CASE " +
                        "WHEN DATEPART(WEEKDAY, GETDATE()) = 1 THEN -6 " +
                        "ELSE 2 - DATEPART(WEEKDAY, GETDATE())" +
                    "END, " +
                "CAST(GETDATE() AS DATE)) AND bill.time <= GETDATE()";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
