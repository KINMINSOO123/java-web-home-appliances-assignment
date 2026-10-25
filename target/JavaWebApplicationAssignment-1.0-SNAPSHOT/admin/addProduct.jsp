<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="jsp/header.jsp" %>
<% if (userType == null) { %>
  <jsp:forward page="/admin/index.jsp"></jsp:forward>
<% return; } %>

<div class="container mt-5">
  <div class="card shadow-lg rounded-4">
    <div class="card-header bg-primary text-white">
      <h4 class="mb-0">Add New Product</h4>
    </div>
    <div class="card-body">
      <form class="row g-4" action="AddProductServlet" method="post" enctype="multipart/form-data">
        
        <div class="col-md-6" style="margin-top:10px">
          <label for="productName" class="form-label">Product Name</label>
          <input type="text" class="form-control" id="productName" name="productName" value="${param.productName}" placeholder="Enter product name" required>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="price" class="form-label">Price ($)</label>
          <input type="number" step="0.01" class="form-control" id="price" name="price" value="${param.price}" placeholder="e.g. 199.99" required>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="stock" class="form-label">Stock Quantity</label>
          <input type="number" class="form-control" id="stock" name="stock" value="${param.stock}" placeholder="e.g. 50" required>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="rating" class="form-label">Rating</label>
          <select class="form-select" id="rating" name="rating" required>
            <c:forEach var="i" begin="1" end="5">
              <option value="${i}" <c:if test="${param.rating == i}">selected</c:if>>${i} Star</option>
            </c:forEach>
          </select>
        </div>

        <div class="col-md-6" style="margin-top:10px">
          <label for="category" class="form-label">Category</label>
          <select class="form-select" id="category" name="category" required>
            <c:forEach var="cat" items="${['Living Room','Kitchen','Bathroom','Dining Room','Bedroom']}">
              <option value="${cat}" <c:if test="${param.category == cat}">selected</c:if>>${cat}</option>
            </c:forEach>
          </select>
        </div>

        <div class="col-12" style="margin-top:10px">
          <label for="description" class="form-label">Description</label>
          <textarea class="form-control" id="description" name="description" rows="4" placeholder="Enter product description" required>${param.description}</textarea>
        </div>

        <div class="col-12" style="margin-top:10px">
          <label for="productPicture" class="form-label">Product Picture</label>
          <input type="file" class="form-control" id="productPicture" name="productPicture" accept="image/*" required>
        </div>

        <c:if test="${not empty error}">
          <div class="alert alert-danger mt-3">${error}</div>
        </c:if>

        <div class="col-12 text-end" style="margin-top:10px">
          <button type="submit" class="btn btn-success px-4">Add Product</button>
        </div>
      </form>
    </div>
  </div>
</div>

<%@include file="jsp/footer.jsp" %>
