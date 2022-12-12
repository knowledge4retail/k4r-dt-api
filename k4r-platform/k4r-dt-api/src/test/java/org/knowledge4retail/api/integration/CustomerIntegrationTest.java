package org.knowledge4retail.api.integration;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.customer.dto.CustomerDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.knowledge4retail.api.test.Data.*;



public class CustomerIntegrationTest extends AbstractIntegrationTest {

    private final String BASE_URL = "/api/v0/customers";

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void saveCustomerReturns400WithInvalidCustomer() throws Exception {

        String customer = "{\"name\": \"\", }";

        mvc.perform(MockMvcRequestBuilders
                .post(BASE_URL, 2).contentType("application/json")
                .content(customer)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getCustomerReturns404IfCustomerNotFoundExceptionWasThrown() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + "/996984").accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(HttpStatus.NOT_FOUND.value(), status);

    }

    @Test
    public void controllerReturns405WhenCalledWithUnsupportedVerb() throws Exception {


        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(BASE_URL).accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), status);

    }


    @Test
    public void getAllCustomersReturnsCustomersInDbAnd200() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<CustomerDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), CustomerDto.class);

        assertNotNull(contentList);
        assertTrue(contentList.size() > 0);
        assertNotNull(contentList.get(0));
        assertNotNull(contentList.get(0).getId());
        assertNotNull(contentList.get(0).getAnonymisedName());
    }

    @Test
    public void getCustomerByIdReturnsCustomer() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(BASE_URL + "/" + CUSTOMER_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        CustomerDto content = mapFromJson(mvcResult.getResponse().getContentAsString(), CustomerDto.class);

        assertNotNull(content);
        assertNotNull(content.getId());
        assertNotNull(content.getAnonymisedName());
    }

    @Test
    public void createCustomerReturns200() throws Exception {

        CustomerDto dto = new CustomerDto();
        dto.setAnonymisedName("Test Customer Name");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createCustomerMissingBodyReturns400() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createCustomerWrongBodyReturns400() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("testing")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createCustomerMissingNameReturns400() throws Exception {

        CustomerDto dto = new CustomerDto();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateCustomerReturns200() throws Exception {

        CustomerDto dto = new CustomerDto();
        dto.setAnonymisedName("new name");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(BASE_URL + "/" + CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void updateCustomerWrongBodyReturns400() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(BASE_URL + "/" + CUSTOMER_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateCustomerNotExistingIdReturns404() throws Exception {

        CustomerDto dto = new CustomerDto();
        dto.setAnonymisedName("new name");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(BASE_URL + "/" + CUSTOMER_ID_404)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }

    @Test
    public void deleteCustomerReturns200() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(BASE_URL + "/" + CUSTOMER_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteCustomerMissingIdReturns405() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(BASE_URL)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteCustomerNotExistingIdReturns404() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(BASE_URL + "/" + CUSTOMER_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}