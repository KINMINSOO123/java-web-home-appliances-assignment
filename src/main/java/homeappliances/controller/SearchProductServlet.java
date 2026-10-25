package homeappliances.controller;

import homeappliances.model.Product;
import jakarta.persistence.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/SearchProductServlet")
public class SearchProductServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String query = request.getParameter("query");

        if (query == null || query.trim().isEmpty()) {
            response.sendRedirect("index.jsp");
            return;
        }

        // Use LIKE for case-insensitive partial search
        TypedQuery<Product> searchQuery = em.createQuery(
            "SELECT p FROM Product p WHERE LOWER(p.productName) LIKE :query", Product.class);
        searchQuery.setParameter("query", "%" + query.toLowerCase() + "%");

        List<Product> results = searchQuery.getResultList();

        request.setAttribute("searchResults", results);
        request.setAttribute("searchQuery", query);
        request.getRequestDispatcher("searchResults.jsp").forward(request, response);
    }
}

