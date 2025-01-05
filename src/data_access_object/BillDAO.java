package data_access_object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class BillDAO {

    public static void storeBill(Connection conn, String billID, LocalDateTime time, double totalPrice) {
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
}
