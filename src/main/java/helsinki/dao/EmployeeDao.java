package helsinki.dao;

import helsinki.model.Employee;
import java.util.List;
import java.util.Map;

public interface EmployeeDao extends BasicDao<Employee, String> {
    List<Employee> getAllSortedBySurname();

    List<Employee> getAllCashiersSortedBySurname();

    Map<String, String> getPhoneAndAddressBySurname(String surname);
}
