package com.technoworld.BulkTradeHub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OrderItems {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	private int productPostId;
	private int lotsQuntity;
	private double lotPrice;
	private long buyerId;
	private long sellerId;
	private double subTotal;
	private int orderId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProductPostId() {
		return productPostId;
	}
	public void setProductPostId(int productPostId) {
		this.productPostId = productPostId;
	}
	public int getLotsQuntity() {
		return lotsQuntity;
	}
	public void setLotsQuntity(int lotsQuntity) {
		this.lotsQuntity = lotsQuntity;
	}
	public double getLotPrice() {
		return lotPrice;
	}
	public void setLotPrice(double lotPrice) {
		this.lotPrice = lotPrice;
	}
	public long getBuyerId() {
		return buyerId;
	}
	public void setBuyerId(long buyerId) {
		this.buyerId = buyerId;
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	@Override
	public String toString() {
		return "OrderItems [id=" + id + ", productPostId=" + productPostId + ", lotsQuntity=" + lotsQuntity
				+ ", lotPrice=" + lotPrice + ", buyerId=" + buyerId + ", sellerId=" + sellerId + ", subTotal="
				+ subTotal + ", orderId=" + orderId + "]";
	}
}
