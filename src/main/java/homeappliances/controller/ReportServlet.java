package homeappliances.controller;

import homeappliances.model.Orders;
import homeappliances.model.Payment;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/admin/ReportServlet")
public class ReportServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ReportServlet.class.getName());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @PersistenceContext(unitName = "JavaAssignmentPU") 
    private EntityManager em;
    
    @Resource 
    private UserTransaction utx;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            sendError(request, response, "No action specified");
            return;
        }

        try {
            switch (action) {
                case "daily":
                    handleDailyReport(request);
                    break;
                case "monthly":
                    handleMonthlyReport(request);
                    break;
                case "yearly":
                    handleYearlyReport(request);
                    break;
                case "topProducts":
                    handleTopProductsReport(request);
                    break;
                default:
                    sendError(request, response, "Invalid action specified");
                    return;
            }

            request.getRequestDispatcher("/admin/viewReport.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/admin/viewReport.jsp").forward(request, response);
        }
    }

    private void handleDailyReport(HttpServletRequest request) throws ParseException {
        String dateStr = request.getParameter("date");
        if (dateStr == null || dateStr.isEmpty()) {
            throw new IllegalArgumentException("Date parameter is required for daily report");
        }

        Date date = DATE_FORMAT.parse(dateStr);

        BigDecimal dailyRevenue = em.createQuery(
            "SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE FUNCTION('DATE', p.paymentTime) = :date AND p.paymentStatus = :status", 
            BigDecimal.class)
            .setParameter("date", date)
            .setParameter("status", Payment.PAID)
            .getSingleResult();

        request.setAttribute("revenue", dailyRevenue);
        request.setAttribute("reportType", "Daily Sales Report for " + dateStr);
        request.setAttribute("date", dateStr);
    }

    private void handleMonthlyReport(HttpServletRequest request) {
        String monthStr = request.getParameter("month");
        String yearStr = request.getParameter("year");
        
        if (monthStr == null || monthStr.isEmpty() || yearStr == null || yearStr.isEmpty()) {
            throw new IllegalArgumentException("Month and year parameters are required for monthly report");
        }

        int month = Integer.parseInt(monthStr);
        int year = Integer.parseInt(yearStr);

        BigDecimal monthlyRevenue = em.createQuery(
            "SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE FUNCTION('MONTH', p.paymentTime) = :month AND FUNCTION('YEAR', p.paymentTime) = :year AND p.paymentStatus = :status", 
            BigDecimal.class)
            .setParameter("month", month)
            .setParameter("year", year)
            .setParameter("status", Payment.PAID)
            .getSingleResult();

        request.setAttribute("revenue", monthlyRevenue);
        request.setAttribute("reportType", "Monthly Sales Report for " + month + "/" + year);
        request.setAttribute("month", month);
        request.setAttribute("year", year);
    }

    private void handleYearlyReport(HttpServletRequest request) {
        String yearStr = request.getParameter("year");
        if (yearStr == null || yearStr.isEmpty()) {
            throw new IllegalArgumentException("Year parameter is required for yearly report");
        }

        int year = Integer.parseInt(yearStr);

        BigDecimal yearlyRevenue = em.createQuery(
            "SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE FUNCTION('YEAR', p.paymentTime) = :year AND p.paymentStatus = :status", 
            BigDecimal.class)
            .setParameter("year", year)
            .setParameter("status", Payment.PAID)
            .getSingleResult();

        request.setAttribute("revenue", yearlyRevenue);
        request.setAttribute("reportType", "Yearly Sales Report for " + year);
        request.setAttribute("year", year);
    }

    private void handleTopProductsReport(HttpServletRequest request) {
        List<Object[]> topProducts = em.createQuery(
            "SELECT oi.product.productName, SUM(oi.quantity), SUM(oi.price * oi.quantity) " +
            "FROM OrderItems oi JOIN oi.order o " +
            "WHERE o.status IN (:placedStatus, :deliveredStatus) " +
            "GROUP BY oi.product.productName " +
            "ORDER BY SUM(oi.quantity) DESC", 
            Object[].class)
            .setParameter("placedStatus", Orders.PLACED)
            .setParameter("deliveredStatus", Orders.DELIVERED)
            .setMaxResults(10)
            .getResultList();

        request.setAttribute("topProducts", topProducts);
        request.setAttribute("reportType", "Top 10 Sold Products");
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, String errorMessage) 
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        request.getRequestDispatcher("/admin/viewReport.jsp").forward(request, response);
    }
}