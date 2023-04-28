package helsinki.controller.check;

import helsinki.lib.Injector;
import helsinki.model.Check;
import helsinki.service.CheckService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllTodayChecksByCashierController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CheckService checkService = (CheckService) injector
            .getInstance(CheckService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cashierId = (String) req.getSession().getAttribute("id_employee");
        List<Check> checks = checkService.getAllByToday(cashierId);
        req.setAttribute("checks", checks);
        req.getRequestDispatcher("/WEB-INF/views/checks/find_all_by_cashier_today.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
