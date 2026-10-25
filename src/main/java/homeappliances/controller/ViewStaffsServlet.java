package homeappliances.controller;

import homeappliances.model.Staff;
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

@WebServlet("/admin/ViewStaffsServlet")
public class ViewStaffsServlet extends HttpServlet {

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

        String userType = (String) session.getAttribute("userType");

        // Block staff from accessing staff info
        if ("staff".equalsIgnoreCase(userType)) {
            req.setAttribute("error", "You have no permission to access staff information.");
            req.getRequestDispatcher("/admin/index.jsp").forward(req, res);
            return;
        }

        // Fetch all staffs
        TypedQuery<Staff> query = em.createQuery("SELECT s FROM Staff s", Staff.class);
        List<Staff> staffs = query.getResultList();
        req.setAttribute("staffs", staffs);
        req.getRequestDispatcher("/admin/viewStaffs.jsp").forward(req, res);
    }
}

