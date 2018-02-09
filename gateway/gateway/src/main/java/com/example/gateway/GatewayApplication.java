package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;

@SpringBootApplication
public class GatewayApplication {

	@Bean
	MapReactiveUserDetailsService authentication() {
		return new MapReactiveUserDetailsService(User.withDefaultPasswordEncoder()
				.roles("USER")
				.username("user")
				.password("password")
				.build());
	}

	@Bean
	SecurityWebFilterChain authorization(ServerHttpSecurity security) {
		//@formatter:off
		return security
				.httpBasic()
				.and()
				.authorizeExchange()
					.pathMatchers("/rl").authenticated()
					.anyExchange().permitAll()
				.and()
				.build();

		//@formatter:on
	}

	@Bean
	RouteLocator rl(RouteLocatorBuilder rlb) {
		return rlb
				.routes()
				.route(predicateSpec -> predicateSpec
						.path("/rl")
						.filters(fs ->
								fs
										.setPath("/hi")
										.requestRateLimiter(RedisRateLimiter.args(2, 4))
						)
						.uri("lb://rest-api")
				)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
}
