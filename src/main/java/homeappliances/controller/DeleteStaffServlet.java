package homeappliances.controller;

import homeappliances.model.Staff;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.UserTransaction;

import java.io.IOException;

@WebServlet("/admin/DeleteStaffServlet")
public class DeleteStaffServlet extends HttpServlet {
    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String staffId = request.getParameter("staffId");

        try {
            utx.begin();

            Staff staff = em.find(Staff.class, staffId);

            if (staff != null) {
                em.remove(staff);
                utx.commit();
                response.sendRedirect("ViewStaffsServlet");  // ✅ Redirect back to staff list after successful deletion
            } else {
                request.setAttribute("error", "Staff not found.");
                request.getRequestDispatcher("admin/deleteStaff.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to delete staff due to server error.");
            request.getRequestDispatcher("admin/deleteStaff.jsp").forward(request, response);
        } 
    }
}
