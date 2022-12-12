package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.StoreCharacteristicDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.STORE_CHARACTERISTIC_ID_404;
import static org.knowledge4retail.api.test.Data.STORE_CHARACTERISTIC_ID_DELETE;

public class StoreCharacteristicIntegrationTest extends AbstractIntegrationTest {

    private static final String STORE_CHARACTERISTICS_URI = "/api/v0/storecharacteristics";

    @BeforeEach
    @Override
    public void setUp() throws Exception{
        super.setUp();
    }

    @Test
    public void getAllStoreCharacteristicsReturns200Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(STORE_CHARACTERISTICS_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        List<StoreCharacteristicDto> contentList = mapListFromJson(itemsMvcResult.getResponse().getContentAsString(), StoreCharacteristicDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
        Assertions.assertNotNull(contentList.get(0).getName());
    }

    @Test
    public void createStoreCharacteristicsReturns200Test() throws Exception {

        StoreCharacteristicDto requestDto = new StoreCharacteristicDto();
        requestDto.setName("TestCharacteristic");
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(STORE_CHARACTERISTICS_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);
    }

    @Test
    public void createStoreCharacteristicsMissingBodyReturns400Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(STORE_CHARACTERISTICS_URI).contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, itemsMvcResult);
    }

    @Test
    public void createStoreCharacteristicsWrongBodyReturns400Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(STORE_CHARACTERISTICS_URI).contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, itemsMvcResult);
    }

    @Test
    public void createStoreCharacteristicsMissingNameReturns405Test() throws Exception {

        StoreCharacteristicDto requestDto = new StoreCharacteristicDto();
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(STORE_CHARACTERISTICS_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, itemsMvcResult);
    }

    @Test
    public void deleteStoreCharacteristicsReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(STORE_CHARACTERISTICS_URI + "/" + STORE_CHARACTERISTIC_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteStoreCharacteristicsMissingIdReturns405Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(STORE_CHARACTERISTICS_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteStoreCharacteristicsNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(STORE_CHARACTERISTICS_URI + "/"+ STORE_CHARACTERISTIC_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
