package com.example.cloudnative.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.example.cloudnative.catalog.Book;
import com.example.cloudnative.catalog.BookClient;
import com.example.cloudnative.event.OrderAcceptedMessage;
import com.example.cloudnative.event.OrderLabeledMessage;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
	private static final Logger log = LoggerFactory.getLogger(OrderService.class);
	
	private final BookClient bookClient;
	private final OrderRepository orderRepository;
	private final StreamBridge streamBridge;

	public OrderService(BookClient bookClient, OrderRepository orderRepository, StreamBridge streamBridge) {
		this.bookClient = bookClient;
		this.orderRepository = orderRepository;
		this.streamBridge = streamBridge;
	}

	public Flux<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Mono<Order> submitOrder(String isbn, int quantity) {
		return bookClient.getBookByIsbn(isbn)
				.map(book -> buildAcceptedOrder(book, quantity))
				.defaultIfEmpty(buildRejectedOrder(isbn, quantity))
				.flatMap(orderRepository::save)
				.doOnNext(this::publishOrderAcceptedEvent);
	}

	private static Order buildAcceptedOrder(Book book, int quantity) {
		return Order.of(book.isbn(), book.title() + " - " + book.author(),
				book.price(), quantity, OrderStatus.ACCEPTED);
	}

	private static Order buildRejectedOrder(String isbn, int quantity) {
		return Order.of(isbn, "", 0.0, quantity, OrderStatus.REJECTED);
	}
	
	private void publishOrderAcceptedEvent(Order order) {
		if (!order.status().equals(OrderStatus.ACCEPTED)) {
			return;
		}
		
		var orderAcceptedMessage = new OrderAcceptedMessage(order.id());
		log.info("Sending order accepted event with id: {}", order.id());
		
		var result = streamBridge.send("accepted-out-0", orderAcceptedMessage);
		log.info("Result of sending data for order with id {}: {}", order.id(), result);
	}
	
	public Flux<Order> consumeOrderLabeledEvent(Flux<OrderLabeledMessage> flux) {
		return flux
				.flatMap(message -> orderRepository.findById(message.orderId()))
				.map(this::buildDispatchedOrder)
				.flatMap(orderRepository::save);
	}

	private Order buildDispatchedOrder(Order existingOrder) {
		return new Order(
				existingOrder.id(),
				existingOrder.bookIsbn(),
				existingOrder.bookName(),
				existingOrder.bookPrice(),
				existingOrder.quantity(),
				OrderStatus.DISPATCHED
		);
	}	

}