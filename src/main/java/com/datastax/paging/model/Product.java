package com.datastax.paging.model;

import java.util.Set;

public class Product {

	private String productId;
	private int capacityLeft;
	private Set<String> orderIds;
	
	public Product(String productId, int capacityLeft, Set<String> orderIds) {
		this.productId = productId;
		this.capacityLeft = capacityLeft;
		this.orderIds = orderIds;
	}

	public String getProductId() {
		return productId;
	}

	public int getCapacityLeft() {
		return capacityLeft;
	}

	public Set<String> getOrderIds() {
		return orderIds;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", capacityLeft=" + capacityLeft + ", orderIds=" + orderIds + "]";
	}
}
