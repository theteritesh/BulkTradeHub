package com.technoworld.BulkTradeHub.retailshop.entity;

import java.util.Arrays;

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
	    
	    @Column(length = 1000) 
	    private String description;
	    
	    private String unitType; 
	    private Double unitValue;
	    
	    private Double price;
	    private Double cost;
	    private int totalQuantity;
	    
	    @Lob
	    @Column(columnDefinition = "LONGBLOB") 
	    private byte[] image;

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

		public byte[] getImage() {
			return image;
		}

		public void setImage(byte[] image) {
			this.image = image;
		}

		@Override
		public String toString() {
			return "Product [id=" + id + ", name=" + name + ", category=" + category + ", brand=" + brand
					+ ", description=" + description + ", unitType=" + unitType + ", unitValue=" + unitValue
					+ ", price=" + price + ", cost=" + cost + ", totalQuantity=" + totalQuantity + ", image="
					+ Arrays.toString(image) + "]";
		} 
	    
	    

}
