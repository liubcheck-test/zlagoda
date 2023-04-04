package liubomyr.stepanenko.dao;

import java.util.List;
import java.util.Optional;
import liubomyr.stepanenko.model.Category;

public interface CategoryDao {
    Category save(Category category);

    Optional<Category> get(Integer id);

    List<Category> getAll();

    Category update(Category category);

    boolean delete(Integer id);
}
