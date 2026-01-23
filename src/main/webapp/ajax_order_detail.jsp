<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<span class="order-close" onclick="closeOrderDetail()">&times;</span>

<h3 class="modal-title">Chi tiết đơn hàng #${order.id}</h3>

<div class="order-summary-header">
    <p><b>Ngày đặt:</b> <fmt:formatDate value="${order.createAt}" pattern="dd/MM/yyyy HH:mm"/></p>
    <p><b>Trạng thái:</b> <span class="status-badge">${order.status}</span></p>
    <p><b>Ghi chú:</b> ${order.note}</p>
</div>

<table class="order-detail-table">
    <thead>
    <tr>
        <th>Sản phẩm</th>
        <th>Số lượng</th>
        <th style="text-align: right;">Thành tiền</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${details}" var="d">
        <tr>
            <td>
                <p style="font-weight: bold;">${d.productName}</p>
                <small>Mã biến thể: ${d.productVariantId}</small>
            </td>
            <td>x${d.quantity}</td>
            <td style="text-align: right;">
                <fmt:formatNumber value="${d.total}" pattern="#,###"/> VND
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<div class="modal-footer-price">
    <p>Phí vận chuyển: <b><fmt:formatNumber value="${order.shippingFee}" pattern="#,###"/> VND</b></p>
    <p class="final-price">Tổng cộng: <span><fmt:formatNumber value="${order.totalOrder}" pattern="#,###"/> VND</span></p>
</div>