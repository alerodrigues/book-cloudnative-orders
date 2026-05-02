package com.example.book_cloudnative_orders;

import org.springframework.boot.SpringApplication;

import com.example.cloudnative.CloudNativeApplication;

public class TestBookCloudnativeOrdersApplication {

	public static void main(String[] args) {
		SpringApplication.from(CloudNativeApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
