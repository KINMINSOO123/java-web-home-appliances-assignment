<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String userType = (String) session.getAttribute("userType");
    String username = (String) session.getAttribute("username");
%>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Home & CO. Dashboard</title>

    <!-- Custom fonts for this template-->
    <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <link
        href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
        rel="stylesheet">

    <!-- Custom styles for this template-->
    <link href="css/sb-admin-2.min.css" rel="stylesheet">

</head>

<body id="page-top">

    <!-- Page Wrapper -->
    <div id="wrapper">

        <!-- Sidebar -->
        <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

            <!-- Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="index.jsp">
                <div class="sidebar-brand-icon rotate-n-15">
                    <i class="fas fa-laugh-wink"></i>
                </div>
                <div class="sidebar-brand-text mx-3">HOME & CO.</div>
            </a>

            <hr class="sidebar-divider my-0">

            <li class="nav-item active">
                <a class="nav-link" href="index.jsp">
                    <i class="fas fa-fw fa-tachometer-alt"></i>
                    <span>Dashboard</span>
                </a>
            </li>

            <hr class="sidebar-divider">

                <!-- Manager Menu -->
                <div class="sidebar-heading">Manager Tools</div>

                <li class="nav-item">
                    <a class="nav-link" href="ViewCustomersServlet">
                        <i class="fas fa-users"></i>
                        <span>Manage Customers</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ViewProductsServlet">
                        <i class="fas fa-box"></i>
                        <span>Manage Products</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ViewStaffsServlet">
                        <i class="fas fa-user-cog"></i>
                        <span>Manage Staff</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ViewOrdersServlet">
                        <i class="fas fa-shopping-cart"></i>
                        <span>View & Update Orders</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ViewPaymentsServlet">
                        <i class="fas fa-credit-card"></i>
                        <span>View Payments</span>
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="ViewCartsServlet">
                        <i class="fas fa-shopping-basket"></i>
                        <span>Manage Carts</span>
                    </a>
                </li>

            <hr class="sidebar-divider d-none d-md-block">

            <!-- Sidebar Toggler (Sidebar) -->
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>
            
            <div class="text-center d-none d-md-inline" style="margin:20px 0px;">
                <form action="logout" method="get">
                    <button class="btn btn-danger" title="Logout">
                        Logout <i class="fas fa-sign-out-alt"></i>
                    </button>
                </form>
            </div>

        </ul>
        <!-- End of Sidebar -->
        
        <!-- Content Wrapper -->
        <div id="content-wrapper" class="d-flex flex-column">
