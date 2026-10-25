package homeappliances.controller;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import homeappliances.model.*;
import homeappliances.util.SSLIgnore;
import jakarta.annotation.Resource;
import jakarta.persistence.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.transaction.UserTransaction;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    @PersistenceContext(unitName = "JavaAssignmentPU") EntityManager em;
    @Resource UserTransaction utx;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        SSLIgnore.disableSSLVerification();
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");
                        //paste your secret api key at here
        String orderId = request.getParameter("orderId");
        if (orderId == null || orderId.trim().isEmpty()) {
            response.sendRedirect("orders.jsp");
            return;
        }

        try {
            Orders order = em.find(Orders.class, orderId);
            if (order == null || order.getItems() == null || order.getItems().isEmpty()) {
                response.sendRedirect("orders.jsp");
                return;
            }

            List<SessionCreateParams.LineItem> stripeItems = new ArrayList<>();
            for (OrderItems item : order.getItems()) {
                SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity((long) item.getQuantity())
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(item.getPrice().multiply(BigDecimal.valueOf(100)).longValue()) // 转成 cents
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(item.getProduct().getProductName())
                                    .build()
                            )
                            .build()
                    )
                    .build();
                stripeItems.add(lineItem);
            }

            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/JavaWebApplicationAssignment/success.jsp?session_id={CHECKOUT_SESSION_ID}&order_id=" + orderId)
                .setCancelUrl("http://localhost:8080/JavaWebApplicationAssignment/orders.jsp")
                .addAllLineItem(stripeItems)
                .build();

            Session stripeSession = Session.create(params);

            response.sendRedirect(stripeSession.getUrl());

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}

