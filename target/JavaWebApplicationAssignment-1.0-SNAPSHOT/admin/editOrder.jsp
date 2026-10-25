<%@page import="homeappliances.model.Orders"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="jsp/header.jsp" %>

<% 
    if (userType == null) { 
%>
    <jsp:forward page="/admin/index.jsp"></jsp:forward>
<%
    return; 
} %>
<jsp:useBean id="order" scope="request" class="Orders" />
<%
    if (order == null) {
        response.sendRedirect("viewOrders.jsp");
        return;
    }
%>

<div class="container mt-5">
  <div class="card shadow rounded-4">
    <div class="card-header bg-primary text-white">
      <h4 class="mb-0">Update Order Status: <%= order.getOrderId() %></h4>
    </div>
    <div class="card-body">
      <form class="row g-4" action="UpdateOrderServlet" method="post">
        
        <input type="hidden" name="orderId" value="<%= order.getOrderId() %>">

        <div class="col-md-6" style="margin-top:10px">
          <label for="status" class="form-label">Order Status</label>
          <select class="form-select" id="status" name="status" required>
            <option value="Pending" <%= "Pending".equals(order.getStatus()) ? "selected" : "" %>>Pending</option>
            <option value="Placed" <%= "Placed".equals(order.getStatus()) ? "selected" : "" %>>Placed</option>
            <option value="Checked" <%= "Checked".equals(order.getStatus()) ? "selected" : "" %>>Checked</option>
            <option value="Packed" <%= "Packed".equals(order.getStatus()) ? "selected" : "" %>>Packed</option>
            <option value="Delivered" <%= "Delivered".equals(order.getStatus()) ? "selected" : "" %>>Delivered</option>
            <option value="Arrived" <%= "Arrived".equals(order.getStatus()) ? "selected" : "" %>>Arrived</option>
          </select>
        </div>

        <c:if test="${not empty error}">
          <div class="col-12">
            <div class="alert alert-danger mt-2">${error}</div>
          </div>
        </c:if>

        <div class="col-12 text-end" style="margin-top:10px">
          <button type="submit" class="btn btn-primary px-4 mt-3">Update Status</button>
        </div>

      </form>
    </div>
  </div>
</div>

<%@include file="jsp/footer.jsp" %>
