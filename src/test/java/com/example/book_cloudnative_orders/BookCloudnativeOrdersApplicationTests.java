package com.example.book_cloudnative_orders;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BookCloudnativeOrdersApplicationTests {

	@Test
	void contextLoads() {
	}

}
