package provider;

import model.Stock;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RemoteStockProvider implements StockProvider {
    private static final String API_KEY = "PYQUCRFBWV9SZ8W2"; 
    private static final String API_URL = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=%s&interval=5min&apikey=" + API_KEY;

    @Override
    public Optional<Stock> getStock(String stockCode) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(String.format(API_URL, stockCode)))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String responseBody = response.body();
                Stock stock = parseStockData(responseBody, stockCode);
                return Optional.of(stock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Stock parseStockData(String responseBody, String stockCode) {
        try {
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonObject timeSeries = jsonObject.getAsJsonObject("Time Series (5min)");
            String latestTime = timeSeries.keySet().iterator().next();
            JsonObject latestData = timeSeries.getAsJsonObject(latestTime);
            double price = latestData.get("1. open").getAsDouble();
            return new Stock(stockCode, price);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}