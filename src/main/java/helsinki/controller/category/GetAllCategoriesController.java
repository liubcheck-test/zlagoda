package helsinki.controller.category;

import helsinki.lib.Injector;
import helsinki.model.Category;
import helsinki.service.CategoryService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllCategoriesController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CategoryService categoryService = (CategoryService) injector
            .getInstance(CategoryService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Category> categories = categoryService.getAll();
        req.setAttribute("categories", categories);
        req.getRequestDispatcher("/WEB-INF/views/categories/all.jsp").forward(req, resp);
    }
}
