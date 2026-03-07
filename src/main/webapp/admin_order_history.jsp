<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LỊCH SỬ ĐƠN HÀNG - ${customer.displayName}</title>
    <link rel="icon" type="image/png" href="../img/logo.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user_admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order_admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_profile_style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order_history_admin.css">

</head>
<body>

<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>

        <main class="content">
            <div class="user-management-panel">
                <div class="page-header-flex">
                    <div>
                        <a href="customer-detail?id=${customer.id}" class="back-to-profile">
                            <i class="fas fa-chevron-left"></i> Quay lại hồ sơ khách hàng
                        </a>
                        <h2 class="page-title">Lịch sử đơn hàng: ${customer.displayName}</h2>
                    </div>
                </div>
                <div class="user-table-container">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã Đơn</th>
                            <th>Ngày Đặt</th>
                            <th>Thanh Toán</th>
                            <th>Tổng Tiền</th>
                            <th>Trạng Thái</th>
                            <th>Thao Tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${listOrders}" var="o">
                            <tr class="order-row" data-status="${o.status}">
                                <td><b>#${o.id}</b></td>
                                <td><fmt:formatDate value="${o.createAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td><span class="badge">${o.paymentStatus}</span></td>
                                <td style="color: #e74c3c; font-weight: bold;">
                                    <fmt:formatNumber value="${o.totalOrder}" pattern="#,###"/> VND
                                </td>
                                <td><span class="badge status-pending">${o.status}</span></td>
                                <td>
                                    <button onclick="showDetail(${o.id})" style="border:none; background:none; color:#4e73df; cursor:pointer;">
                                        <i class="fas fa-eye"></i> Chi tiết
                                    </button>
                                    <div id="data-order-${o.id}" style="display: none;">
                                        <h3 style="border-bottom: 2px solid #eee; padding-bottom: 10px;">Chi tiết đơn hàng #${o.id}</h3>
                                        <div style="display: flex; justify-content: space-between; margin: 15px 0;">
                                            <div>
                                                <p><strong>Người nhận:</strong> ${o.fullName}</p>
                                                <p><strong>Địa chỉ:</strong> ${o.address}</p>
                                            </div>
                                            <div style="text-align: right;">
                                                <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${o.createAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                                            </div>
                                        </div>
                                        <table style="width: 100%; border-collapse: collapse;">
                                            <tr style="background: #f8f9fa;">
                                                <th style="padding: 10px; text-align: left;">Sản phẩm</th>
                                                <th style="padding: 10px;">SL</th>
                                                <th style="padding: 10px; text-align: right;">Đơn giá</th>
                                            </tr>
                                            <c:forEach items="${o.details}" var="d">
                                                <tr style="border-bottom: 1px solid #eee;">
                                                    <td style="padding: 10px;">${d.productName}</td>
                                                    <td style="padding: 10px; text-align: center;">x${d.quantity}</td>
                                                    <td style="padding: 10px; text-align: right;">
                                                        <fmt:formatNumber value="${d.total}" pattern="#,###"/> VND
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </table>
                                        <div style="background: #f4f7f6; padding: 15px; border-radius: 8px; margin-top: 20px;">
                                            <form action="${pageContext.request.contextPath}/admin/update-order-status" method="post">
                                                <input type="hidden" name="orderId" value="${o.id}">
                                                <input type="hidden" name="userId" value="${customer.id}">
                                                <label>Cập nhật trạng thái:</label>
                                                <select name="status" class="filter-select">
                                                    <option value="Chờ xác nhận" ${o.status == 'Chờ xác nhận' ? 'selected' : ''}>Chờ xác nhận</option>
                                                    <option value="Đang giao" ${o.status == 'Đang giao' ? 'selected' : ''}>Đang giao</option>
                                                    <option value="Đã giao" ${o.status == 'Đã giao' ? 'selected' : ''}>Đã giao</option>
                                                    <option value="Đã hủy" ${o.status == 'Đã hủy' ? 'selected' : ''}>Đã hủy</option>
                                                    <option value="Hoàn hàng" ${o.status == 'Hoàn hàng' ? 'selected' : ''}>Hoàn hàng</option>
                                                </select>
                                                <button type="submit" style="background:#27ae60; color:white; border:none; padding:8px 15px; border-radius:4px; cursor:pointer;">Lưu</button>
                                            </form>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>
<div id="orderModal" class="modal">
    <div class="modal-content">
        <span class="close-btn" onclick="closeModal()">&times;</span>
        <div id="modalContainer"></div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/order_history_admin.js"></script>
</body>
</html>