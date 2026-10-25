package homeappliances.controller;

import homeappliances.model.OrderItems;
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
import java.util.List;

@WebServlet("/admin/DeleteOrderServlet")
public class DeleteOrderServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderId = request.getParameter("orderId");

        try {
            utx.begin();

            Orders order = em.find(Orders.class, orderId);

            if (order != null) {
                // Step 1: Fetch and delete all OrderItems by query
                List<OrderItems> orderItems = em.createQuery(
                        "SELECT oi FROM OrderItems oi WHERE oi.order.orderId = :orderId", OrderItems.class)
                        .setParameter("orderId", order.getOrderId())
                        .getResultList();

                for (OrderItems oi : orderItems) {
                    em.remove(oi);
                }

                List<Payment> payments = em.createQuery(
                        "SELECT p FROM Payment p WHERE p.order.orderId = :orderId", Payment.class)
                        .setParameter("orderId", order.getOrderId())
                        .getResultList();
                for (Payment p : payments) {
                    em.remove(p);
                }
                // Step 2: Delete the Order
                em.remove(order);

                utx.commit();
                response.sendRedirect("ViewOrdersServlet");
            } else {
                request.setAttribute("error", "Order not found.");
                request.getRequestDispatcher("/admin/deleteOrder.jsp").forward(request, response);
            }

        } catch (Exception ex) {
            request.setAttribute("error", "Error deleting order: " + ex.getMessage());
            request.getRequestDispatcher("/admin/deleteOrder.jsp").forward(request, response);
        }
    }
}
