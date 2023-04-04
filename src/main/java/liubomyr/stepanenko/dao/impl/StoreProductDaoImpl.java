package liubomyr.stepanenko.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import liubomyr.stepanenko.dao.StoreProductDao;
import liubomyr.stepanenko.exception.DataProcessingException;
import liubomyr.stepanenko.model.Category;
import liubomyr.stepanenko.model.Product;
import liubomyr.stepanenko.model.StoreProduct;
import liubomyr.stepanenko.util.ConnectionUtil;

public class StoreProductDaoImpl implements StoreProductDao {
    @Override
    public StoreProduct save(StoreProduct product) {
        String query = "INSERT INTO Store_Product (UPC, UPC_prom, id_product, selling_price, "
                + "products_number, promotional_product) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement saveProductStatement = connection.prepareStatement(query,
                     Statement.RETURN_GENERATED_KEYS)) {
            saveProductStatement.setString(1, product.getUPC());
            saveProductStatement.setString(2, product.getUPCProm());
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
            product.setProduct(getProductByStoreProductUPC(product.getUPC()));
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
                storeProduct.setProduct(getProductByStoreProductUPC(storeProduct.getUPC())));
        return storeProducts;
    }

    @Override
    public StoreProduct update(StoreProduct product) {
        String query = "UPDATE Store_Product "
                + "SET UPC_prom = ?, id_product = ?, selling_price = ?, "
                + "products_number = ?, promotional_product = ? "
                + "WHERE UPC = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement updateProductStatement
                     = connection.prepareStatement(query)) {
            updateProductStatement.setString(1, product.getUPCProm());
            updateProductStatement.setLong(2, product.getProduct().getId());
            updateProductStatement.setBigDecimal(3, product.getPrice());
            updateProductStatement.setInt(4, product.getAmount());
            updateProductStatement.setBoolean(5, product.getIsPromotional());
            updateProductStatement.setString(6, product.getUPC());
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

    private StoreProduct parseStoreProductFromResultSet(ResultSet resultSet) throws SQLException {
        StoreProduct product = new StoreProduct();
        product.setUPC(resultSet.getString("UPC"));
        product.setUPCProm(resultSet.getString("UPC_prom"));
        product.setPrice(resultSet.getBigDecimal("selling_price"));
        product.setAmount(resultSet.getInt("products_number"));
        product.setIsPromotional(resultSet.getBoolean("promotional_product"));
        return product;
    }

    private Product getProductByStoreProductUPC(String upc) {
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
}
