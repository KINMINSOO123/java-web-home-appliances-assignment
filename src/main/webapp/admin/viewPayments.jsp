<%@page import="homeappliances.model.Payment"%>
<%@page import="java.util.List"%>
<%@include file="jsp/header.jsp" %>
<div class="card-body">
    <div class="table-responsive">
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>Payment ID</th>
                    <th>Order ID</th>
                    <th>Stripe Session ID</th>
                    <th>Payment Method</th>
                    <th>Payment Status</th>
                    <th>Payment Time</th>
                    <th>Amount</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Payment> payments = (List<Payment>) request.getAttribute("payments");
                if (payments != null) {
                    for (Payment payment: payments) {
            %>
                        <tr>
                            <td><%= payment.getPaymentId() %></td>
                            <td><%= payment.getOrder().getOrderId() %></td>
                            <td><%= payment.getStripeSessionId() %></td>
                            <td><%= payment.getPaymentMethod() %></td>
                            <td><%= payment.getPaymentStatus() %></td>
                            <td><%= payment.getPaymentTime()%></td>
                            <td><%= payment.getAmount() %></td>
                        </tr>
            <%
                    }
                }
            %>
            </tbody>
                </table>
    </div>
</div>
<%@include file="jsp/footer.jsp" %>


