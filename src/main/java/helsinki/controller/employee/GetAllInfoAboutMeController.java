package helsinki.controller.employee;

import helsinki.lib.Injector;
import helsinki.model.Employee;
import helsinki.service.EmployeeService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllInfoAboutMeController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final EmployeeService employeeService = (EmployeeService) injector
            .getInstance(EmployeeService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String currentCashierId = (String) req.getSession().getAttribute("id_employee");
        Employee currentCashier = employeeService.get(currentCashierId);
        req.setAttribute("currentCashier", currentCashier);
        req.getRequestDispatcher("/WEB-INF/views/employees/me.jsp").forward(req, resp);
    }
}
