package helsinki.controller.store;

import helsinki.lib.Injector;
import helsinki.model.Employee;
import helsinki.model.Sale;
import helsinki.service.EmployeeService;
import helsinki.service.SaleService;
import helsinki.service.StoreProductService;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTotalSoldSumController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final StoreProductService storeProductService =
            (StoreProductService) injector.getInstance(StoreProductService.class);
    private final SaleService saleService = (SaleService) injector
            .getInstance(SaleService.class);
    private final EmployeeService employeeService = (EmployeeService) injector
            .getInstance(EmployeeService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getRequestDispatcher("/WEB-INF/views/store_products/find_total_sold_sum.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(req.getParameter("startDate")
                + " " + req.getParameter("startTime"), formatter);
        LocalDateTime endDate = LocalDateTime.parse(req.getParameter("endDate")
                + " " + req.getParameter("endTime"), formatter);

        List<Sale> sales = saleService.getSalesByDateRange(startDate, endDate);
        BigDecimal totalSum = BigDecimal.valueOf(sales.stream()
                .map(Sale::getSellingPrice)
                .mapToInt(BigDecimal::intValue)
                .sum());

        req.setAttribute("startDate", startDate);
        req.setAttribute("endDate", endDate);
        req.setAttribute("totalSum", totalSum);

        req.getRequestDispatcher("/WEB-INF/views/store_products/find_total_sold_sum.jsp").forward(req, resp);
    }
}
