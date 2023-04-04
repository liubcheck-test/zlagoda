package helsinki.dao;

import helsinki.model.StoreProduct;
import java.util.List;
import java.util.Optional;

public interface StoreProductDao {
    StoreProduct save(StoreProduct product);

    Optional<StoreProduct> get(String upc);

    List<StoreProduct> getAll();

    StoreProduct update(StoreProduct product);

    boolean delete(String upc);
}
