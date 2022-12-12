package org.knowledge4retail.api.converter;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.scan.converter.ScanConverter;
import org.knowledge4retail.api.scan.dto.ScanDto;
import org.knowledge4retail.api.scan.model.Scan;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
public class ScanConverterTest {

    private Scan scan;
    private String timestamp_ubica;
    private OffsetDateTime offsetDateTime;

    @BeforeEach
    public void setUp() {

        offsetDateTime = OffsetDateTime.of(2021, 5, 27, 6, 59, 22, 0, ZoneOffset.of("+02:00"));
        timestamp_ubica = "2021-05-27T06:59:22";
    }

    @Test
    public void testConvertDtoToModel()
    {

        ScanDto scanDto_ubica = ScanDto.builder().timestamp(timestamp_ubica).entityType("store").id("1000").build();
        Scan newScan = ScanConverter.INSTANCE.dtoToScan(scanDto_ubica);

        scan = Scan.builder().timestamp(offsetDateTime).entityType("store").id("1000").build();

        Assertions.assertEquals(scan.getTimestamp(), newScan.getTimestamp());
        Assertions.assertEquals(scan.getEntityType(), newScan.getEntityType());
        Assertions.assertEquals(scan.getId(), newScan.getId());
    }

    @Test
    public void testConvertModelToDto()
    {
        ScanDto scanDto = ScanDto.builder().timestamp("2021-05-27T04:59:22Z").entityType("store").id("1000").build();

        scan = Scan.builder().timestamp(offsetDateTime).entityType("store").id("1000").build();
        ScanDto newDto = ScanConverter.INSTANCE.scanToDto(scan);

        Assertions.assertEquals(scanDto.getTimestamp(), newDto.getTimestamp());
        Assertions.assertEquals(scanDto.getEntityType(), newDto.getEntityType());
        Assertions.assertEquals(scanDto.getId(), newDto.getId());
    }
}
