package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.product.dto.ProductCharacteristicDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.PRODUCT_CHARACTERISTIC_ID_404;
import static org.knowledge4retail.api.test.Data.PRODUCT_CHARACTERISTIC_ID_DELETE;

public class ProductCharacteristicIntegrationTest extends AbstractIntegrationTest {

    private static final String PRODUCT_CHARACTERISTICS_URI = "/api/v0/productcharacteristics";

    @BeforeEach
    @Override
    public void setUp() throws Exception{
        super.setUp();
    }

    @Test
    public void getAllProductCharacteristicsReturns200Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCT_CHARACTERISTICS_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        List<ProductCharacteristicDto> contentList = mapListFromJson(itemsMvcResult.getResponse().getContentAsString(), ProductCharacteristicDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
        Assertions.assertNotNull(contentList.get(0).getName());
    }

    @Test
    public void createProductCharacteristicsReturns200Test() throws Exception {

        ProductCharacteristicDto requestDto = new ProductCharacteristicDto();
        requestDto.setName("TestCharacteristic");
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_CHARACTERISTICS_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);
    }

    @Test
    public void createProductCharacteristicsMissingBodyReturns400Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_CHARACTERISTICS_URI).contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, itemsMvcResult);
    }

    @Test
    public void createProductCharacteristicsWrongBodyReturns400Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_CHARACTERISTICS_URI).contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, itemsMvcResult);
    }

    @Test
    public void createProductCharacteristicsMissingNameReturns405Test() throws Exception {

        ProductCharacteristicDto requestDto = new ProductCharacteristicDto();
        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_CHARACTERISTICS_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, itemsMvcResult);
    }

    @Test
    public void deleteProductCharacteristicsReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_CHARACTERISTICS_URI + "/" + PRODUCT_CHARACTERISTIC_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteProductCharacteristicsMissingIdReturns405Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_CHARACTERISTICS_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteProductCharacteristicsNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_CHARACTERISTICS_URI + "/"+ PRODUCT_CHARACTERISTIC_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
