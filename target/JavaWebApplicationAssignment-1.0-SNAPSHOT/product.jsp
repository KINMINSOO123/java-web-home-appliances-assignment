<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="homeappliances.model.Product"%>
<% List<Product> products = (List<Product>) request.getAttribute("products");%>
<%@ include file="jsp/header.jsp"%>
<div style="margin-bottom:30px;" class="container mt-5">
    <h2 class="mb-4"> <% if (request.getAttribute("selectedCategory") != null) { %>
        <%= request.getAttribute("selectedCategory") %> Products
    <% } else { %>
        All Products 
    <% } %></h2>
    <% String error = request.getParameter("error"); %>
    <% if (error != null) { %>
        <div class="alert alert-danger mt-3">
            <i class="bi bi-exclamation-triangle-fill"></i> Please login as customer before adding to the cart
        </div>
    <% } %>
    <div class="row g-4">
        <% for (Product product : products) { %>
        <div class="col-md-4">
            <form action="AddToCartServlet" method="get">
            <div class="card">
                <img src="<%= product.getProductpic()%>" height="300" class="card-img-top" alt="Product Image">
                <div class="card-body">
                    <h5 class="card-title"><%= product.getProductName() %></h5>
                    <p class="card-text"><%= product.getDescription() %></p>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="h5 mb-0">RM <%= product.getPrice() %></span>
                        <div>
                            <% 
                                int rating = (int) Math.floor(product.getRating());
                                for (int i = 1; i <= 5; i++) {
                                    if (i <= rating) {
                            %>
                                <i class="bi bi-star-fill text-warning"></i>
                            <%  } else if (i == rating + 1 && product.getRating() % 1 != 0) { %>
                                <i class="bi bi-star-half text-warning"></i>
                            <%  } else { %>
                                <i class="bi bi-star text-warning"></i>
                            <%  } 
                                }
                            %>
                            <small class="text-muted">(<%= product.getRating() %>)</small>
                        </div>
                        <p class="card-text">STOCK :<%= product.getStock() %></p>
                    </div>
                </div>
                <div class="card-footer d-flex justify-content-between bg-light"> 
                    <input type="hidden" name="category" value="<%= request.getAttribute("selectedCategory") %>">
                    <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                    <div class="mb-3">
                        <label for="quantity" class="form-label">Quantity</label>
                        <input type="number" class="form-control" id="quantity" name="quantity" min="1" max="<%= product.getStock() %>" value="1">
                    </div>
                </div>
                <div class="card-footer d-flex justify-content-between bg-light"> 
                    <button type="submit" class="btn btn-primary btn-sm">Add to Cart</button>
                    <button class="btn btn-outline-secondary btn-sm"><i class="bi bi-heart"></i></button>
                </div>
            </div>
          </form>
        </div>
        <% } %>
    </div>
</div>
<%@ include file="jsp/footer.jsp"%>
