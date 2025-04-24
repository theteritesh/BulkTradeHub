package com.technoworld.BulkTradeHub.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ProductPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String productName;
	private String category;
	private String brand;

	@Column(length = 5000)
	private String description;
	private int minOrderQuantity;
	private String bulkPackageType;
	private double retailPrice;
	private double wholesalePrice;
	private int availableQuantity;
	private double bulkDiscount;
	private String deliveryTime;
	private double shippingCost;
	private String availabilityType;
	private String leadTime;
	private boolean codAvailable;
	private LocalDateTime postedAt;
	private int lots;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	private long productId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMinOrderQuantity() {
		return minOrderQuantity;
	}

	public void setMinOrderQuantity(int minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}

	public String getBulkPackageType() {
		return bulkPackageType;
	}

	public void setBulkPackageType(String bulkPackageType) {
		this.bulkPackageType = bulkPackageType;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public double getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(double wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	public double getBulkDiscount() {
		return bulkDiscount;
	}

	public void setBulkDiscount(double bulkDiscount) {
		this.bulkDiscount = bulkDiscount;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public double getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(double shippingCost) {
		this.shippingCost = shippingCost;
	}

	public String getAvailabilityType() {
		return availabilityType;
	}

	public void setAvailabilityType(String availabilityType) {
		this.availabilityType = availabilityType;
	}

	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}

	public boolean isCodAvailable() {
		return codAvailable;
	}

	public void setCodAvailable(boolean codAvailable) {
		this.codAvailable = codAvailable;
	}

	public LocalDateTime getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(LocalDateTime postedAt) {
		this.postedAt = postedAt;
	}

	public User getUser() {
		return user;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	public int getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	
	public int getLots() {
		return lots;
	}

	public void setLots(int lots) {
		this.lots = lots;
	}

	@Override
	public String toString() {
		return "ProductPost [id=" + id + ", productName=" + productName + ", category=" + category + ", brand=" + brand
				+ ", description=" + description + ", minOrderQuantity=" + minOrderQuantity + ", bulkPackageType="
				+ bulkPackageType + ", retailPrice=" + retailPrice + ", wholesalePrice=" + wholesalePrice
				+ ", availableQuantity=" + availableQuantity + ", bulkDiscount=" + bulkDiscount + ", deliveryTime="
				+ deliveryTime + ", shippingCost=" + shippingCost + ", availabilityType=" + availabilityType
				+ ", leadTime=" + leadTime + ", codAvailable=" + codAvailable + ", postedAt=" + postedAt + ", lots="
				+ lots + ", user=" + user + ", productId=" + productId + "]";
	}
	
}
