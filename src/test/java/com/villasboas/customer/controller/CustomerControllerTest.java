package com.villasboas.customer.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.villasboas.CustomerServletApplicationTest;
import com.villasboas.clock.Clock;
import com.villasboas.common.exception.controlleradvice.ExceptionsHandler;
import com.villasboas.customer.controller.usecase.CustomerDto;

@SpringBootTest(classes = { CustomerServletApplicationTest.class })
@Transactional
class CustomerControllerTest {

	@Autowired
	private CustomerController customerController;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ExceptionsHandler exceptionsHandler;

	@MockBean
	private Clock clock;

	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		when(clock.getUtcLocalDate()).thenReturn(LocalDate.of(2020, 9, 20));

		mockMvc = MockMvcBuilders.standaloneSetup(customerController).setControllerAdvice(exceptionsHandler)
				.setCustomArgumentResolvers(
						new PageableHandlerMethodArgumentResolver(new SortHandlerMethodArgumentResolver()))
				.build();
	}

	@Test
	@Sql("/test/scripts/insert-data-to-customer-get-and-pagination-test.sql")
	void testWhenGetThenReturnCustomersOrderedAnPaginated() throws Exception {

		final String url = "/api/customers?page=0&size=2&sort=name,asc";
		final ResultActions resultActions = mockMvc.perform(configureHeaders(get(url)));

		resultActions
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()", is(2)))
				.andExpect(jsonPath("$.content[0].name", is("Alice")))
				.andExpect(jsonPath("$.content[0].id", is("2416bff9-a0ce-429c-8d74-aa0323c0f1b1")))
				.andExpect(jsonPath("$.content[0].cpf", is("03764697067")))
				.andExpect(jsonPath("$.content[0].yearsOld", is(0)))
				.andExpect(jsonPath("$.content[0].birthDate", is("2020-09-20")))
				.andExpect(jsonPath("$.content[1].name", is("Elijah")))
				.andExpect(jsonPath("$.content[1].id", is("708a8323-1dfb-42b9-b92e-a28728b0482f")))
				.andExpect(jsonPath("$.content[1].cpf", is("26545587030")))
				.andExpect(jsonPath("$.content[1].yearsOld", is(2)))
				.andExpect(jsonPath("$.content[1].birthDate", is("2018-01-29")));

	}

	@Test
	@Sql("/test/scripts/insert-data-to-customer-to-filter-by-name.sql")
	void testWheGetThenRetrieveCustomersFilteredByName() throws Exception {

		final String url = "/api/customers?filter=name=El&page=0&size=2&sort=name,asc";
		final ResultActions resultActions = mockMvc.perform(configureHeaders(get(url)));

		resultActions.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content.length()", is(1)))
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
		final ResultActions resultActions = mockMvc.perform(configureHeaders(get(url)));

		resultActions
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content.length()", is(1)))
				.andExpect(jsonPath("$.content[0].name", is("Elijah")))
				.andExpect(jsonPath("$.content[0].id", is("708a8323-1dfb-42b9-b92e-a28728b0482f")))
				.andExpect(jsonPath("$.content[0].cpf", is("26545587030")))
				.andExpect(jsonPath("$.content[0].yearsOld", is(2)))
				.andExpect(jsonPath("$.content[0].birthDate", is("2018-01-29")));
	}

	@Test
	void testWhenPostWithAnValidPayloadThenPersistCustomer() throws Exception {

		final UUID randomUUID = UUID.randomUUID();
		final String customerDtoAsString = createCustomerDto(randomUUID);
		final String url = "/api/customers";

		final ResultActions resultActions = mockMvc.perform(configureHeaders(post(url)).content(customerDtoAsString));

		final String resource = String.format("/api/customers/%s", randomUUID.toString());

		resultActions
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$", is(resource)));

	}

	@Test
	@Sql({ "/test/scripts/insert-data-to-find-by-id-and-delete.sql" })
	void testWhenGetByIdThenReturnCorrectMappedCustomerAndAfterDeleteHimAndFindWith404Status() throws Exception {

		final UUID id = UUID.fromString("50e3b8ac-3ca7-4871-b201-886d60f281a9");
		final String url = String.format("/api/customers/%s", id);

		mockMvc.perform(configureHeaders(get(url)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("50e3b8ac-3ca7-4871-b201-886d60f281a9")))
				.andExpect(jsonPath("$.name", is("Solange")))
				.andExpect(jsonPath("$.cpf", is("17405802060")))
				.andExpect(jsonPath("$.birthDate", is("2016-03-21")))
				.andExpect(jsonPath("$.yearsOld", is(4)));

		mockMvc.perform(configureHeaders(delete(url)))
				.andDo(print())
				.andExpect(status().isNoContent());

		mockMvc.perform(configureHeaders(get(url)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorMessage", is("Entidade não encontrada.")));
	}

	@Test
	@Sql({"/test/scripts/insert-data-to-put.sql"})
	void testWhenPutThenUpdateAllFields() throws Exception {
		
		CustomerDto customerDto = new CustomerDto();
		
		final UUID id = UUID.fromString("23f2a8f3-c32e-48d7-8815-bb8e4b334c09");
		customerDto.setId(id);
		customerDto.setName("Alice");
		customerDto.setCpf("06539014023");
		customerDto.setBirthDate(LocalDate.of(2019, 12, 12));
		final String content = objectMapper.writeValueAsString(customerDto);
		
		final String putUrl = "/api/customers/";
		mockMvc.perform(configureHeaders(put(putUrl))
				.content(content))
				.andDo(print())
				.andExpect(status().isNoContent());
		
		final String getUrl = String.format("/api/customers/%s", id);
		mockMvc.perform(configureHeaders(get(getUrl)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id.toString())))
				.andExpect(jsonPath("$.name", is("Alice")))
				.andExpect(jsonPath("$.cpf", is("06539014023")))
				.andExpect(jsonPath("$.birthDate", is("2019-12-12")));
	
	}
	
	@Test
	@Sql({"/test/scripts/insert-data-to-patch.sql"}) 
	void testWhenPatchThenUpdateOnlyInformedProperties() throws Exception {
		
		final UUID id = UUID.fromString("4d30b206-87c5-4abe-ac86-22e83e54a48d");
		final String url = String.format("/api/customers/%s", id);
		
		final JSONArray arrayPatch = new JSONArray();
		final JSONObject patchName = new JSONObject();
		patchName.put("op", "replace");
		patchName.put("path", "/name");
		patchName.put("value", "Alex");
		arrayPatch.put(patchName);
		
		final JSONObject patchBirthDate = new JSONObject();
		patchBirthDate.put("op", "replace");
		patchBirthDate.put("path", "/birthDate");
		patchBirthDate.put("value", "1986-10-16");
		arrayPatch.put(patchBirthDate);
		
		final String content = arrayPatch.toString();
		mockMvc.perform(configureHeaders(patch(url))
				.content(content))
				.andExpect(jsonPath("$.id", is(id.toString())))
				.andExpect(jsonPath("$.name", is("Alex")))
				.andExpect(jsonPath("$.cpf", is("41575160013")))
				.andExpect(jsonPath("$.birthDate", is("1986-10-16")))
				.andExpect(jsonPath("$.yearsOld", is(33)));
		
		mockMvc.perform(configureHeaders(get(url)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(id.toString())))
				.andExpect(jsonPath("$.name", is("Alex")))
				.andExpect(jsonPath("$.cpf", is("41575160013")))
				.andExpect(jsonPath("$.birthDate", is("1986-10-16")))
				.andExpect(jsonPath("$.yearsOld", is(33)));
		
	}
	
	private MockHttpServletRequestBuilder configureHeaders(
			final MockHttpServletRequestBuilder mockHttpServletRequestBuilder) {
		return mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
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
