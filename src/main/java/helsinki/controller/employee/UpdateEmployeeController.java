package helsinki.controller.employee;

import helsinki.exception.IncorrectPasswordException;
import helsinki.lib.Injector;
import helsinki.model.Employee;
import helsinki.model.composite.Address;
import helsinki.model.composite.FullName;
import helsinki.service.EmployeeService;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateEmployeeController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final EmployeeService employeeService = (EmployeeService) injector
            .getInstance(EmployeeService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String employeeId = req.getParameter("id");
        Employee employee = employeeService.get(employeeId);
        req.setAttribute("employee", employee);
        req.setAttribute("employeeRoles", Employee.Role.values());
        req.getRequestDispatcher("/WEB-INF/views/employees/update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        FullName fullName = new FullName(
                req.getParameter("empl_surname"),
                req.getParameter("empl_name"),
                req.getParameter("empl_patronymic")
        );
        Employee.Role role = Employee.Role.valueOf(req.getParameter("empl_role"));
        BigDecimal salary = new BigDecimal(req.getParameter("salary"));
        LocalDate dateOfBirth = LocalDate.parse(req.getParameter("date_of_birth"));
        LocalDate dateOfStart = LocalDate.parse(req.getParameter("date_of_start"));
        String phoneNumber = req.getParameter("phone_number");
        Address address = new Address(
                req.getParameter("city"),
                req.getParameter("street"),
                req.getParameter("zip_code")
        );
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        Employee employee = new Employee(id, fullName, role, salary,
                dateOfBirth, dateOfStart, phoneNumber, address, email, password);
        try {
            employeeService.update(employee);
            resp.sendRedirect(req.getContextPath() + "/employees");
        } catch (IncorrectPasswordException e) {
            req.setAttribute("employee", employee);
            req.setAttribute("employeeRoles", Employee.Role.values());
            req.setAttribute("message", "Incorrect password, please try again.");
            req.getRequestDispatcher("/WEB-INF/views/employees/update.jsp").forward(req, resp);
        }
    }
}
