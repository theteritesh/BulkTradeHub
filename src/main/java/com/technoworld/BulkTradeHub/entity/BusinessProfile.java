package com.technoworld.BulkTradeHub.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;

@Entity
public class BusinessProfile extends Profile {

    private String businessName;
    private String businessType;
    private String gstNumber;
    private String bankAccountHolderName;
    private String bankAccountNumber;
    private String bankName;
    private String ifscCode;
    private String upiId;
    private String paymentMethods;
    
    @Transient  // Not stored in DB, used for form binding
    private List<String> paymentMethodsList = new ArrayList<>();
    
    @Lob
    @Column(columnDefinition = "LONGBLOB") 
    private byte[] profileImg;
    
    public BusinessProfile() {
    }

    public BusinessProfile(String shopName, String shopType, String gstNumber, 
                             String bankAccountHolderName, String bankAccountNumber, 
                             String bankName, String ifscCode, String upiId) {
        this.businessName = shopName;
        this.businessType = shopType;
        this.gstNumber = gstNumber;
        this.bankAccountHolderName = bankAccountHolderName;
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
        this.ifscCode = ifscCode;
        this.upiId = upiId;
    }

    

    public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getBankAccountHolderName() {
        return bankAccountHolderName;
    }

    public void setBankAccountHolderName(String bankAccountHolderName) {
        this.bankAccountHolderName = bankAccountHolderName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
    
    
	public String getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(String paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
	
	public List<String> getPaymentMethodsList() {
        return paymentMethodsList;
    }

    public void setPaymentMethodsList(List<String> paymentMethodsList) {
        this.paymentMethodsList = paymentMethodsList;
        this.paymentMethods = String.join(",", paymentMethodsList);
    }
    
    
	public byte[] getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(byte[] profileImg) {
		this.profileImg = profileImg;
	}

	@Override
	public String toString() {
		return "RetailShopProfile [businessName=" + businessName + ", businessType=" + businessType + ", gstNumber=" + gstNumber
				+ ", bankAccountHolderName=" + bankAccountHolderName + ", bankAccountNumber=" + bankAccountNumber
				+ ", bankName=" + bankName + ", ifscCode=" + ifscCode + ", upiId=" + upiId + ", paymentMethods="
				+ paymentMethods + "]";
	}
    
}
