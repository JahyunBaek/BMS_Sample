package com.szs.jobis;

import com.google.gson.Gson;
import com.szs.jobis.Dto.UserDTO;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class JobisApplicationTests {

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PasswordEncoder passwordEncoder;


	private static String AccessToken;
	private static String RefreshToken;

	@Test
	@Order(1)
	void SignUpTest() throws Exception {
		UserDTO userDTO = UserDTO.builder()
				.userId("test1").password("1q2w3e4e")
				.name("김둘리")
				.regNo("921108-1582816").build();

		String content = new Gson().toJson(userDTO);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/szs/signup")
				.content(content)
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();

	}



	@Test
	@Order(2)
	void LoginTest() throws Exception {
		UserDTO userDTO = UserDTO.builder()
				.userId("test1").password("1q2w3e4e")
				.name("김둘리")
				.regNo("921108-1582816").build();

		String content = new Gson().toJson(userDTO);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/szs/login")
				.content(content)
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();

		List<String> authorization = mvcResult.getResponse().getHeaders("Authorization");

		AccessToken = authorization.get(0);
		RefreshToken = authorization.get(1);

	}

	@Test
	@Order(3)
	void MeTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/szs/me")
				.header("Authorization",AccessToken)
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
				
	}
	@Test
	@Order(4)
	void scrapTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/szs/scrap")
				.header("Authorization",AccessToken)
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();


	}

	@Test
	@Order(5)
	void refundTest() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/szs/refund")
				.header("Authorization",AccessToken)
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();


		System.out.println(mvcResult.getResponse().getContentAsString());
	}
}
