package org.knowledge4retail.api.shared.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class OffSetDateTimeMapper {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public OffsetDateTime map(String timestamp) {

        LocalDateTime dateTime = LocalDateTime.parse(timestamp, formatter);
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.of("Europe/Berlin"));

        return zonedDateTime.toOffsetDateTime();
    }

    public String map(OffsetDateTime offsetDateTime) {

        return offsetDateTime.atZoneSameInstant(ZoneId.of("Europe/Berlin")).format(DateTimeFormatter.ISO_INSTANT);
        //return offsetDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
