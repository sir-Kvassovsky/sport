package edu.khlep.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Component
public class StringToOffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

    private static final ZoneId MOSCOW_ZONE = ZoneId.of("Europe/Moscow");

    @Override
    public OffsetDateTime convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        LocalDateTime ldt = LocalDateTime.parse(source);
        return ldt.atZone(MOSCOW_ZONE).toOffsetDateTime();
    }
}
