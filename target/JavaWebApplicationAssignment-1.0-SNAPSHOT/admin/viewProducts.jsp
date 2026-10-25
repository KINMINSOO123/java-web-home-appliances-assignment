<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="homeappliances.model.Product"%>
<%@page import="java.util.List"%>
<%@include file="jsp/header.jsp" %>
<div class="card-body">
    <div class="table-responsive">
        <c:if test="${not empty error}">
          <div class="col-12">
            <div class="alert alert-danger mt-2">${error}</div>
          </div>
        </c:if>
        <a href="addProduct.jsp"><button>Add Product</button></a>
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Product Name</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Rating</th>
                    <th>Product Picture Link</th>
                    <th>Last Updated</th>
                    <th>Category</th>
                    <th>Edit</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Product> products = (List<Product>) request.getAttribute("products");
                if (products != null) {
                    for (Product product : products) {
            %>
                        <tr>
                            <td><%= product.getProductId() %></td>
                            <td><%= product.getProductName() %></td>
                            <td><%= product.getDescription() %></td>
                            <td><%= product.getPrice() %></td>
                            <td><%= product.getStock() %></td>
                            <td><%= product.getRating() %></td>
                            <td><%= product.getProductpic() %></td>
                            <td><%= product.getLastUpdated() %></td>
                            <td><%= product.getCategory() %></td>
                            <td><a href="GetProductByIdServlet?id=<%= product.getProductId() %>&action=edit"><button>Edit</button></a></td>
                            <td><a href="GetProductByIdServlet?id=<%= product.getProductId() %>&action=delete"><button>Delete</button></a></td>
                        </tr>
            <%
                    }
                }
            %>
            </tbody>
                </table>
    </div>
</div>
<%@include file="jsp/footer.jsp" %>
