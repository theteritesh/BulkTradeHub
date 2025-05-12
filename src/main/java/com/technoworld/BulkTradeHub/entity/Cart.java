package com.technoworld.BulkTradeHub.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_post_id")
    private ProductPost productPost;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    private int quantity;
    private LocalDateTime addedAt;
    private Boolean status;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ProductPost getProductPost() {
		return productPost;
	}
	public void setProductPost(ProductPost productPost) {
		this.productPost = productPost;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
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
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Cart [id=" + id + ", user=" + user + ", productPost=" + productPost + ", product=" + product
				+ ", quantity=" + quantity + ", addedAt=" + addedAt + ", status=" + status + "]";
	}
}
