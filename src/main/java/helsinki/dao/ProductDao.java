package helsinki.dao;

import helsinki.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Product save(Product product);

    Optional<Product> get(Long id);

    List<Product> getAll();

    Product update(Product product);

    boolean delete(Long id);
}
