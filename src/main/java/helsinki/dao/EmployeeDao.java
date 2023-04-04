package helsinki.dao;

import java.util.List;
import java.util.Optional;
import helsinki.model.Employee;

public interface EmployeeDao {
    Employee save(Employee employee);

    Optional<Employee> get(String id);

    List<Employee> getAll();

    Employee update(Employee employee);

    boolean delete(String id);
}
