package homeappliances.controller;

import homeappliances.model.*;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/DeleteCustomerServlet")
public class DeleteCustomerServlet extends HttpServlet {

    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String userType = (session != null) ? (String) session.getAttribute("userType") : null;

        if (userType == null || !userType.equalsIgnoreCase("manager")) {
            response.sendRedirect("admin/index.jsp");
            return;
        }

        String customerId = request.getParameter("id");

        if (customerId == null || customerId.isEmpty()) {
            request.setAttribute("error", "Customer ID is missing.");
            request.getRequestDispatcher("viewCustomers.jsp").forward(request, response);
            return;
        }

        try {
            utx.begin();

            Customer customer = em.find(Customer.class, customerId);
            if (customer == null) {
                request.setAttribute("error", "Customer not found.");
                request.getRequestDispatcher("viewCustomers.jsp").forward(request, response);
                return;
            }

            // 1. Delete CartItems
            List<CartItem> cartItems = em.createQuery(
                    "SELECT ci FROM CartItem ci WHERE ci.cart.customer.customerId = :customerId", CartItem.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
            for (CartItem item : cartItems) {
                em.remove(item);
            }

            // 2. Delete Cart
            List<Cart> carts = em.createQuery(
                    "SELECT c FROM Cart c WHERE c.customer.customerId = :customerId", Cart.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
            for (Cart cart : carts) {
                em.remove(cart);
            }

            // 3. Delete OrderItems and Payments
            List<Orders> orders = em.createQuery(
                    "SELECT o FROM Orders o WHERE o.customer.customerId = :customerId", Orders.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
            for (Orders order : orders) {
                // Delete OrderItems
                List<OrderItems> orderItems = em.createQuery(
                        "SELECT oi FROM OrderItems oi WHERE oi.order.orderId = :orderId", OrderItems.class)
                        .setParameter("orderId", order.getOrderId())
                        .getResultList();
                for (OrderItems oi : orderItems) {
                    em.remove(oi);
                }

                // Delete Payments
                List<Payment> payments = em.createQuery(
                        "SELECT p FROM Payment p WHERE p.order.orderId = :orderId", Payment.class)
                        .setParameter("orderId", order.getOrderId())
                        .getResultList();
                for (Payment p : payments) {
                    em.remove(p);
                }

                // Delete Order
                em.remove(order);
            }

            // 4. Finally, delete Customer
            em.remove(customer);

            utx.commit();

            response.sendRedirect("ViewCustomersServlet");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while deleting the customer.");
            request.getRequestDispatcher("/admin/deleteCutomer.jsp").forward(request, response);
        }
    }
}

