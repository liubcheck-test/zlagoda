package helsinki.controller.customer;

import helsinki.lib.Injector;
import helsinki.model.CustomerCard;
import helsinki.service.CustomerCardService;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetCustomerCardsByLastNameController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CustomerCardService customerCardService =
            (CustomerCardService) injector.getInstance(CustomerCardService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String lastName = req.getParameter("lastName");
        List<CustomerCard> customerCards = customerCardService.getAllByLastName(lastName);
        req.setAttribute("customerCards", customerCards);
        req.getRequestDispatcher("/WEB-INF/views/customer_cards/find_by_last_name.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
