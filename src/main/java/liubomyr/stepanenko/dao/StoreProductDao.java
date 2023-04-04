package liubomyr.stepanenko.dao;

import java.util.List;
import java.util.Optional;
import liubomyr.stepanenko.model.StoreProduct;

public interface StoreProductDao {
    StoreProduct save(StoreProduct product);

    Optional<StoreProduct> get(String upc);

    List<StoreProduct> getAll();

    StoreProduct update(StoreProduct product);

    boolean delete(String upc);
}
