package org.knowledge4retail.api.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.store.dto.FacingDto;
import org.knowledge4retail.api.store.dto.ShelfDto;
import org.knowledge4retail.api.store.dto.ShelfLayerDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.knowledge4retail.api.test.Data.*;

@Slf4j
public class PositionIntegrationTest extends AbstractIntegrationTest {

    private static final String POSITION_URI = "/api/v0/";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getFacingByExternalReferenceIdAndGtinReturns200Test() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(POSITION_URI + "/shelflayers/" + SHELF_LAYER_EXTERNAL_REFERENCE_ID + "/productgtins/" + GTIN + "/facing")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        FacingDto content = mapFromJson(mvcResult.getResponse().getContentAsString(), FacingDto.class);
        Assertions.assertNotNull(content);
        Assertions.assertNotNull(content.getId());
    }

    @Test
    public void getAllPositionsByStoreIdAndGtinReturns200Test() throws  Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(POSITION_URI + "/stores/" + STORE_ID + "/productgtins/" + GTIN + "/positions")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        JsonNode contentList = new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString());
        JsonNode contentListFirstElement = new ObjectMapper().readTree(contentList.get(0).toString());
        FacingDto contentListFirstElementFacing = mapFromJson(contentListFirstElement.get("facingDto").toString(), FacingDto.class);
        ShelfLayerDto contentListFirstElementShelfLayer = mapFromJson(contentListFirstElement.get("shelfLayerDto").toString(), ShelfLayerDto.class);
        ShelfDto contentListFirstElementShelf = mapFromJson(contentListFirstElement.get("shelfDto").toString(), ShelfDto.class);
        String contentListFirstElementGtin = mapFromJson(contentListFirstElement.get("gtin").toString(), String.class);


        Assertions.assertNotNull(contentList);
        Assertions.assertNotNull(contentListFirstElement);
        Assertions.assertNotNull(contentListFirstElementFacing);
        Assertions.assertNotNull(contentListFirstElementShelfLayer);
        Assertions.assertNotNull(contentListFirstElementShelf);
        Assertions.assertNotNull(contentListFirstElementGtin);
    }
}
