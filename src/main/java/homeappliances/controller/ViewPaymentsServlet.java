package homeappliances.controller;

import homeappliances.model.Payment;
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

@WebServlet("/admin/ViewPaymentsServlet")
public class ViewPaymentsServlet extends HttpServlet {

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

        TypedQuery<Payment> query = em.createQuery("SELECT p FROM Payment p", Payment.class);
        List<Payment> payments = query.getResultList();
        req.setAttribute("payments", payments);
        req.getRequestDispatcher("/admin/viewPayments.jsp").forward(req, res);
    }
}

