package helsinki.controller.store;

import helsinki.lib.Injector;
import helsinki.model.ProductStat;
import helsinki.service.StoreProductService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductStatsController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final StoreProductService productService = (StoreProductService)
            injector.getInstance(StoreProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/store_products/product_stats.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        LocalDateTime startDate = LocalDateTime.parse(req.getParameter("start_date"));
        LocalDateTime endDate = LocalDateTime.parse(req.getParameter("end_date"));
        List<ProductStat> productStats = productService.requestProductStat(startDate, endDate);
        req.setAttribute("productStats", productStats);
        req.getRequestDispatcher("/WEB-INF/views/store_products/product_stats.jsp").forward(req, resp);
    }
}
