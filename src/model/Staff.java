package model;
import java.sql.Time;
import java.time.LocalTime;

public class Staff {
	private String staffID;
	private String userName;	// K cần sửa
	private String password;	// K cần sửa
	private String fullName;
	private String phone;
	private String position;
	private byte sex;
	private Time startShift;	// K cần sửa
	private Time endShift;		// K cần sửa	

	public Staff(String staffID, String userName, String password, String fullName, String phone, String position, byte sex,
			Time startShift, Time endShift) {
		this.staffID = staffID;
		this.userName = userName;
		this.password = password;
		this.fullName = fullName;
		this.phone = phone;
		this.position = position;
		this.sex = sex;
		this.startShift = startShift;
		this.endShift = endShift;
	}

	public void updateInfo(Staff tmp) {
		this.staffID = tmp.staffID;
		this.userName = tmp.userName;
		this.password = tmp.password;
		this.fullName = tmp.fullName;
		this.phone = tmp.phone;
		this.position = tmp.position;
		this.sex = tmp.sex;
		this.startShift = tmp.startShift;
		this.endShift = tmp.endShift;
	}


	public String getStaffID() {
		return staffID;
	}


	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getPosition() {
		return position;
	}

	
	public void setPosition(String position) {
		this.position = position;
	}
	
	
	public byte getSex() {
		return sex;
	}


	public void setSex(byte sex) {
		this.sex = sex;
	}


	public Time getStartShift() {
		return startShift;
	}


	public void setStartShift(Time startShift) {
		this.startShift = startShift;
	}


	public Time getEndShift() {
		return endShift;
	}


	public void setEndShift(Time endShift) {
		this.endShift = endShift;
	}
	
	
}
