package helsinki.dao.impl;

import helsinki.dao.CheckDao;
import helsinki.dao.SaleDao;
import helsinki.dao.StoreProductDao;
import helsinki.exception.DataProcessingException;
import helsinki.lib.Dao;
import helsinki.lib.Inject;
import helsinki.model.Sale;
import helsinki.util.ConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Dao
public class SaleDaoImpl implements SaleDao {
    @Inject
    private StoreProductDao storeProductDao;
    @Inject
    private CheckDao checkDao;

    @Override
    public Sale save(Sale sale) {
        String query = "INSERT INTO Sale (UPC, check_number, "
                + "product_number, selling_price) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement saveSaleStatement = connection.prepareStatement(query)) {
            saveSaleStatement.setString(1, sale.getStoreProduct().getUpc());
            saveSaleStatement.setString(2, sale.getCheck().getCheckNumber());
            saveSaleStatement.setInt(3, sale.getProductNumber());
            saveSaleStatement.setBigDecimal(4, sale.getSellingPrice());
            saveSaleStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create " + sale + ". ", e);
        }
        return sale;
    }

    @Override
    public List<Sale> getSalesByCheckNumber(String checkNumber) {
        String query = "SELECT s.UPC, s.check_number, s.product_number, s.selling_price "
                + "FROM Sale s "
                + "JOIN `Check` c ON s.check_number = c.check_number "
                + "WHERE c.check_number = ?";
        List<Sale> sales = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, checkNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String upc = resultSet.getString("UPC");
                int productNumber = resultSet.getInt("product_number");
                BigDecimal sellingPrice = resultSet.getBigDecimal("selling_price");
                Sale sale = new Sale();
                sale.setStoreProduct(storeProductDao.get(upc).get());
                sale.setCheck(checkDao.get(checkNumber).get());
                sale.setProductNumber(productNumber);
                sale.setSellingPrice(sellingPrice);
                sales.add(sale);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get sales by check number " + checkNumber + ". ", e);
        }
        return sales;
    }

    @Override
    public List<Sale> getSalesByUpcAndDateRange(String upc, LocalDateTime from, LocalDateTime to) {
        String query = "SELECT s.UPC, s.check_number, s.product_number, s.selling_price "
                + "FROM Sale s "
                + "JOIN `Check` c ON s.check_number = c.check_number "
                + "WHERE s.UPC = ? AND c.print_date BETWEEN ? AND ?";
        List<Sale> sales = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, upc);
            statement.setTimestamp(2, Timestamp.valueOf(from));
            statement.setTimestamp(3, Timestamp.valueOf(to));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String checkNumber = resultSet.getString("check_number");
                int productNumber = resultSet.getInt("product_number");
                BigDecimal sellingPrice = resultSet.getBigDecimal("selling_price");
                Sale sale = new Sale();
                sale.setStoreProduct(storeProductDao.get(upc).get());
                sale.setCheck(checkDao.get(checkNumber).get());
                sale.setProductNumber(productNumber);
                sale.setSellingPrice(sellingPrice);
                sales.add(sale);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get sales by UPC " + upc + " and date range. ", e);
        }
        return sales;
    }

    @Override
    public List<Sale> getSalesByCashierAndDateRange(String cashierId, LocalDateTime from, LocalDateTime to) {
        String query = "SELECT s.UPC, s.check_number, s.product_number, s.selling_price "
                + "FROM Sale s "
                + "JOIN `Check` c ON s.check_number = c.check_number "
                + "WHERE c.id_employee = ? AND c.print_date BETWEEN ? AND ?";
        List<Sale> sales = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cashierId);
            statement.setTimestamp(2, Timestamp.valueOf(from));
            statement.setTimestamp(3, Timestamp.valueOf(to));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String upc = resultSet.getString("UPC");
                String checkNumber = resultSet.getString("check_number");
                int productNumber = resultSet.getInt("product_number");
                BigDecimal sellingPrice = resultSet.getBigDecimal("selling_price");
                Sale sale = new Sale();
                sale.setStoreProduct(storeProductDao.get(upc).get());
                sale.setCheck(checkDao.get(checkNumber).get());
                sale.setProductNumber(productNumber);
                sale.setSellingPrice(sellingPrice);
                sales.add(sale);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get sales by cashier " + cashierId + " and date range. ", e);
        }
        return sales;
    }

    @Override
    public List<Sale> getSalesByDateRange(LocalDateTime from, LocalDateTime to) {
        String query = "SELECT s.UPC, s.check_number, s.product_number, s.selling_price "
                + "FROM Sale s "
                + "JOIN `Check` c ON s.check_number = c.check_number "
                + "WHERE c.print_date BETWEEN ? AND ?";
        List<Sale> sales = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(from));
            statement.setTimestamp(2, Timestamp.valueOf(to));
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String upc = resultSet.getString("UPC");
                String checkNumber = resultSet.getString("check_number");
                int productNumber = resultSet.getInt("product_number");
                BigDecimal sellingPrice = resultSet.getBigDecimal("selling_price");
                Sale sale = new Sale();
                sale.setStoreProduct(storeProductDao.get(upc).get());
                sale.setCheck(checkDao.get(checkNumber).get());
                sale.setProductNumber(productNumber);
                sale.setSellingPrice(sellingPrice);
                sales.add(sale);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get sales", e);
        }
        return sales;
    }
}
