package helsinki.controller.customer;

import helsinki.util.ConnectionUtil;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class ReportCustomerCardsController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get the employees.jrxml file from the resources folder
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream customerCardsReportStream = classLoader.getResourceAsStream("customer_cards.jrxml");

            // Compile the report to a JasperReport object
            JasperReport jasperReport = JasperCompileManager.compileReport(customerCardsReportStream);

            // Create a map to pass parameters to the report
            Map<String, Object> parameters = new HashMap<String, Object>();

            // Get the database connection
            Connection connection = ConnectionUtil.getConnection();

            // Fill the report using the JasperReport object
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            // Export the report to a PDF file
            ServletOutputStream outputStream = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            // Set the response properties
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=employees_report.pdf");

            // Close the output stream
            outputStream.flush();
            outputStream.close();
        } catch (JRException | IOException e) {
            throw new RuntimeException("Exception occurred", e);
        }
    }
}
