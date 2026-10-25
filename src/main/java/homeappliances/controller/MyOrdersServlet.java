package homeappliances.controller;

import homeappliances.model.Customer;
import homeappliances.model.Orders;
import homeappliances.model.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/MyOrdersServlet")
public class MyOrdersServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check if customer is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get the logged-in customer
        Customer customer = (Customer) session.getAttribute("customer");

        TypedQuery<Orders> query = em.createQuery(
            "SELECT o FROM Orders o WHERE o.customer.customerId = :customerId",
            Orders.class
        );
        query.setParameter("customerId", customer.getCustomerId());
        List<Orders> orders = query.getResultList();

        for (Orders order : orders) {
            TypedQuery<Payment> paymentQuery = em.createQuery(
                "SELECT p FROM Payment p WHERE p.order.orderId = :orderId", Payment.class);
            paymentQuery.setParameter("orderId", order.getOrderId());
            List<Payment> payments = paymentQuery.getResultList();
            order.setPayments(payments); // 你有这个 setter
        }

        session.setAttribute("orders", orders);

        // Forward to JSP
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }
}
