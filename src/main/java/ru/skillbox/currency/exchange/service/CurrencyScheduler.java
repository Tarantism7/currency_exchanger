package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyScheduler {

    private final CurrencyService currencyService;

    @Scheduled(fixedRate = 3600000) // 1 час = 3 600 000 мс
    public void scheduleCurrencyUpdate() {
        log.info("Starting scheduled currency update execution");
        currencyService.updateCurrenciesFromCbr();
    }
}
