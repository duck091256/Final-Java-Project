package data_access_object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.JOptionPane;

import database.JDBCUtil;
import model.Bill;
import model.Dish;

public class BillDAO {
	private static BillDAO instance;
	
	public static BillDAO getInstance() {
		if(instance == null) {
			instance = new BillDAO();
		}
		return instance;
	}
	public static HashMap<String, Bill> map;
    public static ArrayList<Bill> list;

    public static void loadData() {
    	map = new HashMap<String, Bill>();
        list = new ArrayList<>();

        String sql = "SELECT * FROM bill";

        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Bill bill = new Bill(
                        rs.getString("billID"),
                        rs.getBoolean("wasPay"),
                        rs.getDate("time"),
                        rs.getDouble("payment")
                );
                map.put(bill.getBillID(), bill);
                list.add(bill);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public static Bill getBill(String billID) {
		Optional<Bill> result = list.stream().filter(new Predicate<Bill>() {
			@Override
			public boolean test(Bill bill) {
				return bill.getBillID().equals(billID);
			}
		}).findFirst();

		return result.orElse(null);
	}
	
    public static boolean addBill(Bill bill) {
		
		if (getBill(bill.getBillID()) != null) {
			JOptionPane.showMessageDialog(null, "Thêm hóa đơn thất bại!", "Thông báo", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		list.add(bill);
		map.put(bill.getBillID(), bill);
        JOptionPane.showMessageDialog(null, "Đã hóa đơn mới thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

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
