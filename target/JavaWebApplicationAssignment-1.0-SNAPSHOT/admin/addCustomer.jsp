<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="jsp/header.jsp" %>
<% if (userType == null || !userType.equalsIgnoreCase("manager")) { 
  request.setAttribute("error","You have no permission to add customer"); %>
  <jsp:forward page="/admin/index.jsp"></jsp:forward>
<% return; } %>

<div class="container mt-5">
  <div class="card shadow rounded-4">
    <div class="card-header bg-primary text-white">
      <h4 class="mb-0">Add New Customer</h4>
    </div>
    <div class="card-body">
      <form class="row g-4" action="AddCustomerServlet" method="post">

        <div class="col-md-6" style="margin-top:10px">
          <label for="username" class="form-label">Customer Name</label>
          <input type="text" class="form-control" id="username" name="name" value="${param.name}" placeholder="e.g. John Doe" required>
          <small class="text-danger">${errors.name}</small>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="password" class="form-label">Password</label>
          <input type="password" class="form-control" id="password" name="password" placeholder="Enter password" required>
          <small class="text-danger">${errors.password}</small>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="email" class="form-label">Email</label>
          <input type="email" class="form-control" id="email" name="email" value="${param.email}" placeholder="e.g. john@example.com" required>
          <small class="text-danger">${errors.email}</small>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="phone" class="form-label">Phone</label>
          <input type="tel" class="form-control" id="phone" name="phone" value="${param.phone}" placeholder="e.g. 0123456789" required>
          <small class="text-danger">${errors.phone}</small>
        </div>

        <div class="col-12" style="margin-top:10px">
          <label for="inputAddress" class="form-label">Shipping Address</label>
          <input type="text" class="form-control" name="shippingAddress" id="inputAddress" value="${param.shippingAddress}" placeholder="123 Main St" required>
          <small class="text-danger">${errors.shippingAddress}</small>
        </div>

        <div class="col-12" style="margin-top:10px">
          <label for="inputAddress2" class="form-label">Billing Address</label>
          <input type="text" class="form-control" name="billingAddress" id="inputAddress2" value="${param.billingAddress}" placeholder="Apartment, suite, etc." required>
          <small class="text-danger">${errors.billingAddress}</small>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="dob" class="form-label">Date of Birth</label>
          <input type="date" id="datepicker" class="form-control" name="dob" value="${param.dob}" required>
          <small class="text-danger">${errors.dob}</small>
        </div>

        <c:if test="${not empty errors.error}">
          <div class="col-12">
            <div class="alert alert-danger mt-2">${errors.error}</div>
          </div>
        </c:if>

        <div class="col-12 text-end" style="margin-top:10px">
          <button type="submit" class="btn btn-success px-4 mt-3">Add Customer</button>
        </div>

      </form>
    </div>
  </div>
</div>

<%@include file="jsp/footer.jsp" %>

