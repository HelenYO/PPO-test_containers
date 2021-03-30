package PersonalAccount;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AppTestExchange {
    private final String SUCCESS = "SUCCESS";

    @ClassRule
    public static GenericContainer simpleWebServer
            = new FixedHostPortGenericContainer("hello-app:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080);

    @Test
    public void test() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/addStock?name=$AIR&price=42.24&count=1000"))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(SUCCESS, response.body());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/addStock?name=$AAL&price=24.19&count=2000"))
                .GET()
                .build();

        HttpResponse<String> response2 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(SUCCESS, response2.body());

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/addStock?name=$ATRO&price=18&count=200"))
                .GET()
                .build();

        HttpResponse<String> response3 = HttpClient.newHttpClient().send(request3, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(SUCCESS, response3.body());

        //check functions in moscow exchange

        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/getStockCount?name=$ATRO"))
                .GET()
                .build();

        HttpResponse<String> response4 = HttpClient.newHttpClient().send(request4, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("200", response4.body());

        HttpRequest request5 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/getStockPrice?name=$ATRO"))
                .GET()
                .build();

        HttpResponse<String> response5 = HttpClient.newHttpClient().send(request5, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("18.0", response5.body());

        HttpRequest request6 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/buyStocks?name=$ATRO&count=100"))
                .GET()
                .build();

        HttpResponse<String> response6 = HttpClient.newHttpClient().send(request6, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(SUCCESS, response6.body());

        HttpRequest request7 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/sellStocks?name=$ATRO&count=25"))
                .GET()
                .build();

        HttpResponse<String> response7 = HttpClient.newHttpClient().send(request7, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(SUCCESS, response7.body());

        HttpRequest request8 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/getStockCount?name=$ATRO"))
                .GET()
                .build();

        HttpResponse<String> response8 = HttpClient.newHttpClient().send(request8, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("275", response8.body());

        HttpRequest request9 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/changePrice?name=$ATRO&newPrice=14.7"))
                .GET()
                .build();

        HttpResponse<String> response9 = HttpClient.newHttpClient().send(request9, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals(SUCCESS, response9.body());

        HttpRequest request10 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/getStockPrice?name=$ATRO"))
                .GET()
                .build();

        HttpResponse<String> response10 = HttpClient.newHttpClient().send(request10, HttpResponse.BodyHandlers.ofString());
        Assert.assertEquals("14.7", response10.body());


        // check Personal Account
        PersonalAccount pa = new PersonalAccount();
        Assert.assertEquals(pa.registerUser(1), SUCCESS);
        Assert.assertEquals(pa.registerUser(2), SUCCESS);
        Assert.assertEquals(pa.registerUser(3), SUCCESS);
        Assert.assertEquals(pa.registerUser(4), SUCCESS);

        Assert.assertEquals(pa.addMoneyToUser(1, 500), SUCCESS);
        Assert.assertEquals(pa.addMoneyToUser(2, 1500), SUCCESS);
        Assert.assertEquals(pa.addMoneyToUser(3, 2500), SUCCESS);
        Assert.assertEquals(pa.addMoneyToUser(4, 3500), SUCCESS);

        Assert.assertNotEquals(pa.addMoneyToUser(5, 3500), SUCCESS);

        Assert.assertEquals(pa.showStocks(1), "");

        Assert.assertEquals(pa.showSummary(1), "0.0");

        Assert.assertEquals(pa.buyStocks(1, "$AAL", 27), "error, aborted");

        Assert.assertEquals(pa.buyStocks(2, "$AAL", 27), SUCCESS);

        Assert.assertEquals(pa.showStocks(2), "$AAL : 27 x 24.19\n");

        Assert.assertEquals(pa.sellStocks(2, "$AAL", 2), SUCCESS);

        Assert.assertEquals(pa.showSummary(2), "604.75");
    }
}
/*
http://localhost:8080/addStock?name=$AIR&price=42&count=3
http://localhost:8080/getStockCount?name=$AIR
http://localhost:8080/getStockPrice?name=$AIR
http://localhost:8080/buyStocks?name=$AIR&count=3
http://localhost:8080/sellStocks?name=$AIR&count=2
http://localhost:8080/changePrice?name=$AIR&newPrice=33
 */
