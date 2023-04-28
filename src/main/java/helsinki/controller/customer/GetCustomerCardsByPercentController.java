package helsinki.controller.customer;

import helsinki.lib.Injector;
import helsinki.model.CustomerCard;
import helsinki.service.CustomerCardService;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetCustomerCardsByPercentController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final CustomerCardService customerCardService = (CustomerCardService) injector
            .getInstance(CustomerCardService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String percentValue = req.getParameter("percent");
        if (percentValue != null) {
            int percent = Integer.parseInt(percentValue);
            List<CustomerCard> customers = customerCardService
                    .getAllByPercentSortedBySurname(percent);
            if (!customers.isEmpty()) {
                req.setAttribute("customers", customers);
            } else {
                req.setAttribute("errorMessage", "No customers with " + percent + "% found.");
            }
        }
        List<Integer> percentages = customerCardService.getAll().stream()
                .map(CustomerCard::getPercent)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        req.setAttribute("percentages", percentages);
        req.getRequestDispatcher("/WEB-INF/views/customer_cards/find_by_percent.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int percent = Integer.parseInt(req.getParameter("percent"));
        List<CustomerCard> customers = customerCardService.getAllByPercentSortedBySurname(percent)
                .stream()
                .sorted((c1, c2) -> c1.getCustomerFullName().getSurname()
                        .compareToIgnoreCase(c2.getCustomerFullName().getSurname()))
                .collect(Collectors.toList());
        if (customers.isEmpty()) {
            req.setAttribute("errorMessage",
                    "No customers found with customer card percentage " + percent);
            req.getRequestDispatcher("/WEB-INF/views/customer_cards/find_by_percent.jsp").forward(req, resp);
        } else {
            req.setAttribute("customers", customers);
            String url = String.format("%s?percent=%s", req.getRequestURI(),
                    URLEncoder.encode(String.valueOf(percent), StandardCharsets.UTF_8));
            resp.sendRedirect(url);
        }
    }
}
