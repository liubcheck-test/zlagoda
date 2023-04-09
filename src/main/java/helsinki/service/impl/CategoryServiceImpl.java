package helsinki.service.impl;

import helsinki.dao.CategoryDao;
import helsinki.dao.EmployeeDao;
import helsinki.lib.Inject;
import helsinki.lib.Service;
import helsinki.model.Category;
import helsinki.service.CategoryService;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Inject
    private CategoryDao categoryDao;

    @Override
    public Category create(Category category) {
        return categoryDao.save(category);
    }

    @Override
    public Category get(Integer integer) {
        return categoryDao.get(integer).orElse(null);
    }

    @Override
    public List<Category> getAll() {
        return categoryDao.getAll();
    }

    @Override
    public Category update(Category category) {
        return categoryDao.update(category);
    }

    @Override
    public boolean delete(Integer integer) {
        return categoryDao.delete(integer);
    }
}
