package homeappliances.controller;

import homeappliances.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/admin/GetCustomerByIdServlet")
public class GetCustomerByIdServlet extends HttpServlet {
    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    //end of added
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String action = request.getParameter("action"); 

        if (id == null || id.isEmpty() || action == null || action.isEmpty()) {
            response.sendRedirect("viewCustomers.jsp");
            return;
        }

        try {
            Customer customer = em.find(Customer.class, id);
            if (customer == null) {
                response.sendRedirect("viewCustomers.jsp");
                return;
            }
            request.setAttribute("customer", customer);

            if ("edit".equalsIgnoreCase(action)) {
                request.getRequestDispatcher("/admin/editCustomer.jsp").forward(request, response);
            } else if ("delete".equalsIgnoreCase(action)) {
                request.getRequestDispatcher("/admin/deleteCustomer.jsp").forward(request, response);
            } else {
                response.sendRedirect("viewCustomers.jsp");
            }

        } catch (Exception ex) {
            Logger.getLogger(FileUploadServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/admin/index.jsp").forward(request, response);
            return;
        } 
    }
}

