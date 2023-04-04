package helsinki.dao;

import helsinki.model.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryDao {
    Category save(Category category);

    Optional<Category> get(Integer id);

    List<Category> getAll();

    Category update(Category category);

    boolean delete(Integer id);
}
