package model;

public class Dish {
	private String dishID;
	private String dishName;
	private double dishPrice;
	private int quantity;
	private String dishCategory;
	private String dishImage;

	public Dish(String dishID, String dishName, double dishPrice, String dishCategory, String dishImage) {
		super();
		this.dishID = dishID;
		this.dishName = dishName;
		this.dishCategory = dishCategory;
		this.dishPrice = dishPrice;
		this.quantity = 1;
		this.dishImage = dishImage;
	}

	@Override
	public Dish clone() {
		return new Dish(this.dishID, this.dishName, this.dishPrice, this.dishCategory, this.dishImage);
	}

	public String getDishImage() {
		return dishImage;
	}

	public void setDishImage(String dishImage) {
		this.dishImage = dishImage;
	}

	public int getQuantity() {return this.quantity;}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
