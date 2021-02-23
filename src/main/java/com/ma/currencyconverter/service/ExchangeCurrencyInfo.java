package com.ma.currencyconverter.service;

import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

/**
 * This class holds all necessary values for exchange currency
 * @since 23-02-2021
 * */
public class ExchangeCurrencyInfo {

    private static final Logger logger = LogManager.getLogger(ExchangeCurrencyInfo.class);

    private String currencySymbol;
    private Locale numberLocale;
    private BigDecimal rate;
    private BigDecimal amount;
    private String currencyCode;
    private String exchangeCurrencyCode;
    private BigDecimal exchangeAmount;
    private String localNumberFormatStr;

    /**
     * This will set currency symbol and local number format based on
     * the exchange currency
     * @since 23-02-2021
     * @param allRates
     * @param exchangeCurrency
     */
    public void setSymbolLocaleInfo(JsonObject allRates, String exchangeCurrency){
        Optional<String> matchCurrency =  allRates.keySet()
                .stream()
                .filter(key -> key.equalsIgnoreCase(exchangeCurrency))
                .findAny();
        String currency = null;
        if(matchCurrency.isPresent()) {
           currency =  matchCurrency.get();
        }else{
            currency = exchangeCurrency;
        }
        // There are 37 countries using euro.
        // I need at least one country to get euro currency symbol
        // and number format. I assume all European country has
        // same number format.
        if(null != currency &&  currency.equalsIgnoreCase("eur")){
            currency = "FI";
        }
        if(null != currency){
            String currencyCode = currency.substring(0,2);
            Locale locale = new Locale("EN",currencyCode);
            numberLocale = new Locale(currencyCode);
            Currency symbolCurrency = Currency.getInstance(locale);
            currencySymbol = symbolCurrency.getSymbol();
            logger.info("currencySymbol: "+currencySymbol);
            setLocalNumberFormatStr();
        }
    }

    /**
     * This will set a localized string with local number format and
     * the currency symbol
     * the exchange currency
     * @since 10-02-2020
     */
    private void setLocalNumberFormatStr(){
        NumberFormat numberFormat = NumberFormat.getInstance(numberLocale);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        localNumberFormatStr = "Exchange rate is: "+numberFormat.format(rate)+"\n"
                + amount +" "+ currencyCode +" exchanged amount will be "
                + numberFormat.format(exchangeAmount)+" "+currencySymbol;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Locale getNumberLocale() {
        return numberLocale;
    }

    public void setNumberLocale(Locale numberLocale) {
        this.numberLocale = numberLocale;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExchangeAmount() {
        return exchangeAmount;
    }

    public void setExchangeAmount(BigDecimal exchangeAmount) {
        this.exchangeAmount = exchangeAmount.multiply(rate);
    }

    public String getLocalNumberFormatStr() {
        return localNumberFormatStr;
    }

    public void setLocalNumberFormatStr(String localNumberFormatStr) {
        this.localNumberFormatStr = localNumberFormatStr;
    }

    public void setExchangeCurrencyCode(String exchangeCurrencyCode) {
        this.exchangeCurrencyCode = exchangeCurrencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        if(currencyCode.isEmpty()){
            this.currencyCode = "euro";
        }else{
            this.currencyCode = currencyCode;
        }

    }

    public String getExchangeCurrencyCode() {
        return exchangeCurrencyCode;
    }
}
