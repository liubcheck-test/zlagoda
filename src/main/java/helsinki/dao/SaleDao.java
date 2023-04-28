package helsinki.dao;

import helsinki.model.Sale;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleDao {
    Sale save(Sale sale);

    List<Sale> getSalesByCheckNumber(String checkNumber);

    List<Sale> getSalesByUpcAndDateRange(String upc, LocalDateTime from, LocalDateTime to);

    List<Sale> getSalesByCashierAndDateRange(String upc, LocalDateTime from, LocalDateTime to);

    List<Sale> getSalesByDateRange(LocalDateTime from, LocalDateTime to);
}
