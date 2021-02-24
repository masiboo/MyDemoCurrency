# MyDemoCurrency

## Requirement

The Assignment - Java Currency Convertor

Create a site backed by a RESTful API which receives it will get three inputs:

* A source currency
* A target currency
* A monetary value The API must leverage the exchange rates provided at https://exchangeratesapi.io/ and leverage that
  to return a converted value. So if your input as 30, USD and GBP, you would need to return the calculated result.

Expose your logic behind a RESTful API The API has to be written in Java. Feel free to use a framework of your choice.
Develop the API as you would be developing actual production software - leverage validation, testing, caching We want to
see and test your API, so make the code available to us together with the link to the Git repository

Not to make this open to interpretation, treat as bad input everything that’s not in the list of supported currencies on
the site.

Extra Points: Deploy your application to a hosted service such as Heroku or AWS

Once you are ready, send us a link to your code and Git repository.

# My implementation

This demo API is hosted at Heroku. To test the API, Swagger UI is added to test easily.

https://demo-currency-converter.herokuapp.com/swagger-ui/#/

There are 2 GET RESTful API exposed. Please try it by the above Swagger UI or simply by curl

curl -X
GET "https://demo-currency-converter.herokuapp.com:443/api/v1/revisions/exchange-currency?localCurrency=eur&exCurrency=usd&amount=100.50"
-H "accept: application/json"

curl -X GET "https://demo-currency-converter.herokuapp.com:443/api/v1/revisions/all-supported-currency?baseCurrency=eur"
-H "accept: application/json"  