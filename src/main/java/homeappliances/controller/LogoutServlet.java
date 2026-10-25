package homeappliances.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet({"/logout", "/admin/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession(false); // don't create if doesn't exist
        String userType = null;
        String redirectPage = "index.jsp"; // default page

        if (session != null) {
            userType = (String) session.getAttribute("userType");

            if ("manager".equals(userType) || "staff".equals(userType)) {
                redirectPage = "index.jsp";
            }

            session.invalidate(); // logout
        }

        res.sendRedirect(redirectPage);
    }
}


