package data_access_object;

import model.DetailReceipt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.JDBCUtil;

public class DetailReceiptDAO {
	public static ArrayList<DetailReceipt> list = new ArrayList<>();
    private static Connection conn = JDBCUtil.getConnection();

    public static void loadData() {
        list.clear(); // Xóa dữ liệu cũ trước khi tải mới
        String query = "SELECT * FROM detail_receipt";
        
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String dishID = rs.getString("dishID");
                String billID = rs.getString("billID");
                int dishQuantity = rs.getInt("dishQuantity");

                DetailReceipt detail = new DetailReceipt(dishID, billID, dishQuantity);
                list.add(detail);
//                System.out.println(dishID + " " + billID + " " + dishQuantity + "\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	public static List<DetailReceipt> getDetailReceiptList(String billID) {
		
        List<DetailReceipt> list = new ArrayList<>();
        String query = "SELECT dishID, dishQuantity FROM detail_receipt WHERE billID = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, billID);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String dishID = rs.getString("dishID");
                int quantity = rs.getInt("dishQuantity");
                list.add(new DetailReceipt(dishID, billID, quantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
	
    public static void storeDetailReceipt(Connection conn, ArrayList<DetailReceipt> list) {
        String sql = "INSERT INTO detail_receipt VALUES(?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (DetailReceipt d : list) {
                stmt.setString(1, d.getDishID());
                stmt.setString(2, d.getBillID());
                stmt.setInt(3, d.getDishQuantity());
                stmt.executeUpdate();
            }
            loadData();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
