package helsinki.dao.impl;

import helsinki.dao.StoreProductDao;
import helsinki.exception.DataProcessingException;
import helsinki.lib.Dao;
import helsinki.model.Category;
import helsinki.model.CategoryMaxPriceProduct;
import helsinki.model.Product;
import helsinki.model.ProductStat;
import helsinki.model.StoreProduct;
import helsinki.util.ConnectionUtil;
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
import java.util.Random;

@Dao
public class StoreProductDaoImpl implements StoreProductDao {
    private final Random random = new Random();

    @Override
    public StoreProduct save(StoreProduct product) {
        String query = "INSERT INTO Store_Product (UPC, UPC_prom, id_product, selling_price, "
                + "products_number, promotional_product) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement saveProductStatement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            long upcLong = Math.abs(random.nextLong() % 1000000000000L);
            String upc = String.format("%012d", upcLong);
            saveProductStatement.setString(1, upc);
            saveProductStatement.setString(2, upc);
            saveProductStatement.setLong(3, product.getProduct().getId());
            saveProductStatement.setBigDecimal(4, product.getPrice());
            saveProductStatement.setInt(5, product.getAmount());
            saveProductStatement.setBoolean(6, product.getIsPromotional());
            saveProductStatement.executeUpdate();
            ResultSet resultSet = saveProductStatement.getGeneratedKeys();
            if (resultSet.next()) {
                product.setUpc(resultSet.getObject(1, String.class));
                product.setUpcProm(resultSet.getObject(2, String.class));
            }
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
        String query = "SELECT * FROM Store_Product WHERE products_number > 0";
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
        String query = "SELECT * FROM Store_Product WHERE products_number > 0 ORDER BY products_number ";
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
        Map<String, Object> storeProductData = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getProductStatement = connection.prepareStatement(query)) {
            getProductStatement.setString(1, upc);
            ResultSet resultSet = getProductStatement.executeQuery();
            while (resultSet.next()) {
                storeProductData = parseProductDataFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get store product data "
                    + "from store products database.", e);
        }
        return storeProductData;
    }

    @Override
    public Map<String, Object> getStoreProductPriceAndAmountByUpc(String upc) {
        String query = "SELECT selling_price, products_number "
                + "FROM store_product "
                + "WHERE UPC = ?";
        Map<String, Object> storeProductData = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getProductStatement = connection.prepareStatement(query)) {
            getProductStatement.setString(1, upc);
            ResultSet resultSet = getProductStatement.executeQuery();
            while (resultSet.next()) {
                storeProductData = parseProductSmallDataFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get store product data "
                    + "from store products database.", e);
        }
        return storeProductData;
    }

    @Override
    public List<StoreProduct> getAllPromotionalSortedByAmountAndName() {
        String query = "SELECT * FROM Store_Product "
                + "JOIN product ON product.id_product = store_product.id_product "
                + "WHERE promotional_product = true "
                + "ORDER BY products_number, product.product_name";
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
    public List<StoreProduct> getAllNonPromotionalSortedByAmountAndName() {
        String query = "SELECT * FROM Store_Product "
                + "JOIN product ON product.id_product = store_product.id_product "
                + "WHERE promotional_product = false "
                + "ORDER BY products_number, product.product_name";
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
    public List<CategoryMaxPriceProduct> requestCategory() {
        List<CategoryMaxPriceProduct> categoryMaxPriceProducts = new ArrayList<>();
        String query = "SELECT Category.category_number AS category_number, category_name, Product.id_product as id_product, product_name, selling_price "
                + "FROM (Store_product INNER JOIN Product ON Store_product.id_product = Product.id_product) "
                + "INNER JOIN Category ON Category.category_number = Product.category_number "
                + "WHERE (selling_price, Category.category_number) IN ( "
                + "SELECT MAX(selling_price), category_number "
                + "FROM Store_product INNER JOIN Product ON Store_product.id_product = Product.id_product "
                + "GROUP BY category_number);";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement requestCategoryStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = requestCategoryStatement.executeQuery();
            while (resultSet.next()) {
                CategoryMaxPriceProduct categoryMaxPriceProduct = new CategoryMaxPriceProduct();
                categoryMaxPriceProduct.setCategoryNumber(resultSet.getInt("category_number"));
                categoryMaxPriceProduct.setCategoryName(resultSet.getString("category_name"));
                categoryMaxPriceProduct.setIdProduct(resultSet.getLong("id_product"));
                categoryMaxPriceProduct.setProductName(resultSet.getString("product_name"));
                categoryMaxPriceProduct.setSellingPrice(resultSet.getBigDecimal("selling_price"));
                categoryMaxPriceProducts.add(categoryMaxPriceProduct);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't execute requestCategory query. ", e);
        }
        return categoryMaxPriceProducts;
    }

    @Override
    public List<StoreProduct> requestProduct(LocalDateTime startDate, LocalDateTime endDate) {
        List<StoreProduct> productDetailsList = new ArrayList<>();
        String query = "SELECT DISTINCT P.id_product AS id_product, Store_product.UPC AS UPC, P.product_name AS product_name, P.characteristics AS characteristics, C.category_number AS category_number, C.category_name AS category_name "
                + "FROM (((Product P INNER JOIN Store_product ON P.id_product = Store_product.id_product) "
                + "INNER JOIN Sale ON Sale.UPC = Store_product.UPC) "
                + "INNER JOIN `Check` ON `Check`.check_number = Sale.check_number) "
                + "INNER JOIN Category C ON C.category_number = P.category_number "
                + "WHERE print_date BETWEEN ? AND ?"
                + "AND NOT EXISTS ( "
                + "SELECT Product.id_product "
                + "FROM (Product INNER JOIN Store_product ON Product.id_product = Store_product.id_product) "
                + "INNER JOIN Sale ON Sale.UPC = Store_product.UPC "
                + "WHERE Product.id_product = P.id_product "
                + "AND Store_product.UPC NOT IN ( "
                + "SELECT UPC "
                + "FROM Store_product "
                + "WHERE promotional_product = 1));";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement requestProductStatement = connection.prepareStatement(query)) {
            requestProductStatement.setTimestamp(1, Timestamp.valueOf(startDate));
            requestProductStatement.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet resultSet = requestProductStatement.executeQuery();
            while (resultSet.next()) {
                Category category = new Category();
                category.setCategoryNumber(resultSet.getInt("category_number"));
                category.setCategoryName(resultSet.getString("category_name"));

                Product productDetails = new Product();
                productDetails.setId(resultSet.getLong("id_product"));
                productDetails.setName(resultSet.getString("product_name"));
                productDetails.setCharacteristics(resultSet.getString("characteristics"));
                productDetails.setCategory(category);

                StoreProduct storeProduct = new StoreProduct();
                storeProduct.setProduct(productDetails);
                storeProduct.setUpc(resultSet.getString("UPC"));

                productDetailsList.add(storeProduct);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't execute requestProduct query. ", e);
        }
        return productDetailsList;
    }

    @Override
    public List<ProductStat> requestProductStat(LocalDateTime startDate, LocalDateTime endDate) {
        List<ProductStat> productStats = new ArrayList<>();
        String query = "SELECT Product.id_product AS id_product, product_name, characteristics, SUM(Sale.product_number) AS total_number, SUM(Sale.product_number * Sale.selling_price) AS total_sum "
                + "FROM (Product INNER JOIN Store_product ON Product.id_product = Store_product.id_product) "
                + "INNER JOIN Sale ON Sale.UPC = Store_product.UPC "
                + "WHERE check_number IN ( "
                + "SELECT check_number "
                + "FROM `Check` "
                + "WHERE print_date BETWEEN ? AND ? "
                + ") "
                + "GROUP BY Product.id_product;";

        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement requestProductStatStatement = connection.prepareStatement(query)) {
            requestProductStatStatement.setTimestamp(1, Timestamp.valueOf(startDate));
            requestProductStatStatement.setTimestamp(2, Timestamp.valueOf(endDate));
            ResultSet resultSet = requestProductStatStatement.executeQuery();
            while (resultSet.next()) {
                ProductStat productStat = new ProductStat();
                productStat.setIdProduct(resultSet.getLong("id_product"));
                productStat.setProductName(resultSet.getString("product_name"));
                productStat.setCharacteristics(resultSet.getString("characteristics"));
                productStat.setTotalNumber(resultSet.getInt("total_number"));
                productStat.setTotalSum(resultSet.getBigDecimal("total_sum"));
                productStats.add(productStat);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't execute requestProductStat query. ", e);
        }
        return productStats;
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
        String query = "SELECT product.id_product, product.category_number, category_name, "
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

    private Map<String, Object> parseProductSmallDataFromResultSet(ResultSet resultSet)
            throws SQLException {
        Map<String, Object> storeProductData = new HashMap<>();
        storeProductData.put("selling_price", resultSet.getBigDecimal("selling_price"));
        storeProductData.put("products_number", resultSet.getInt("products_number"));
        return storeProductData;
    }
}
