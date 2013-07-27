package com.keita.painganautoinsurance.entity;

public class InsurancePolicy {
	private String location = null;
	private TextImage[] textImage = null;
	private String caseNo = null;
	private String caseOwner = null;
	private String caseDriver = null;
	private String relationShip = null;
	private String caseOwnerPhone = null;
	private String caseDriverPhone = null;
	private String caseDriverLicence = null;
	private String caseCarNo = null;
	private String caseCarType = null;
	private String caseThirdCarNo = null;
	private String caseThirdCarType = null;
	private String recordPath = null;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public TextImage[] getTextImage() {
		return textImage;
	}
	public void setTextImage(TextImage[] textImage) {
		this.textImage = textImage;
	}
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	public String getCaseOwner() {
		return caseOwner;
	}
	public void setCaseOwner(String caseOwner) {
		this.caseOwner = caseOwner;
	}
	public String getCaseDriver() {
		return caseDriver;
	}
	public void setCaseDriver(String caseDriver) {
		this.caseDriver = caseDriver;
	}
	public String getRelationShip() {
		return relationShip;
	}
	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}
	public String getCaseOwnerPhone() {
		return caseOwnerPhone;
	}
	public void setCaseOwnerPhone(String caseOwnerPhone) {
		this.caseOwnerPhone = caseOwnerPhone;
	}
	public String getCaseDriverPhone() {
		return caseDriverPhone;
	}
	public void setCaseDriverPhone(String caseDriverPhone) {
		this.caseDriverPhone = caseDriverPhone;
	}
	public String getCaseDriverLicence() {
		return caseDriverLicence;
	}
	public void setCaseDriverLicence(String caseDriverLicence) {
		this.caseDriverLicence = caseDriverLicence;
	}
	public String getCaseCarNo() {
		return caseCarNo;
	}
	public void setCaseCarNo(String caseCarNo) {
		this.caseCarNo = caseCarNo;
	}
	public String getCaseCarType() {
		return caseCarType;
	}
	public void setCaseCarType(String caseCarType) {
		this.caseCarType = caseCarType;
	}
	public String getCaseThirdCarNo() {
		return caseThirdCarNo;
	}
	public void setCaseThirdCarNo(String caseThirdCarNo) {
		this.caseThirdCarNo = caseThirdCarNo;
	}
	public String getCaseThirdCarType() {
		return caseThirdCarType;
	}
	public void setCaseThirdCarType(String caseThirdCarType) {
		this.caseThirdCarType = caseThirdCarType;
	}
}
