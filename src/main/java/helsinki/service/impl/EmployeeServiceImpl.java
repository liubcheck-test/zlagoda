package helsinki.service.impl;

import helsinki.lib.Service;
import helsinki.model.Employee;
import helsinki.service.EmployeeService;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public Employee create(Employee employee) {
        return null;
    }

    @Override
    public Employee get(String s) {
        return null;
    }

    @Override
    public List<Employee> getAll() {
        return null;
    }

    @Override
    public Employee update(Employee employee) {
        return null;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return Optional.empty();
    }
}
