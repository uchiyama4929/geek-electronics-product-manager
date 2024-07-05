package com.example.demo;

import com.example.demo.filter.AuthenticationFilter;
import com.example.demo.service.ManagerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProductManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductManagerApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthenticationFilter> authenticationFilter(ManagerService managerService) {
		FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new AuthenticationFilter(managerService));
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}

}
