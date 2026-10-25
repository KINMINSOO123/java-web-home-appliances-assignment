<%@page import="homeappliances.model.Orders"%>
<%@page import="java.util.List"%>
<%@include file="jsp/header.jsp" %>
<div class="card-body">
    <div class="table-responsive">
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer ID</th>
                    <th>Order Date</th>
                    <th>Total Amount</th>
                    <th>Status</th>
                    <th>Edit</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Orders> orders = (List<Orders>) request.getAttribute("orders");
                if (orders != null) {
                    for (Orders order: orders) {
            %>
                        <tr>
                            <td><%= order.getOrderId() %></td>
                            <td><%= order.getCustomer().getCustomerId() %></td>
                            <td><%= order.getOrderDate() %></td>
                            <td><%= order.getTotalAmount() %></td>
                            <td><%= order.getStatus() %></td>
                            <td><a href="GetOrderByIdServlet?id=<%= order.getOrderId() %>&action=edit"><button>Edit</button></a></td>
                            <td><a href="GetOrderByIdServlet?id=<%= order.getOrderId() %>&action=delete"><button>Delete</button></a></td>
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

