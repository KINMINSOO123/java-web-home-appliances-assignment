package homeappliances.controller;

import homeappliances.model.Product;
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

@WebServlet("/admin/DeleteProductServlet")
public class DeleteProductServlet extends HttpServlet {

    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("id");

        try {
            utx.begin();

            // Step 1: Delete related CartItems
            em.createQuery("DELETE FROM CartItem ci WHERE ci.product.productId = :productId")
                .setParameter("productId", productId)
                .executeUpdate();

            // Step 2: Delete related OrderItems
            em.createQuery("DELETE FROM OrderItems oi WHERE oi.product.productId = :productId")
                .setParameter("productId", productId)
                .executeUpdate();

            // Step 3: Delete the Product itself
            Product product = em.find(Product.class, productId);
            if (product != null) {
                em.remove(product);
            }

            utx.commit();
            
            response.sendRedirect("ViewProductsServlet");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to delete product. It may have dependencies.");
            request.getRequestDispatcher("admin/deleteProduct.jsp").forward(request, response);
        } 
    }
}
