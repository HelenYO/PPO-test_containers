package PersonalAccount;

//import javafx.util.Pair;

import PersonalAccount.PersonalAccount.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {


    private class StockInfo {
        Double price;
        int count;

        public StockInfo(Double price, int count) {
            this.price = price;
            this.count = count;
        }


    }

    private double balance = 0;
    Map<String, StockInfo> stocks = new HashMap<>();
    int id;

    public User(int id) {
        this.id = id;
    }

    public boolean addMoney(double money) {
        if (money < 0) {
            return false;
        } else {
            balance += money;
            return true;
        }
    }

    public List<Pair> getStocks() {
        ArrayList<Pair> list = new ArrayList<>();
        for (Map.Entry<String, StockInfo> stock : stocks.entrySet()) {
            if (stock.getValue().count != 0) {
                list.add(new Pair(stock.getKey(), stock.getValue().count));
            }
        }
        return list;
    }

    public boolean buyStocks(String name, int count, double price) {
        double sum = count * price;
        if (sum > balance) {
            return false;
        }
        balance -= sum;
        if (stocks.containsKey(name)) {
            StockInfo prev = stocks.get(name);
            prev.price = Math.max(prev.price, price);//для приблизительного расчета выгоды, выгода будет не меньше посчитанной
            prev.count += count;
            stocks.put(name, prev);
        } else {
            stocks.put(name, new StockInfo(price, count));
        }
        return true;
    }

    public boolean sellStocks(String name, int count, double price) {
        if (!stocks.containsKey(name)) {
            return false;
        }
        StockInfo prev = stocks.get(name);
        if (prev.count < count) {
            return false;
        }
        prev.count -= count;
        stocks.put(name, prev);
        balance += price * count;
        return true;
    }

//    public List<String> getStocks() {
//        ArrayList<String> list = new ArrayList<>();
//        for(Map.Entry<String,StockInfo> stock : stocks.entrySet()) {
//            double price = 0;// todo:: обратиться к бирже и найти стоимость
//            list.add(stock.getKey() + " : " + stock.getValue().count + " x " + price);
//        }
//        return list;
//    }

//    public double getSummary() {
//        double summary = 0;
//        for(Map.Entry<String,StockInfo> stock : stocks.entrySet()) {
//            double price = 0;// todo:: обратиться к бирже и найти стоимость
//            summary += stock.getValue().count * price;
//        }
//        summary += balance;
//        return summary;
//    }


}
