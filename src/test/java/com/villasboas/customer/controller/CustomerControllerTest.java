package com.villasboas.customer.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.villasboas.CustomerServletApplicationTest;
import com.villasboas.clock.Clock;
import com.villasboas.customer.controller.usecase.CustomerDto;

@SpringBootTest(classes = { CustomerServletApplicationTest.class })
@Transactional
class CustomerControllerTest {

	@Autowired
	private CustomerController customerController;

	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private Clock clock;

	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		when(clock.getUtcLocalDate()).thenReturn(LocalDate.of(2020, 9, 20));

		mockMvc = MockMvcBuilders.standaloneSetup(customerController).setCustomArgumentResolvers(
				new PageableHandlerMethodArgumentResolver(new SortHandlerMethodArgumentResolver())).build();
	}

	@Test
	@Sql("/test/scripts/insert-data-to-customer-get-and-pagination-test.sql")
	void testWhenGetThenReturnCustomersOrderedAnPaginated() throws Exception {

		final String url = "/api/customers?page=0&size=2&sort=name,asc";
		final ResultActions resultActions = mockMvc
				.perform(get(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

		resultActions.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()", is(2)))
				.andExpect(jsonPath("$.content[0].id", is("2416bff9-a0ce-429c-8d74-aa0323c0f1b1")))
				.andExpect(jsonPath("$.content[0].name", is("Alice")))
				.andExpect(jsonPath("$.content[0].cpf", is("03764697067")))
				.andExpect(jsonPath("$.content[0].yearsOld", is(0)))
				.andExpect(jsonPath("$.content[0].birthDate", is("2020-09-20")))
				.andExpect(jsonPath("$.content[1].id", is("708a8323-1dfb-42b9-b92e-a28728b0482f")))
				.andExpect(jsonPath("$.content[1].name", is("Elijah")))
				.andExpect(jsonPath("$.content[1].cpf", is("26545587030")))
				.andExpect(jsonPath("$.content[1].yearsOld", is(2)))
				.andExpect(jsonPath("$.content[1].birthDate", is("2018-01-29")));

	}

	@Test
	@Sql("/test/scripts/insert-data-to-customer-to-filter-by-name.sql")
	void testWheGetThenRetrieveCustomersFilteredByName() throws Exception {

		final String url = "/api/customers?filter=name=El&page=0&size=2&sort=name,asc";
		final ResultActions resultActions = mockMvc
				.perform(get(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		
		resultActions.andDo(print()).andExpect(status().isOk())
		.andExpect(jsonPath("$.content.length()", is(1)))
		.andExpect(jsonPath("$.content[0].id", is("708a8323-1dfb-42b9-b92e-a28728b0482f")))
		.andExpect(jsonPath("$.content[0].name", is("Elijah")))
		.andExpect(jsonPath("$.content[0].cpf", is("26545587030")))
		.andExpect(jsonPath("$.content[0].yearsOld", is(2)))
		.andExpect(jsonPath("$.content[0].birthDate", is("2018-01-29")));
	}
		
	@Test
	@Sql("/test/scripts/insert-data-to-customer-to-filter-by-name.sql")
	void testWhGetThenFilterByCnpj() throws Exception {

		final String url = "/api/customers?filter=cpf=26545587030&page=0&size=2&sort=name,asc";
		final ResultActions resultActions = mockMvc
				.perform(get(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		
		resultActions.andDo(print()).andExpect(status().isOk())
		.andExpect(jsonPath("$.content.length()", is(1)))
		.andExpect(jsonPath("$.content[0].id", is("708a8323-1dfb-42b9-b92e-a28728b0482f")))
		.andExpect(jsonPath("$.content[0].name", is("Elijah")))
		.andExpect(jsonPath("$.content[0].cpf", is("26545587030")))
		.andExpect(jsonPath("$.content[0].yearsOld", is(2)))
		.andExpect(jsonPath("$.content[0].birthDate", is("2018-01-29")));
	}
	
	@Test
	void testWhenPostWithAnValidPayloadThenPersistCustomer() throws Exception {

		final UUID randomUUID = UUID.randomUUID();
		final String customerDtoAsString = createCustomerDto(randomUUID);
		
		final String url = "/api/customers";
		final ResultActions resultActions = mockMvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.content(customerDtoAsString));
		
		final String resource = String.format("/api/customers/%s", randomUUID.toString());
		
		resultActions.andDo(print())
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$", is(resource)));

	}

	private String createCustomerDto(final UUID randomUUID) throws JsonProcessingException {
		final CustomerDto customerDto = new CustomerDto();
		customerDto.setId(randomUUID);
		customerDto.setName("Amora");
		customerDto.setCpf("40630141002");
		customerDto.setBirthDate(LocalDate.of(2014, 12, 24));
		final String content = objectMapper.writeValueAsString(customerDto);
		return content;
	}
	
	
}
