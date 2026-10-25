/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeappliances.controller;

import homeappliances.model.Manager;
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

@WebServlet("/admin/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Check Manager
            TypedQuery<Manager> managerQuery = em.createQuery(
                    "SELECT m FROM Manager m WHERE m.username = :username", Manager.class);
            managerQuery.setParameter("username", username);

            List<Manager> managers = managerQuery.getResultList();
            if (!managers.isEmpty() && managers.get(0).getPassword().equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("userType", "manager");
                session.setAttribute("username", username);
                response.sendRedirect("index.jsp");
                return;
            }

            // Check Staff
            TypedQuery<Staff> staffQuery = em.createQuery(
                    "SELECT s FROM Staff s WHERE s.username = :username", Staff.class);
            staffQuery.setParameter("username", username);

            List<Staff> staffList = staffQuery.getResultList();
            if (!staffList.isEmpty() && staffList.get(0).getPassword().equals(password)) {
                HttpSession session = request.getSession();
                session.setAttribute("userType", "staff");
                session.setAttribute("username", username);
                response.sendRedirect("index.jsp");
                return;
            }

            // If no match
            request.setAttribute("error", "Invalid username or password");
            request.getRequestDispatcher("/admin/login.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error",ex.getMessage());
            request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
        }
    }
}

