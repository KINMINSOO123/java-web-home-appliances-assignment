<%@page import="homeappliances.model.Cart"%>
<%@page import="java.util.List"%>
<%@include file="jsp/header.jsp" %>
<div class="card-body">
    <div class="table-responsive">
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>Cart ID</th>
                    <th>Customer ID</th>
                    <th>Status</th>
                    <th>Created At</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Cart> carts = (List<Cart>) request.getAttribute("carts");
                if (carts != null) {
                    for (Cart cart : carts) {
            %>
                        <tr>
                            <td><%= cart.getCartId() %></td>
                            <td><%= cart.getCustomer().getCustomerId() %></td>
                            <td><%= cart.getStatus() %></td>
                            <td><%= cart.getCreatedAt() %></td>
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

