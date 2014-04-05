package com.keita.painganautoinsurance.entity;

/*
 * 情景模版
 */
public class Template {
	private String templateName;
	private String caseNo;
	private String insuranceId;
	private String peopleName;
	private String phone;
	private String hurtNum;
	private String deadNum;
	private String CarNo;
	private String Licence;
	private String vin;
	private String CarType;
	private String CarReason;
	private String CaseLoss;
	private String AccidentReason;
	private String AccidentDetail;
	private String Date;
	
	
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}

	public String getInsuranceId() {
		return insuranceId;
	}
	public void setInsuranceId(String insuranceId) {
		this.insuranceId = insuranceId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getPeopleName() {
		return peopleName;
	}
	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}
	public String getCaseLoss() {
		return CaseLoss;
	}
	public void setCaseLoss(String caseLoss) {
		CaseLoss = caseLoss;
	}


	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCarNo() {
		return CarNo;
	}
	public void setCarNo(String carNo) {
		CarNo = carNo;
	}
	public String getLicence() {
		return Licence;
	}
	public void setLicence(String licence) {
		Licence = licence;
	}
	public String getCarType() {
		return CarType;
	}
	public void setCarType(String carType) {
		CarType = carType;
	}
	public String getCarReason() {
		return CarReason;
	}
	public void setCarReason(String carReason) {
		CarReason = carReason;
	}
	public String getAccidentReason() {
		return AccidentReason;
	}
	public void setAccidentReason(String accidentReason) {
		AccidentReason = accidentReason;
	}
	public String getAccidentDetail() {
		return AccidentDetail;
	}
	public void setAccidentDetail(String accidentDetail) {
		AccidentDetail = accidentDetail;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getHurtNum() {
		return hurtNum;
	}
	public void setHurtNum(String hurtNum) {
		this.hurtNum = hurtNum;
	}
	public String getDeadNum() {
		return deadNum;
	}
	public void setDeadNum(String deadNum) {
		this.deadNum = deadNum;
	}
	
}
