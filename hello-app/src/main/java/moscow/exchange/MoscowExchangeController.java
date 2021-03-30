package moscow.exchange;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoscowExchangeController {

    private final String SUCCESS = "SUCCESS";

    private MoscowExchange me = new MoscowExchange();

    private String error(String text) {
        return text;
    }


    @RequestMapping("/addStock")
    public String addStock(String name, Double price, int count) {
        System.out.println("hello!");
        if (count <= 0) {
            return error("type non-zero positive count");
        }
        if (price <= 0) {
            return error("type non-zero positive price");
        }
        if (me.AddStock(name, price, count)) {
            return SUCCESS;
        } else {
            error("error, aborted");
        }
        return SUCCESS;
    }

    @RequestMapping("/getStockCount")
    public String getStockCount(String name) {
        return Integer.toString(me.getStockCount(name));
    }

    @RequestMapping("/getStockPrice")
    public String getStockPrice(String name) {
        double res = me.getStockPrice(name);
        if (res == -1) {
            return error("no info");
        }
        return Double.toString(me.getStockPrice(name));
    }

    @RequestMapping("/buyStocks")
    public String buyStocks(String name, int count) {
        if (count <= 0) {
            return error("type non-zero positive count");
        }
        if (me.buyStocks(name, count)) {
            return SUCCESS;
        } else {
            return error("error, aborted");
        }
    }

    @RequestMapping("/sellStocks")
    public String sellStocks(String name, int count) {
        if (count <= 0) {
            return error("type non-zero positive count");
        }
        if (me.sellStocks(name, count)) {
            return SUCCESS;
        } else {
            return error("error, aborted");
        }
    }

    @RequestMapping("/changePrice")
    public String changePrice(String name, double newPrice) {
        if (newPrice <= 0) {
            return error("type non-zero positive price");
        }
        if (me.changePrice(name, newPrice)) {
            return SUCCESS;
        } else {
            return error("error, aborted");
        }
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
