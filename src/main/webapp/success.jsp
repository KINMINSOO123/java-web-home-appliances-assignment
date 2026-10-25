<%@ include file="/jsp/header.jsp"%>
<%
    String sessionId = request.getParameter("session_id");
    String orderId = request.getParameter("order_id"); // ?????
    if (sessionId != null && orderId != null) {
        response.sendRedirect("SavePaymentServlet?session_id=" + sessionId + "&order_id=" + orderId);
    } else {
%>
    <div class="alert alert-success mt-4">? Payment completed successfully!</div>
    <a href="orders.jsp" class="btn btn-primary mt-3">View My Orders</a>
<%
    }
%>
<%@ include file="/jsp/footer.jsp"%>