package data_access_object;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SessionDAO {

    public static void storeSession(Connection conn, String sessionID, String staffID, String tableID, String billID) {
        String sql = "INSERT INTO session VALUES (?, ?, ?, ?)";
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
}
