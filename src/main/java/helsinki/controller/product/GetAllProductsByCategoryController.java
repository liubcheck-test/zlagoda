package helsinki.controller.product;

import helsinki.lib.Injector;
import helsinki.model.Category;
import helsinki.model.Product;
import helsinki.service.CategoryService;
import helsinki.service.ProductService;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllProductsByCategoryController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);
    private final CategoryService categoryService = (CategoryService) injector
            .getInstance(CategoryService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String categoryName = req.getParameter("category");
        if (categoryName != null) {
            List<Product> products = productService.getAllByCategorySortedByName(categoryName);
            if (products != null || products.isEmpty()) {
                req.setAttribute("products", products);
            } else {
                req.setAttribute("errorMessage", "No products with category " + categoryName + " found.");
            }
        }
        List<String> categories = categoryService.getAllSortedByName().stream()
                .map(Category::getCategoryName)
                .toList();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/products/find_by_category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String categoryName = req.getParameter("category");
        List<Product> products = productService.getAllByCategorySortedByName(categoryName);
        if (products == null || products.isEmpty()) {
            req.setAttribute("errorMessage", "No products with category " + categoryName + " found.");
            req.getRequestDispatcher("/WEB-INF/views/products/find_by_category.jsp").forward(req, resp);
        } else {
            req.setAttribute("products", products);
            String url = String.format("%s?category=%s", req.getRequestURI(),
                    URLEncoder.encode(categoryName, StandardCharsets.UTF_8));
            resp.sendRedirect(url);
        }
    }
}
