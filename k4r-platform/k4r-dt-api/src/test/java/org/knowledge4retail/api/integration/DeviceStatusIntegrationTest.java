package org.knowledge4retail.api.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.device.dto.DeviceStatusDto;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.knowledge4retail.api.test.Data.*;

public class DeviceStatusIntegrationTest extends AbstractIntegrationTest {

    private static final String DEVICE_URI = "/api/v0/devices/";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void getAllDevices() throws Exception {

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(DEVICE_URI + DEVICE_ID + "/statuses")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        List<DeviceStatusDto> contentList = mapListFromJson(mvcResult.getResponse().getContentAsString(), DeviceStatusDto.class);
        Assertions.assertNotNull(contentList);
    }
}
