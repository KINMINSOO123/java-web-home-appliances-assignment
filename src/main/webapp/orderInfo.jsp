<%@page import="java.math.BigDecimal"%>
<%@page import="homeappliances.model.OrderItems"%>
<%@ page import="homeappliances.model.Orders" %>
<%@ include file="/jsp/header.jsp" %>
<jsp:useBean id="order" class="Orders" scope="request" />
<%
    if (order.getOrderId() == null) {
        response.sendRedirect("orders.jsp");
        return;
    }
%>

<link rel="stylesheet" href="css/orderInfo.css">
<div style="min-height: 800px; background-color: #eee;">
<section class="h-100 h-custom" style="margin-bottom:100px;"> 
  <div class="container py-5 h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
      <div class="col-lg-8 col-xl-6">
        <div class="card border-top border-bottom border-3" style="border-color: #f37a27 !important;">
          <div class="card-body p-5">

            <p class="lead fw-bold mb-5" style="color: #f37a27;">Purchase Receipt</p>

            <div class="row">
              <div class="col mb-3">
                <p class="small text-muted mb-1">Date</p>
                <p><%= order.getOrderDate() %></p>
              </div>
              <div class="col mb-3">
                <p class="small text-muted mb-1">Order No.</p>
                <p><%= order.getOrderId() %></p>
              </div>
            </div>

            <div class="mx-n5 px-5 py-4" style="background-color: #f2f2f2;">
              <% for (OrderItems item : order.getItems()) { %>
              <div class="row">
                <div class="col-md-8 col-lg-9">
                  <p><%= item.getProduct().getProductName() %> (x<%= item.getQuantity() %>)</p>
                </div>
                <div class="col-md-4 col-lg-3">
                  <p>$ <%= String.format("%.2f", item.getTotalPrice()) %></p>
                </div>
              </div>
              <% } %>
              <%
                BigDecimal shippingFee = order.getSubtotal().compareTo(new BigDecimal(1000)) >= 0 ? BigDecimal.ZERO : homeappliances.model.Orders.getSHIPPING();
              %>
              <div class="row">
                <div class="col-md-8 col-lg-9">
                  <p class="mb-0">Shipping</p>
                </div>
                <div class="col-md-4 col-lg-3">
                  <p class="mb-0">$ <%= String.format("%.2f",shippingFee ) %></p>
                </div>
              </div>
              <div class="row">
                <div class="col-md-8 col-lg-9">
                  <p class="mb-0">Tax</p>
                </div>
                <div class="col-md-4 col-lg-3">
                  <p class="mb-0">$ <%= String.format("%.2f",order.getTax()) %></p>
                </div>
              </div>
            </div>

            <div class="row my-4">
              <div class="col-md-4 offset-md-8 col-lg-3 offset-lg-9">
                <p class="lead fw-bold mb-0" style="color: #f37a27;">
                  $ <%= String.format("%.2f", order.getTotalAmount()) %>
                </p>
              </div>
            </div>

            <p class="lead fw-bold mb-4 pb-2" style="color: #f37a27;">Tracking Order</p>

            <div class="row">
              <div class="col-lg-12">
                <div class="horizontal-timeline">
                  <ul class="list-inline items d-flex justify-content-between">
                    <%
                        String[] stages = {"Placed", "Checked", "Packed", "Delivered", "Arrived"};
                        for (String stage : stages) {
                            boolean reached = stage.equalsIgnoreCase(order.getStatus()) || 
                                              java.util.Arrays.asList(stages).indexOf(stage) <= java.util.Arrays.asList(stages).indexOf(order.getStatus());
                    %>
                    <li class="list-inline-item items-list">
                      <p class="py-1 px-2 rounded text-white" style="background-color: <%= reached ? "#f37a27" : "#ccc" %>;">
                        <%= stage %>
                      </p>
                    </li>
                    <% } %>
                  </ul>
                </div>
              </div>
            </div>

            <p class="mt-4 pt-2 mb-0">Want any help? <a href="contactUs.jsp" style="color: #f37a27;">Please contact us</a></p>
            
          </div>
        </div>
      </div>
    </div>
  </div>
</section>
</div>
<%@ include file="/jsp/footer.jsp"%>

