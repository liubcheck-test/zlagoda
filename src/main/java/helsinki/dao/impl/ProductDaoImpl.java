package helsinki.dao.impl;

import helsinki.dao.ProductDao;
import helsinki.exception.DataProcessingException;
import helsinki.lib.Dao;
import helsinki.model.Category;
import helsinki.model.Product;
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
public class ProductDaoImpl implements ProductDao {
    @Override
    public Product save(Product product) {
        String query = "INSERT INTO Product (category_number, product_name, characteristics) "
                + "VALUES (?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement saveProductStatement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            saveProductStatement.setInt(1, product.getCategory().getCategoryNumber());
            saveProductStatement.setString(2, product.getName());
            saveProductStatement.setString(3, product.getCharacteristics());
            saveProductStatement.executeUpdate();
            ResultSet resultSet = saveProductStatement.getGeneratedKeys();
            if (resultSet.next()) {
                product.setId(resultSet.getObject(1, Long.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create "
                    + product + ". ", e);
        }
        return product;
    }

    @Override
    public Optional<Product> get(Long id) {
        String query = "SELECT id_product, product.category_number, category_name, "
                + "product_name, characteristics FROM product "
                + "JOIN category ON product.category_number = category.category_number "
                + "WHERE id_product = ?";
        Product product = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getProductStatement = connection.prepareStatement(query)) {
            getProductStatement.setLong(1, id);
            ResultSet resultSet = getProductStatement.executeQuery();
            if (resultSet.next()) {
                product = parseProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get product by id " + id, e);
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT id_product, product.category_number, category_name, "
                + "product_name, characteristics FROM product "
                + "JOIN category ON product.category_number = category.category_number ";
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllProductsStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllProductsStatement.executeQuery();
            while (resultSet.next()) {
                products.add(parseProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of products "
                    + "from products database.", e);
        }
        return products;
    }

    @Override
    public Product update(Product product) {
        String query = "UPDATE Product "
                + "SET category_number = ?, product_name = ?, characteristics = ? "
                + "WHERE id_product = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateProductStatement = connection.prepareStatement(query)) {
            updateProductStatement.setInt(1, product.getCategory().getCategoryNumber());
            updateProductStatement.setString(2, product.getName());
            updateProductStatement.setString(3, product.getCharacteristics());
            updateProductStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update "
                    + product + " in products database.", e);
        }
        return product;
    }

    @Override
    public boolean delete(Long id) {
        String query = "DELETE FROM Product WHERE id_product = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteProductStatement = connection.prepareStatement(query)) {
            deleteProductStatement.setLong(1, id);
            return deleteProductStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete product with id " + id, e);
        }
    }

    @Override
    public Optional<Product> getByProductName(String name) {
        String query = "SELECT id_product, product.category_number, category_name, "
                + "product_name, characteristics FROM product "
                + "JOIN category ON product.category_number = category.category_number "
                + "WHERE product_name = ?";
        Product product = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getProductStatement = connection.prepareStatement(query)) {
            getProductStatement.setString(1, name);
            ResultSet resultSet = getProductStatement.executeQuery();
            if (resultSet.next()) {
                product = parseProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get product by name " + name, e);
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> getAllSortedByName() {
        String query = "SELECT id_product, product.category_number, category_name, "
                + "product_name, characteristics FROM product "
                + "JOIN category ON product.category_number = category.category_number "
                + "ORDER BY product_name";
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllProductsStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllProductsStatement.executeQuery();
            while (resultSet.next()) {
                products.add(parseProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of products "
                    + "from products database.", e);
        }
        return products;
    }

    @Override
    public List<Product> getAllByCategorySortedByName(Integer categoryNumber) {
        String query = "SELECT id_product, product.category_number, category_name, "
                + "product_name, characteristics FROM product "
                + "JOIN category ON product.category_number = category.category_number "
                + "WHERE product.category_number = ?"
                + "ORDER BY product_name";
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllProductsStatement = connection.prepareStatement(query)) {
            getAllProductsStatement.setInt(1, categoryNumber);
            ResultSet resultSet = getAllProductsStatement.executeQuery();
            while (resultSet.next()) {
                products.add(parseProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of products "
                    + "from products database.", e);
        }
        return products;
    }

    private Product parseProductFromResultSet(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setId(resultSet.getObject("id_product", Long.class));
        product.setCategory(new Category(
                resultSet.getInt("category_number"),
                resultSet.getString("category_name")
        ));
        product.setName(resultSet.getString("product_name"));
        product.setCharacteristics(resultSet.getString("characteristics"));
        return product;
    }
}
