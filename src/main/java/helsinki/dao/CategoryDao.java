package helsinki.dao;

import helsinki.model.Category;
import java.util.List;

public interface CategoryDao extends BasicDao<Category, Integer> {
    List<Category> getAllSortedByName();
}
