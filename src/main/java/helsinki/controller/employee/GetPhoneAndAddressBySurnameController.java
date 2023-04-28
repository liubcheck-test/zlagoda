package helsinki.controller.employee;

import helsinki.lib.Injector;
import helsinki.model.Employee;
import helsinki.service.EmployeeService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetPhoneAndAddressBySurnameController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final EmployeeService employeeService = (EmployeeService) injector
            .getInstance(EmployeeService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String surname = req.getParameter("surname");
        if (surname != null) {
            Map<String, String> employeeData = employeeService.getPhoneAndAddressBySurname(surname);
            if (employeeData != null) {
                req.setAttribute("employeeData", employeeData);
            } else {
                req.setAttribute("errorMessage", "No employee with surname " + surname + " found.");
            }
        }
        List<String> surnames = employeeService.getAllSortedBySurname().stream()
                .map(employee -> employee.getEmployeeFullName().getSurname())
                .collect(Collectors.toList());
        req.setAttribute("surnames", surnames);
        req.getRequestDispatcher("/WEB-INF/views/employees/find_phone_and_address.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String surname = req.getParameter("surname");
        Map<String, String> employeeData = employeeService.getPhoneAndAddressBySurname(surname);
        if (employeeData == null || employeeData.isEmpty()) {
            req.setAttribute("errorMessage", "No employee with surname " + surname + " found.");
            req.getRequestDispatcher("/WEB-INF/views/employees/find_phone_and_address.jsp").forward(req, resp);
        } else {
            req.setAttribute("employeeData", employeeData);
            String url = String.format("%s?surname=%s", req.getRequestURI(),
                    URLEncoder.encode(surname, StandardCharsets.UTF_8));
            resp.sendRedirect(url);
        }
    }
}
