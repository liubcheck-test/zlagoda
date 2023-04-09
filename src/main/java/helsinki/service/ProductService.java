package helsinki.service;

import helsinki.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService extends BasicService<Product, Long> {

    Optional<Product> getByProductName(String name);

    List<Product> getAllSortedByName();

    List<Product> getAllByCategorySortedByName(Integer categoryNumber);
}
