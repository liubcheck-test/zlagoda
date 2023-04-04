package liubomyr.stepanenko.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import liubomyr.stepanenko.dao.CheckDao;
import liubomyr.stepanenko.exception.DataProcessingException;
import liubomyr.stepanenko.model.Check;
import liubomyr.stepanenko.model.CustomerCard;
import liubomyr.stepanenko.model.Employee;
import liubomyr.stepanenko.model.composite.Address;
import liubomyr.stepanenko.model.composite.FullName;
import liubomyr.stepanenko.util.ConnectionUtil;

public class CheckDaoImpl implements CheckDao {
    @Override
    public Check save(Check check) {
        String query = "INSERT INTO `Check` (check_number, id_employee, "
                + "card_number, print_date, sum_total, vat) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement saveCheckStatement = connection.prepareStatement(query,
                     Statement.RETURN_GENERATED_KEYS)) {
            saveCheckStatement.setString(1, check.getCheckNumber());
            saveCheckStatement.setString(2, check.getEmployee().getId());
            saveCheckStatement.setString(3, check.getCard().getCardNumber());
            saveCheckStatement.setDate(4, Date.valueOf(check.getPrintDate().toLocalDate()));
            saveCheckStatement.setBigDecimal(5, check.getSumTotal());
            saveCheckStatement.setBigDecimal(6, check.getVAT());
            saveCheckStatement.executeUpdate();
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
            check.setCard(getCardByCheckNumber(check.getCheckNumber()));
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
                check.setCard(getCardByCheckNumber(check.getCheckNumber())));
        return checks;
    }

    @Override
    public Check update(Check check) {
        String query = "UPDATE `Check` "
                + "SET id_employee = ?, card_number = ?, print_date = ?, "
                + "sum_total = ?, vat = ? WHERE check_number = ?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement updateCheckStatement
                     = connection.prepareStatement(query)) {
            updateCheckStatement.setString(1, check.getEmployee().getId());
            updateCheckStatement.setString(2, check.getCard().getCardNumber());
            updateCheckStatement.setDate(3,
                    Date.valueOf(check.getPrintDate().toLocalDate()));
            updateCheckStatement.setBigDecimal(4, check.getSumTotal());
            updateCheckStatement.setBigDecimal(5, check.getVAT());
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

    private Check parseCheckFromResultSet(ResultSet resultSet) throws SQLException {
        Check check = new Check();
        check.setCheckNumber(resultSet.getString("check_number"));
        check.setPrintDate(resultSet.getTimestamp("print_date").toLocalDateTime());
        check.setSumTotal(resultSet.getBigDecimal("sum_total"));
        check.setVAT(resultSet.getBigDecimal("vat"));
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
            throw new DataProcessingException("Couldn't get customer card by check number " + number, e);
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
}
