package com.ma.currencyconverter.controller;

import com.ma.currencyconverter.service.ExchangeCurrencyInfo;
import com.ma.currencyconverter.service.ForeignExchangeRateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CurrencyConverterControllerTest {

    @InjectMocks
    private CurrencyConverterController currencyConverterController;

    @Mock
    private ForeignExchangeRateService foreignExchangeRateService;


    @Test
    void getExCurrency() throws Exception {
        // Arrange
        ExchangeCurrencyInfo exchangeCurrencyInfo = new ExchangeCurrencyInfo();
        exchangeCurrencyInfo.setLocalNumberFormatStr( "Exchange rate is: 1.21\n" +
                "10 EUR exchanged amount will be 12.14 $");
        // Mock
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(foreignExchangeRateService.getExchangeCurrencyInfo(any(), any(), any())).thenReturn(exchangeCurrencyInfo);

        // Act
        String result = currencyConverterController.getExCurrency("eur", "usd", 10);

        // Assert
        Assertions.assertEquals(result, exchangeCurrencyInfo.getLocalNumberFormatStr());
    }

    @Test
    void getAllSupportedCurrency() throws IOException {
        // Arrange
        List<String> expectedCurrencyList = List.of("cad", "usd", "gbp", "sek", "aud");

        // Mock
        when(foreignExchangeRateService.getAllSupportedCurrency("eur")).thenReturn(expectedCurrencyList);

        // Act
        var actualCurrencyList =  currencyConverterController.getAllSupportedCurrency("eur");

        // Assert
        assertLinesMatch(expectedCurrencyList, actualCurrencyList);
    }
}
