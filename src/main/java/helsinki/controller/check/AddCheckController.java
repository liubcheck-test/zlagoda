package helsinki.controller.check;

import helsinki.lib.Injector;
import helsinki.model.Check;
import helsinki.model.CustomerCard;
import helsinki.model.Employee;
import helsinki.model.Sale;
import helsinki.model.StoreProduct;
import helsinki.service.CheckService;
import helsinki.service.CustomerCardService;
import helsinki.service.EmployeeService;
import helsinki.service.SaleService;
import helsinki.service.StoreProductService;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCheckController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("helsinki");
    private final StoreProductService storeProductService = (StoreProductService) injector
            .getInstance(StoreProductService.class);
    private final SaleService saleService = (SaleService) injector
            .getInstance(SaleService.class);
    private final EmployeeService employeeService = (EmployeeService) injector
            .getInstance(EmployeeService.class);
    private final CustomerCardService customerCardService =
            (CustomerCardService) injector.getInstance(CustomerCardService.class);
    private final CheckService checkService = (CheckService) injector
            .getInstance(CheckService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        List<StoreProduct> storeProducts = storeProductService.getAllSortedByAmount();
        List<CustomerCard> customerCards = customerCardService.getAllSortedBySurname();
        req.setAttribute("storeProducts", storeProducts);
        req.setAttribute("customerCards", customerCards);
        req.getRequestDispatcher("/WEB-INF/views/checks/add.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        if (!validateAmounts(req)) {
            req.setAttribute("errorMessage", "Insufficient amount of a store product.");
            doGet(req, resp);
            return;
        }

        Check check = createCheck(req);
        checkService.create(check);

        saveSalesAndUpdateStoreProducts(req, check);

        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }

    private boolean validateAmounts(HttpServletRequest req) {
        String[] upcs = req.getParameterValues("upc");
        String[] amounts = req.getParameterValues("amount");
        for (int i = 0; i < upcs.length; i++) {
            String upc = upcs[i];
            int amount = Integer.parseInt(amounts[i]);
            StoreProduct storeProduct = storeProductService.get(upc);
            if (amount > storeProduct.getAmount()) {
                return false;
            }
        }
        return true;
    }

    private Check createCheck(HttpServletRequest req) {
        String id_employee = (String) req.getSession().getAttribute("id_employee");
        String card_number = req.getParameter("card_number");
        LocalDateTime print_date = LocalDateTime.now();
        BigDecimal sum_total = calculateSumTotal(req);
        Employee employee = employeeService.get(id_employee);
        CustomerCard customerCard = customerCardService.get(card_number);

        return new Check(null, employee, customerCard, print_date, sum_total);
    }

    private BigDecimal calculateSumTotal(HttpServletRequest req) {
        String[] amounts = req.getParameterValues("amount");
        String[] upcs = req.getParameterValues("upc");
        BigDecimal sum_total = BigDecimal.ZERO;

        for (int i = 0; i < amounts.length; i++) {
            int amount = Integer.parseInt(amounts[i]);
            String upc = upcs[i];
            StoreProduct storeProduct = storeProductService.get(upc);
            BigDecimal price = storeProduct.getPrice();
            sum_total = sum_total.add(price.multiply(BigDecimal.valueOf(amount)));
        }

        return sum_total;
    }

    private void saveSalesAndUpdateStoreProducts(HttpServletRequest req, Check check) {
        String[] upcs = req.getParameterValues("upc");
        String[] amounts = req.getParameterValues("amount");

        for (int i = 0; i < upcs.length; i++) {
            String upc = upcs[i];
            int amount = Integer.parseInt(amounts[i]);
            StoreProduct storeProduct = storeProductService.get(upc);

            Sale sale = createSale(check, storeProduct, amount);
            saleService.save(sale);

            updateStoreProductAmount(storeProduct, amount);
        }
    }

    private Sale createSale(Check check, StoreProduct storeProduct, int amount) {
        Sale sale = new Sale();
        sale.setStoreProduct(storeProduct);
        sale.setCheck(check);
        sale.setProductNumber(amount);
        sale.setSellingPrice(storeProduct.getPrice());
        return sale;
    }

    private void updateStoreProductAmount(StoreProduct storeProduct, int amount) {
        storeProduct.setAmount(storeProduct.getAmount() - amount);
        storeProductService.update(storeProduct);
    }
}
