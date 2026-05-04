package com.example.cloudnative.catalog;

public record Book(
	String isbn,
	String title,
	String author,
	Double price
){}