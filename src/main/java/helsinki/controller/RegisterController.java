package helsinki.controller;

import helsinki.lib.Injector;
import helsinki.model.Employee;
import helsinki.model.composite.Address;
import helsinki.model.composite.FullName;
import helsinki.service.AuthenticationService;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegisterController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.setAttribute("employeeRoles", Employee.Role.values());
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        FullName fullName = new FullName(
                req.getParameter("empl_surname"),
                req.getParameter("empl_name"),
                req.getParameter("empl_patronymic")
        );
        Employee.Role role = Employee.Role.valueOf(req.getParameter("empl_role"));
        BigDecimal salary = BigDecimal.valueOf(Long.parseLong(req.getParameter("salary")));
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
        Employee employee = new Employee(null, fullName, role, salary,
                dateOfBirth, dateOfStart, phoneNumber, address, email, password);
        authenticationService.register(employee);
        HttpSession session = req.getSession();
        session.setAttribute("chosenEmployee", employee);
        session.setAttribute("id_employee", employee.getId());
        resp.sendRedirect(req.getContextPath() + "/index");
    }
}
