package com.ma.currencyconverter.controller;

import com.ma.currencyconverter.service.ExchangeCurrencyInfo;
import com.ma.currencyconverter.service.ForeignExchangeRateService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest({CurrencyConverterController.class})
class CurrencyConverterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForeignExchangeRateService foreignExchangeRateService;

    @Test
    void getExCurrency() throws IOException {

        ExchangeCurrencyInfo exchangeCurrencyInfo = new ExchangeCurrencyInfo();
        exchangeCurrencyInfo.setLocalNumberFormatStr( "Exchange rate is: 1.21\n" +
                "10 EUR exchanged amount will be 12.14 $");

        when(foreignExchangeRateService.getExchangeCurrencyInfo(any(), any(), any())).thenReturn(exchangeCurrencyInfo);
    }
    
}