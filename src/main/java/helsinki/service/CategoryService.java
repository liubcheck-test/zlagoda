package helsinki.service;

import helsinki.model.Category;
import java.util.List;

public interface CategoryService extends BasicService<Category, Integer> {
    List<Category> getAllSortedByName();
}
