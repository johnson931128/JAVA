package provider;
import model.Stock;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LocalStockProvider implements StockProvider {
    private final Map<String, Stock> stockData;

    public LocalStockProvider() {
        stockData = new HashMap<>();
        stockData.put("AAPL", new Stock("Apple Inc.", 172.5));
        stockData.put("TSLA", new Stock("Tesla Inc.", 648.3));
        stockData.put("GOOGL", new Stock("Alphabet Inc.", 2894.7));
    }

    @Override
    public Optional<Stock> getStock(String code) {
        return Optional.ofNullable(stockData.get(code));
    }
}

