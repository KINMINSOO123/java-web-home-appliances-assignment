package homeappliances.controller;

import homeappliances.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/GetProductByIdServlet")
public class GetProductByIdServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    EntityManager em;

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String id = req.getParameter("id");
        String action = req.getParameter("action");

        if (id == null || action == null) {
            res.sendRedirect("viewProducts.jsp");
            return;
        }

        Product product = em.find(Product.class, id);

        if (product == null) {
            res.sendRedirect("viewProducts.jsp?error=ProductNotFound");
            return;
        }

        req.setAttribute("product", product);

        if (action.equalsIgnoreCase("edit")) {
            req.getRequestDispatcher("/admin/editProduct.jsp").forward(req, res);
        } else if (action.equalsIgnoreCase("delete")) {
            req.getRequestDispatcher("/admin/deleteProduct.jsp").forward(req, res);
        } else {
            res.sendRedirect("viewProducts.jsp");
        }
    }
}
