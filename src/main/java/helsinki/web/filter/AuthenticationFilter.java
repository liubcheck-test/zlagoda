package helsinki.web.filter;

import helsinki.lib.Injector;
import helsinki.model.Employee;
import helsinki.service.EmployeeService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthenticationFilter implements Filter {
    private static final String MANAGER_ROLE = "Manager";
    private static final String CASHIER_ROLE = "Cashier";
    private static final Injector injector = Injector.getInstance("helsinki");
    private final EmployeeService employeeService = (EmployeeService) injector
            .getInstance(EmployeeService.class);
    private Set<String> allowedUrls = new HashSet<>();
    private Set<String> managerUrls;
    private Set<String> cashierUrls;
    private Set<String> commonUrls;

    @Override
    public void init(FilterConfig filterConfig) {
        allowedUrls.add("/login");
        allowedUrls.add("/register");
        allowedUrls.add("/index");

        managerUrls = Set.of("/employees/add",
                "/employees/update",
                "/employees/delete",
                "/employees",
                "/employees/cashiers",
                "/employees/find-phone-and-address",
                "/customer_cards/add",
                "/customer_cards/update",
                "/customer_cards/delete",
                "/customer_cards",
                "/customer_cards/find-by-percent",
                "/categories/add",
                "/categories/update",
                "/categories/delete",
                "/categories",
                "/products/add",
                "/products/update",
                "/products/delete",
                "/products",
                "/products/find-by-category",
                "/products/find-by-upc",
                "/store_products/add",
                "/store_products/update",
                "/store_products/delete",
                "/store_products",
                "/checks/delete",
                "/checks/find-all-by-cashier-and-period",
                "/checks/find-all-by-period",
                "/checks/find-total-sold-amount",
                "/checks/find-total-sold-amount-all-cashiers",
                "/checks/find-total-sold-units",
                "/store_products/promotional",
                "/store_products/non-promotional",
                "/report",
                "/report/employees", "/report/categories",
                "/report/products", "/report/store_products",
                "/report/customer_cards", "/report/checks"
        );


        cashierUrls = Set.of(
                "/employees/me",
                "/store_products/find-by-upc-for-cashier",
                "/checks/today",
                "/checks/find-all-mine-by-period",
                "/checks/find-by-check-number"
        );

        commonUrls = Set.of(
                "/products",
                "/customer_cards",
                "/customer_cards/find-by-last-name",
                "/store_products",
                "/store_products/promotional",
                "/store_products/non-promotional",
                "/store_products/find-by-upc",
                "/products/find-by-name",
                "/products/find-by-category",
                "/store_products/find-by-upc-for-cashier",
                "/checks/today",
                "/checks/find-all-mine-by-period",
                "/checks/find-by-check-number"
        );
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String employeeId = (String) session.getAttribute("id_employee");
        Employee employee = employeeId != null ? employeeService.get(employeeId) : null;
        String servletPath = request.getServletPath();

        if (allowedUrls.contains(servletPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (employee == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = employee.getEmployeeRole().getName();

        if (role.equals(MANAGER_ROLE) || commonUrls.contains(servletPath) || managerUrls.contains(servletPath)) {
            filterChain.doFilter(request, response);
        } else if (role.equals(CASHIER_ROLE) && (cashierUrls.contains(servletPath) || commonUrls.contains(servletPath))) {
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
    }
}
