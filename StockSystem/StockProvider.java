package provider;
import model.Stock;

import java.util.Optional;
public interface StockProvider {
    Optional<Stock> getStock(String code);
}
