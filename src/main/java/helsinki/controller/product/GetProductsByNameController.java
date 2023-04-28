package helsinki.controller.product;

import helsinki.lib.Injector;
import helsinki.model.Category;
import helsinki.model.Product;
import helsinki.service.CategoryService;
import helsinki.service.ProductService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetProductsByNameController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        List<Product> products = productService.getProductsByName(name);
        req.setAttribute("products", products);
        req.getRequestDispatcher("/WEB-INF/views/products/find_by_name.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
