package model;

public class UsedDish {
	private String dishID;
	private int billID;
	private int dishQuantity;
	
	public UsedDish(String dishID, int billID, int dishQuantity) {
		super();
		this.dishID = dishID;
		this.billID = billID;
		this.dishQuantity = dishQuantity;
	}

	public String getDishID() {
		return dishID;
	}

	public void setDishID(String dishID) {
		this.dishID = dishID;
	}

	public int getBillID() {
		return billID;
	}

	public void setBillID(int billID) {
		this.billID = billID;
	}

	public int getDishQuantity() {
		return dishQuantity;
	}

	public void setDishQuantity(int dishQuantity) {
		this.dishQuantity = dishQuantity;
	}

}
