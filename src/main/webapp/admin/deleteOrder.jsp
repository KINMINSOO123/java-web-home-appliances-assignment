<%@ page import="homeappliances.model.Orders" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="jsp/header.jsp" %>

<% 
  if (userType == null || !userType.equalsIgnoreCase("manager")) { 
    request.setAttribute("error","You have no permission to delete order"); %>
    <jsp:forward page="/admin/index.jsp"></jsp:forward>
<% return; } %>

<jsp:useBean id="order" scope="request" class="homeappliances.model.Orders" />
<%
    if (order == null) {
        response.sendRedirect("viewOrders.jsp");
        return;
    }
%>

<div class="container mt-5">
  <div class="card shadow rounded-4">
    <div class="card-header bg-primary text-white">
      <h4 class="mb-0">Delete Order: <%= order.getOrderId() %></h4>
    </div>
    <div class="card-body">
      <form class="row g-4" action="DeleteOrderServlet" method="post">

        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">  

        <div class="col-md-6" style="margin-top:10px">
          <label for="orderDate" class="form-label">Order Date</label>
          <input type="text" class="form-control" id="orderDate" name="orderDate" value="<%= order.getOrderDate() %>" readonly>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="status" class="form-label">Status</label>
          <input type="text" class="form-control" id="status" name="status" value="<%= order.getStatus() %>" readonly>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="totalAmount" class="form-label">Total Amount</label>
          <input type="text" class="form-control" id="totalAmount" name="totalAmount" value="<%= order.getTotalAmount() %>" readonly>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="customerId" class="form-label">Customer ID</label>
          <input type="text" class="form-control" id="customerId" name="customerId" value="<%= (order.getCustomer() != null) ? order.getCustomer().getCustomerId() : "N/A" %>" readonly>
        </div>

        <c:if test="${not empty error}">
          <div class="col-12">
            <div class="alert alert-danger mt-2">${error}</div>
          </div>
        </c:if>

        <div class="col-12 text-end" style="margin-top:10px">
          <button type="submit" class="btn btn-primary px-4 mt-3">Delete Order</button>
        </div>

      </form>
    </div>
  </div>
</div>

<%@ include file="jsp/footer.jsp" %>

