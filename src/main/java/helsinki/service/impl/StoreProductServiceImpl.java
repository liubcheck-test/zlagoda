package helsinki.service.impl;

import helsinki.dao.StoreProductDao;
import helsinki.lib.Inject;
import helsinki.lib.Service;
import helsinki.model.StoreProduct;
import helsinki.service.StoreProductService;
import java.util.List;

@Service
public class StoreProductServiceImpl implements StoreProductService {

    @Inject
    private StoreProductDao storeProductDao;

    @Override
    public StoreProduct create(StoreProduct storeProduct) {
        return storeProductDao.save(storeProduct);
    }

    @Override
    public StoreProduct get(String s) {
        return storeProductDao.get(s).orElse(null);
    }

    @Override
    public List<StoreProduct> getAll() {
        return storeProductDao.getAll();
    }

    @Override
    public StoreProduct update(StoreProduct storeProduct) {
        return storeProductDao.update(storeProduct);
    }

    @Override
    public boolean delete(String s) {
        return storeProductDao.delete(s);
    }
}
