package com.keita.painganautoinsurance.entity;
/*
 * 案件列表对应的每一个item
 */
public class CaseListItem  {
	private String id = null;
	private String location = null;
	private String insurancePhotoId = null;
	private String textId = null;
	private String Date = null;
	private String driverName = null;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTextId() {
		return textId;
	}
	public void setTextId(String textId) {
		this.textId = textId;
	}
	private String recordPath = null;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	public String getInsurancePhotoId() {
		return insurancePhotoId;
	}
	public void setInsurancePhotoId(String insurancePhotoId) {
		this.insurancePhotoId = insurancePhotoId;
	}
	
	public String getRecordPath() {
		return recordPath;
	}
	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	
}
