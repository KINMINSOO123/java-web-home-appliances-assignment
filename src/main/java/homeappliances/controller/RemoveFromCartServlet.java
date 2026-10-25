package homeappliances.controller;

import homeappliances.model.Cart;
import homeappliances.model.CartItem;
import homeappliances.model.Product;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet("/RemoveFromCartServlet")
public class RemoveFromCartServlet extends HttpServlet {   
    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        String productId = req.getParameter("productId");
        Map<String,String> errors = new HashMap<>();
        
        if (cart != null && productId != null) {
            // 1. Get the item first before removing
            CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst().orElse(null);

            if (itemToRemove != null) {
                int quantityToRestore = itemToRemove.getQuantity();
                cart.removeItem(productId);
                try {
                    utx.begin();

                    // --- 1. Find and remove CartItem ---
                    CartItem cartItem = em.find(CartItem.class, itemToRemove.getCartItemId());
                    if (cartItem != null) {
                        em.remove(cartItem);
                    }

                    // --- 2. Update product stock ---
                    Product product = em.find(Product.class, productId);
                    if (product != null) {
                        product.setStock(product.getStock() + quantityToRestore);
                        em.merge(product);
                    }

                    utx.commit();

                    // Optional: update cart object in session (safe practice)
                    session.setAttribute("cart", cart);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    errors.put("error", "Database error: " + ex.getMessage());
                    req.setAttribute("errors", errors);
                    req.getRequestDispatcher("/cart.jsp").forward(req, res);
                    return;
                }
            }
        }

        res.sendRedirect("cart.jsp");
    }
}
