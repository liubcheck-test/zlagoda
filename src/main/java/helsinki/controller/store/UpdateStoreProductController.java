package helsinki.controller.store;

import helsinki.lib.Injector;
import helsinki.model.Product;
import helsinki.model.StoreProduct;
import helsinki.service.ProductService;
import helsinki.service.StoreProductService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateStoreProductController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final StoreProductService storeProductService = (StoreProductService) injector
            .getInstance(StoreProductService.class);
    private final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String upc = req.getParameter("upc");
        StoreProduct storeProduct = storeProductService.get(upc);
        req.setAttribute("storeProduct", storeProduct);
        List<Product> products = productService.getAll();
        req.setAttribute("products", products);
        req.getRequestDispatcher("/WEB-INF/views/store_products/update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        String upc = req.getParameter("UPC");
        String upcProm = req.getParameter("UPC_prom");
        Product product = productService.get(Long.parseLong(req.getParameter("id_product")));
        BigDecimal sellingPrice = BigDecimal.valueOf(Double
                .parseDouble(req.getParameter("selling_price")));
        Integer amount = Integer.parseInt(req.getParameter("products_number"));
        Boolean isPromotional = Boolean.parseBoolean(req.getParameter("promotional_product"));
        StoreProduct storeProduct = new StoreProduct(upc, upcProm, product,
                sellingPrice, amount, isPromotional);
        storeProductService.update(storeProduct);
        resp.sendRedirect(req.getContextPath() + "/store_products");
    }
}
