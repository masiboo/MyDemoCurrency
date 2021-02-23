package com.ma.currencyconverter.service;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This service fetch currency exchange rate data from exchangeratesapi
 * @since 23-02-2021
 * */
@Service
public class ForeignExchangeRateService {

    private static final Logger logger = LogManager.getLogger(ForeignExchangeRateService.class);

    private static String exchangeRateApi = "https://api.exchangeratesapi.io/latest?base=";
    private ExchangeCurrencyInfo exchangeCurrencyInfo;

    /**
     * This will return ExchangeCurrencyInfo instance with all required value
     * @since 23-02-2021
     * @param localCurrency
     * @param exchangeCurrency
     * @param amount
     * @return
     */

    public ExchangeCurrencyInfo getExchangeCurrencyInfo(String localCurrency, String exchangeCurrency, BigDecimal amount ) throws IOException {
        HttpURLConnection connection;
        try {
            var url = new URL(exchangeRateApi+localCurrency.toUpperCase());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
        } catch (MalformedURLException e) {
            throw new MalformedURLException(e.getLocalizedMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        // Convert to JSON
        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
        JsonObject members = root.getAsJsonObject();
        // Accessing object
        JsonElement rates = members.get("rates");
        JsonObject allRates = rates.getAsJsonObject();

        BigDecimal exchangeRate;
        if(localCurrency.equals(exchangeCurrency)){
            // both same then exchangeRate must be 1.
            exchangeRate = new BigDecimal(1);
        }else{
            // gets actual exchange rate
            exchangeRate = exchangeRateFormServer(allRates, exchangeCurrency);
        }
        // No exchange rate found
        if(exchangeRate == null ){
            return null;
        }

        // crate a new instance of exchangeCurrencyInfo for each new request
        exchangeCurrencyInfo = new ExchangeCurrencyInfo() ;
        exchangeCurrencyInfo.setRate(exchangeRate);
        exchangeCurrencyInfo.setAmount(amount);
        exchangeCurrencyInfo.setCurrencyCode(localCurrency.toUpperCase());
        exchangeCurrencyInfo.setExchangeCurrencyCode(exchangeCurrency.toUpperCase());
        exchangeCurrencyInfo.setExchangeAmount(amount);
        exchangeCurrencyInfo.setSymbolLocaleInfo(allRates, exchangeCurrency );
        return exchangeCurrencyInfo;
    }

    /**
     * This will return  exchange rate
     * @since 23-02-2021
     * @param allRates
     * @param currency
     * @return
     */

    public BigDecimal exchangeRateFormServer(JsonObject allRates, String currency) {
        AtomicReference<BigDecimal> exchangeRate = new AtomicReference<>();
        allRates.keySet().stream().filter(key -> key.equalsIgnoreCase(currency)).forEach(key -> {
            Object value = allRates.get(key);
            logger.info("key: " + key + " exchange value: " + value);
            try {
                var decimalKey =  NumberUtils.parseNumber(value.toString(), BigDecimal.class);
               // decimalKey = decimalKey.setScale(2, RoundingMode.HALF_EVEN);
                exchangeRate.set(decimalKey);
            } catch (NumberFormatException ex) {
                throw new NumberFormatException(ex.getMessage());
            }
        });
        return exchangeRate.get();
    }
}