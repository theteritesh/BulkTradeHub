package com.technoworld.BulkTradeHub.retailshop.entity;

import java.util.ArrayList;
import java.util.List;

import com.technoworld.BulkTradeHub.entity.Profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;

@Entity
public class RetailShopProfile extends Profile {

    private String shopName;
    private String shopType;
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
    
    public RetailShopProfile() {
    }

    public RetailShopProfile(String shopName, String shopType, String gstNumber, 
                             String bankAccountHolderName, String bankAccountNumber, 
                             String bankName, String ifscCode, String upiId) {
        this.shopName = shopName;
        this.shopType = shopType;
        this.gstNumber = gstNumber;
        this.bankAccountHolderName = bankAccountHolderName;
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
        this.ifscCode = ifscCode;
        this.upiId = upiId;
    }

    // Getters and Setters
    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
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
		return "RetailShopProfile [shopName=" + shopName + ", shopType=" + shopType + ", gstNumber=" + gstNumber
				+ ", bankAccountHolderName=" + bankAccountHolderName + ", bankAccountNumber=" + bankAccountNumber
				+ ", bankName=" + bankName + ", ifscCode=" + ifscCode + ", upiId=" + upiId + ", paymentMethods="
				+ paymentMethods + "]";
	}
    
}
