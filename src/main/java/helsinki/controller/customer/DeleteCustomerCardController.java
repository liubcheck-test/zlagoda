package helsinki.controller.customer;

import helsinki.lib.Injector;
import helsinki.service.CustomerCardService;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteCustomerCardController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CustomerCardService customerCardService = (CustomerCardService) injector
            .getInstance(CustomerCardService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        customerCardService.delete(req.getParameter("cardNumber"));
        resp.sendRedirect(req.getContextPath() + "/customer_cards");
    }
}
