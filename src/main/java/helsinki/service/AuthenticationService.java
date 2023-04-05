package helsinki.service;

import helsinki.exception.AuthenticationException;
import helsinki.model.Employee;

public interface AuthenticationService {
    Employee login(String login, String password) throws AuthenticationException;
}
