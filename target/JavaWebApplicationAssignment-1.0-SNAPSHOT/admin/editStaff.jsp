<%@page import="homeappliances.model.Staff"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="jsp/header.jsp" %>
<% if (userType == null || !userType.equalsIgnoreCase("manager")) { %>
  <jsp:forward page="/admin/index.jsp"></jsp:forward>
<% return; } %>
<jsp:useBean id="staff" scope="request" class="Staff"></jsp:useBean>
<%  if (staff == null) {
        response.sendRedirect("viewStaffs.jsp");
        return;
    }
%>
<div class="container mt-5">
  <div class="card shadow rounded-4">
    <div class="card-header bg-primary text-white">
      <h4 class="mb-0">Update Staff : <%= staff.getStaffId() %></h4>
    </div>
    <div class="card-body">
      <form class="row g-4" action="UpdateStaffServlet" method="post">
          
        <input type="hidden" name="staffId" value="<%= staff.getStaffId()%>">   
        
        <div class="col-md-6" style="margin-top:10px">
          <label for="username" class="form-label">Username</label>
          <input type="text" class="form-control" id="username" name="username" placeholder="Enter username" value="<%= staff.getUsername() %>" required>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="password" class="form-label">Password</label>
          <input type="password" class="form-control" id="password" name="password" placeholder="Enter password" value="<%= staff.getPassword()%>" required>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="name" class="form-label">Name</label>
          <input type="text" class="form-control" id="name" name="name" placeholder="e.g. John Doe" value="<%= staff.getName()%>" required>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="email" class="form-label">Email</label>
          <input type="email" class="form-control" id="email" name="email" placeholder="e.g. john@example.com" value="<%= staff.getEmail()%>" required>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="phone" class="form-label">Phone</label>
          <input type="tel" class="form-control" id="phone" name="phone" placeholder="e.g. 0123456789" value="<%= staff.getPhone() %>" required>
        </div>

        <c:if test="${not empty error}">
          <div class="col-12">
            <div class="alert alert-danger mt-2">${error}</div>
          </div>
        </c:if>

        <div class="col-12 text-end" style="margin-top:10px">
          <button type="submit" class="btn btn-primary px-4 mt-3">Update Staff</button>
        </div>

      </form>
    </div>
  </div>
</div>

<%@include file="jsp/footer.jsp" %>

