package homeappliances.controller;

import homeappliances.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ProductsServlet")
public class ProductsServlet extends HttpServlet {
    
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String error = (String) req.getAttribute("error");
        String category = req.getParameter("category");
        List<Product> products;

        try {
            if (category == null || "All".equalsIgnoreCase(category)) {
                // JPA: 获取所有产品
                TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
                products = query.getResultList();
            } else {
                // JPA: 按分类获取产品
                TypedQuery<Product> query = em.createQuery(
                        "SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category)", Product.class);
                query.setParameter("category", category);
                products = query.getResultList();
                req.setAttribute("selectedCategory", category);
            }

            req.setAttribute("products", products);
            req.setAttribute("error", error);
            req.getRequestDispatcher("/product.jsp").forward(req, res);

        } catch (Exception ex) {
            Logger.getLogger(ProductsServlet.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, res);
        } 
    }
}