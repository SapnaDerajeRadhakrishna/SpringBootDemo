package org.maxwell.springboot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.maxwell.springboot.redis.RedisConfig;
import org.maxwell.springboot.server.MySpringBoot;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MySpringBoot.class)
@WebAppConfiguration
public class SampleControllerUnitTest {

	public static final String UPDATE_URL = "/updateDetails";

	public static final String GET_URL = "/getDetails";

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	RedisConfig redisConfig;

	@Autowired
	ObjectMapper objectMapper;

	StudentDetails studDetails;

	@Configuration
	static class Config {
		@Bean
		public JedisConnectionFactory connectionFactory() {
			JedisConnectionFactory factory = Mockito.mock(JedisConnectionFactory.class);
			RedisConnection connection = Mockito.mock(RedisConnection.class);
			Mockito.when(factory.getConnection()).thenReturn(connection);
			return factory;
		}
	}

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		studDetails = new StudentDetails();
		studDetails.setStudentID(1);
		studDetails.setStudentName("maxwell");
	}

	@Test
	public void testUpdateStudents() throws Exception {
		this.mockMvc.perform(post(UPDATE_URL).contentType(MediaType.APPLICATION_JSON)
		        .content(objectMapper.writeValueAsString((studDetails)))).andExpect(status().isOk());
	}

	@Test
	public void testGetDetails() throws Exception {
		this.mockMvc.perform(get(GET_URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}
