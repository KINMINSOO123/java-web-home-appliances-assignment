<%@page import="homeappliances.model.Orders"%>
<%@page import="homeappliances.model.Payment"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collection"%>
<%
    List<Orders> orders = (List<Orders>) session.getAttribute("orders");
%>
<%@ include file="/jsp/header.jsp"%>
<% if(isLoggedIn == false){%>
    <script type="text/javascript">
        alert("Please register or log in as a customer to view your orders.");
        window.location = "index.jsp";
    </script>
<%
        return; // Make sure to stop JSP execution after redirect
    }
%>
<link rel="stylesheet" href="css/orders.css">
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.2/css/all.css">
<script href="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

<div style="min-height: 600px;">
    <h2 style="margin:30px 5px" class="text-uppercase">My recent orders</h2>

    <% if (orders == null || orders.isEmpty()) { %>
        <div class="alert alert-info mt-4">You have no orders yet. ?</div>
    <% } else { 
        for (Orders order : orders) { 
            boolean isPaid = false;
            List<Payment> payments = order.getPayments();
            if (payments != null) {
                for (Payment payment : payments) {
                    if ("PAID".equals(payment.getPaymentStatus())) {
                        isPaid = true;
                        break;
                    }
                }
            }

            // Determine progress steps
            String status = order.getStatus();
            int step = 0;
            if ("Placed".equalsIgnoreCase(status)) step = 1;
            else if ("Checked".equalsIgnoreCase(status)) step = 2;
            else if ("Packed".equalsIgnoreCase(status)) step = 3;
            else if ("Delivered".equalsIgnoreCase(status)) step = 4;
            else if ("Arrived".equalsIgnoreCase(status)) step = 5;
    %>

    <div class="order my-3 bg-light">
        <div class="row">
            <div class="col-lg-4">
                <div class="d-flex flex-column justify-content-between order-summary">
                    <div class="d-flex align-items-center">
                        <div class="text-uppercase">Order <%= order.getOrderId() %></div>
                        <div class="blue-label ms-auto text-uppercase"><%= isPaid ? "PAID" : "UNPAID" %></div>
                    </div>
                    <div class="fs-8"><%= order.getOrderDate() %></div>
                    <div class="rating d-flex align-items-center pt-1">
                        <img src="https://www.freepnglogos.com/uploads/like-png/like-png-hand-thumb-sign-vector-graphic-pixabay-39.png" alt="">
                        <span class="px-2">Rating:</span>
                        <span class="fas fa-star"></span>
                        <span class="fas fa-star"></span>
                        <span class="fas fa-star"></span>
                        <span class="fas fa-star"></span>
                        <span class="far fa-star"></span>
                    </div>
                </div>
            </div>
            <div class="col-lg-8">
                <div class="d-sm-flex align-items-sm-start justify-content-sm-between">
                    <div class="status">Status : <%= isPaid ? order.getStatus() : "Pending" %></div>
                    <% if (isPaid) { %>
                    <a href="OrderInfoServlet?order_id=<%= order.getOrderId() %>"><div class="btn btn-primary text-uppercase">Order Info</div></a>
                    <% } else { %>
                        <form action="CheckoutServlet" method="post">
                            <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">
                            <button type="submit" class="btn btn-success text-uppercase">Proceed to Checkout</button>
                        </form>
                    <% } %>
                </div>

                <div class="progressbar-track">
                    <ul class="progressbar">
                        <li class="text-muted <%= step >= 1 ? "green" : "" %>">
                            <span class="fas fa-gift"></span>
                        </li>
                        <li class="text-muted <%= step >= 2 ? "green" : "" %>">
                            <span class="fas fa-check"></span>
                        </li>
                        <li class="text-muted <%= step >= 3 ? "green" : "" %>">
                            <span class="fas fa-box"></span>
                        </li>
                        <li class="text-muted <%= step >= 4 ? "green" : "" %>">
                            <span class="fas fa-truck"></span>
                        </li>
                        <li class="text-muted <%= step >= 5 ? "green" : "" %>">
                            <span class="fas fa-box-open"></span>
                        </li>
                    </ul>
                    <div id="tracker"></div>
                </div>
            </div>
        </div>
    </div>

    <% } // end for
    } // end if-else %>
</div>

<%@ include file="/jsp/footer.jsp"%>
