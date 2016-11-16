package org.maxwell.springboot.redis;

import org.junit.Before;
import org.junit.Test;

import com.fiftyonred.mock_jedis.MockJedis;

import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;

public class MockJedisTest {
	private Jedis j = null;

	@Before
	public void setUp() {
		j = new MockJedis("test");
	}

	@Test
	public void testSet() {
		assertEquals("OK", j.set("test", "123"));
	}

	@Test
	public void testGet() {
		j.set("test", "123");
		assertEquals("123", j.get("test"));
		assertEquals(null, j.get("unknown"));
	}
}
