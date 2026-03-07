
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - ĐƠN HÀNG</title>
    <link rel="icon" type="image/png"  href="../img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user_admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order_admin.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_profile_style.css">




</head>
<body>

<div class="admin-container">

        <%@ include file="admin_header.jsp" %>
        <div class="main-wrapper">
            <%@ include file="admin_sidebar.jsp" %>
        <main class="content">
            <div class="user-management-panel">
                <h2 class="page-title">Quản Lý Đơn Hàng</h2>

                <div class="controls-bar">
                    <div class="search-filters">
                        <div class="search-input-wrapper">
                            <i class="fas fa-search search-icon"></i>
                            <input type="text" id="orderSearch" placeholder="Tìm theo mã đơn (#)..." class="search-input-order" onkeyup="filterOrders()">
                        </div>
                        <select class="filter-select" id="statusFilter" onchange="filterOrders()">
                            <option value="">Tất cả trạng thái</option>
                            <option value="Chờ xác nhận">Chờ xác nhận</option>
                            <option value="Đang giao">Đang giao</option>
                            <option value="Đã giao">Đã giao</option>
                            <option value="Đã hủy">Đã hủy</option>
                            <option value="Hoàn hàng">Hoàn hàng</option>
                        </select>
                    </div>

                </div>

                <div class="user-table-container">
                    <table>
                        <thead>
                        <tr>
                            <th>Mã Đơn</th>
                            <th>Khách Hàng</th>
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
                                <td>${o.fullName}</td>
                                <td><fmt:formatDate value="${o.createAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td>
                <span class="badge ${o.paymentStatus == 'Đã thanh toán' ? 'payment-paid' : 'payment-pending'}">
                        ${o.paymentStatus}
                </span>
                                </td>
                                <td style="font-weight: bold; color: #e74c3c;">
                                    <fmt:formatNumber value="${o.totalOrder}" pattern="#,###"/> VND
                                </td>
                                <td>
                <span class="badge status-${o.status == 'Chờ xác nhận' ? 'pending' : 'other'}">
                        ${o.status}
                </span>
                                </td>
                                <td>
                                    <a href="javascript:void(0)" onclick="showDetail(${o.id})">
                                        <i class="fas fa-eye" title="Xem chi tiết"></i>
                                    </a>

                                    <div id="data-order-${o.id}" style="display: none;">
                                        <h3 style="border-bottom: 2px solid #eee; padding-bottom: 10px;">Chi tiết đơn hàng #${o.id}</h3>

                                        <div style="display: flex; justify-content: space-between; margin-bottom: 15px;">
                                            <div>
                                                <p><strong>Khách hàng:</strong> ${o.fullName}</p>
                                                <p><strong>Số điện thoại:</strong> ${o.phone}</p>
                                                <p><strong>Địa chỉ:</strong> ${o.address}</p>
                                            </div>
                                            <div style="text-align: right;">
                                                <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${o.createAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                                                <p><strong>Trạng thái:</strong> ${o.status}</p>
                                            </div>
                                        </div>
                                        <div class="modal-body-scroll">
                                        <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px;">
                                            <thead>
                                            <tr style="background: #f8f9fa; border-bottom: 2px solid #dee2e6;">
                                                <th style="padding: 10px; text-align: left;">Sản phẩm</th>
                                                <th style="padding: 10px; text-align: center;">Số lượng</th>
                                                <th style="padding: 10px; text-align: right;">Đơn giá</th>
                                                <th style="padding: 10px; text-align: right;">Thành tiền</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach items="${o.details}" var="d">
                                                <tr style="border-bottom: 1px solid #eee;">
                                                    <td style="padding: 10px;">${d.productName}</td> <td style="padding: 10px; text-align: center;">x${d.quantity}</td>
                                                    <td style="padding: 10px; text-align: right;">
                                                        <fmt:formatNumber value="${d.total / d.quantity}" pattern="#,###"/> VND
                                                    </td>
                                                    <td style="padding: 10px; text-align: right; font-weight: bold;">
                                                        <fmt:formatNumber value="${d.total}" pattern="#,###"/> VND
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            </tbody>
                                        </table>
                                        <div style="background: #f4f7f6; padding: 15px; border-radius: 8px; margin-top: 20px;">
                                            <h5 style="margin-top: 0; color: #333; border-bottom: 1px solid #ddd; padding-bottom: 5px;">Cập nhật thông tin đơn hàng</h5>
                                            <form action="admin-orders" method="post" style="display: flex; flex-direction: column; gap: 15px;">
                                                <input type="hidden" name="orderId" value="${o.id}">

                                                <div style="display: flex; gap: 10px;">
                                                    <div style="flex: 1;">
                                                        <label style="font-size: 0.85em; font-weight: bold;">Trạng thái đơn hàng:</label>
                                                        <select name="status" class="filter-select">
                                                            <option value="Chờ xác nhận" ${o.status == 'Chờ xác nhận' ? 'selected' : ''}>Chờ xác nhận</option>
                                                            <option value="Đang giao" ${o.status == 'Đang giao' ? 'selected' : ''}>Đang giao</option>
                                                            <option value="Đã giao" ${o.status == 'Đã giao' ? 'selected' : ''}>Đã giao</option>
                                                            <option value="Đã hủy" ${o.status == 'Đã hủy' ? 'selected' : ''}>Đã hủy</option>
                                                            <option value="Hoàn hàng" ${o.status == 'Hoàn hàng' ? 'selected' : ''}>Hoàn hàng</option>
                                                        </select>
                                                    </div>

                                                    <div style="flex: 1;">
                                                        <label style="font-size: 0.85em; font-weight: bold;">Trạng thái thanh toán:</label>
                                                        <select name="paymentStatus" class="filter-select" style="width: 100%; padding: 8px; margin-top: 5px;">
                                                            <option value="Chưa thanh toán" ${o.paymentStatus == 'Chưa thanh toán' ? 'selected' : ''}>Chưa thanh toán</option>
                                                            <option value="Đã thanh toán" ${o.paymentStatus == 'Đã thanh toán' ? 'selected' : ''}>Đã thanh toán</option>
                                                        </select>
                                                    </div>
                                                </div>

                                                <button type="submit" style="background: #27ae60; color: white; border: none; padding: 10px; border-radius: 4px; cursor: pointer; font-weight: bold;">
                                                    Cập nhật đơn hàng
                                                </button>

                                            </form>
                                        </div>
                                        <div class="summary-horizontal-card">
                                            <div class="summary-section">
                                                <span class="summary-title">Tạm tính</span>
                                                <span class="summary-amount"><fmt:formatNumber value="${o.subTotal}" pattern="#,###"/> VND</span>
                                            </div>

                                            <div class="summary-divider-vertical"></div>

                                            <div class="summary-section">
                                                <span class="summary-title">Thuế (8%)</span>
                                                <span class="summary-amount"><fmt:formatNumber value="${o.taxAmount}" pattern="#,###"/> VND</span>
                                            </div>

                                            <div class="summary-divider-vertical"></div>

                                            <div class="summary-section">
                                                <span class="summary-title">Phí giao hàng</span>
                                                <span class="summary-amount"><fmt:formatNumber value="${o.shippingFee}" pattern="#,###"/> VND</span>
                                            </div>

                                            <div class="summary-section total-box">
                                                <span class="summary-title">Tổng cộng</span>
                                                <span class="summary-amount total-final"><fmt:formatNumber value="${o.totalOrder}" pattern="#,###"/> VND</span>
                                            </div>
                                        </div> </div>

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
<div id="orderModal" class="modal" style="display:none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5);">
    <div class="modal-content" style="background: #fff; margin: 5% auto; padding: 20px; width: 50%; border-radius: 8px; position: relative;">
        <span onclick="closeModal()" style="position: absolute; right: 15px; top: 10px; cursor: pointer; font-size: 24px;">&times;</span>
        <div id="modalContainer">
        </div>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/js/order_admin.js"></script>

</html>