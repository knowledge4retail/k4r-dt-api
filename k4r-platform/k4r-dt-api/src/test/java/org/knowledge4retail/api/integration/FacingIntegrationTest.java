package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.FacingDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.knowledge4retail.api.test.Data.*;

public class FacingIntegrationTest extends AbstractIntegrationTest {

    private final String FACINGS_BASE_URL = "/api/v0/facings/";

    @Override
    @BeforeEach
    public void setUp() throws Exception{
        super.setUp();
    }

    @Test
    public void createFacingReturns200WhenInputIsValid() throws Exception {

        int shelfLayerRelativePosition = 11;
        int idInBody = 12345;
        int noOfItemsWidth = 1;
        int noOfItemsDepth = 1;
        int noOfItemsHeight = 1;
        FacingDto dto = new FacingDto(idInBody,999,shelfLayerRelativePosition,noOfItemsWidth,noOfItemsDepth,noOfItemsHeight,1, 1, 0, 1000,"123");

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/shelflayers/%1$s/facings", SHELF_LAYER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        FacingDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), FacingDto.class);

        assertEquals(noOfItemsWidth, returnedDto.getNoOfItemsWidth());
        assertEquals(noOfItemsDepth, returnedDto.getNoOfItemsDepth());
        assertEquals(SHELF_LAYER_ID, returnedDto.getShelfLayerId());
        assertEquals(shelfLayerRelativePosition, returnedDto.getLayerRelativePosition());
        assertNotEquals(idInBody, returnedDto.getId());
    }

    @Test
    public void createFacingReturns404WhenShelfLayerIdNotFound() throws Exception {

        int idInBody = 12345;
        int shelfLayerRelativePosition = 11;
        FacingDto dto = new FacingDto(idInBody,999,shelfLayerRelativePosition,1,1,1, 1,1, 0, 1000,"123");

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(String.format("/api/v0/shelflayers/%1$s/facings", SHELF_LAYER_ID_404))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(dto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void retrieveFacingsReturnListOfFacingAndResponseCde200() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(String.format("/api/v0/shelflayers/%1$s/facings", SHELF_LAYER_ID))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        List<FacingDto> returnedDtos = mapListFromJson(result.getResponse().getContentAsString(), FacingDto.class);

        assertTrue(returnedDtos.size() >= 1);
    }

    @Test
    public void retrieveFacingReturns200WithFoundFacing() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(FACINGS_BASE_URL + FACING_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        FacingDto returnedDto = mapFromJson(result.getResponse().getContentAsString(), FacingDto.class);

        assertEquals(SHELF_LAYER_ID, returnedDto.getId());
    }

    @Test
    public void retrieveFacingReturns404WhenFacingDoesNotExist() throws Exception {

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(FACINGS_BASE_URL + FACING_ID_404)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }

    @Test
    public void putFacingReturns200WhenUpdatingAnExistingFacing() throws Exception {

        int layerRelativePosition = 5;
        int idInBody = 3453455;
        int noOfItemsWidth = 2;
        int noOfItemsDepth = 2;
        int noOfItemsHeight = 2;

        FacingDto facingDto = new FacingDto(idInBody, SHELF_LAYER_ID,
                layerRelativePosition,noOfItemsWidth,
                noOfItemsDepth,noOfItemsHeight,
                1, 1,0, 1000,
                "123");

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .put(FACINGS_BASE_URL + FACING_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(facingDto))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        FacingDto returnedDto = mapFromJson(mvcResult.getResponse().getContentAsString(), FacingDto.class);

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        assertEquals(FACING_ID, returnedDto.getId());
        assertEquals(SHELF_LAYER_ID, returnedDto.getShelfLayerId());
        assertEquals(noOfItemsWidth, returnedDto.getNoOfItemsWidth());
        assertEquals(noOfItemsDepth, returnedDto.getNoOfItemsDepth());
        assertEquals(layerRelativePosition, returnedDto.getLayerRelativePosition());

    }

    @Test
    public void deleteFacingReturns200WhenDeletionIsDone() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete(FACINGS_BASE_URL + FACING_ID_DELETE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, result);

        MvcResult result2 = mvc.perform(MockMvcRequestBuilders
                .get(FACINGS_BASE_URL + FACING_ID_DELETE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result2);
    }
    @Test
    public void deleteFacingReturns404WhenFacingNotFound() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .delete(FACINGS_BASE_URL + FACING_ID_404)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.NOT_FOUND, result);
    }
}