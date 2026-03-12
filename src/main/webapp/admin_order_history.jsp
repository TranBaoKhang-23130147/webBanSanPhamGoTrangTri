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

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

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

                            <i class="fas fa-chevron-left"></i>
                            Quay lại hồ sơ khách hàng

                        </a>

                        <h2 class="page-title">
                            Lịch sử đơn hàng: ${customer.displayName}
                        </h2>

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

                                <td>
                                    <b>#${o.id}</b>
                                </td>

                                <td>
                                    <fmt:formatDate value="${o.createAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>

                                <td>
                                    <span class="badge">${o.paymentStatus}</span>
                                </td>

                                <td class="order-price">

                                    <fmt:formatNumber value="${o.totalOrder}" pattern="#,###"/> VND

                                </td>

                                <td>
                                    <span class="badge status-pending">
                                            ${o.status}
                                    </span>
                                </td>

                                <td>

                                    <button onclick="showDetail(${o.id})"
                                            class="btn-view-detail">

                                        <i class="fas fa-eye"></i>
                                        Chi tiết

                                    </button>


                                    <div id="data-order-${o.id}" class="order-detail-hidden">

                                        <h3 class="order-detail-title">

                                            Chi tiết đơn hàng #${o.id}

                                        </h3>


                                        <div class="order-info-row">

                                            <div>

                                                <p><strong>Người nhận:</strong> ${o.fullName}</p>

                                                <p><strong>Địa chỉ:</strong> ${o.address}</p>

                                            </div>

                                            <div class="text-right">

                                                <p>

                                                    <strong>Ngày đặt:</strong>

                                                    <fmt:formatDate value="${o.createAt}" pattern="dd/MM/yyyy HH:mm"/>

                                                </p>

                                            </div>

                                        </div>


                                        <table class="order-detail-table">

                                            <tr class="order-table-header">

                                                <th>Sản phẩm</th>
                                                <th>SL</th>
                                                <th>Đơn giá</th>

                                            </tr>

                                            <c:forEach items="${o.details}" var="d">

                                                <tr class="order-row-detail">

                                                    <td class="td-padding">
                                                            ${d.productName}
                                                    </td>

                                                    <td class="td-center">
                                                        x${d.quantity}
                                                    </td>

                                                    <td class="td-right">

                                                        <fmt:formatNumber value="${d.total}" pattern="#,###"/> VND

                                                    </td>

                                                </tr>

                                            </c:forEach>

                                        </table>


                                        <div class="order-update-box">

                                            <form action="${pageContext.request.contextPath}/admin/update-order-status"
                                                  method="post"
                                                  class="update-form">

                                                <input type="hidden" name="orderId" value="${o.id}">
                                                <input type="hidden" name="userId" value="${customer.id}">

                                                <label>Cập nhật trạng thái:</label>

                                                <select name="status" class="filter-select">

                                                    <option value="Chờ xác nhận"
                                                        ${o.status == 'Chờ xác nhận' ? 'selected' : ''}>
                                                        Chờ xác nhận
                                                    </option>

                                                    <option value="Đang giao"
                                                        ${o.status == 'Đang giao' ? 'selected' : ''}>
                                                        Đang giao
                                                    </option>

                                                    <option value="Đã giao"
                                                        ${o.status == 'Đã giao' ? 'selected' : ''}>
                                                        Đã giao
                                                    </option>

                                                    <option value="Đã hủy"
                                                        ${o.status == 'Đã hủy' ? 'selected' : ''}>
                                                        Đã hủy
                                                    </option>

                                                    <option value="Hoàn hàng"
                                                        ${o.status == 'Hoàn hàng' ? 'selected' : ''}>
                                                        Hoàn hàng
                                                    </option>

                                                </select>


                                                <button type="submit" class="btn-save-status">

                                                    Lưu

                                                </button>

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