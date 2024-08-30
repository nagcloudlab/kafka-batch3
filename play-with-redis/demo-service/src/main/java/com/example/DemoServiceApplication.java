package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@SpringBootApplication
@RestController
public class DemoServiceApplication {

	//String requestCount="0";

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new StringRedisSerializer());
		return template;
	}

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	

	@GetMapping("/hello/{name}")
	public String getMethodName(@PathVariable("name") String name) {
		//requestCount = Integer.toString(Integer.parseInt(requestCount)+1);
		 Long requestCount = redisTemplate.opsForValue().increment("request-count", 1);
		return "Hello "+name+"! This is request number "+requestCount;
	}
	

	public static void main(String[] args) {
		SpringApplication.run(DemoServiceApplication.class, args);
	}

}
