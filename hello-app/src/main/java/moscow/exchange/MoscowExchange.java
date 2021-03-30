package moscow.exchange;

import java.util.HashMap;
import java.util.Map;

public class MoscowExchange {

    private class StockInfo {
        Double price;
        int count;

        public StockInfo(Double price, int count) {
            this.price = price;
            this.count = count;
        }
    }

    private final Map<String, StockInfo> stocks = new HashMap<>();

    public boolean AddStock(String name, Double price, int count) {
        if (stocks.containsKey(name)) {
            StockInfo prev = stocks.get(name);
            if (!prev.price.equals(price)) {
                return false;
            } else {
                stocks.put(name, new StockInfo(price, count + prev.count));
            }
        } else {
            stocks.put(name, new StockInfo(price, count));
        }
        return true;
    }

    public int getStockCount(String name) {
        if (!stocks.containsKey(name)) {
            return 0;
        } else {
            return stocks.get(name).count;
        }
    }

    public double getStockPrice(String name) {
        if (!stocks.containsKey(name)) {
            return -1;
        } else {
            return stocks.get(name).price;
        }
    }

    public boolean buyStocks(String name, int count) {
        if (!stocks.containsKey(name)) {
            return false;
        }
        StockInfo prev = stocks.get(name);
        stocks.put(name, new StockInfo(prev.price, prev.count + count));
        return true;
    }

    public boolean sellStocks(String name, int count) {
        if (!stocks.containsKey(name)) {
            return false;
        }
        StockInfo prev = stocks.get(name);
        if (prev.count < count) {
            return false;
        }
        stocks.put(name, new StockInfo(prev.price, prev.count - count));
        return true;
    }

    public boolean changePrice(String name, double newPrice) {
        if (!stocks.containsKey(name)) {
            return false;
        }
        StockInfo prev = stocks.get(name);
        stocks.put(name, new StockInfo(newPrice, prev.count));
        return true;
    }
}
