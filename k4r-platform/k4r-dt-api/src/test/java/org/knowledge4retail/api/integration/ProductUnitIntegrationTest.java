package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.product.dto.ProductUnitDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class ProductUnitIntegrationTest extends AbstractIntegrationTest {

    private static final String PRODUCT_UNIT_URI = "/api/v0/productunits";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllProductUnitsReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCT_UNIT_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<ProductUnitDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), ProductUnitDto.class);
        Assertions.assertNotNull(contentList);
        Assertions.assertTrue(contentList.size() > 0);
        Assertions.assertNotNull(contentList.get(0));
        Assertions.assertNotNull(contentList.get(0).getId());
        Assertions.assertNotNull(contentList.get(0).getHeight());
        Assertions.assertNotNull(contentList.get(0).getLength());
        Assertions.assertNotNull(contentList.get(0).getWidth());
    }

    @Test
    public void getProductUnitReturns200Test() throws Exception {

        MvcResult itemsMvcResult = mvc.perform(MockMvcRequestBuilders.get(PRODUCT_UNIT_URI + "/" + PRODUCT_UNIT_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, itemsMvcResult);

        ProductUnitDto content = mapFromJson(itemsMvcResult.getResponse().getContentAsString(), ProductUnitDto.class);
        Assertions.assertNotNull(content);
        Assertions.assertNotNull(content.getId());
        Assertions.assertNotNull(content.getWidth());
        Assertions.assertNotNull(content.getLength());
        Assertions.assertNotNull(content.getWidth());
    }

    @Test
    public void createProductUnitReturns200Test() throws Exception {

        ProductUnitDto requestDto = new ProductUnitDto();
        requestDto.setProductId(PRODUCT_ID);
        requestDto.setLength(LENGTH);
        requestDto.setWidth(WIDTH);
        requestDto.setHeight(HEIGHT);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_UNIT_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void createProductUnitMissingBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_UNIT_URI).contentType(JSON_CONTENT_TYPE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createProductUnitWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_UNIT_URI).contentType(JSON_CONTENT_TYPE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void createProductUnitMissingSizesReturns400Test() throws Exception {

        ProductUnitDto requestDto = new ProductUnitDto();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(PRODUCT_UNIT_URI).contentType(JSON_CONTENT_TYPE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void updateProductUnitReturns200Test() throws Exception {

        ProductUnitDto requestDto = new ProductUnitDto();
        requestDto.setHeight(HEIGHT);
        requestDto.setWidth(WIDTH);
        requestDto.setLength(LENGTH);
        requestDto.setMaxStackSize(1234);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(PRODUCT_UNIT_URI + "/" + PRODUCT_UNIT_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content(mapToJson(requestDto))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void updateProductUnitWrongBodyReturns400Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(PRODUCT_UNIT_URI + "/" + PRODUCT_UNIT_ID).contentType(MediaType.APPLICATION_JSON_VALUE).content("test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, mvcResult);
    }

    @Test
    public void deleteProductUnitReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_UNIT_URI + "/" + PRODUCT_UNIT_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);
    }

    @Test
    public void deleteProductUnitMissingIdReturns405Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_UNIT_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.METHOD_NOT_ALLOWED, mvcResult);
    }

    @Test
    public void deleteProductUnitNotExistingIdReturns404Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(PRODUCT_UNIT_URI + "/" + PRODUCT_UNIT_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.NOT_FOUND, mvcResult);
    }
}
