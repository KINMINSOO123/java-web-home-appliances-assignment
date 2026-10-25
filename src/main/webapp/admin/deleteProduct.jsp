<%@page import="homeappliances.model.Product"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="jsp/header.jsp" %>
<% if (userType == null || !userType.equalsIgnoreCase("manager")) { 
    request.setAttribute("error","You have no permission to delete product"); %>
    <jsp:forward page="/admin/index.jsp"></jsp:forward>
<% return; } %>
<jsp:useBean id="product" scope="request" class="Product"></jsp:useBean>
<%  if (product == null) {
        response.sendRedirect("viewProducts.jsp");
        return;
    }
%>
<div class="container mt-5">
  <div class="card shadow rounded-4">
    <div class="card-header bg-primary text-white">
      <h4 class="mb-0">Delete Product : <%= product.getProductId() %></h4>
    </div>
    <div class="card-body">
      <form class="row g-4" action="DeleteProductServlet" method="post">

        <input type="hidden" name="id" value="<%= product.getProductId() %>">

        <div class="col-md-6" style="margin-top:10px">
          <label for="productName" class="form-label">Product Name</label>
          <input type="text" class="form-control" id="productName" name="productName" value="<%= product.getProductName() %>" readonly>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="price" class="form-label">Price ($)</label>
          <input type="number" step="0.01" class="form-control" id="price" name="price" value="<%= product.getPrice() %>" placeholder="e.g. 199.99" readonly>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="stock" class="form-label">Stock Quantity</label>
          <input type="number" class="form-control" id="stock" name="stock" value="<%= product.getStock() %>" placeholder="e.g. 50" readonly>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="rating" class="form-label">Rating</label>
          <input type="text" class="form-control" id="rating" name="rating" value="<%= product.getRating() %> Star" placeholder="e.g. 5" readonly>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="category" class="form-label">Category</label>
          <input type="text" class="form-control" id="category" name="category" value="<%= product.getCategory() %> " readonly>
        </div>

        <div class="col-12" style="margin-top:10px">
          <label for="description" class="form-label">Description</label>
          <textarea class="form-control" id="description" name="description" rows="4" placeholder="Enter product description" readonly><%= product.getDescription()%></textarea>
        </div>

        <div class="col-12" style="margin-top:10px">
            <label for="productPicture" class="form-label">Product Picture</label><br>

            <c:if test="${not empty product.productpic}">
              <img src="../${product.productpic}" alt="Product Picture" class="img-thumbnail mb-2" style="max-height:150px;">
            </c:if>
        </div>
        
        <c:if test="${not empty error}">
          <div class="col-12">
            <div class="alert alert-danger mt-2">${error}</div>
          </div>
        </c:if>

        <div class="col-12 text-end" style="margin-top:10px">
          <button type="submit" class="btn btn-primary px-4 mt-3">Delete Product</button>
        </div>

      </form>
    </div>
  </div>
</div>

<%@include file="jsp/footer.jsp" %>

