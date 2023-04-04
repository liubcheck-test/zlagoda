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
import java.util.UUID;
import liubomyr.stepanenko.dao.EmployeeDao;
import liubomyr.stepanenko.exception.DataProcessingException;
import liubomyr.stepanenko.model.Employee;
import liubomyr.stepanenko.model.composite.Address;
import liubomyr.stepanenko.model.composite.FullName;
import liubomyr.stepanenko.util.ConnectionUtil;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public Employee save(Employee employee) {
        String query = "INSERT INTO Employee (id_employee, empl_surname, empl_name, "
                + "empl_patronymic, empl_role, salary, date_of_birth, date_of_start, "
                + "phone_number, city, street, zip_code)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement saveEmployeeStatement =
                        connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            saveEmployeeStatement.setString(1, UUID.randomUUID().toString());
            saveEmployeeStatement.setString(2, employee.getEmployeeFullName().getSurname());
            saveEmployeeStatement.setString(3, employee.getEmployeeFullName().getName());
            saveEmployeeStatement.setString(4, employee.getEmployeeFullName().getPatronymic());
            saveEmployeeStatement.setString(5, employee.getEmployeeRole().name());
            saveEmployeeStatement.setBigDecimal(6, employee.getSalary());
            saveEmployeeStatement.setDate(7, Date.valueOf(employee.getDateOfBirth()));
            saveEmployeeStatement.setDate(8, Date.valueOf(employee.getDateOfBirth()));
            saveEmployeeStatement.setString(9, employee.getPhoneNumber());
            saveEmployeeStatement.setString(10, employee.getAddress().getCity());
            saveEmployeeStatement.setString(11, employee.getAddress().getStreet());
            saveEmployeeStatement.setString(12, employee.getAddress().getZipCode());
            saveEmployeeStatement.executeUpdate();
            ResultSet resultSet = saveEmployeeStatement.getGeneratedKeys();
            if (resultSet.next()) {
                employee.setId(resultSet.getObject(1, String.class));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't save employee " + employee, e);
        }
        return employee;
    }

    @Override
    public Optional<Employee> get(String id) {
        String query = "SELECT * FROM Employee WHERE id_employee = ?";
        Employee employee = null;
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getEmployeeStatement = connection.prepareStatement(query)) {
            getEmployeeStatement.setString(1, id);
            ResultSet resultSet = getEmployeeStatement.executeQuery();
            if (resultSet.next()) {
                employee = parseEmployeeFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get employee by id " + id, e);
        }
        return Optional.ofNullable(employee);
    }

    @Override
    public List<Employee> getAll() {
        String query = "SELECT * FROM Employee";
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement getAllEmployeesStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = getAllEmployeesStatement.executeQuery();
            while (resultSet.next()) {
                employees.add(parseEmployeeFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't get a list of employees "
                    + "from employees database", e);
        }
        return employees;
    }

    @Override
    public Employee update(Employee employee) {
        String query = "UPDATE Employee "
                + "SET empl_surname = ?, empl_name = ?, empl_patronymic = ?, "
                + "empl_role = ?, salary = ?, date_of_birth = ?, date_of_start = ?, "
                + "phone_number = ?, city = ?, street = ?, zip_code = ? "
                + "WHERE id_employee = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateEmployeeStatement = connection.prepareStatement(query)) {
            updateEmployeeStatement.setString(1,
                    employee.getEmployeeFullName().getSurname());
            updateEmployeeStatement.setString(2,
                    employee.getEmployeeFullName().getName());
            updateEmployeeStatement.setString(3,
                    employee.getEmployeeFullName().getPatronymic());
            updateEmployeeStatement.setString(4, employee.getEmployeeRole().name());
            updateEmployeeStatement.setBigDecimal(5, employee.getSalary());
            updateEmployeeStatement.setDate(6, Date.valueOf(employee.getDateOfBirth()));
            updateEmployeeStatement.setDate(7, Date.valueOf(employee.getDateOfStart()));
            updateEmployeeStatement.setString(8, employee.getPhoneNumber());
            updateEmployeeStatement.setString(9, employee.getAddress().getCity());
            updateEmployeeStatement.setString(10, employee.getAddress().getStreet());
            updateEmployeeStatement.setString(11, employee.getAddress().getZipCode());
            updateEmployeeStatement.setString(12, employee.getId());
            updateEmployeeStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't update "
                    + employee + " in employees database.", e);
        }
        return employee;
    }

    @Override
    public boolean delete(String id) {
        String query = "DELETE FROM Employee WHERE id_employee = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement deleteEmployeeStatement = connection.prepareStatement(query)) {
            deleteEmployeeStatement.setString(1, id);
            return deleteEmployeeStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Couldn't delete employee with id " + id, e);
        }
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
}
