<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="order-detail-container">
    <h4>Chi tiết đơn hàng #${order.id}</h4>
    <p>Khách hàng: <strong>${order.fullName}</strong></p>

    <table class="table">
        <thead>
        <tr>
            <th>Sản phẩm</th>
            <th>Số lượng</th>
            <th>Tổng cộng</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${details}" var="d">
            <tr>
                <td>${d.productName}</td>
                <td>${d.quantity}</td>
                <td>
                    <fmt:formatNumber value="${d.total}" pattern="#,###"/> VND
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="status-update-form" style="margin-top: 20px;">
        <h5>Cập nhật trạng thái xử lý</h5>
        <form action="UpdateOrderStatus" method="post">
            <input type="hidden" name="orderId" value="${order.id}">
            <select name="status">
                <option value="Chờ xác nhận" ${order.status == 'Chờ xác nhận' ? 'selected' : ''}>Chờ xác nhận</option>
                <option value="Đang xử lý" ${order.status == 'Đang xử lý' ? 'selected' : ''}>Đang xử lý</option>
                <option value="Đã gửi" ${order.status == 'Đã gửi' ? 'selected' : ''}>Đã gửi</option>
                <option value="Đã giao" ${order.status == 'Đã giao' ? 'selected' : ''}>Đã giao</option>
                <option value="Đã hủy" ${order.status == 'Đã hủy' ? 'selected' : ''}>Đã hủy</option>
            </select>
            <button type="submit">Lưu</button>
        </form>
    </div>
</div>
