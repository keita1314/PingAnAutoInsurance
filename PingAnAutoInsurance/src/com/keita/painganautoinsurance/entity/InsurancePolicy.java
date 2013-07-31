package com.keita.painganautoinsurance.entity;

import java.util.ArrayList;

/*
 * 保单类的实现
 */
public class InsurancePolicy {
	private String id = null;
	private String location = null;
	private String insurancePhotoId = null;
	private String textId = null;
	private String Date = null;
	
	
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
	
}
