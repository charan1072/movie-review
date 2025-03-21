package com.example.movies.test_controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class TestMoviesApplication {

	TestMoviesApplication(){
		System.out.println("tetsing constructor");
	}
	@BeforeAll
	static void setup() {
        System.out.println("testing setup");
    }
@Test
	void testAddition() {
		int result = 2 + 3;
		System.out.println("tetsing classes "+result);

	}


}
