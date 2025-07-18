package com.technoworld.BulkTradeHub.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    

	    private String name;
	    private String category;
	    private String brand;
	    
	    @Column(length = 5000) 
	    private String description;
	    
	    private String unitType; 
	    private Double unitValue;
	    
	    private Double price;
	    private Double cost;
	    private int totalQuantity;
	    private String gTin;
	    
	    @JsonIgnore
	    @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;
	    
	    @Lob
	    @Column(columnDefinition = "LONGBLOB") 
	    private byte[] mainImage;
	    
	    @JsonIgnore
	    @Lob
	    @Column(columnDefinition = "LONGBLOB") 
	    private byte[] firstImage;
	    
	    @JsonIgnore
	    @Lob
	    @Column(columnDefinition = "LONGBLOB") 
	    private byte[] secondImage;
	    
	    @JsonIgnore
	    @Lob
	    @Column(columnDefinition = "LONGBLOB") 
	    private byte[] thirdImage;
	    
	    @JsonIgnore
	    @Lob
	    @Column(columnDefinition = "LONGBLOB") 
	    private byte[] fourthImage;
	    
	    @JsonIgnore
	    @Lob
	    @Column(columnDefinition = "LONGBLOB") 
	    private byte[] fifthImage;
	    
	    private String warranty;
	    private LocalDate expiryDate;
	    private LocalDateTime createdAt;
	    private LocalDateTime modifiedAt;
	    
	    
	    

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public String getUnitType() {
			return unitType;
		}

		public void setUnitType(String unitType) {
			this.unitType = unitType;
		}

		public Double getUnitValue() {
			return unitValue;
		}

		public void setUnitValue(Double unitValue) {
			this.unitValue = unitValue;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		public Double getCost() {
			return cost;
		}

		public void setCost(Double cost) {
			this.cost = cost;
		}

		public int getTotalQuantity() {
			return totalQuantity;
		}

		public void setTotalQuantity(int totalQuantity) {
			this.totalQuantity = totalQuantity;
		}

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public byte[] getMainImage() {
			return mainImage;
		}

		public void setMainImage(byte[] mainImage) {
			this.mainImage = mainImage;
		}

		public byte[] getFirstImage() {
			return firstImage;
		}

		public void setFirstImage(byte[] firstImage) {
			this.firstImage = firstImage;
		}

		public byte[] getSecondImage() {
			return secondImage;
		}

		public void setSecondImage(byte[] secondImage) {
			this.secondImage = secondImage;
		}

		public byte[] getThirdImage() {
			return thirdImage;
		}

		public void setThirdImage(byte[] thirdImage) {
			this.thirdImage = thirdImage;
		}

		public byte[] getFourthImage() {
			return fourthImage;
		}

		public void setFourthImage(byte[] fourthImage) {
			this.fourthImage = fourthImage;
		}

		public byte[] getFifthImage() {
			return fifthImage;
		}

		public void setFifthImage(byte[] fifthImage) {
			this.fifthImage = fifthImage;
		}

		public String getgTin() {
			return gTin;
		}

		public void setgTin(String gTin) {
			this.gTin = gTin;
		}
		
		public String getWarranty() {
			return warranty;
		}

		public void setWarranty(String warranty) {
			this.warranty = warranty;
		}

		public LocalDate getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(LocalDate expiryDate) {
			this.expiryDate = expiryDate;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public LocalDateTime getModifiedAt() {
			return modifiedAt;
		}

		public void setModifiedAt(LocalDateTime modifiedAt) {
			this.modifiedAt = modifiedAt;
		}

		 @Transient
		    public String getBase64MainImage() {
		        return mainImage != null ? Base64.getEncoder().encodeToString(mainImage) : "";
		    }
		@Override
		public String toString() {
			return "Product [id=" + id + ", name=" + name + ", category=" + category + ", brand=" + brand
					+ ", description=" + description + ", unitType=" + unitType + ", unitValue=" + unitValue
					+ ", price=" + price + ", cost=" + cost + ", totalQuantity=" + totalQuantity + ", gTin=" + gTin
					+ ", user=" + user + ", mainImage=" + Arrays.toString(mainImage) + ", firstImage="
					+ Arrays.toString(firstImage) + ", secondImage=" + Arrays.toString(secondImage) + ", thirdImage="
					+ Arrays.toString(thirdImage) + ", fourthImage=" + Arrays.toString(fourthImage) + ", fifthImage="
					+ Arrays.toString(fifthImage) + ", warranty=" + warranty + ", expiryDate=" + expiryDate
					+ ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + "]";
		}
}
