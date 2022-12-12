package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.product.dto.ProductGtinDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class ProductGtinIntegrationTest extends AbstractIntegrationTest {

    private static final String PRODUCT_GTIN_URI = "/api/v0/productgtins";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllProductGtinsReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCT_GTIN_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<ProductGtinDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), ProductGtinDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
    }

    @Test
    public void getProductGtinReturns200Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCT_GTIN_URI + "/" + PRODUCT_GTIN_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        ProductGtinDto content = mapFromJson(itemsMvcResult.getResponse().getContentAsString(), ProductGtinDto.class);
        Assertions.assertNotNull(content);
        Assertions.assertNotNull(content.getId());
    }

    @Test
    public void createProductGtinReturns200Test() throws Exception {

        ProductGtinDto requestDto = new ProductGtinDto();
        requestDto.setId(1234);
        requestDto.setProductUnitId(PRODUCT_UNIT_ID);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_GTIN_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createProductGtinMissingBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_GTIN_URI).contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createProductGtinWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_GTIN_URI).contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateProductGtinReturns200Test() throws Exception {

        ProductGtinDto requestDto = new ProductGtinDto();
        requestDto.setGtinType("test");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(PRODUCT_GTIN_URI + "/" + PRODUCT_GTIN_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void updateProductGtinWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(PRODUCT_GTIN_URI + "/" + PRODUCT_GTIN_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void deleteProductGtinReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_GTIN_URI + "/" + PRODUCT_GTIN_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteProductGtinMissingIdReturns405Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_GTIN_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteProductGtinNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_GTIN_URI + "/" + PRODUCT_GTIN_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
