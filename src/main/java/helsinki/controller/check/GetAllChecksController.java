package helsinki.controller.check;

import helsinki.lib.Injector;
import helsinki.service.CategoryService;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetAllChecksController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CategoryService categoryService = (CategoryService) injector
            .getInstance(CategoryService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        categoryService.delete(Integer.parseInt(req.getParameter("categoryNumber")));
        resp.sendRedirect(req.getContextPath() + "/categories");
    }
}
