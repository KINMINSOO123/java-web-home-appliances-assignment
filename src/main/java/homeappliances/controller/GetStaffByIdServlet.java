package homeappliances.controller;

import homeappliances.model.Staff;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.transaction.UserTransaction;
import java.io.IOException;

@WebServlet("/admin/GetStaffByIdServlet")
public class GetStaffByIdServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String staffId = req.getParameter("id");
        String action = req.getParameter("action");

        if (staffId == null || action == null) {
            res.sendRedirect("viewStaffs.jsp");
            return;
        }

        Staff staff = em.find(Staff.class, staffId);

        if (staff == null) {
            res.sendRedirect("viewStaffs.jsp");
            return;
        }

        if (action.equals("edit")) {
            req.setAttribute("staff", staff);
            req.getRequestDispatcher("/admin/editStaff.jsp").forward(req, res);
        } else if (action.equals("delete")) {
            req.setAttribute("staff", staff);
            req.getRequestDispatcher("/admin/deleteStaff.jsp").forward(req, res);
        } else {
            res.sendRedirect("viewStaffs.jsp");
        }
    }
}

