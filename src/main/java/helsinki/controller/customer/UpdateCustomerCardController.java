package helsinki.controller.customer;

import helsinki.lib.Injector;
import helsinki.model.CustomerCard;
import helsinki.model.composite.Address;
import helsinki.model.composite.FullName;
import helsinki.service.CustomerCardService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateCustomerCardController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CustomerCardService customerCardService = (CustomerCardService) injector
            .getInstance(CustomerCardService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String cardNumber = req.getParameter("cardNumber");
        CustomerCard customerCard = customerCardService.get(cardNumber);
        req.setAttribute("customerCard", customerCard);
        req.getRequestDispatcher("/WEB-INF/views/customer_cards/update.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        String cardNumber = req.getParameter("card_number");
        FullName fullName = new FullName(
                req.getParameter("cust_surname"),
                req.getParameter("cust_name"),
                req.getParameter("cust_patronymic")
        );
        String phoneNumber = req.getParameter("phone_number");
        Address address = new Address(
                req.getParameter("city"),
                req.getParameter("street"),
                req.getParameter("zip_code")
        );
        Integer percent = Integer.parseInt(req.getParameter("percent"));
        CustomerCard customerCard = new CustomerCard(cardNumber, fullName,
                phoneNumber, address, percent);
        customerCardService.update(customerCard);
        resp.sendRedirect(req.getContextPath() + "/customer_cards");
    }
}
