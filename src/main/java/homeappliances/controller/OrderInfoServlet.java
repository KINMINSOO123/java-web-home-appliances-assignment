package homeappliances.controller;

import homeappliances.model.*;
import jakarta.persistence.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/OrderInfoServlet")
public class OrderInfoServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String orderId = req.getParameter("order_id");

        if (orderId == null || orderId.trim().isEmpty()) {
            res.sendRedirect("MyOrdersServlet");
            return;
        }

        try {
            Orders order = em.find(Orders.class, orderId);

            if (order == null) {
                req.setAttribute("errorMessage", "Order not found.");
                req.getRequestDispatcher("MyOrdersServlet").forward(req, res);
                return;
            }

            req.setAttribute("order", order);
            req.getRequestDispatcher("/orderInfo.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("MyOrdersServlet").forward(req, res);
        }
    }
}

