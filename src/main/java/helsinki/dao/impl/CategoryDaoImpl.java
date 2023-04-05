package helsinki.dao.impl;

import helsinki.dao.CategoryDao;
import helsinki.exception.DataProcessingException;
import helsinki.lib.Dao;
import helsinki.model.Category;
import helsinki.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class CategoryDaoImpl implements CategoryDao {
    @Override
    public Category save(Category category) {
        String query = "INSERT INTO Category (category_name) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement saveCategoryStatement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            saveCategoryStatement.setString(1, category.getCategoryName());
            saveCategoryStatement.executeUpdate();
            ResultSet resultSet = saveCategoryStatement.getGeneratedKeys();
            if (resultSet.next()) {
                category.setCategoryNumber(resultSet.getObject(1, Integer.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create " + category + ". ", e);
        }
        return category;
    }

    @Override
    public Optional<Category> get(Integer id) {
        String query = "SELECT * FROM Category WHERE category_number = ?";
        Category category = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getCategoryStatement = connection.prepareStatement(query)) {
            getCategoryStatement.setInt(1, id);
            ResultSet resultSet = getCategoryStatement.executeQuery();
            if (resultSet.next()) {
                category = parseCategoryFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get category by id " + id, e);
        }
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> getAll() {
        String query = "SELECT * FROM Category";
        List<Category> categories = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllCategoriesStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllCategoriesStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(parseCategoryFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of categories "
                    + "from categories database.", e);
        }
        return categories;
    }

    @Override
    public Category update(Category category) {
        String query = "UPDATE Category SET category_name = ? WHERE category_number = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateCategoryStatement = connection.prepareStatement(query)) {
            updateCategoryStatement.setString(1, category.getCategoryName());
            updateCategoryStatement.setInt(2, category.getCategoryNumber());
            updateCategoryStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update "
                    + category + " in categories database.", e);
        }
        return category;
    }

    @Override
    public boolean delete(Integer id) {
        String query = "DELETE FROM Category WHERE category_number = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteCategoryStatement = connection.prepareStatement(query)) {
            deleteCategoryStatement.setLong(1, id);
            return deleteCategoryStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete category with id " + id, e);
        }
    }

    @Override
    public List<Category> getAllSortedByName() {
        String query = "SELECT * FROM Category ORDER BY category_name";
        List<Category> categories = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement getAllCategoriesStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllCategoriesStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(parseCategoryFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of categories "
                    + "from categories database.", e);
        }
        return categories;
    }

    private Category parseCategoryFromResultSet(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setCategoryNumber(resultSet.getInt("category_number"));
        category.setCategoryName(resultSet.getString("category_name"));
        return category;
    }
}
