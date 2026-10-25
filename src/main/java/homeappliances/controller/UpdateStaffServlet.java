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

@WebServlet("/admin/UpdateStaffServlet")
public class UpdateStaffServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU")
    private EntityManager em;

    @Resource
    private UserTransaction utx;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String staffId = req.getParameter("staffId"); // hidden field or you can use username if ID not passed
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        if (staffId == null || username == null || password == null || name == null || email == null || phone == null) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/admin/editStaff.jsp").forward(req, res);
            return;
        }

        try {
            Staff staff = em.find(Staff.class, staffId);
            if (staff == null) {
                res.sendRedirect("viewStaffs.jsp");
                return;
            }

            utx.begin();
            staff.setUsername(username);
            staff.setPassword(password);
            staff.setName(name);
            staff.setEmail(email);
            staff.setPhone(phone);
            em.merge(staff);
            utx.commit();

            res.sendRedirect("ViewStaffsServlet?success=updated");
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Failed to update staff: " + e.getMessage());
            req.getRequestDispatcher("/admin/editStaff.jsp").forward(req, res);
        }
    }
}

