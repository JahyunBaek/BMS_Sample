package com.szs.jobis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.szs.jobis.Dto.UserDTO;
import com.szs.jobis.Entity.ScrapEntity;
import org.assertj.core.util.VisibleForTesting;
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

	private String AccessToken;
	private String RefreshToken;

	@Order(1)
	@Test
	void SignUpTest() throws Exception {
		UserDTO userDTO = UserDTO.builder()
				.userId("test1").password("1q2w3e4e")
				.name("홍길동")
				.regNo("860824-1655068").build();

		String content = new Gson().toJson(userDTO);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/szs/signup")
				.content(content)
				.characterEncoding(StandardCharsets.UTF_8)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();

	}


	@Order(2)
	@Test
	void LoginTest() throws Exception {
		//SignUpTest();
		UserDTO userDTO = UserDTO.builder()
				.userId("test1").password("1q2w3e4e")
				.name("홍길동")
				.regNo("860824-1655068").build();

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
	void test() throws Exception{
		/*String json = "{\"status\":\"success\",\"data\":{\"jsonList\":{\"급여\":[{\"소득내역\":\"급여\",\"총지급액\":\"30,000,000\",\"업무시작일\":\"2020.10.02\",\"기업명\":\"(주)활빈당\",\"이름\":\"홍길동\",\"지급일\":\"2020.11.02\",\"업무종료일\":\"2021.11.02\",\"주민등록번호\":\"860824-1655068\",\"소득구분\":\"근로소득(연간)\",\"사업자등록번호\":\"012-34-56789\"}],\"산출세액\":\"600,000\",\"errMsg\":\"\",\"company\":\"삼쩜삼\",\"svcCd\":\"test01\",\"소득공제\":[{\"금액\":\"100,000\",\"소득구분\":\"보험료\"},{\"금액\":\"200,000\",\"소득구분\":\"교육비\"},{\"금액\":\"150,000\",\"소득구분\":\"기부금\"},{\"금액\":\"700,000\",\"소득구분\":\"의료비\"},{\"총납임금액\":\"1,333,333.333\",\"소득구분\":\"퇴직연금\"}]},\"appVer\":\"2021112501\",\"hostNm\":\"jobis-codetest\",\"workerResDt\":\"2022-08-16T06:27:35.160789\",\"workerReqDt\":\"2022-08-16T06:27:35.160851\"},\"errors\":{}}";
		ObjectMapper objectMapper = new ObjectMapper();
		ScrapEntity scrapEntity = objectMapper.readValue(json, new TypeReference<ScrapEntity>() {
		});
		//ScrapEntity scrapEntity = objectMapper.readValue(json,ScrapEntity.class);
		System.out.println("test");*/
	}
}
