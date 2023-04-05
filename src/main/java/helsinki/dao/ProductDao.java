package helsinki.dao;

import helsinki.model.Product;
import java.util.List;

public interface ProductDao extends BasicDao<Product, Long> {
    List<Product> getAllSortedByName();

    List<Product> getAllByCategorySortedByName(Integer categoryNumber);
}
