package data_access_object;

import model.DetailReceipt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class DetailReceiptDAO {

    public static void storeDetailReceipt(Connection conn, ArrayList<DetailReceipt> list) {
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
}
