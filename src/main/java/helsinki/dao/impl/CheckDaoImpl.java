package helsinki.dao.impl;

import helsinki.dao.CheckDao;
import helsinki.exception.DataProcessingException;
import helsinki.lib.Dao;
import helsinki.model.Check;
import helsinki.model.CustomerCard;
import helsinki.model.Employee;
import helsinki.model.composite.Address;
import helsinki.model.composite.FullName;
import helsinki.util.ConnectionUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Dao
public class CheckDaoImpl implements CheckDao {
    private final Random random = new Random();

    @Override
    public Check save(Check check) {
        String query = "INSERT INTO `Check` (check_number, id_employee, "
                + "card_number, print_date, sum_total, vat) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement saveCheckStatement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            long checkNumberLong = Math.abs(random.nextLong() % 1000000000L);
            String checkNumber = String.format("%09d", checkNumberLong);
            saveCheckStatement.setString(1, checkNumber);
            saveCheckStatement.setString(2, check.getEmployee().getId());
            saveCheckStatement.setString(3, check.getCustomerCard().getCardNumber());
            saveCheckStatement.setDate(4, Date.valueOf(check.getPrintDate().toLocalDate()));
            saveCheckStatement.setBigDecimal(5, check.getSumTotal());
            saveCheckStatement.setBigDecimal(6, check.getVat());
            saveCheckStatement.executeUpdate();
            check.setCheckNumber(checkNumber);
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't create " + check + ". ", e);
        }
        return check;
    }

    @Override
    public Optional<Check> get(String number) {
        String query = "SELECT * FROM `check` WHERE check_number = ?";
        Check check = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getCheckStatement = connection.prepareStatement(query)) {
            getCheckStatement.setString(1, number);
            ResultSet resultSet = getCheckStatement.executeQuery();
            if (resultSet.next()) {
                check = parseCheckFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get check by number " + number, e);
        }
        if (check != null) {
            check.setEmployee(getEmployeeByCheckNumber(check.getCheckNumber()));
            check.setCustomerCard(getCardByCheckNumber(check.getCheckNumber()));
        }
        return Optional.ofNullable(check);
    }

    @Override
    public List<Check> getAll() {
        String query = "SELECT * FROM `Check`";
        List<Check> checks = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllChecksStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllChecksStatement.executeQuery();
            while (resultSet.next()) {
                checks.add(parseCheckFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of checks "
                    + "from checks database.", e);
        }
        checks.forEach(check ->
                check.setEmployee(getEmployeeByCheckNumber(check.getCheckNumber())));
        checks.forEach(check ->
                check.setCustomerCard(getCardByCheckNumber(check.getCheckNumber())));
        return checks;
    }

    @Override
    public Check update(Check check) {
        String query = "UPDATE `Check` "
                + "SET id_employee = ?, card_number = ?, print_date = ?, "
                + "sum_total = ?, vat = ? WHERE check_number = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateCheckStatement = connection.prepareStatement(query)) {
            updateCheckStatement.setString(1, check.getEmployee().getId());
            updateCheckStatement.setString(2, check.getCustomerCard().getCardNumber());
            updateCheckStatement.setDate(3,
                    Date.valueOf(check.getPrintDate().toLocalDate()));
            updateCheckStatement.setBigDecimal(4, check.getSumTotal());
            updateCheckStatement.setBigDecimal(5, check.getVat());
            updateCheckStatement.setString(6, check.getCheckNumber());
            updateCheckStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update "
                    + check + " in checks database.", e);
        }
        return check;
    }

    @Override
    public boolean delete(String number) {
        String query = "DELETE FROM `Check` WHERE check_number = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteCheckStatement = connection.prepareStatement(query)) {
            deleteCheckStatement.setString(1, number);
            return deleteCheckStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete check with number " + number, e);
        }
    }

    @Override
    public Map<String, Object> getAllInfoByNumber(String number) {
        String query = "SELECT `check`.check_number, id_employee, "
                + "card_number, print_date, sum_total, vat, "
                + "product_name, product_number, s.selling_price FROM `check` "
                + "JOIN sale s on `check`.check_number = s.check_number "
                + "JOIN store_product sp on s.UPC = sp.UPC "
                + "JOIN product p on p.id_product = sp.id_product "
                + "WHERE `check`.check_number = ?";
        Check check = null;
        Map<String, Object> checkAllInfo = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getCheckStatement = connection.prepareStatement(query)) {
            getCheckStatement.setString(1, number);
            ResultSet resultSet = getCheckStatement.executeQuery();
            if (resultSet.next()) {
                check = parseCheckFromResultSet(resultSet);
                checkAllInfo = parseCheckDataFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get check by number " + number, e);
        }
        if (check != null) {
            check.setEmployee(getEmployeeByCheckNumber(check.getCheckNumber()));
            check.setCustomerCard(getCardByCheckNumber(check.getCheckNumber()));
            checkAllInfo.put("check", check);
        }
        return checkAllInfo;
    }

    @Override
    public List<Check> getAllByToday(String employeeId) {
        String query = "SELECT * FROM `Check` WHERE id_employee = ? AND print_date BETWEEN ? AND ?";
        List<Check> checks = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllChecksStatement = connection.prepareStatement(query)) {
            getAllChecksStatement.setString(1, employeeId);
            getAllChecksStatement.setTimestamp(2,
                    Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.MIN)));
            getAllChecksStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ResultSet resultSet = getAllChecksStatement.executeQuery();
            while (resultSet.next()) {
                checks.add(parseCheckFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of checks "
                    + "from checks database.", e);
        }
        checks.forEach(check ->
                check.setEmployee(getEmployeeByCheckNumber(check.getCheckNumber())));
        checks.forEach(check ->
                check.setCustomerCard(getCardByCheckNumber(check.getCheckNumber())));
        return checks;
    }

    @Override
    public List<Check> getAllChecksByCashier(String cashierId) {
        String query = "SELECT * FROM `Check` WHERE id_employee = ?";
        List<Check> checks = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement getAllChecksStatement = connection.prepareStatement(query)) {
            getAllChecksStatement.setString(1, cashierId);
            ResultSet resultSet = getAllChecksStatement.executeQuery();
            while (resultSet.next()) {
                checks.add(parseCheckFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of checks "
                    + "from checks database.", e);
        }
        checks.forEach(check ->
                check.setEmployee(getEmployeeByCheckNumber(check.getCheckNumber())));
        checks.forEach(check ->
                check.setCustomerCard(getCardByCheckNumber(check.getCheckNumber())));
        return checks;
    }

    @Override
    public List<Check> getAllChecksByCashierAndPeriod(String employeeId,
                                                      LocalDateTime from, LocalDateTime to) {
        String query = "SELECT * FROM `Check` "
                + "WHERE id_employee = ? AND print_date BETWEEN ? AND ? "
                + "ORDER BY print_date";
        List<Check> checks = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllChecksStatement = connection.prepareStatement(query)) {
            getAllChecksStatement.setString(1, employeeId);
            getAllChecksStatement.setTimestamp(2, Timestamp.valueOf(from));
            getAllChecksStatement.setTimestamp(3, Timestamp.valueOf(to));
            ResultSet resultSet = getAllChecksStatement.executeQuery();
            while (resultSet.next()) {
                checks.add(parseCheckFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of checks "
                    + "from checks database.", e);
        }
        checks.forEach(check ->
                check.setEmployee(getEmployeeByCheckNumber(check.getCheckNumber())));
        checks.forEach(check ->
                check.setCustomerCard(getCardByCheckNumber(check.getCheckNumber())));
        return checks;
    }

    @Override
    public List<Check> getAllChecksByPeriod(LocalDateTime from, LocalDateTime to) {
        String query = "SELECT * FROM `Check` "
                + "WHERE print_date BETWEEN ? AND ?"
                + "ORDER BY print_date";
        List<Check> checks = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllChecksStatement = connection.prepareStatement(query)) {
            getAllChecksStatement.setTimestamp(1, Timestamp.valueOf(from));
            getAllChecksStatement.setTimestamp(2, Timestamp.valueOf(to));
            ResultSet resultSet = getAllChecksStatement.executeQuery();
            while (resultSet.next()) {
                checks.add(parseCheckFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of checks "
                    + "from checks database.", e);
        }
        checks.forEach(check ->
                check.setEmployee(getEmployeeByCheckNumber(check.getCheckNumber())));
        checks.forEach(check ->
                check.setCustomerCard(getCardByCheckNumber(check.getCheckNumber())));
        return checks;
    }

    @Override
    public BigDecimal getTotalSumByCashierAndPeriod(String employeeId,
                                                    LocalDateTime from, LocalDateTime to) {
        String query = "SELECT SUM(sum_total) AS total_sum FROM `Check` "
                + "WHERE id_employee = ? AND print_date BETWEEN ? AND ?";
        BigDecimal totalSum = BigDecimal.ZERO;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getTotalAmountStatement = connection.prepareStatement(query)) {
            getTotalAmountStatement.setTimestamp(2, Timestamp.valueOf(from));
            getTotalAmountStatement.setTimestamp(3, Timestamp.valueOf(to));
            ResultSet resultSet = getTotalAmountStatement.executeQuery();
            if (resultSet.next()) {
                totalSum = resultSet.getBigDecimal("total_sum");
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a total "
                    + "products sum from checks database.", e);
        }
        return totalSum;
    }

    @Override
    public BigDecimal getTotalSumByPeriod(LocalDateTime from, LocalDateTime to) {
        String query = "SELECT SUM(sum_total) AS total_sum FROM `Check` "
                + "WHERE print_date BETWEEN ? AND ?";
        BigDecimal totalSum = BigDecimal.ZERO;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getTotalAmountStatement = connection.prepareStatement(query)) {
            getTotalAmountStatement.setTimestamp(2, Timestamp.valueOf(from));
            getTotalAmountStatement.setTimestamp(3, Timestamp.valueOf(to));
            ResultSet resultSet = getTotalAmountStatement.executeQuery();
            if (resultSet.next()) {
                totalSum = BigDecimal.valueOf(resultSet.getInt("total_sum"));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a total "
                    + "products sum from checks database.", e);
        }
        return totalSum;
    }

    @Override
    public Integer getTotalAmountByProductAndPeriod(String productId,
                                                    LocalDateTime from, LocalDateTime to) {
        String query = "SELECT COUNT(products_number) FROM `check` "
                + "JOIN sale s on `check`.check_number = s.check_number "
                + "JOIN store_product sp on sp.UPC = s.UPC "
                + "WHERE id_product = ? AND print_date BETWEEN ? AND ?";
        int totalAmount = 0;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getTotalAmountStatement = connection.prepareStatement(query)) {
            getTotalAmountStatement.setString(1, productId);
            getTotalAmountStatement.setTimestamp(2, Timestamp.valueOf(from));
            getTotalAmountStatement.setTimestamp(3, Timestamp.valueOf(to));
            ResultSet resultSet = getTotalAmountStatement.executeQuery();
            if (resultSet.next()) {
                totalAmount = resultSet.getInt("total_amount");
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of store products "
                    + "from store products database.", e);
        }
        return totalAmount;
    }

    private Check parseCheckFromResultSet(ResultSet resultSet) throws SQLException {
        Check check = new Check();
        check.setCheckNumber(resultSet.getString("check_number"));
        check.setPrintDate(resultSet.getTimestamp("print_date").toLocalDateTime());
        check.setSumTotal(resultSet.getBigDecimal("sum_total"));
        check.setVat(resultSet.getBigDecimal("vat"));
        return check;
    }

    private Employee getEmployeeByCheckNumber(String number) {
        String query = "SELECT * FROM employee "
                + "JOIN `check` on employee.id_employee = `check`.id_employee "
                + "WHERE `check`.check_number = ?";
        Employee employee = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getEmployeeStatement = connection.prepareStatement(query)) {
            getEmployeeStatement.setString(1, number);
            ResultSet resultSet = getEmployeeStatement.executeQuery();
            if (resultSet.next()) {
                employee = parseEmployeeFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get employee by check number " + number, e);
        }
        return employee;
    }

    private Employee parseEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getString("id_employee"));
        employee.setEmployeeFullName(new FullName(
                resultSet.getString("empl_surname"),
                resultSet.getString("empl_name"),
                resultSet.getString("empl_patronymic")
        ));
        employee.setEmployeeRole(Employee.Role.valueOf(
                resultSet.getString("empl_role")));
        employee.setSalary(resultSet.getBigDecimal("salary"));
        employee.setDateOfBirth(resultSet.getDate("date_of_birth").toLocalDate());
        employee.setDateOfStart(resultSet.getDate("date_of_start").toLocalDate());
        employee.setPhoneNumber(resultSet.getString("phone_number"));
        employee.setAddress(new Address(
                resultSet.getString("city"),
                resultSet.getString("street"),
                resultSet.getString("zip_code")
        ));
        employee.setEmail(resultSet.getString("email"));
        employee.setPassword(resultSet.getString("password"));
        return employee;
    }

    private CustomerCard getCardByCheckNumber(String number) {
        String query = "SELECT * FROM customer_card "
                + "JOIN `check` on customer_card.card_number = `check`.card_number "
                + "WHERE `check`.check_number = ?";
        CustomerCard customerCard = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getCardStatement = connection.prepareStatement(query)) {
            getCardStatement.setString(1, number);
            ResultSet resultSet = getCardStatement.executeQuery();
            if (resultSet.next()) {
                customerCard = parseCustomerCardFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get customer card "
                    + "by check number " + number, e);
        }
        return customerCard;
    }

    private CustomerCard parseCustomerCardFromResultSet(ResultSet resultSet) throws SQLException {
        CustomerCard customerCard = new CustomerCard();
        customerCard.setCardNumber(resultSet.getString("card_number"));
        customerCard.setCustomerFullName(new FullName(
                resultSet.getString("cust_surname"),
                resultSet.getString("cust_name"),
                resultSet.getString("cust_patronymic")
        ));
        customerCard.setPhoneNumber(resultSet.getString("phone_number"));
        customerCard.setCustomerAddress(new Address(
                resultSet.getString("city"),
                resultSet.getString("street"),
                resultSet.getString("zip_code")
        ));
        customerCard.setPercent(resultSet.getInt("percent"));
        return customerCard;
    }

    private Map<String, Object> parseCheckDataFromResultSet(ResultSet resultSet)
            throws SQLException {
        Map<String, Object> checkData = new HashMap<>();
        checkData.put("product_name", resultSet.getString("product_name"));
        checkData.put("product_number", resultSet.getString("product_number"));
        checkData.put("selling_price", resultSet.getString("selling_price"));
        return checkData;
    }
}
