<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page import="homeappliances.model.Customer"%>
<%@include file="jsp/header.jsp" %>

<% 
    if (userType == null || !userType.equalsIgnoreCase("manager")) { 
        request.setAttribute("error","You have no permission to edit customer"); %>
        <jsp:forward page="/admin/index.jsp"></jsp:forward>
<%
    return; 
} %>
<%  
    Customer customer = (Customer)request.getAttribute("customer");
    if (customer == null) {
        response.sendRedirect("viewCustomers.jsp");
        return;
    }
%>

<div class="container mt-5">
  <div class="card shadow rounded-4">
    <div class="card-header bg-primary text-white">
      <h4 class="mb-0">Update Customer : <%= customer.getCustomerId() %></h4>
    </div>
    <div class="card-body">
      <form class="row g-4" action="UpdateCustomerServlet" method="post" enctype="multipart/form-data">

        <!-- Hidden field to send the Customer ID -->
        <input type="hidden" name="id" value="<%= customer.getCustomerId() %>">

        <div class="col-md-6" style="margin-top:10px">
          <label for="username" class="form-label">Customer Name</label>
          <input type="text" class="form-control" id="username" name="name" value="<%= customer.getCustomerName() %>" required>
          <small class="text-danger">${errors.name}</small>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="password" class="form-label">Password</label>
          <input type="password" class="form-control" id="password" name="password" value="<%= customer.getPassword() %>" required>
          <small class="text-danger">${errors.password}</small>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="email" class="form-label">Email</label>
          <input type="email" class="form-control" id="email" name="email" value="<%= customer.getEmail() %>" required>
          <small class="text-danger">${errors.email}</small>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="phone" class="form-label">Phone</label>
          <input type="tel" class="form-control" id="phone" name="phone" value="<%= customer.getPhone() %>" required>
          <small class="text-danger">${errors.phone}</small>
        </div>

        <div class="col-12" style="margin-top:10px">
          <label for="inputAddress" class="form-label">Shipping Address</label>
          <input type="text" class="form-control" name="shippingAddress" id="inputAddress" value="<%= customer.getShippingAddress() %>" required>
          <small class="text-danger">${errors.shippingAddress}</small>
        </div>

        <div class="col-12" style="margin-top:10px">
          <label for="inputAddress2" class="form-label">Billing Address</label>
          <input type="text" class="form-control" name="billingAddress" id="inputAddress2" value="<%= customer.getBillingAddress() %>" required>
          <small class="text-danger">${errors.billingAddress}</small>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="dob" class="form-label">Date of Birth</label>
          <input type="date" id="datepicker" class="form-control" name="dob" value="<fmt:formatDate value='${customer.dateOfBirth}' pattern='yyyy-MM-dd' />" required>
          <small class="text-danger">${errors.dob}</small>
        </div>

        <div class="col-12" style="margin-top:10px">
            <label for="profilePic" class="form-label">Customer Profile Picture</label><br>

            <c:if test="${not empty customer.profilePic}">
              <img src="../${customer.profilePic}" alt="Profile Picture" class="img-thumbnail mb-2" style="max-height:150px;">
            </c:if>
            <input type="hidden" name="oldProfilePic" value="${customer.profilePic}">
            <input type="file" class="form-control" id="profilePic" name="profilePic" accept="image/*">
          </div>
        
        <c:if test="${not empty errors.error}">
          <div class="col-12">
            <div class="alert alert-danger mt-2">${errors.error}</div>
          </div>
        </c:if>

        <div class="col-12 text-end" style="margin-top:10px">
          <button type="submit" class="btn btn-primary px-4 mt-3">Update Customer</button>
        </div>

      </form>
    </div>
  </div>
</div>

<%@include file="jsp/footer.jsp" %>
