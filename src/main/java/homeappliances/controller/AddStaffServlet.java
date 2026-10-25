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
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.UserTransaction;

import java.io.IOException;
import java.util.Date;

@WebServlet("/admin/AddStaffServlet")
public class AddStaffServlet extends HttpServlet {

    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String userType = (session != null) ? (String) session.getAttribute("userType") : null;

        // 权限检查：只有 admin 可以添加 staff
        if (userType == null || !userType.equalsIgnoreCase("manager")) {
            req.setAttribute("error", "You have no permission to add staff.");
            req.getRequestDispatcher("/admin/index.jsp").forward(req, res);
            return;
        }

        // 获取表单数据
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        try {
            
            Staff staff = new Staff();
            staff.setStaffId(generateStaffId());
            staff.setUsername(username);
            staff.setPassword(password);
            staff.setName(name);
            staff.setEmail(email);
            staff.setPhone(phone);
            staff.setCreatedAt(new Date());

            utx.begin();
            em.persist(staff);
            utx.commit();

            // 添加成功后重定向
            res.sendRedirect("ViewStaffsServlet");

        } catch (Exception e) {
            req.setAttribute("error", "Error occurred while adding staff.");
            req.getRequestDispatcher("/admin/addStaff.jsp").forward(req, res);
        }
    }
    
    public String generateStaffId() {
        String newId;
        do {
            newId = "STF" + (1000 + (int) (Math.random() * 9000));
        } while (staffExists(newId));
        return newId;
    }

    private boolean staffExists(String staffId) {
        return em.find(Staff.class, staffId) != null;
    }
}

