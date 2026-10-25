<%@page import="homeappliances.model.Customer"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean scope="session" class="homeappliances.model.Customer" id="customer"></jsp:useBean>
<%
    boolean isLoggedIn = false;
    String name = "User";

    if (customer != null && customer.getCustomerName() != null && !customer.getCustomerName().isEmpty()) {
        isLoggedIn = true;
        name = customer.getCustomerName();
    }
%>
<jsp:useBean scope="session" class="homeappliances.model.Cart" id="cart"></jsp:useBean>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/style.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@25.3.1/build/css/intlTelInput.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous" defer></script>
    </head>
    <body>
        <header id="main-header">
            <nav class="navbar navbar-expand-lg bg-body-tertiary">
                <div class="container-fluid">
                    <a class="navbar-brand" href="index.jsp">Home & Co.</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarScroll" aria-controls="navbarScroll" aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarScroll">
                        <ul class="navbar-nav me-auto my-2 my-lg-0 navbar-nav-scroll" style="--bs-scroll-height: 100px;">
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                  Home & Living
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="ProductsServlet?category=All">All</a></li>
                                    <li><a class="dropdown-item" href="ProductsServlet?category=Living+Room">Living Room</a></li>
                                    <li><a class="dropdown-item" href="ProductsServlet?category=Kitchen"">Kitchen</a></li>
                                    <li><a class="dropdown-item" href="ProductsServlet?category=Bathroom">Bathroom</a></li>
                                    <li><a class="dropdown-item" href="ProductsServlet?category=Dining+Room">Dining Room</a></li>
                                    <li><a class="dropdown-item" href="ProductsServlet?category=Bedroom">Bedroom</a></li>
                                </ul>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="MyOrdersServlet">Your Orders</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="contactUs.jsp">Contact Us</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="aboutUs.jsp">About Us</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link disabled" aria-disabled="true">Link</a>
                            </li>
                        </ul>
                        <form class="d-flex" action="SearchProductServlet" method="get" role="search" style="margin-right:20px;">
                            <input class="form-control me-2" type="search" placeholder="Search" name="query" aria-label="Search">
                            <button class="btn btn-outline-success" type="submit">Search</button>
                        </form>

                        <% if(isLoggedIn == true){ %>
                        <div style="margin-left:5px; margin-right:20px;">
                            <a href="cart.jsp" class="cart-icon-wrapper">
                                <i class="bi bi-cart"></i>
                                <span class="cart-count"><%= cart.getTotalItems() %></span>
                            </a>
                        </div>
                            <a href="customerProfile.jsp" style="display: inline-block; margin-right: 10px; text-decoration: none;">
                                 <div style="width: 40px; height: 40px; border-radius: 50%; overflow: hidden; background-color: #f0f0f0; border: 2px solid #ddd;">
                                     <% if(customer.getProfilePic() != null) { %>
                                         <img src="<%= customer.getProfilePic() %>" 
                                              alt="Profile" 
                                              style="width: 100%; height: 100%; object-fit: cover;">
                                     <% } else { %>
                                         <img src="image/blank-profile.png" 
                                              alt="Profile" 
                                              style="width: 100%; height: 100%; object-fit: cover;">
                                     <% } %>
                                 </div>
                             </a>
                            <a href="logout"><button type="button" style="margin-right:20px;" class="btn btn-danger">LOGOUT</button></a>
                        <% } else { %>
                        <a href="login.jsp"><button type="button" style="margin-right:20px;" class="btn btn-warning">LOGIN</button></a>
                        <a href="register.jsp"><button type="button" style="margin-right:20px;" class="btn btn-dark">REGISTER</button></a>
                        <% } %>
                    </div>
                </div>
            </nav>
        </header>
