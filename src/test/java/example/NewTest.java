package example;		

import org.testng.Assert;		
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;	
import org.testng.annotations.AfterTest;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.greaterThan;

import java.util.ArrayList;
import java.util.Map;

public class NewTest {		
	
    private static final String API_KEY = "cEbqsr3od6Hy2EFwkNowKJ6EY7TscXki";
    private static final String BASE_URL = "https://api.apilayer.com/fixer/latest?apikey=" + API_KEY;
    
    @Test
    public void testExchangeRatesGreaterThan20() {
        RequestSpecification request = RestAssured.given();
        Response response = request.get(BASE_URL);
        
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code");
        
        JsonPath jsonPath = response.jsonPath();
        Map<String, Object> rates = jsonPath.get("rates");
        int size = rates.size();
        
        Assert.assertTrue(size > 20, "Number of exchange rates is not greater than 20");
    }
    
    @Test
    public void testExchangeRatesContainsGBP_USD() {
        RequestSpecification request = RestAssured.given();
        Response response = request.get(BASE_URL);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code");
        System.out.println(response.getStatusLine());
        System.out.println(response.getBody().asString());
        JsonPath jsonPathEvaluator = response.jsonPath();
        Map<String, Object> rates = jsonPathEvaluator.getMap("rates");
        Assert.assertTrue(rates.containsKey("GBP"), "GBP not found in exchange rates");
        Assert.assertTrue(rates.containsKey("USD"), "USD not found in exchange rates");
    }
    
    @Test
    public void testNumberOfElementsAndRateAbbreviations() {
        RequestSpecification request = RestAssured.given();
        Response response = request.get(BASE_URL);

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code");

        JsonPath jsonPath = response.jsonPath();
        Map<String, Float> rates = jsonPath.getMap("rates");

        ArrayList<String> rateAbbreviations = new ArrayList<>(rates.keySet());

        Assert.assertTrue(rates.size() > 0, "No rates found");
        System.out.println("Number of rates found: " + rates.size());
        System.out.println("Rate abbreviations: " + rateAbbreviations);
    }
}
