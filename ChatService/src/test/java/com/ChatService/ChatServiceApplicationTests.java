package com.ChatService;

import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class ChatServiceApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	void testRedis() {
		redisTemplate.opsForValue().set("chatMessage", "hello");

		Object o = redisTemplate.opsForValue().get("chatMessage");

		int i = 0;
	}

}
