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

public class AddStoreProductController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final StoreProductService storeProductService = (StoreProductService) injector
            .getInstance(StoreProductService.class);
    private final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<Product> products = productService.getAll();
        req.setAttribute("products", products);
        req.getRequestDispatcher("/WEB-INF/views/store_products/add.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        Product product = productService.get(Long.parseLong(req.getParameter("id_product")));
        BigDecimal sellingPrice = BigDecimal.valueOf(Long
                .parseLong(req.getParameter("selling_price")));
        Integer amount = Integer.parseInt(req.getParameter("products_number"));
        Boolean isPromotional = Boolean.parseBoolean(req.getParameter("promotional_product"));
        StoreProduct storeProduct = new StoreProduct(null, null, product,
                sellingPrice, amount, isPromotional);
        storeProductService.create(storeProduct);
        resp.sendRedirect(req.getContextPath() + "/store_products");
    }
}
