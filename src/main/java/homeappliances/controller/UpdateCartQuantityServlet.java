package homeappliances.controller;

import homeappliances.model.Cart;
import homeappliances.model.CartItem;
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

@WebServlet("/UpdateCartQuantityServlet")
public class UpdateCartQuantityServlet extends HttpServlet {
    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        String productId = req.getParameter("productId");
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        if (cart != null && productId != null) {
            cart.updateQuantity(productId, quantity); // update in memory
            try {
                utx.begin();

                // 2. Find matching CartItem in DB by looping session cart items
                CartItem itemToUpdate = cart.getItems().stream()
                        .filter(ci -> ci.getProduct().getProductId().equals(productId))
                        .findFirst()
                        .orElse(null);

                if (itemToUpdate != null) {
                    CartItem cartItem = em.find(CartItem.class, itemToUpdate.getCartItemId());
                    if (cartItem != null) {
                        cartItem.setQuantity(quantity);
                        em.merge(cartItem);
                    }
                }

                utx.commit();

                // 3. Refresh session cart (optional, but safe)
                session.setAttribute("cart", cart);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        res.sendRedirect("cart.jsp");
    }
}
