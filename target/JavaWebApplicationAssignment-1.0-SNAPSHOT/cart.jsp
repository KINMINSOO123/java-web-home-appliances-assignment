<%@page import="java.math.BigDecimal"%>
<%@page import="homeappliances.model.CartItem"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="jsp/header.jsp"%>
<% if(isLoggedIn == false){%>
    <script type="text/javascript">
        alert("Please register or log in as a customer to access the cart.");
        window.location = "index.jsp";
    </script>
<%
        return; // Make sure to stop JSP execution after redirect
    }
%>
<div class="container py-5">
    <h1 class="mb-5">Your Shopping Cart</h1>
    <div class="row">
        <div class="col-lg-8">
            <!-- Cart Items -->
            <div class="card mb-4">
                ${errors.error}
                ${error}
                ${errorMessage}
                <div class="card-body">
                    <% List<CartItem> items = cart.getItems(); %>
                    <% if(items.size() == 0){ %>
                        Cart Empty, Go get Your Item
                        <% } %>
                    <% for(CartItem item : items){ %>
                        <div class="row cart-item mb-3">
                            <div class="col-md-3">
                                <img src="<%= item.getProduct().getProductpic()%>" alt="<%= item.getProduct().getProductId() %>" class="img-fluid rounded">
                            </div>
                            <div class="col-md-5">
                                <h5 class="card-title">Product ID : <%= item.getProduct().getProductId() %></h5>
                                <p class="text-muted">Category: <%= item.getProduct().getCategory() %></p>
                            </div>
                            <div class="col-md-2">
                                <form method="post" action="UpdateCartQuantityServlet" class="quantity-form">
                                    <input type="hidden" name="productId" value="<%= item.getProduct().getProductId() %>">
                                    <div class="input-group">
                                        <button class="btn btn-outline-secondary btn-sm" type="button" onclick="updateQuantity(this, -1)">-</button>
                                        <input style="max-width:100px" type="text" name="quantity" class="form-control form-control-sm text-center quantity-input"
                                               min="1" max="<%= item.getProduct().getStock() %>" value="<%= item.getQuantity() %>">
                                        <button class="btn btn-outline-secondary btn-sm" type="button" onclick="updateQuantity(this, 1)">+</button>
                                    </div>
                                </form>
                            </div>
                            <div class="col-md-2 text-end">
                                <p class="fw-bold">$<%= item.getProduct().getPrice() %></p>
                                <form method="post" action="RemoveFromCartServlet" class="d-inline">
                                    <input type="hidden" name="productId" value="<%= item.getProduct().getProductId() %>">
                                    <button type="submit" class="btn btn-sm btn-outline-danger" onclick="return confirm('Are you sure to remove this item?')">
                                        <i class="bi bi-trash"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                        <hr>
                        <% } %>
                </div>
            </div>
            <!-- Continue Shopping Button -->
            <div class="text-start mb-4">
                <a href="ProductsServlet?category=All" class="btn btn-outline-primary">
                    <i class="bi bi-arrow-left me-2"></i>Continue Shopping
                </a>
            </div>
        </div>
        <div class="col-lg-4">
            <!-- Cart Summary -->
            <div class="card cart-summary">
                <div class="card-body">
                    <h5 class="card-title mb-4">Order Summary</h5>
                    <form method="post" action="PlaceOrderServlet">
                        <div class="d-flex justify-content-between mb-3">
                            <span>Total Items Quantity</span>
                            <span><%= cart.getTotalItems() %></span>
                        </div>
                        <div class="d-flex justify-content-between mb-3">
                            <span>Subtotal</span>
                            <span><%= cart.getSubtotal() %></span>
                        </div>
                        <div class="d-flex justify-content-between mb-3">
                            <span>Shipping</span>
                            <% 
                                BigDecimal subtotal = cart.getSubtotal();
                                BigDecimal shippingFee = subtotal.compareTo(BigDecimal.valueOf(1000)) >= 0 ? BigDecimal.ZERO : homeappliances.model.Cart.getSHIPPING();
                            %>
                            <% if(items.size() == 0){ %>
                            <span> 0 </span>
                            <% }else{ %>
                            <span><%= shippingFee %></span>
                            <% } %>
                        </div>
                        <div class="d-flex justify-content-between mb-3">
                            <span>Tax</span>
                            <span><%= cart.getTax() %></span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between mb-4">
                            <strong>Total</strong>
                            <% if(items.size() == 0){ %>
                            <strong> 0 </strong>
                            <% }else{ %>
                            <strong><%= cart.getTotal() %></strong>
                            <% } %>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Place Order</button>
                    </form>
                </div>
            </div>
            <!-- Promo Code -->
            <div class="card mt-4">
                <div class="card-body">
                    <h5 class="card-title mb-3">Apply Promo Code</h5>
                    <div class="input-group mb-3">
                        <input type="text" class="form-control" placeholder="Enter promo code">
                        <button class="btn btn-outline-secondary" type="button">Apply</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
function updateQuantity(button, change) {
    const input = button.parentElement.querySelector('input[name="quantity"]');
    let currentVal = parseInt(input.value);
    const min = parseInt(input.getAttribute("min"));
    const max = parseInt(input.getAttribute("max"));

    if (!isNaN(currentVal)) {
        let newVal = currentVal + change;
        if (newVal >= min && newVal <= max) {
            input.value = newVal;
            // submit the parent form
            button.closest("form").submit();
        }
    }
}
</script>
<%@ include file="jsp/footer.jsp"%>

