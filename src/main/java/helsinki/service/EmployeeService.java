package helsinki.service;

import helsinki.model.Employee;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeeService extends BasicService<Employee, String> {
    List<Employee> getAllSortedBySurname();

    List<Employee> getAllCashiersSortedBySurname();

    Map<String, String> getPhoneAndAddressBySurname(String surname);

    Optional<Employee> findByEmail(String email);
}
