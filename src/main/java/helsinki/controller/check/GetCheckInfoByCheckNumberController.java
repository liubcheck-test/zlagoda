package helsinki.controller.check;

import helsinki.lib.Injector;
import helsinki.model.Check;
import helsinki.service.CheckService;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetCheckInfoByCheckNumberController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CheckService checkService = (CheckService) injector
            .getInstance(CheckService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String checkNumber = req.getParameter("selectedCheckNumber");
        if (checkNumber != null) {
            Check check = checkService.get(checkNumber);
            if (check != null) {
                req.setAttribute("check", check);
            } else {
                req.setAttribute("errorMessage", "No check by number " + check + " found.");
            }
        }
        String cashierId = (String) req.getSession().getAttribute("id_employee");
        List<String> checkNumbers = checkService.getAllChecksByCashier(cashierId).stream()
                .map(Check::getCheckNumber)
                .toList();
        req.setAttribute("checkNumbers", checkNumbers);
        req.getRequestDispatcher("/WEB-INF/views/checks/find_by_check_number.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String checkNumber = req.getParameter("checkNumber");
        req.setAttribute("selectedCheckNumber", checkNumber); // set the selectedUpc attribute here
        Check check = checkService.get(checkNumber);
        if (check == null) {
            req.setAttribute("errorMessage", "No check by number " + checkNumber + " found.");
            req.getRequestDispatcher("/WEB-INF/views/checks/find_by_check_number.jsp").forward(req, resp);
        } else {
            req.setAttribute("check", check);
            String url = String.format("%s?selectedCheckNumber=%s", req.getRequestURI(),
                    URLEncoder.encode(checkNumber, StandardCharsets.UTF_8));
            resp.sendRedirect(url);
        }
    }
}
