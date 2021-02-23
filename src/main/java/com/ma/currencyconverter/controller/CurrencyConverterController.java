package com.ma.currencyconverter.controller;

import com.ma.currencyconverter.service.ExchangeCurrencyInfo;
import com.ma.currencyconverter.service.ForeignExchangeRateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;


/**
 * Controller to get currency exchanged currency amount
 *
 * @since 23-02-2021
 */
@RestController
@RequestMapping(path = CurrencyConverterController.REQUEST_URL)
public class CurrencyConverterController {

    public static final String REQUEST_URL = "/api/v1/revisions/";
    public static final String GET_EXCHANGE_REQUEST = "exchange-currency";

    private static final Logger logger = LogManager.getLogger(CurrencyConverterController.class);

    @Autowired
    private ForeignExchangeRateService foreignExchangeRateService;

    /**
     * This will send exchanged currency amount in response with HTTP status code 200.
     *
     * @param localCurrency
     * @param exCurrency
     * @param amount
     * @return
     * @since 10-02-2020
     */
    @ApiOperation(value = "Returns a page of all routing rules revisions")
    @GetMapping(value = GET_EXCHANGE_REQUEST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getExCurrency(
            @ApiParam(value = "Local currency 3 character alphabetic currency codes (ISO 4217), i.e. eur, usd, gpb")
            @RequestParam(value = "localCurrency", required = true) String localCurrency,
            @ApiParam(value = "Exchanged currency 3 character alphabetic currency codes (ISO 4217), i.e. eur, usd, gpb")
            @RequestParam(value = "exCurrency", required = true) String exCurrency,
            @ApiParam(value = "A desired amount of money for exchange. It can have a dot (.) or comma (,) as a decimal " +
                    "number or just whole number i.e. 10, 10,50 or 10.50 ")
            @RequestParam(required = true) double amount) {
        logger.info("amount: " + amount + " currency: " + localCurrency + " exCurrency: " + exCurrency);
        BigDecimal bigDecimalAmount = new BigDecimal(amount);
        ExchangeCurrencyInfo exchangeCurrencyInfo;
        try {
            exchangeCurrencyInfo = foreignExchangeRateService.getExchangeCurrencyInfo(localCurrency, exCurrency, bigDecimalAmount);
        } catch (IOException ex){
            return "The requested local currency is not supported in our system";
        }
        if(exchangeCurrencyInfo == null){
            return "The requested exchange currency is not found";
        }
        return exchangeCurrencyInfo.getLocalNumberFormatStr();
    }
}
