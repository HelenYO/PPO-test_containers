package PersonalAccount;

//import javafx.util.Pair;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalAccount {

    public static class Pair {
        String key;
        int value;

        public Pair(String key, int value) {
            this.key = key;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }
    }

    private String error(String text) {
        return text;
    }

    private final String SUCCESS = "SUCCESS";

    Map<Integer, User> users = new HashMap<Integer, User>();

    public String registerUser(int id) {
        if (users.containsKey(id)) {
            return error("id is in use");
        } else {
            users.put(id, new User(id));
            return SUCCESS;
        }
    }

    public String addMoneyToUser(int id, double money) {
        if (!users.containsKey(id)) {
            return error("no user");
        }
        if (money <= 0) {
            return error("type positive money");
        }
        User prev = users.get(id);

        if (prev.addMoney(money)) {
            users.put(id, prev);
            return SUCCESS;
        } else {
            return error("error, aborted");
        }
    }

    public String showStocks(int id) throws Exception {
        if (!users.containsKey(id)) {
            return error("no user");
        }
        List<Pair> stocks = users.get(id).getStocks();
        StringBuilder res = new StringBuilder();
        for (Pair stock : stocks) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/getStockPrice?name=" + stock.getKey()))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            res.append(stock.getKey()).append(" : ").append(stock.getValue()).append(" x ").append(response.body()).append("\n");
        }
        return res.toString();
    }

    public String showSummary(int id) throws Exception {//только деньги в акциях
        if (!users.containsKey(id)) {
            return error("no user");
        }
        List<Pair> stocks = users.get(id).getStocks();
        double res = 0;
        for (Pair stock : stocks) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/getStockPrice?name=" + stock.getKey()))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            res += Double.parseDouble(response.body()) * stock.value;
        }
        return Double.toString(res);
    }

    public String buyStocks(int id, String name, int count) throws Exception {
        if (!users.containsKey(id)) {
            return error("no user");
        }
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/getStockCount?name=" + name))
                .GET()
                .build();

        HttpResponse<String> response1 = HttpClient.newHttpClient().send(request1, HttpResponse.BodyHandlers.ofString());
        int haveCount = Integer.parseInt(response1.body());
        if (haveCount < count) {
            return error("not this count");
        }


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/getStockPrice?name=" + name))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        double price = Double.parseDouble(response.body());
        User prev = users.get(id);
        if (prev.buyStocks(name, count, price)) {
            HttpRequest request3 = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/sellStocks?name=" + name + "&count=" + count))
                    .GET()
                    .build();

            HttpResponse<String> response3 = HttpClient.newHttpClient().send(request3, HttpResponse.BodyHandlers.ofString());
            if (response3.body().equals(SUCCESS)) {
                users.put(id, prev);
                return SUCCESS;
            }
        }
        return error("error, aborted");
    }

    public String sellStocks(int id, String name, int count) throws Exception {
        if (!users.containsKey(id)) {
            return error("no user");
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/getStockPrice?name=" + name))
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        double price = Double.parseDouble(response.body());
        User prev = users.get(id);
        if (prev.sellStocks(name, count, price)) {
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/buyStocks?name=" + name + "&count=" + count))
                    .GET()
                    .build();

            HttpResponse<String> response2 = HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
            if (response2.body().equals(SUCCESS)) {
                users.put(id, prev);
                return SUCCESS;
            } else {
                return response2.body();
            }
        }
        return error("error, aborted");
    }

}
