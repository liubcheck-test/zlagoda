package helsinki.controller.store;

import helsinki.lib.Injector;
import helsinki.service.StoreProductService;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteStoreProductController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final StoreProductService storeProductService = (StoreProductService) injector
            .getInstance(StoreProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        storeProductService.delete(req.getParameter("upc"));
        resp.sendRedirect(req.getContextPath() + "/store_products");
    }
}
