package homeappliances.controller;

import homeappliances.model.Customer;
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

@WebServlet("/admin/ViewCustomersServlet")
public class ViewCustomersServlet extends HttpServlet {

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

        // Fetch all customers
        TypedQuery<Customer> query = em.createQuery("SELECT c FROM Customer c", Customer.class);
        List<Customer> customers = query.getResultList();
        req.setAttribute("customers", customers);
        req.getRequestDispatcher("/admin/viewCustomers.jsp").forward(req, res);
        
    }
}

