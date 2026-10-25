package homeappliances.controller;

import homeappliances.model.Cart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/ViewCartsServlet")
public class ViewCartsServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    private EntityManager em;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userType") == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        TypedQuery<Cart> query = em.createQuery("SELECT c FROM Cart c", Cart.class);
        List<Cart> carts = query.getResultList();
        req.setAttribute("carts", carts);
        req.getRequestDispatcher("/admin/viewCarts.jsp").forward(req, res);
    }
}

