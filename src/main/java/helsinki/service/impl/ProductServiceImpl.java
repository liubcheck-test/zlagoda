package helsinki.service.impl;

import helsinki.dao.ProductDao;
import helsinki.lib.Inject;
import helsinki.lib.Service;
import helsinki.model.Product;
import helsinki.service.ProductService;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Inject
    private ProductDao productDao;

    @Override
    public Product create(Product product) {
        return productDao.save(product);
    }

    @Override
    public Product get(Long id) {
        return productDao.get(id).orElse(null);
    }

    @Override
    public List<Product> getAll() {
        return productDao.getAll();
    }

    @Override
    public Product update(Product product) {
        return productDao.update(product);
    }

    @Override
    public boolean delete(Long id) {
        return productDao.delete(id);
    }
}
