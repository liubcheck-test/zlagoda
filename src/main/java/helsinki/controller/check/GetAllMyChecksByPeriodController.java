package helsinki.controller.check;

import helsinki.lib.Injector;
import helsinki.model.Check;
import helsinki.model.Employee;
import helsinki.service.CheckService;
import helsinki.service.EmployeeService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllMyChecksByPeriodController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CheckService checkService = (CheckService) injector
            .getInstance(CheckService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/checks/find_all_mine_by_period.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String cashierId = (String) req.getSession().getAttribute("id_employee");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(req.getParameter("startDate")
                + " " + req.getParameter("startTime"), formatter);
        LocalDateTime endDate = LocalDateTime.parse(req.getParameter("endDate")
                + " " + req.getParameter("endTime"), formatter);

        List<Check> checks = checkService.getAllChecksByCashierAndPeriod(cashierId, startDate, endDate);

        req.setAttribute("checks", checks);
        req.setAttribute("cashier", cashierId);
        req.setAttribute("chosenCashierId", cashierId);
        req.setAttribute("startDate", startDate);
        req.setAttribute("endDate", endDate);

        req.getRequestDispatcher("/WEB-INF/views/checks/find_all_mine_by_period.jsp").forward(req, resp);
    }
}
