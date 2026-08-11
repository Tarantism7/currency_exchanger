package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.currency.exchange.dto.CbrValuteDto;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyListDto;
import ru.skillbox.currency.exchange.dto.ShortCurrencyDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;
    private final CbrClient cbrClient;

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id).orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
        return mapper.convertToDto(currency);
    }

    public Double convertValue(Long value, Long numCode) {
        log.info("CurrencyService method convertValue executed");
        Currency currency = repository.findByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        return mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }

    public CurrencyListDto getAllCurrencies() {
        List<ShortCurrencyDto> currencyList = repository.findAll().stream()
                .map(currency -> new ShortCurrencyDto(currency.getName(), currency.getValue()))
                .collect(Collectors.toList());
        return new CurrencyListDto(currencyList);
    }

    @Transactional
    public void updateCurrenciesFromCbr() {
        List<CbrValuteDto> cbrValutes = cbrClient.fetchCurrencies();

        cbrValutes.stream().forEach(dto -> {
            Double parsedValue = Double.parseDouble(dto.getValue().replace(",", "."));

            Currency currency = repository.findByIsoCharCode(dto.getCharCode())
                    .orElseGet(Currency::new);

            currency.setName(dto.getName());
            currency.setNominal(dto.getNominal());
            currency.setIsoNumCode(dto.getNumCode());
            currency.setIsoCharCode(dto.getCharCode());
            currency.setValue(parsedValue);

            repository.save(currency);
        });

        log.info("Currency exchange rates successfully updated in database");
    }
}
