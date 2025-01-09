package data_access_object;

import database.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RankingStaffDAO {
    public static void updateRankings(String staffID, double money) {
        String sql = "INSERT INTO ranking_staff(staffID, rating) VALUES(?, ?)";

        try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staffID);
            stmt.setDouble(2, money);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
