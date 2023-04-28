package helsinki.controller.store;

import helsinki.lib.Injector;
import helsinki.model.StoreProduct;
import helsinki.service.StoreProductService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTotalAmountAndSumByPeriodController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final StoreProductService storeProductService = (StoreProductService) injector
            .getInstance(StoreProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/store_products/total_amount_and_sum_by_period.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        LocalDateTime startDate = LocalDateTime.parse(req.getParameter("start_date"));
        LocalDateTime endDate = LocalDateTime.parse(req.getParameter("end_date"));

        List<StoreProduct> productDetailsList = storeProductService.requestProduct(startDate, endDate);

        req.setAttribute("productDetailsList", productDetailsList);
        req.setAttribute("start_date", startDate);
        req.setAttribute("end_date", endDate);

        req.getRequestDispatcher("/WEB-INF/views/store_products/total_amount_and_sum_by_period.jsp").forward(req, resp);
    }
}
