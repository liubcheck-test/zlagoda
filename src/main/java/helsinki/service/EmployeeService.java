package helsinki.service;

import helsinki.model.Employee;
import java.util.Optional;

public interface EmployeeService extends BasicService<Employee, String> {
    Optional<Employee> findByEmail(String email);
}
