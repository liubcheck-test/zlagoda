package helsinki.controller.store;

import helsinki.lib.Injector;
import helsinki.model.StoreProduct;
import helsinki.service.StoreProductService;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetStoreProductByUpcForCashierController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final StoreProductService storeProductService = (StoreProductService) injector
            .getInstance(StoreProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String upc = req.getParameter("selectedUpc");
        if (upc != null) {
            Map<String, Object> storeProductData = storeProductService.getStoreProductPriceAndAmountByUpc(upc);
            if (storeProductData != null || storeProductData.isEmpty()) {
                req.setAttribute("product", storeProductData);
            } else {
                req.setAttribute("errorMessage", "No store product by UPC " + upc + " found.");
            }
        }
        List<String> upcs = storeProductService.getAll().stream()
                .map(StoreProduct::getUpc)
                .toList();
        req.setAttribute("upcs", upcs);
        req.getRequestDispatcher("/WEB-INF/views/store_products/find_by_upc.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String upc = req.getParameter("upc");
        req.setAttribute("selectedUpc", upc); // set the selectedUpc attribute here
        Map<String, Object> storeProductData = storeProductService.getStoreProductPriceAndAmountByUpc(upc);
        if (storeProductData == null || storeProductData.isEmpty()) {
            req.setAttribute("errorMessage", "No store products by UPC " + upc + " found.");
            req.getRequestDispatcher("/WEB-INF/views/store_products/find_by_upc.jsp").forward(req, resp);
        } else {
            req.setAttribute("product", storeProductData);
            String url = String.format("%s?selectedUpc=%s", req.getRequestURI(),
                    URLEncoder.encode(upc, StandardCharsets.UTF_8));
            resp.sendRedirect(url);
        }
    }
}
