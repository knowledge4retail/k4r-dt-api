package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.product.dto.ProductDto;
import org.knowledge4retail.api.product.dto.ProductGroupDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.knowledge4retail.api.test.Data.*;

public class ProductGroupIntegrationTest extends AbstractIntegrationTest {

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }


    @Test
    public void createProductGroupReturns200AndCreatesGroup() throws Exception {

        String name = "TestName";
        ProductGroupDto dto = ProductGroupDto.builder().name(name).build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/productgroups", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);
        ProductGroupDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ProductGroupDto.class);
        assertEquals(name, returnedDto.getName());
        assertEquals(STORE_ID, returnedDto.getStoreId());
        assertNotNull(returnedDto.getId());
    }

    @Test
    public void createProductGroupReturns400WhenNameIsNull() throws Exception {

        ProductGroupDto dto = ProductGroupDto.builder().build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/productgroups", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void createProductGroupUsesStoreIdFromPath() throws Exception {
        ProductGroupDto dto = ProductGroupDto.builder().name("testtest").storeId(STORE_ID_404).build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/productgroups", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        ProductGroupDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ProductGroupDto.class);
        assertEquals(STORE_ID, returnedDto.getStoreId());
    }

    @Test
    public void createProductGroupReturns400WhenDuplicateProductsInList() throws Exception {

        ProductDto p = new ProductDto();
        p.setId(PRODUCT_ID);
        ProductDto p2 = new ProductDto();
        p2.setId(PRODUCT_ID);
        List<ProductDto> products = new ArrayList<>();
        products.add(p);
        products.add(p2);

        ProductGroupDto dto = ProductGroupDto.builder().name("TestName2").products(products).build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/productgroups", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void createProductGroupReturns400WhenGroupNameAlreadyExistsForStore() throws Exception {

        ProductGroupDto dto = ProductGroupDto.builder().name("PRODUCT_GROUP_ID").build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/productgroups", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void createProductGroupReturns400WhenIdSubmittedAndAlreadyPresent() throws Exception {

        ProductGroupDto dto = ProductGroupDto.builder().id(PRODUCT_GROUP_ID).name("TestName3").build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/productgroups", STORE_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void createProductGroupReturns404WhenStoreNotFound() throws Exception {

        ProductGroupDto dto = ProductGroupDto.builder().name("TestName4").build();

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/stores/%1$s/productgroups", STORE_ID_404))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void retrieveProductGroupsReturns200AndGroups() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/stores/%1$s/productgroups", STORE_ID))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        List<ProductGroupDto> returnedDtos = mapListFromJson(result.getResponse().getContentAsString(), ProductGroupDto.class);

        assertTrue(returnedDtos.size() >= 1);
    }

    @Test
    public void retrieveProductGroupsReturns404WhenStoreNotFound() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/stores/%1$s/productgroups", STORE_ID_404))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void retrieveProductGroupReturns200AndGroup() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/productgroups/" + PRODUCT_GROUP_ID)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        ProductGroupDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), ProductGroupDto.class);

        assertEquals(PRODUCT_GROUP_ID, returnedDto.getId());
    }

    @Test
    public void retrieveProductGroupReturns404WhenGroupNotFound() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/productgroups/" + PRODUCT_GROUP_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void deleteProductGroupReturns200AndDeletesGroup() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/productgroups/" + PRODUCT_GROUP_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        MvcResult result2 = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/productgroups/" + PRODUCT_GROUP_ID_DELETE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result2);
    }

    @Test
    public void deleteProductGroupReturns404WhenGroupNotFound() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete("/api/v0/productgroups/" + PRODUCT_GROUP_ID_404)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void addProductToProductGroupReturns200AndAddsProduct() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/productgroups/%1$s/products/%2$s", PRODUCT_GROUP_ID, PRODUCT_ID_2))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);
        ProductGroupDto newDto = loadProductGroup(PRODUCT_GROUP_ID);
        assertTrue(newDto.getProducts().stream().anyMatch(p -> p.getId().equals(PRODUCT_ID_2)));
    }

    @Test
    public void addProductToProductGroupReturns400WhenProductWasAlreadyInGroup() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/productgroups/%1$s/products/%2$s", PRODUCT_GROUP_ID, PRODUCT_ID))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void addProductToProductGroupReturns404WhenProductNotFound() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/productgroups/%1$s/products/%2$s", PRODUCT_GROUP_ID, PRODUCT_ID_404))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void addProductToProductGroupReturns404WhenProductGroupNotFound() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/productgroups/%1$s/products/%2$s", PRODUCT_GROUP_ID_404, PRODUCT_ID))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void removeProductFromProductGroupReturns200AndRemovesProduct() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/productgroups/%1$s/products/%2$s", PRODUCT_GROUP_ID, PRODUCT_ID_3))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        MvcResult result2 = mvc.perform(MockMvcRequestBuilders
                .delete(String.format("/api/v0/productgroups/%1$s/products/%2$s", PRODUCT_GROUP_ID, PRODUCT_ID_3))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        ProductGroupDto newDto = loadProductGroup(PRODUCT_GROUP_ID);

        assertFalse(newDto.getProducts().stream().anyMatch(p -> p.getId().equals(PRODUCT_ID_3)));
    }

    @Test
    public void removeProductFromProductGroupReturns400WhenProductWasNotInGroup() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete(String.format("/api/v0/productgroups/%1$s/products/%2$s", PRODUCT_GROUP_ID, PRODUCT_ID_3))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.BAD_REQUEST, result);
    }

    @Test
    public void removeProductFromProductGroupReturns404WhenProductNotFound() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete(String.format("/api/v0/productgroups/%1$s/products/%2$s", PRODUCT_GROUP_ID, PRODUCT_ID_404))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void removeProductFromProductGroupReturns404WhenProductGroupNotFound() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete(String.format("/api/v0/productgroups/%1$s/products/%2$s", PRODUCT_GROUP_ID_404, PRODUCT_ID))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    private ProductGroupDto loadProductGroup(Integer productGroupId) throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/api/v0/productgroups/" + productGroupId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        return mapFromJson(result.getResponse().getContentAsString(), ProductGroupDto.class);
    }
}
