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

@WebServlet("/admin/GetOrderByIdServlet")
public class GetOrderByIdServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String orderId = req.getParameter("id");
        String action = req.getParameter("action");

        if (orderId == null || action == null) {
            res.sendRedirect("viewOrders.jsp");
            return;
        }

        Orders order = em.find(Orders.class, orderId);

        if (order == null) {
            res.sendRedirect("viewOrders.jsp");
            return;
        }

        if (action.equals("edit")) {
            req.setAttribute("order", order);
            req.getRequestDispatcher("/admin/editOrder.jsp").forward(req, res);
        } else if (action.equals("delete")) {
            req.setAttribute("order", order);
            req.getRequestDispatcher("/admin/deleteOrder.jsp").forward(req, res);
        } else {
            res.sendRedirect("viewOrders.jsp");
        }
    }
}

