package homeappliances.controller;

import homeappliances.model.Orders;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.transaction.UserTransaction;
import java.io.IOException;

@WebServlet("/admin/UpdateOrderServlet")
public class UpdateOrderServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        String status = req.getParameter("status");

        if (orderId == null || status == null) {
            req.setAttribute("error", "Order ID and status are required.");
            req.getRequestDispatcher("/admin/editOrder.jsp").forward(req, res);
            return;
        }

        try {
            Orders order = em.find(Orders.class, orderId);
            if (order == null) {
                res.sendRedirect("viewOrders.jsp");
                return;
            }

            utx.begin();
            order.setStatus(status);
            em.merge(order);
            utx.commit();

            res.sendRedirect("ViewOrdersServlet?success=updated");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to update order: " + e.getMessage());
            req.getRequestDispatcher("/admin/editOrder.jsp").forward(req, res);
        }
    }
}
