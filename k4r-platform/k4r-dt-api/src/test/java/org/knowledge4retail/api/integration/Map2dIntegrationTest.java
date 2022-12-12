package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.knowledge4retail.api.test.Data.STORE_ID;
import static org.knowledge4retail.api.test.Data.STORE_ID_404;

public class Map2dIntegrationTest extends AbstractIntegrationTest {

    private final String URL = "/api/v0/stores/{storeId}/map2d" ;
    private final String VALID_MAP2D = "{\"rosMetadata\":{\"timestamp\":1233453453,\"frameId\":\"cool456456456\",\"deviceId\":\"ROB-1000\"},\"resolution\":0.02,\"width\":123.0,\"height\":1111.0,\"lengthUnitId\":1000,\"pose\":{\"position\":{\"x\":123,\"y\":345,\"z\":345},\"orientation\":{\"x\":1,\"y\":2,\"z\":3,\"w\":4}},\"data\": [0,-1,56,100,24,26663,90]}";

    @BeforeEach
    @Override
    public void setUp() throws Exception{
        super.setUp();
    }

    @Test
    public void saveMapReturns400WhenMapObjectInvalidCool() throws Exception {
        // Arrange
        String map2d = "{\"depth\": \"fre\", header: { \"sequenceId\" : 234 } }";

        // Act
        // Assert
        mvc.perform(MockMvcRequestBuilders
                .post(URL, 2).contentType("application/json")
                .content(map2d)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void saveMapReturns200WhenMapObjectIsValid() throws Exception {

        mvc.perform(MockMvcRequestBuilders
                .post(URL, STORE_ID).contentType("application/json")
                .content(VALID_MAP2D)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getMapReturns404WhenMapNotFound() throws Exception {

        mvc.perform(MockMvcRequestBuilders
        .get(URL, STORE_ID_404)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getMapReturns200AndMapWhenMapIsFound() throws Exception {

        MvcResult mvcResult1 = mvc.perform(MockMvcRequestBuilders
                .post(URL, STORE_ID).contentType("application/json")
                .content(VALID_MAP2D)).andReturn();
        Assertions.assertEquals(200, mvcResult1.getResponse().getStatus());

        // Act
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(URL, STORE_ID)).andReturn();

        // Assert
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
    }
}