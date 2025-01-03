package model;

public class Dish {
	private String dishID;
	private String dishName;
	private String dishCategory;
	private double dishPrice;
	
	public Dish(String dishID, String dishName, String dishCategory, double dishPrice) {
		super();
		this.dishID = dishID;
		this.dishName = dishName;
		this.dishCategory = dishCategory;
		this.dishPrice = dishPrice;
	}

	public String getDishID() {
		return dishID;
	}

	public void setDishID(String dishID) {
		this.dishID = dishID;
	}

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public String getDishCategory() {
		return dishCategory;
	}

	public void setDishCategory(String dishCategory) {
		this.dishCategory = dishCategory;
	}

	public double getDishPrice() {
		return dishPrice;
	}

	public void setDishPrice(double dishPrice) {
		this.dishPrice = dishPrice;
	}
	
	
}
