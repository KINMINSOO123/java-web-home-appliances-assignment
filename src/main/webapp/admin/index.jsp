<%@include file="jsp/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentYear" value="${now}" pattern="yyyy" />
<fmt:formatDate var="today" value="${now}" pattern="yyyy-MM-dd" />

<div class="d-flex justify-content-center align-items-center flex-column" style="height: 80vh; text-align: center;">
    <h2>Welcome, <c:out value="${not empty username ? username : 'to the Admin Page'}" />!</h2>

    <c:choose>
        <c:when test="${userType eq 'manager'}">
            <h2>Manager Dashboard</h2>

            <div class="mt-4" style="max-width: 600px;">
                <!-- Daily Sales Report -->
                <form action="ReportServlet" method="get" class="mb-3">
                    <div class="input-group">
                        <input type="hidden" name="action" value="daily">
                        <input type="date" name="date" class="form-control" 
                               value="${today}" max="${today}" required>
                        <button type="submit" class="btn btn-primary">View Daily Sales</button>
                    </div>
                </form>

                <!-- Monthly Sales Report -->
                <form action="ReportServlet" method="get" class="mb-3">
                    <div class="input-group">
                        <input type="hidden" name="action" value="monthly">
                        <select name="month" class="form-select" required>
                            <option value="">Select Month</option>
                            <c:forEach begin="1" end="12" var="month">
                                <option value="${month}">${month}</option>
                            </c:forEach>
                        </select>
                        <input type="number" name="year" class="form-control" 
                               placeholder="Enter Year" min="2000" max="${currentYear + 5}" 
                               value="${currentYear}" required>
                        <button type="submit" class="btn btn-success">View Monthly Sales</button>
                    </div>
                </form>

                <!-- Yearly Sales Report -->
                <form action="ReportServlet" method="get" class="mb-3">
                    <div class="input-group">
                        <input type="hidden" name="action" value="yearly">
                        <input type="number" name="year" class="form-control" 
                               placeholder="Enter Year" min="2000" max="${currentYear + 5}" 
                               value="${currentYear}" required>
                        <button type="submit" class="btn btn-warning">View Yearly Sales</button>
                    </div>
                </form>

                <!-- Top 10 Products Report -->
                <a href="ReportServlet?action=topProducts" 
                   class="btn btn-info mt-3">Top 10 Sold Products</a>
            </div>
        </c:when>
        
        <c:when test="${userType eq 'staff'}">
            <h2>Staff Dashboard</h2>
            <!-- Add staff-specific content here -->
        </c:when>
        
        <c:otherwise>
            <p>Please <a href="login.jsp">login</a> to continue.</p>
        </c:otherwise>
    </c:choose>

    <c:if test="${not empty error}">
        <div class="alert alert-danger mt-3"><c:out value="${error}" /></div>
    </c:if>
</div>

<%@include file="jsp/footer.jsp" %>