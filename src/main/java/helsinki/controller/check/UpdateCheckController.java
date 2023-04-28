package helsinki.controller.check;

import helsinki.lib.Injector;
import helsinki.model.Check;
import helsinki.model.CustomerCard;
import helsinki.model.Employee;
import helsinki.service.CheckService;
import helsinki.service.CustomerCardService;
import helsinki.service.EmployeeService;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateCheckController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CheckService checkService = (CheckService) injector
            .getInstance(CheckService.class);
    private final EmployeeService employeeService = (EmployeeService) injector
            .getInstance(EmployeeService.class);
    private final CustomerCardService cardService = (CustomerCardService) injector
            .getInstance(CustomerCardService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String checkNumber = req.getParameter("checkNumber");
        Check check = checkService.get(checkNumber);
        List<Employee> employees = employeeService.getAll();
        List<CustomerCard> cards = cardService.getAll();
        req.setAttribute("check", check);
        req.setAttribute("employees", employees);
        req.setAttribute("cards", cards);
        req.getRequestDispatcher("/WEB-INF/views/checks/update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        String checkNumber = req.getParameter("check_number");
        Employee employee = employeeService.get(req.getParameter("id_employee"));
        CustomerCard card = cardService.get(req.getParameter("card_number"));
        LocalDateTime printDate = LocalDateTime.parse(req.getParameter("print_date"));
        BigDecimal sumTotal = BigDecimal.valueOf(Long.parseLong(req.getParameter("sum_total")));
        Check check = new Check(checkNumber, employee, card, printDate, sumTotal);
        checkService.update(check);
        resp.sendRedirect(req.getContextPath() + "/checks");
    }
}
