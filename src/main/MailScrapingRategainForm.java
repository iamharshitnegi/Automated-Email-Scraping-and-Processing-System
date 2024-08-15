package com.resavenue.mars.form;

import org.springframework.stereotype.Component;

@Component
public class MailScrapingRategainForm {

	//storing scraped mail detailed and insert to db
	private String firstname;
	private String lastname;
	private String bookingDate;
	private String currency;
	private String email;
	private String origin;
	private String degderefno;
	private String service;
	private String phone;
	private String ratePlan;
	private String cancellationpolicy;
	private String mod_amt;
	private String propName;
	private String bookingRefNo;
	
	
	// storing payment details to send mail from db 
	private String merRefenceNo;
    private String merAmount;
    private String merCurrId;
    private String merCustFirstName;
    private String merCustMiddleName;
    private String merCustLastName;
    private String invTaxAmt;
    private String totalTaxAmt;
    private String invType;
    private String roomPrice;
    private String totalRoomPrice;
	
    private String serviceCharge;
    private String municipalityFee;
    private String vat;
    private String serviceChargePer;
    private String municipalityFeePer;
    private String vatPer;
    private String totaltoPay;
    
    private String mod_checksum;
    
    private String checkin;
    private String checkout;
    
	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public String getCurrency() {
		return currency;
	}

	public String getEmail() {
		return email;
	}

	public String getOrigin() {
		return origin;
	}

	public String getDegderefno() {
		return degderefno;
	}

	public String getService() {
		return service;
	}

	public String getPhone() {
		return phone;
	}

	public String getRatePlan() {
		return ratePlan;
	}

	public String getCancellationpolicy() {
		return cancellationpolicy;
	}

	public String getMod_amt() {
		return mod_amt;
	}

	public String getCheckin() {
		return checkin;
	}

	public String getPropName() {
		return propName;
	}

	public String getBookingRefNo() {
		return bookingRefNo;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public void setDegderefno(String degderefno) {
		this.degderefno = degderefno;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setRatePlan(String ratePlan) {
		this.ratePlan = ratePlan;
	}

	public void setCancellationpolicy(String cancellationpolicy) {
		this.cancellationpolicy = cancellationpolicy;
	}

	public void setMod_amt(String mod_amt) {
		this.mod_amt = mod_amt;
	}

	public void setCheckin(String checkin) {
		this.checkin = checkin;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public void setBookingRefNo(String bookingRefNo) {
		this.bookingRefNo = bookingRefNo;
	}


	public String getMerAmount() {
		return merAmount;
	}

	public String getMerCurrId() {
		return merCurrId;
	}

	public String getMerCustFirstName() {
		return merCustFirstName;
	}

	public String getMerCustMiddleName() {
		return merCustMiddleName;
	}

	public String getMerCustLastName() {
		return merCustLastName;
	}

	public String getInvTaxAmt() {
		return invTaxAmt;
	}

	public String getTotalTaxAmt() {
		return totalTaxAmt;
	}

	public String getInvType() {
		return invType;
	}

	public String getRoomPrice() {
		return roomPrice;
	}

	public String getTotalRoomPrice() {
		return totalRoomPrice;
	}


	public void setMerAmount(String merAmount) {
		this.merAmount = merAmount;
	}

	public void setMerCurrId(String merCurrId) {
		this.merCurrId = merCurrId;
	}

	public void setMerCustFirstName(String merCustFirstName) {
		this.merCustFirstName = merCustFirstName;
	}

	public void setMerCustMiddleName(String merCustMiddleName) {
		this.merCustMiddleName = merCustMiddleName;
	}

	public void setMerCustLastName(String merCustLastName) {
		this.merCustLastName = merCustLastName;
	}

	public void setInvTaxAmt(String invTaxAmt) {
		this.invTaxAmt = invTaxAmt;
	}

	public void setTotalTaxAmt(String totalTaxAmt) {
		this.totalTaxAmt = totalTaxAmt;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public void setRoomPrice(String roomPrice) {
		this.roomPrice = roomPrice;
	}

	public void setTotalRoomPrice(String totalRoomPrice) {
		this.totalRoomPrice = totalRoomPrice;
	}

	public String getMerRefenceNo() {
		return merRefenceNo;
	}

	public void setMerRefenceNo(String merRefenceNo) {
		this.merRefenceNo = merRefenceNo;
	}

	public String getServiceCharge() {
		return serviceCharge;
	}

	public String getMunicipalityFee() {
		return municipalityFee;
	}

	public String getVat() {
		return vat;
	}

	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	public void setMunicipalityFee(String municipalityFee) {
		this.municipalityFee = municipalityFee;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public String getTotaltoPay() {
		return totaltoPay;
	}

	public void setTotaltoPay(String totaltoPay) {
		this.totaltoPay = totaltoPay;
	}

	public String getServiceChargePer() {
		return serviceChargePer;
	}

	public String getMunicipalityFeePer() {
		return municipalityFeePer;
	}

	public String getVatPer() {
		return vatPer;
	}

	public void setServiceChargePer(String serviceChargePer) {
		this.serviceChargePer = serviceChargePer;
	}

	public void setMunicipalityFeePer(String municipalityFeePer) {
		this.municipalityFeePer = municipalityFeePer;
	}

	public void setVatPer(String vatPer) {
		this.vatPer = vatPer;
	}

	public String getMod_checksum() {
		return mod_checksum;
	}

	public void setMod_checksum(String mod_checksum) {
		this.mod_checksum = mod_checksum;
	}

	public String getCheckout() {
		return checkout;
	}

	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}
	
	
	
	
	
	
	
}
