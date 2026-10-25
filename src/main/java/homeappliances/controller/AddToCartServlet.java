package homeappliances.controller;

import homeappliances.model.Cart;
import homeappliances.model.CartItem;
import homeappliances.model.Customer;
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
import java.util.Date;

@WebServlet("/AddToCartServlet")
public class AddToCartServlet extends HttpServlet {

    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String productId = req.getParameter("productId");
        String category = req.getParameter("category");
        int quantity = 0;

        try {
            quantity = Integer.parseInt(req.getParameter("quantity"));
        } catch (NumberFormatException e) {
            handleError(res, "Invalid quantity", category);
            return;
        }

        if (category == null || category.isEmpty() || "null".equals(category)) {
            category = "All";
        }

        HttpSession session = req.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            res.sendRedirect("ProductsServlet?category=" + category + "&error=" + java.net.URLEncoder.encode("Please login as customer before adding to the cart", "UTF-8"));
            return;
        }

        try {
            utx.begin();

            // Get or create cart
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null || cart.getCartId() == null || cart.getCartId().isEmpty()) {
                cart = new Cart(generateCartId(), "ACTIVE", new Date());
                cart.setCustomer(customer);
                em.persist(cart);
            } else {
                cart = em.merge(cart); // Ensure cart is managed
            }

            // Get product
            Product product = em.find(Product.class, productId);
            if (product == null) {
                handleError(res, "Product not found", category);
                utx.rollback();
                return;
            }

            // Check stock
            if (product.getStock() < quantity) {
                handleError(res, "Insufficient stock", category);
                utx.rollback();
                return;
            }

            // Update stock
            product.setStock(product.getStock() - quantity);
            em.merge(product);

            // Create and persist cart item
            CartItem cartItem = new CartItem();
            cartItem.setCartItemId(generateCartItemId());
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setAddedAt(new Date());
            em.persist(cartItem);

            utx.commit();

            cart.getItems().add(cartItem);
            session.setAttribute("cart", cart);
            res.sendRedirect("ProductsServlet?category=" + category);
        } catch (Exception ex) {
            try {
                if (utx != null) utx.rollback();
            } catch (Exception e) {
                ex.addSuppressed(e);
            }
            handleError(res, "Database error: " + ex.getMessage(), category);
            ex.printStackTrace();
        }
    }

    private void handleError(HttpServletResponse res, String error, String category) throws IOException {
        res.sendRedirect("ProductsServlet?category=" + category + "&error=" + error.replace(" ", "%20"));
    }
    /*
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String productId = req.getParameter("productId");
        String category = req.getParameter("category");
        int quantity = 0;

        try {
            quantity = Integer.parseInt(req.getParameter("quantity"));
        } catch (NumberFormatException e) {
            handleError(req, res, "Invalid quantity", category);
            return;
        }

        if (category == null || category.isEmpty() || "null".equals(category)) {
            category = "All";
        }

        HttpSession session = req.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            handleError(req, res, "Please login as customer before adding to the cart", category);
            return;
        }

        try {
            // Start a single transaction for all operations
            utx.begin();
            
            // Get or create cart
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null || cart.getCartId() == null || cart.getCartId().isEmpty()) {
                cart = new Cart(generateCartId(), "ACTIVE", new Date());
                cart.setCustomer(customer);
                em.persist(cart);
            } else {
                cart = em.merge(cart); // Ensure cart is managed
            }

            // Get product
            Product product = em.find(Product.class, productId);
            if (product == null) {
                handleError(req, res, "Product not found", category);
                return;
            }

            // Check stock
            if (product.getStock() < quantity) {
                handleError(req, res, "Insufficient stock", category);
                return;
            }

            // Update stock
            product.setStock(product.getStock() - quantity);
            em.merge(product);

            // Create and persist cart item
            CartItem cartItem = new CartItem();
            cartItem.setCartItemId(generateCartItemId());
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setAddedAt(new Date());
            em.persist(cartItem);

            // Commit transaction
            utx.commit();
            cart.getItems().add(cartItem);
            session.setAttribute("cart", cart);
            res.sendRedirect("ProductsServlet?category=" + category);
        } catch (Exception ex) {
            try {
                if (utx != null) utx.rollback();
            } catch (Exception e) {
                ex.addSuppressed(e);
            }
            handleError(req, res, "Database error: " + ex.getMessage(), category);
            ex.printStackTrace();
        }
    }

    private void handleError(HttpServletRequest req, HttpServletResponse res, String error, String category) 
            throws ServletException, IOException {
        req.setAttribute("error", error);
        req.getRequestDispatcher("ProductsServlet?category=" + category).forward(req, res);
    }*/
    
    public String generateCartId() {
        String newId;
        do {
            newId = "CART" + (1000 + (int) (Math.random() * 9000));
        } while (cartExists(newId));
        return newId;
    }

    private boolean cartExists(String cartId) {
        return em.find(Cart.class, cartId) != null;
    }
    
    public String generateCartItemId() {
        String newId;
        do {
            newId = "CITEM" + (100000 + (int) (Math.random() * 900000));
        } while (cartItemExists(newId));
        return newId;
    }

    private boolean cartItemExists(String cartItemId) {
        return em.find(CartItem.class, cartItemId) != null;
    }

}
