package com.mycom.myapp.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	OpenAPI openApi() {
		return new OpenAPI()
				.components(new Components())
				.info(apiInfo());
	}
	
	private Info apiInfo() {
		return new Info()
				.title("문제은행 시스템")				
				.description("문제은행 시스템 기능 테스트.")
				.version("0.0.1")
				;
	}
	
}
