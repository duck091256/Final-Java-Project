package model;

import java.sql.Date;

public class Bill {
	private String billID;
	private boolean wasPay;
	private Date time;

	public Bill(String billID, boolean wasPay, Date time) {
		super();
		this.billID = billID;
		this.wasPay = wasPay;
		this.time = time;
	}
	
	
	
	public String getBillID() {
		return billID;
	}
	public void setBillID(String billID) {
		this.billID = billID;
	}
	public boolean isWasPay() {
		return wasPay;
	}
	public void setWasPay(boolean wasPay) {
		this.wasPay = wasPay;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}
