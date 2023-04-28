package helsinki.controller.product;

import helsinki.lib.Injector;
import helsinki.model.Category;
import helsinki.model.Product;
import helsinki.service.CategoryService;
import helsinki.service.ProductService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateProductController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);
    private final CategoryService categoryService = (CategoryService) injector
            .getInstance(CategoryService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(req.getParameter("id"));
        Product product = productService.get(id);
        List<Category> categories = categoryService.getAll();
        req.setAttribute("product", product);
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/products/update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(req.getParameter("id"));
        Category category = categoryService.get(Integer
                .parseInt(req.getParameter("category_number").substring(0, 1)));
        String name = req.getParameter("product_name");
        String characteristics = req.getParameter("characteristics");
        Product product = new Product(id, category, name, characteristics);
        productService.update(product);
        resp.sendRedirect(req.getContextPath() + "/products");
    }
}
