package com.villasboas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.villasboas" })
public class CustomerServletApplicationTest extends SpringBootServletInitializer {
	private static ConfigurableApplicationContext context;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CustomerApiApplication.class);
	}

	public static void main(String[] args) {
		context = SpringApplication.run(CustomerApiApplication.class, args);
	}

	public static ApplicationContext getContext() {
		return context;
	}

}
