package moscow.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoscowExchangeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoscowExchangeApplication.class, args);
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
