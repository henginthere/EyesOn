package com.backend.eyeson;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EyesOnApplicationTests {

	@BeforeAll
	public void setup() {

	}

	@Test
	void contextLoads() {
	}

	@Test
	public void practice() {
		String message = "hello";
		assertEquals("hello", message);
		System.out.println("dd");
	}

	@AfterAll
	public void tearDown() {

	}


}
