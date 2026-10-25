<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="homeappliances.model.Customer"%>
<%@page import="java.util.List"%>
<%@include file="jsp/header.jsp" %>
<div class="card-body">
    <div class="table-responsive">
        <c:if test="${not empty error}">
          <div class="col-12">
            <div class="alert alert-danger mt-2">${error}</div>
          </div>
        </c:if>
        <a href="addCustomer.jsp"><button>Add Customer</button></a>
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>Customer ID</th>
                    <th>Customer Name</th>
                    <th>Password</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Shipping Address</th>
                    <th>Billing Address</th>
                    <th>Date of Birth</th>
                    <th>Profile Picture link</th>
                    <th>Created At</th>
                    <th>Edit</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Customer> customers = (List<Customer>) request.getAttribute("customers");
                if (customers != null) {
                    for (Customer customer : customers) {
            %>
                        <tr>
                            <td><%= customer.getCustomerId() %></td>
                            <td><%= customer.getCustomerName() %></td>
                            <td><%= customer.getPassword() %></td>
                            <td><%= customer.getEmail() %></td>
                            <td><%= customer.getPhone() %></td>
                            <td><%= customer.getShippingAddress() %></td>
                            <td><%= customer.getBillingAddress() %></td>
                            <td><%= customer.getDateOfBirth() %></td>
                            <td><%= customer.getProfilePic() %></td>
                            <td><%= customer.getCreatedAt() %></td>
                            <td><a href="GetCustomerByIdServlet?id=<%= customer.getCustomerId() %>&action=edit"><button>Edit</button></a></td>
                            <td><a href="GetCustomerByIdServlet?id=<%= customer.getCustomerId() %>&action=delete"><button>Delete</button></a></td>
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