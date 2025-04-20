package data_access_object;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.JOptionPane;

import database.JDBCUtil;
import model.Bill;
import model.DetailReceipt;
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
                
                List<DetailReceipt> details = DetailReceiptDAO.getDetailReceiptList(bill.getBillID());
                for (DetailReceipt detail : details) {
                    bill.addDetail(detail);
                }

                list.add(bill);
                map.put(bill.getBillID(), bill);
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

    public static ArrayList<Bill> loadBill() {
        String sql = "SELECT * FROM bill";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            ArrayList<Bill> ans = new ArrayList<>();
            while(rs.next()) {
                ans.add(new Bill(rs.getString(1), rs.getBoolean(2), rs.getDate(3), rs.getDouble(4)));
            }
            return ans;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void printReceiptToFile(String billID) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("DetailReceipt.txt"))) {
            DetailReceiptDAO detailDAO = new DetailReceiptDAO();
            DishDAO dishDAO = new DishDAO();

            List<DetailReceipt> detailList = detailDAO.getDetailReceiptList(billID);
            
            Map<String, Dish> dishMap = DishDAO.map;

            writer.write("=========================================================\n");
            writer.write("                    HÓA ĐƠN BÁN HÀNG                     \n");
            writer.write("=========================================================\n");
            writer.write("	    Mã hóa đơn: " + billID + "\n");
            writer.write("---------------------------------------------------------\n");
            writer.write(String.format("%-40s %-5s %10s%n", "Mặt hàng", "SL", "T.Tiền"));

            double total = 0;
            for (DetailReceipt detail : detailList) {
                Dish dish = dishMap.get(detail.getDishID());
                if (dish != null) {
                    double totalPrice = dish.getDishPrice() * detail.getDishQuantity();
                    total += totalPrice;
                    writer.write(String.format("%-40s %-5d %,10.2f%n",
                            dish.getDishName(), detail.getDishQuantity(), totalPrice));
                }
            }

            writer.write("---------------------------------------------------------\n");
            writer.write(String.format("%57s%n", String.format("Tổng tiền: %,.2f", total)));
            writer.write("=========================================================\n");
            writer.write("           ありがとうございました！またお会いしましょう！             \r\n"
            		+ "            Cảm ơn quý khách! Hẹn gặp lại!               ");
            
            JOptionPane.showMessageDialog(null, "Đã xuất hóa đơn chi tiết thành công!");
            
            System.out.println("Hóa đơn đã được lưu vào file DetailReceipt.txt\n");
            System.out.println("Để in hóa đơn trực tiếp từ file ta làm theo cách sau:\n");
            System.out.println("Đối với Windows đã kết nối với máy in -> mở CMD và chạy: notepad /p DetailReceipt.txt\n");

            int response = JOptionPane.showConfirmDialog(null,
                    "Bạn có muốn xem hóa đơn?", "Mở hóa đơn",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                openBillFile();
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }
    
    public static void openBillFile() {
        try {
        	ProcessBuilder pb = new ProcessBuilder("notepad", "DetailReceipt.txt");
        	pb.start();
            System.out.println("Đã mở hóa đơn trong Notepad.");
        } catch (IOException e) {
            System.err.println("Lỗi khi mở file: " + e.getMessage());
        }
    }
}
