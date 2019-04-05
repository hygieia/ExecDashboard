package com.capitalone.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application boot started class
 * 
 * @param ...
 * @return
 */
@SpringBootApplication
@ComponentScan("com.capitalone.dashboard")
public class Application {

	/**
	 * main Method
	 * 
	 * @param args...
	 * @return
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
