<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="homeappliances.model.Customer"%>
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
    <link href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css' rel='stylesheet'>
    <script type='text/javascript' src='https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>
    <style>
    /* 滚动条样式 - 只影响这个页面 */
    .profile-page ::-webkit-scrollbar {
      width: 8px;
    }
    .profile-page ::-webkit-scrollbar-track {
      background: #f1f1f1; 
    }
    .profile-page ::-webkit-scrollbar-thumb {
      background: #888; 
    }
    .profile-page ::-webkit-scrollbar-thumb:hover {
      background: #555; 
    }
    
    /* 页面特定样式 */
    .profile-page {
        background-color: #000;
        min-height: 100vh;
    }
    
    /* 主要内容容器 */
    .profile-container {
        padding: 20px 0;
    }
    
    /* 卡片样式 */
    .profile-card {
        width: 350px;
        background-color: #efefef;
        border: none;
        cursor: pointer;
        transition: all 0.5s;
    }
    
    .profile-card:hover .profile-image img {
        transform: scale(1.5);
    }
    
    /* 图片样式 */
    .profile-image img {
        transition: all 0.5s;
        height: 100px;
        width: 100px;
        object-fit: cover;
    }
    
    /* 按钮样式 */
    .profile-btn-round {
        height: 140px;
        width: 140px;
        border-radius: 50%;
    }
    
    .profile-btn-edit {
        height: 40px;
        width: 150px;
        border: none;
        background-color: #000;
        color: #aeaeae;
        font-size: 15px;
    }
    
    /* 文本样式 */
    .profile-name {
        font-size: 22px;
        font-weight: bold;
    }
    
    .profile-username {
        font-size: 14px;
        font-weight: 600;
    }
    
    .profile-userid {
        font-size: 12px;
    }
    
    .profile-points {
        font-size: 22px;
        font-weight: bold;
    }
    
    .profile-points-label {
        font-size: 12px;
        font-weight: 500;
        color: #444444;
    }
    
    .profile-info-text span {
        font-size: 13px;
        color: #545454;
        font-weight: 500;
    }
    
    /* 其他元素 */
    .profile-icons i {
        font-size: 19px;
    }
    
    .profile-birthday {
        background-color: #ccc;
    }
    
    .profile-birthday-text {
        font-size: 14px;
        color: #a0a0a0;
        font-weight: bold;
    }
    </style>
    
    <body class="profile-page">
        <!-- 主要内容区域 -->
        <div class="profile-container container mt-4 mb-4 p-3 d-flex justify-content-center"> 
            <div class="profile-card p-4"> 
                <div class="profile-image d-flex flex-column justify-content-center align-items-center"> 
                    <button class="profile-btn-round btn btn-secondary"> 
                        <% if(customer.getProfilePic() != null) { %>
                            <img src="<%= customer.getProfilePic() %>" alt="Profile">
                        <% } else { %>
                            <img src="image/blank-profile.png" alt="Profile">
                        <% } %>
                    </button> 
                        <span class="profile-name mt-3">${customer.customerName}</span> 
                        <span class="profile-username">@${customer.customerName}</span> 
                    <div class="d-flex flex-row justify-content-center align-items-center gap-2"> 
                        <span class="profile-userid">${customer.customerId}</span> 
                        <span><i class="fa fa-copy"></i></span> 
                    </div> 
                    <div class="d-flex flex-row justify-content-center align-items-center mt-3"> 
                        <span class="profile-points">
                            <%--${customer.customerPoint} --%>
                            <span class="profile-points-label">Points</span>
                        </span> 
                    </div> 
                    <div class="d-flex mt-2"> 
                        <a href="customerEditProfile.jsp">
                            <button class="profile-btn-edit btn-dark">Edit Profile</button>
                        </a> 
                    </div> 
                    <div class="profile-info-text mt-3"> 
                        <span>Email: ${customer.email}</span> 
                    </div> 
                    <div class="profile-info-text mt-3"> 
                        <span>Phone: ${customer.phone}</span> 
                    </div> 
                    <div class="profile-info-text mt-3"> 
                        <span>Shipping Address: ${customer.shippingAddress}</span> 
                    </div> 
                    <div class="profile-info-text mt-3"> 
                        <span>Billing Address: ${customer.billingAddress}</span> 
                    </div> 
                    <div class="gap-3 mt-3 profile-icons d-flex flex-row justify-content-center align-items-center"> 
                        <!-- 可以在这里添加社交图标 -->
                    </div> 
                    <div class="px-2 rounded mt-4 profile-birthday"> 
                        BirthDAY
                        <span class="profile-birthday-text"><fmt:formatDate value="${customer.dateOfBirth}" pattern="dd/MM/yyyy" />
</span> 
                    </div> 
                    <div class="d-flex mt-2"> 
                        <a href="index.jsp">
                            <button style="margin-top:30px;" class="btn btn-primary" type="button">
                                <-- Go Back
                            </button>
                        </a>
                    </div> 
                </div> 
            </div>     
        </div>
        
        <script type='text/javascript' src='https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js'></script>
        <script type='text/javascript'>
            var myLink = document.querySelector('a[href="#"]');
            myLink.addEventListener('click', function(e) {
                e.preventDefault();
            });
        </script>
    </body>
    <%@ include file="jsp/footer.jsp"%>