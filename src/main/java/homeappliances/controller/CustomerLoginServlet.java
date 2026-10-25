/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeappliances.controller;
import homeappliances.model.Cart;
import homeappliances.model.CartItem;
import homeappliances.model.Customer;
import homeappliances.model.Orders;
import homeappliances.model.Payment;
import homeappliances.util.PasswordUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@WebServlet("/CustomerLoginServlet")
public class CustomerLoginServlet extends HttpServlet {
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        HashMap<String, String> errors = new HashMap<>();

        try {
            Customer customer = isValidLogin(em, email, password);

            if (customer == null) {
                errors.put("error", "Invalid Email or Password");
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/login.jsp").forward(req, res);
                return;
            }

            HttpSession session = req.getSession();
            session.setAttribute("customer", customer);

            // --- JPA Logic to retrieve cart and items ---
            Cart cart = null;
            try {
                TypedQuery<Cart> query = em.createQuery(
                    "SELECT c FROM Cart c WHERE c.customer = :customer AND c.status = 'ACTIVE'", Cart.class);
                query.setParameter("customer", customer);
                query.setMaxResults(1);
                cart = query.getSingleResult();

                // Fetch cart items
                TypedQuery<CartItem> itemQuery = em.createQuery(
                    "SELECT ci FROM CartItem ci WHERE ci.cart.cartId = :cartId", CartItem.class);
                itemQuery.setParameter("cartId", cart.getCartId());
                List<CartItem> items = itemQuery.getResultList();
                cart.setItems(items);

            } catch (NoResultException e) {
                // Optional: create empty cart in memory (not persisted yet)
                cart = new Cart();
                cart.setCartId(customer.getCustomerId()); // or use generateCartId()
                cart.setCustomer(customer);
                cart.setStatus("ACTIVE");
                cart.setCreatedAt(new Date());
                cart.setItems(new ArrayList<>());
            }

            session.setAttribute("cart", cart);
            
            TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o WHERE o.customer.customerId = :customerId",
                Orders.class
            );
            query.setParameter("customerId", customer.getCustomerId());
            List<Orders> orders = query.getResultList();

            for (Orders order : orders) {
                TypedQuery<Payment> paymentQuery = em.createQuery(
                    "SELECT p FROM Payment p WHERE p.order.orderId = :orderId", Payment.class);
                paymentQuery.setParameter("orderId", order.getOrderId());
                List<Payment> payments = paymentQuery.getResultList();
                order.setPayments(payments); // 你有这个 setter
            }

            session.setAttribute("orders", orders);
            res.sendRedirect("index.jsp");

        } catch (Exception ex) {
            ex.printStackTrace();
            errors.put("database", "Database error: " + ex.getMessage());
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/login.jsp").forward(req, res);
        }
    }
    
    public Customer isValidLogin(EntityManager em, String email, String password) {
        try {
            TypedQuery<Customer> query = em.createQuery(
                "SELECT c FROM Customer c WHERE c.email = :email", Customer.class);
            query.setParameter("email", email);
            Customer customer = query.getSingleResult();

            // 验证密码
            if (PasswordUtil.verifyPassword(password, customer.getPassword())) {
                return customer;
            } else {
                return null;
            }
        } catch (NoResultException e) {
            return null;
        }
    }
}

