package helsinki.dao;

import helsinki.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDao extends BasicDao<Product, Long> {
    List<Product> getProductsByName(String name);

    List<Product> getAllSortedByName();

    List<Product> getAllByCategorySortedByName(String categoryName);
}
