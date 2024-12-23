package model;

import java.sql.Date;

public class Employee {
	private String employee_id;
	private String employee_name;
	private String gender;
	private String phone_num;
	private String position;
	
	public Employee() {}

	public Employee(String employee_id, String employee_name, String gender, String phone_num, String position) {
		this.employee_id = employee_id;
		this.employee_name = employee_name;
		this.gender = gender;
		this.phone_num = phone_num;
		this.position = position;
	}

	public String getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone_num() {
		return phone_num;
	}

	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
}
