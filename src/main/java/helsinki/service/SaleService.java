package helsinki.service;

import helsinki.model.Check;
import helsinki.model.Sale;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleService {
    Sale save(Sale sale);

    List<Sale> getSalesByCheck(Check check);

    List<Sale> getSalesByUpcAndDateRange(String upc, LocalDateTime from, LocalDateTime to);

    List<Sale> getSalesByCashierAndDateRange(String upc, LocalDateTime from, LocalDateTime to);

    List<Sale> getSalesByDateRange(LocalDateTime from, LocalDateTime to);
}
