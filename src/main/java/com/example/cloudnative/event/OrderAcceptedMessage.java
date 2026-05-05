package com.example.cloudnative.event;

public record OrderAcceptedMessage (
		Long orderId
){}