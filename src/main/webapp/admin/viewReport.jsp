<%@page import="java.math.BigDecimal"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="java.util.List"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="jsp/header.jsp" %>

<%
    // Set up currency formatting
    NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
    currency.setMaximumFractionDigits(2);
    currency.setMinimumFractionDigits(2);
%>

<div class="container mt-5">
    <div class="card">
        <div class="card-header bg-primary text-white">
            <h2 class="mb-0">${reportType}</h2>
        </div>
        
        <div class="card-body">
            <!-- Report Period Information -->
            <c:if test="${not empty date}">
                <div class="row mb-4">
                    <div class="col-md-12">
                        <h4><i class="fas fa-calendar-day"></i> Report Date: ${date}</h4>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${not empty month and not empty year}">
                <div class="row mb-4">
                    <div class="col-md-12">
                        <h4><i class="fas fa-calendar-alt"></i> Report Period: ${month}/${year}</h4>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${not empty year and empty month}">
                <div class="row mb-4">
                    <div class="col-md-12">
                        <h4><i class="fas fa-calendar"></i> Report Year: ${year}</h4>
                    </div>
                </div>
            </c:if>

            <!-- Revenue Display -->
            <c:if test="${not empty revenue}">
                <div class="alert alert-success">
                    <div class="d-flex justify-content-between align-items-center">
                        <h4 class="mb-0">
                            <i class="fas fa-money-bill-wave"></i> Total Revenue: 
                            <strong>$ <fmt:formatNumber value="${revenue}" type="currency" currencySymbol="" /></strong>
                        </h4>
                        <button class="btn btn-sm btn-outline-secondary" onclick="window.print()">
                            <i class="fas fa-print"></i> Print Report
                        </button>
                    </div>
                </div>
            </c:if>

            <!-- Top Products Table -->
            <c:if test="${not empty topProducts}">
                <div class="table-responsive">
                    <table class="table table-striped table-hover mt-4">
                        <thead class="thead-dark">
                            <tr>
                                <th>#</th>
                                <th>Product Name</th>
                                <th class="text-right">Quantity Sold</th>
                                <th class="text-right">Total Revenue</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="prod" items="${topProducts}" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${prod[0]}</td>
                                    <td class="text-right">${prod[1]}</td>
                                    <td class="text-right">RM <fmt:formatNumber value="${prod[2]}" type="currency" currencySymbol=""/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="mt-3">
                    <button class="btn btn-primary" onclick="exportToExcel()">
                        <i class="fas fa-file-excel"></i> Export to Excel
                    </button>
                </div>
            </c:if>

            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <h4><i class="fas fa-exclamation-triangle"></i> Error</h4>
                    <p>${error}</p>
                    <a href="index.jsp" class="btn btn-outline-primary">
                        <i class="fas fa-arrow-left"></i> Back to DashBoard
                    </a>
                </div>
            </c:if>
        </div>
        
        <div class="card-footer text-muted">
            Report generated on: <%= new java.util.Date().toString() %>
        </div>
    </div>
</div>

<script>
    function exportToExcel() {
        var table = document.querySelector("table"); // 找到页面的第一个 table
        var html = table.outerHTML;

        var url = 'data:application/vnd.ms-excel,' + encodeURIComponent(html);
        var link = document.createElement('a');
        link.href = url;
        link.download = 'report.xls'; // 文件名
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
</script>

<style>
    @media print {
        .no-print, .btn {
            display: none !important;
        }
        body {
            padding: 20px;
            font-size: 12pt;
        }
        .card {
            border: none;
        }
        .table {
            width: 100%;
        }
    }
    
    .table th {
        white-space: nowrap;
    }
</style>

<%@include file="jsp/footer.jsp" %>