package helsinki.service.impl;

import helsinki.dao.SaleDao;
import helsinki.lib.Inject;
import helsinki.lib.Service;
import helsinki.model.Check;
import helsinki.model.Sale;
import helsinki.service.SaleService;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {
    @Inject
    private SaleDao saleDao;

    @Override
    public Sale save(Sale sale) {
        return saleDao.save(sale);
    }

    @Override
    public List<Sale> getSalesByCheck(Check check) {
        return saleDao.getSalesByCheckNumber(check.getCheckNumber());
    }

    @Override
    public List<Sale> getSalesByUpcAndDateRange(String upc, LocalDateTime from, LocalDateTime to) {
        return saleDao.getSalesByUpcAndDateRange(upc, from, to);
    }

    @Override
    public List<Sale> getSalesByCashierAndDateRange(String cashierId, LocalDateTime from, LocalDateTime to) {
        return saleDao.getSalesByCashierAndDateRange(cashierId, from, to);
    }

    @Override
    public List<Sale> getSalesByDateRange(LocalDateTime from, LocalDateTime to) {
        return saleDao.getSalesByDateRange(from, to);
    }
}
