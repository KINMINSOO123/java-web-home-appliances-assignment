<%@page import="homeappliances.model.Staff"%>
<%@page import="java.util.List"%>
<%@include file="jsp/header.jsp" %>
<div class="card-body">
    <div class="table-responsive">
        <a href="addStaff.jsp"><button>Add Staff</button></a>
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>Staff ID</th>
                    <th>UserName</th>
                    <th>Password</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Created At</th>
                    <th>Edit</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
            <%
                List<Staff> staffs = (List<Staff>) request.getAttribute("staffs");
                if (staffs != null) {
                    for (Staff staff : staffs) {
            %>
                        <tr>
                            <td><%= staff.getStaffId() %></td>
                            <td><%= staff.getUsername() %></td>
                            <td><%= staff.getPassword() %></td>
                            <td><%= staff.getName() %></td>
                            <td><%= staff.getEmail() %></td>
                            <td><%= staff.getPhone() %></td>
                            <td><%= staff.getCreatedAt() %></td>
                            <td><a href="GetStaffByIdServlet?id=<%= staff.getStaffId() %>&action=edit"><button>Edit</button></a></td>
                            <td><a href="GetStaffByIdServlet?id=<%= staff.getStaffId() %>&action=delete"><button>Delete</button></a></td>
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
