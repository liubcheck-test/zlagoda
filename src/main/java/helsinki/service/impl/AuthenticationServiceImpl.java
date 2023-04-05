package helsinki.service.impl;

import helsinki.exception.AuthenticationException;
import helsinki.lib.Inject;
import helsinki.lib.Service;
import helsinki.model.Employee;
import helsinki.service.AuthenticationService;
import helsinki.service.EmployeeService;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private EmployeeService employeeService;

    @Override
    public Employee login(String login, String password) throws AuthenticationException {
        Optional<Employee> employee = employeeService.findByEmail(login);
        if (employee.isEmpty() || !employee.get().getPassword().equals(password)) {
            throw new AuthenticationException("Invalid login or password");
        }
        return employee.get();
    }
}
