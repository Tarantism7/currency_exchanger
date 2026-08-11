package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.skillbox.currency.exchange.dto.CbrValCursDto;
import ru.skillbox.currency.exchange.dto.CbrValuteDto;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.net.URL;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CbrClient {

    @Value("${cbr.url}")
    private String cbrUrl;

    public List<CbrValuteDto> fetchCurrencies() {
        try {
            log.info("Sending request to CBR API: {}", cbrUrl);
            JAXBContext context = JAXBContext.newInstance(CbrValCursDto.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            URL url = new URL(cbrUrl);
            CbrValCursDto valCurs = (CbrValCursDto) unmarshaller.unmarshal(url);
            log.info("Successfully fetched {} currencies from CBR", valCurs.getValutes().size());
            return valCurs.getValutes();
        } catch (Exception e) {
            log.error("Failed to fetch currencies from CBR API", e);
            return Collections.emptyList();
        }
    }
}
