package provider;
import java.util.Optional;
import model.Stock;
public interface StockProvider {
    Optional<Stock> getStock(String code);
}
