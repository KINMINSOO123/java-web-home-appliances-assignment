<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="jsp/header.jsp"%>
<% if(isLoggedIn == false){%>
    <script type="text/javascript">
        alert("Please register or log in as a customer to access the customer profile.");
        window.location = "index.jsp";
    </script>
<%
        return; // Make sure to stop JSP execution after redirect
    }
%>
        <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css' rel='stylesheet'>
        <link href="css/editProfile.css" rel="stylesheet"></link>    
        <div class="container-xl px-4 mt-4">
            <a href="index.jsp"><button class="btn btn-primary" type="button"><-- Go Back</button></a>
    <hr class="mt-0 mb-4">
    <div class="row">
        <div class="col-xl-4">
            <!-- Profile picture card-->
            <div class="card mb-4 mb-xl-0">
                <div class="card-header">Profile Picture</div>
                <div class="card-body text-center">
                    <!-- Profile picture image-->
                    <% if(customer.getProfilePic() != null) { %>
                            <img src="<%= customer.getProfilePic() %>" 
                                 alt="Profile" 
                                 height="100" width="100">
                        <% } else { %>
                            <img src="image/blank-profile.png" 
                                 alt="Profile" 
                                 height="100" width="100">
                        <% } %>
                    <!-- Profile picture help block-->
                    <div class="small font-italic text-muted mb-4">JPG or PNG no larger than 5 MB</div>
                    <font color="red">
                            ${errors.error}
                            ${errors.database}
                      </font>
                    <!-- Profile picture upload button-->
                    <form action="FileUploadServlet" method="post" enctype="multipart/form-data">
                        <input id="ajaxfile" type="file" name="file" class="form-control mb-3" accept="image/png, image/jpeg" required>
                        <button class="btn btn-warning" type="submit" onclick="//uploadFile()">Upload new image</button>
                    </form>
                </div>
            </div>
        </div>
        <div class="col-xl-8">
            <!-- Account details card-->
            <div class="card mb-4">
                <div class="card-header">${customer.customerId} Account Details</div>
                <div class="card-body">
                    <font color="red">
                            ${errors.editError}
                            ${errors.editDatabase}
                      </font>
                    <form action="CustomerEditProfileServlet" method="get">
                        <!-- Form Group (username)-->
                        <div class="mb-3">
                            <label class="small mb-1" for="inputUsername">Username (how your name will appear to other users on the site)</label>
                            <input class="form-control" name="name" id="inputUsername" type="text" placeholder="Enter your username" value="${customer.customerName}">
                            <font color="red">
                                ${errors.name}
                            </font>
                        </div>
                        <!-- Form Row-->
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1" for="inputFirstName">Email Address</label>
                                <input class="form-control" name="email" id="inputFirstName" type="email" placeholder="Enter your email" value="${customer.email}">
                                <font color="red">
                                ${errors.email}
                                </font>
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1" for="inputLastName">Phone Number</label>
                                <input class="form-control" name="phone" id="inputLastName" type="text" placeholder="Enter your phone number" value="${customer.phone}">
                                <font color="red">
                                ${errors.phone}
                                </font>
                            </div>
                        </div>
                        <!-- Form Row        -->
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1" for="inputOrgName">Shipping Address</label>
                                <input class="form-control" name="shippingAddress" id="inputOrgName" type="text" placeholder="Enter your shipping address" value="${customer.shippingAddress}">
                                <font color="red">
                                ${errors.sAddress}
                                </font>
                            </div>
                            <!-- Form Group (location)-->
                            <div class="col-md-6">
                                <label class="small mb-1" for="inputLocation">Billing Address</label>
                                <input class="form-control" name="billingAddress" id="inputLocation" type="text" placeholder="Enter your location" value="${customer.billingAddress}">
                                <font color="red">
                                ${errors.address}
                                </font>
                            </div>
                        </div>
                        <!-- Form Group (email address)-->
                        <div class="mb-3">
                            <label class="small mb-1" for="inputEmailAddress">Birthday</label>
                            <input class="form-control" name="dob" id="inputEmailAddress" type="date" placeholder="Enter your birthday" value="<fmt:formatDate value='${customer.dateOfBirth}' pattern='yyyy-MM-dd' />">
                            ${errors.dob}
                        </div>
                        <!-- Save changes button-->
                        <button class="btn btn-warning" type="submit">Save changes</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
                        <script>
                            /*async function uploadFile(){
                                let formData = new FormData();
                                formData.append("file", ajaxfile.files[0]);
                                await fetch('FileUploadServlet',{
                                    method: "POST",
                                    body: formData
                                });
                                alert('The file uploaded successfully');
                            }*/
                        </script>
<%@ include file="jsp/footer.jsp"%>
