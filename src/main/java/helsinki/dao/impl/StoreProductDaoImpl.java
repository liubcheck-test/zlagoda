package helsinki.dao.impl;

import helsinki.dao.StoreProductDao;
import helsinki.exception.DataProcessingException;
import helsinki.lib.Dao;
import helsinki.model.Category;
import helsinki.model.Product;
import helsinki.model.StoreProduct;
import helsinki.util.ConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Dao
public class StoreProductDaoImpl implements StoreProductDao {
    @Override
    public StoreProduct save(StoreProduct product) {
        String query = "INSERT INTO Store_Product (UPC, UPC_prom, id_product, selling_price, "
                + "products_number, promotional_product) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement saveProductStatement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            saveProductStatement.setString(1, product.getUpc());
            saveProductStatement.setString(2, product.getUpcProm());
            saveProductStatement.setLong(3, product.getProduct().getId());
            saveProductStatement.setBigDecimal(4, product.getPrice());
            saveProductStatement.setInt(5, product.getAmount());
            saveProductStatement.setBoolean(6, product.getIsPromotional());
            saveProductStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create " + product + ". ", e);
        }
        return product;
    }

    @Override
    public Optional<StoreProduct> get(String upc) {
        String query = "SELECT * FROM Store_Product WHERE UPC = ?";
        StoreProduct product = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getProductStatement = connection.prepareStatement(query)) {
            getProductStatement.setString(1, upc);
            ResultSet resultSet = getProductStatement.executeQuery();
            if (resultSet.next()) {
                product = parseStoreProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get store product by UPC " + upc, e);
        }
        if (product != null) {
            product.setProduct(getProductByStoreProductUpc(product.getUpc()));
        }
        return Optional.ofNullable(product);
    }

    @Override
    public List<StoreProduct> getAll() {
        String query = "SELECT * FROM Store_Product";
        List<StoreProduct> storeProducts = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllProductsStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllProductsStatement.executeQuery();
            while (resultSet.next()) {
                storeProducts.add(parseStoreProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of store products "
                    + "from store products database.", e);
        }
        storeProducts.forEach(storeProduct ->
                storeProduct.setProduct(getProductByStoreProductUpc(storeProduct.getUpc())));
        return storeProducts;
    }

    @Override
    public StoreProduct update(StoreProduct product) {
        String query = "UPDATE Store_Product "
                + "SET UPC_prom = ?, id_product = ?, selling_price = ?, "
                + "products_number = ?, promotional_product = ? "
                + "WHERE UPC = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateProductStatement = connection.prepareStatement(query)) {
            updateProductStatement.setString(1, product.getUpcProm());
            updateProductStatement.setLong(2, product.getProduct().getId());
            updateProductStatement.setBigDecimal(3, product.getPrice());
            updateProductStatement.setInt(4, product.getAmount());
            updateProductStatement.setBoolean(5, product.getIsPromotional());
            updateProductStatement.setString(6, product.getUpc());
            updateProductStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update "
                    + product + " in store product database.", e);
        }
        return product;
    }

    @Override
    public boolean delete(String upc) {
        String query = "DELETE FROM Store_Product WHERE UPC = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteProductStatement = connection.prepareStatement(query)) {
            deleteProductStatement.setString(1, upc);
            return deleteProductStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete store product with UPC " + upc, e);
        }
    }

    @Override
    public List<StoreProduct> getAllSortedByAmount() {
        String query = "SELECT * FROM Store_Product ORDER BY products_number";
        List<StoreProduct> storeProducts = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement getAllProductsStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllProductsStatement.executeQuery();
            while (resultSet.next()) {
                storeProducts.add(parseStoreProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of store products "
                    + "from store products database.", e);
        }
        storeProducts.forEach(storeProduct ->
                storeProduct.setProduct(getProductByStoreProductUpc(storeProduct.getUpc())));
        return storeProducts;
    }

    @Override
    public Map<String, Object> getStoreProductDataByUpc(String upc) {
        String query = "SELECT selling_price, products_number, product_name, characteristics "
                + "FROM store_product "
                + "JOIN product ON product.id_product = store_product.id_product "
                + "WHERE UPC = ?";
        Map<String, Object> storeProductData = Map.of(
                "selling_price", BigDecimal.ZERO,
                "products_number", 0,
                "product_name", "",
                "characteristics", ""
        );
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getProductStatement = connection.prepareStatement(query)) {
            getProductStatement.setString(1, upc);
            ResultSet resultSet = getProductStatement.executeQuery();
            while (resultSet.next()) {
                storeProductData = parseProductDataFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list store product data "
                    + "from store products database.", e);
        }
        return storeProductData;
    }

    @Override
    public List<StoreProduct> getAllNonPromotionalSortedByAmountAndName() {
        String query = "SELECT UPC, product_name, selling_price, "
                + "products_number, product.characteristics FROM Store_Product "
                + "JOIN product ON product.id_product = store_product.id_product "
                + "WHERE promotional_product = false "
                + "ORDER BY products_number, product_name";
        List<StoreProduct> storeProducts = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllProductsStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllProductsStatement.executeQuery();
            while (resultSet.next()) {
                storeProducts.add(parseStoreProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of store products "
                    + "from store products database.", e);
        }
        storeProducts.forEach(storeProduct ->
                storeProduct.setProduct(getProductByStoreProductUpc(storeProduct.getUpc())));
        return storeProducts;
    }

    private StoreProduct parseStoreProductFromResultSet(ResultSet resultSet) throws SQLException {
        StoreProduct product = new StoreProduct();
        product.setUpc(resultSet.getString("UPC"));
        product.setUpcProm(resultSet.getString("UPC_prom"));
        product.setPrice(resultSet.getBigDecimal("selling_price"));
        product.setAmount(resultSet.getInt("products_number"));
        product.setIsPromotional(resultSet.getBoolean("promotional_product"));
        return product;
    }

    private Product getProductByStoreProductUpc(String upc) {
        String query = "SELECT id_product, product.category_number, category_name, "
                + "product_name, characteristics FROM product "
                + "JOIN store_product sp on product.id_product = sp.id_product "
                + "JOIN category ON product.category_number = category.category_number "
                + "WHERE sp.UPC = ?";
        Product product = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getProductStatement = connection.prepareStatement(query)) {
            getProductStatement.setString(1, upc);
            ResultSet resultSet = getProductStatement.executeQuery();
            if (resultSet.next()) {
                product = parseProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get store product by upc " + upc, e);
        }
        return product;
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

    private Map<String, Object> parseProductDataFromResultSet(ResultSet resultSet)
            throws SQLException {
        Map<String, Object> storeProductData = new HashMap<>();
        storeProductData.put("selling_price", resultSet.getBigDecimal("selling_price"));
        storeProductData.put("products_number", resultSet.getInt("products_number"));
        storeProductData.put("product_name", resultSet.getString("product_name"));
        storeProductData.put("characteristics", resultSet.getString("characteristics"));
        return storeProductData;
    }
}
