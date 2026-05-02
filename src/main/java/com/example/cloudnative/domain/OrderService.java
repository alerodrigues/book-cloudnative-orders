package com.example.cloudnative.domain;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class OrderService {
	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public Flux<Order> getAllOrders() {
		return orderRepository.findAll();
	}
	
}
