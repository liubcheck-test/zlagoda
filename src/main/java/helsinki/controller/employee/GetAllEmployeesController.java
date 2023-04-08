package helsinki.controller.employee;

import helsinki.lib.Injector;
import helsinki.model.Employee;
import helsinki.service.EmployeeService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllEmployeesController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final EmployeeService employeeService = (EmployeeService) injector
            .getInstance(EmployeeService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<Employee> employees = employeeService.getAll();
        req.setAttribute("employees", employees);
        req.getRequestDispatcher("/WEB-INF/views/employees/all.jsp").forward(req, resp);
    }
}
