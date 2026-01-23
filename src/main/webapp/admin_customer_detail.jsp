<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>CHI TIẾT KHÁCH HÀNG - ${customer.username}</title>
    <link rel="stylesheet" href="../css/admin_customer_style.css">
    <link rel="stylesheet" href="../css/admin_profile_style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        .detail-card { background: white; border-radius: 12px; padding: 25px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); margin-top: 20px; }
        .back-btn { background: #6c757d; color: white; padding: 8px 15px; border-radius: 5px; text-decoration: none; margin-bottom: 20px; display: inline-block; }
        .info-grid { display: grid; grid-template-columns: 300px 1fr; gap: 30px; }
        .avatar-box { text-align: center; border-right: 1px solid #eee; padding-right: 30px; }
        .addr-item { background: #f8f9fc; border-radius: 8px; padding: 15px; margin-bottom: 10px; border-left: 4px solid #4e73df; }
    </style>
</head>
<body>
<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="content-area">
            <a href="customers" class="back-btn"><i class="fas fa-arrow-left"></i> Quay lại danh sách</a>

            <div class="detail-card">
                <h2 style="color: #4e73df; margin-bottom: 25px; border-bottom: 2px solid #f1f1f1; padding-bottom: 10px;">
                    Hồ Sơ Khách Hàng: ${customer.displayName}
                </h2>

                <div class="info-grid">
                    <div class="avatar-box">
                        <img src="${not empty customer.avatarUrl ? customer.avatarUrl : 'https://i.pinimg.com/474x/f1/7a/28/f17a28e82524a427ea89fd3c1b5f9266.jpg'}"
                             style="width: 150px; height: 150px; border-radius: 50%; object-fit: cover; border: 3px solid #eee;">
                        <h3 style="margin-top: 15px;">${customer.username}</h3>
                        <p class="status ${customer.status == 'Active' ? 'active-status' : 'inactive-status'}" style="display: inline-block; margin-top: 10px;">
                            ${customer.status == 'Active' ? 'Hoạt Động' : 'Đã Khóa'}
                        </p>
                    </div>

                    <div>
                        <h4 style="margin-bottom: 15px;"><i class="fas fa-info-circle"></i> Thông tin cơ bản</h4>
                        <table style="width: 100%; border-collapse: collapse;">
                            <tr><td style="padding: 8px 0; color: #666;">Email:</td><td><strong>${customer.email}</strong></td></tr>
                            <tr><td style="padding: 8px 0; color: #666;">Số điện thoại:</td><td><strong>${customer.phone != null ? customer.phone : 'Chưa cập nhật'}</strong></td></tr>
                            <tr><td style="padding: 8px 0; color: #666;">Giới tính:</td><td><strong>${customer.gender != null ? customer.gender : 'Chưa cập nhật'}</strong></td></tr>
                            <tr><td style="padding: 8px 0; color: #666;">Ngày sinh:</td><td><strong><fmt:formatDate value="${customer.birthDate}" pattern="dd/MM/yyyy"/></strong></td></tr>
                            <tr><td style="padding: 8px 0; color: #666;">Ngày tạo:</td><td><strong><fmt:formatDate value="${customer.createAt}" pattern="dd/MM/yyyy HH:mm"/></strong></td></tr>
                        </table>

                        <h4 style="margin-top: 30px; margin-bottom: 15px;"><i class="fas fa-map-marker-alt"></i> Sổ địa chỉ</h4>
                        <c:if test="${empty addresses}">
                            <p style="color: #999; font-style: italic;">Khách hàng chưa thêm địa chỉ nào.</p>
                        </c:if>
                        <c:forEach items="${addresses}" var="a">
                            <div class="addr-item">
                                <strong>${a.name}</strong> - ${a.phone}
                                <c:if test="${a.isDefault == 1}"><span style="color: #4e73df; font-size: 11px; margin-left: 10px;">[MẶC ĐỊNH]</span></c:if>
                                <div style="font-size: 13px; margin-top: 5px; color: #555;">${a.detail}, ${a.commune}, ${a.district}, ${a.province}</div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>