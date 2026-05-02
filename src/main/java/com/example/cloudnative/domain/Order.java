package com.example.cloudnative.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Order")
public record Order (

	@Id
	Long id,
	String bookIsbn,
	String bookName,
	Double bookPrice,
	Integer quantity,
	OrderStatus status

){

	public static Order of(String bookIsbn, String bookName, Double bookPrice, Integer quantity, OrderStatus status) {
		return new Order(null, bookIsbn, bookName, bookPrice, quantity, status);
	}

}