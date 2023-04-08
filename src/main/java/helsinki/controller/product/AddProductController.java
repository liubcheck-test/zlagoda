package helsinki.controller.product;

import helsinki.lib.Injector;
import helsinki.model.Category;
import helsinki.service.CategoryService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CategoryService categoryService = (CategoryService) injector
            .getInstance(CategoryService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/categories/add.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String categoryName = req.getParameter("category_name");
        Category category = new Category(null, categoryName);
        categoryService.create(category);
        resp.sendRedirect(req.getContextPath() + "/categories");
    }
}
