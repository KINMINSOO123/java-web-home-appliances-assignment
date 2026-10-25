package homeappliances.controller;

import homeappliances.model.*;
import jakarta.annotation.Resource;
import jakarta.persistence.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/PlaceOrderServlet")
public class PlaceOrderServlet extends HttpServlet {

    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        Customer customer = (Customer) session.getAttribute("customer");

        if (customer == null || cart == null || cart.getItems().isEmpty()) {
            res.sendRedirect("cart.jsp");
            return;
        }

        try {
            utx.begin();

            // Create Order
            Orders order = new Orders();
            order.setOrderId(generateOrderId());
            order.setCustomer(customer);
            order.setStatus(homeappliances.model.Orders.PENDING);
            order.setTotalAmount(cart.getTotal());
            order.setOrderDate(new Date());

            em.persist(order);

            for (CartItem cartItem : cart.getItems()) {
                OrderItems item = new OrderItems();
                item.setOrderItemId(generateOrderItemId());
                item.setOrder(order);
                item.setProduct(cartItem.getProduct());
                item.setQuantity(cartItem.getQuantity());
                item.setPrice(cartItem.getProduct().getPrice());

                em.persist(item);

                // If you maintain bidirectional relationship
                order.getItems().add(item);
            }
            
            // Remove CartItems first (because of FK constraint)
            TypedQuery<CartItem> itemQuery = em.createQuery(
                "SELECT ci FROM CartItem ci WHERE ci.cart.cartId = :cartId", CartItem.class);
            itemQuery.setParameter("cartId", cart.getCartId());
            List<CartItem> cartItems = itemQuery.getResultList();

            for (CartItem ci : cartItems) {
                em.remove(em.merge(ci)); // merge in case it's detached
            }

            // Then remove the Cart
            Cart managedCart = em.find(Cart.class, cart.getCartId());
            if (managedCart != null) {
                em.remove(managedCart);
            }
            
            utx.commit();

            session.removeAttribute("cart");
            
            TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o WHERE o.customer = :customer ORDER BY o.orderDate DESC",
                Orders.class
            );
            query.setParameter("customer", customer);
            List<Orders> orders = query.getResultList();
            
            session.setAttribute("orders", orders);
            res.sendRedirect("MyOrdersServlet");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/cart.jsp").forward(req, res);
        } 
    }
    
    public String generateOrderId() {
        String newId;
        do {
            newId = "ORD" + (1000 + (int) (Math.random() * 9000));
        } while (orderExists(newId));
        return newId;
    }

    private boolean orderExists(String orderId) {
        return em.find(Orders.class, orderId) != null;
    }

    public String generateOrderItemId() {
        String newId;
        do {
            newId = "OITEM" + (10000 + (int) (Math.random() * 90000));
        } while (orderItemExists(newId));
        return newId;
    }

    private boolean orderItemExists(String orderItemId) {
        return em.find(OrderItems.class, orderItemId) != null;
    }
}

