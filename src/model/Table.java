package model;

public class Table {
	private String tableID;
	private String floorStay;
	private String operatingStatus;
	private String responsibleBy;
	private String clientNum;
	
	public Table(String tableID, String floorStay, String operatingStatus, String responsibleBy, String clientNum) {
		this.tableID = tableID;
		this.floorStay = floorStay;
		this.operatingStatus = operatingStatus;
		this.responsibleBy = responsibleBy;
		this.clientNum = clientNum;
	}
	public String getTableID() {
		return tableID;
	}
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}
	public String getFloorStay() {
		return floorStay;
	}
	public void setFloorStay(String floorStay) {
		this.floorStay = floorStay;
	}
	public String getOperatingStatus() {
		return operatingStatus;
	}
	public void setOperatingStatus(String operatingStatus) {
		this.operatingStatus = operatingStatus;
	}
	public String getResponsibleBy() {
		return responsibleBy;
	}
	public void setResponsibleBy(String responsibleBy) {
		this.responsibleBy = responsibleBy;
	}
	public String getClientNum() {
		return clientNum;
	}
	public void setClientNum(String clientNum) {
		this.clientNum = clientNum;
	}
	
}
