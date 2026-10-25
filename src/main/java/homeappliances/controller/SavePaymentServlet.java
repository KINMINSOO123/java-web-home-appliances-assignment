package homeappliances.controller;

import homeappliances.model.Customer;
import homeappliances.model.Orders;
import homeappliances.model.Payment;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@WebServlet("/SavePaymentServlet")
public class SavePaymentServlet extends HttpServlet {

    //added
    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;
    //end of added

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String sessionId = req.getParameter("session_id");
        String orderId = req.getParameter("order_id");
        HttpSession httpSession = req.getSession();
        Orders order = em.find(Orders.class, orderId);

        if (sessionId == null || order == null) {
            res.sendRedirect("error.jsp");
            return;
        }

        try {
            utx.begin();

            Payment payment = new Payment();
            payment.setPaymentId(generatePaymentId());
            payment.setOrder(order);
            payment.setStripeSessionId(sessionId);
            payment.setPaymentMethod("Stripe");
            payment.setPaymentStatus(homeappliances.model.Payment.PAID);
            payment.setPaymentTime(new Date());
            payment.setAmount(order.getTotalAmount());

            em.persist(payment);

            // 更新订单状态
            Orders managedOrder = em.find(Orders.class, order.getOrderId());
            if (managedOrder != null) {
                managedOrder.setStatus(homeappliances.model.Orders.PLACED);
            }

            utx.commit();

            // 可选：刷新 orders 列表
            Customer customer = (Customer) httpSession.getAttribute("customer");
            TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o WHERE o.customer.customerId = :customerId",
                Orders.class
            );
            query.setParameter("customerId", customer.getCustomerId());
            List<Orders> orders = query.getResultList();

            for (Orders o : orders) {
                TypedQuery<Payment> paymentQuery = em.createQuery(
                    "SELECT p FROM Payment p WHERE p.order.orderId = :orderId", Payment.class);
                paymentQuery.setParameter("orderId", o.getOrderId());
                List<Payment> payments = paymentQuery.getResultList();
                o.setPayments(payments); // 你有这个 setter
            }

            httpSession.setAttribute("orders", orders);

            res.sendRedirect("paymentSuccess.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

    private String generatePaymentId() {
        String id;
        do {
            id = "PAY" + (10000 + (int) (Math.random() * 90000));
        } while (em.find(Payment.class, id) != null);
        return id;
    }
}
