package com.technoworld.BulkTradeHub.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private int userId;
    private int productPostId;
    private long productId;
    private int quantity;
    private LocalDateTime addedAt;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductPostId() {
		return productPostId;
	}
	public void setProductPostId(int productPostId) {
		this.productPostId = productPostId;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public LocalDateTime getAddedAt() {
		return addedAt;
	}
	public void setAddedAt(LocalDateTime addedAt) {
		this.addedAt = addedAt;
	}
	
	@Override
	public String toString() {
		return "Cart [id=" + id + ", userId=" + userId + ", productPostId=" + productPostId + ", productId=" + productId
				+ ", quantity=" + quantity + ", addedAt=" + addedAt + "]";
	}
}
