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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_customer_detail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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

                        <img id="user-avatar-display"
                             src="${pageContext.request.contextPath}${not empty customer.avatarUrl ? customer.avatarUrl : '/img/logo.png'}"
                             alt="Avatar"
                             style="width: 150px; height: 150px; border-radius: 50%; object-fit: cover; border: 3px solid #eee;"
                             onerror="this.src='${pageContext.request.contextPath}/img/logo.png';">
                        <h3 style="margin-top: 15px;">${customer.username}</h3>
                        <p class="status ${customer.status == 'Active' ? 'active-status' : 'inactive-status'}" style="display: inline-block; margin-top: 10px;">
                            ${customer.status == 'Active' ? 'Hoạt Động' : 'Đã Khóa'}
                        </p>

                        <div style="text-align: left; margin-top: 25px; padding-left: 20px;">
                            <h5 style="color: #555; font-size: 14px; text-transform: uppercase; margin-bottom: 12px;">
                                <i class="fas fa-cog"></i> CÀI ĐẶT & QUYỀN RIÊNG TƯ
                            </h5>
                            <ul style="list-style: none; padding: 0; margin: 0;">
                                <li style="margin-bottom: 10px;">
                                    <a href="javascript:void(0)" onclick="openEditForm()" style="text-decoration: none; color: #4e73df; font-size: 14px;">
                                        <i class="fas fa-user-circle" style="width: 20px;"></i> Thông tin cá nhân
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:void(0)" onclick="openPassForm()" style="text-decoration: none; color: #4e73df; font-size: 14px;">
                                        <i class="fas fa-shield-alt" style="width: 20px;"></i> Mật khẩu và bảo mật
                                    </a>
                                </li>
                                <li style="margin-bottom: 10px;margin-top: 10px;">
                                <a href="${pageContext.request.contextPath}/AdminOrderHistoryController?userId=${customer.id}"
                                   style="text-decoration: none; color: #4e73df; font-size: 14px; display:flex; align-items:center;">
                                    <i class="fas fa-shopping-cart" style="width: 20px;"></i>
                                    Xem lịch sử mua hàng
                                </a>
                            </li>

                            </ul>
                        </div>
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
<div id="editProfileModal" class="modal-overlay" style="display:none;">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Chỉnh sửa hồ sơ: ${customer.username}</h3>
            <span class="close-btn" onclick="closeEditForm()">&times;</span>
        </div>

        <form action="${pageContext.request.contextPath}/AdminUpdateCustomerController" method="post">
            <input type="hidden" name="userId" value="${customer.id}">

            <div class="profile-left-custom">
                <div class="avatar-edit">
                    <img id="user-avatar-display"
                         src="${pageContext.request.contextPath}${not empty customer.avatarUrl ? customer.avatarUrl : '/img/logo.png'}"
                         alt="Avatar"
                         style="width:120px; height:120px; object-fit:cover; border-radius:50%; border: 2px solid #4e73df; padding: 3px;"
                         onerror="this.src='${pageContext.request.contextPath}/img/logo.png';">

                    <button type="button" class="camera-btn" onclick="selectAvatarWithCKFinder()">
                        <i class="fas fa-camera"></i>
                    </button>

                    <input type="hidden" name="avatar_id" id="user-avatar-url" value="${customer.avatarUrl}">
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Tên đăng nhập (Username):</label>
                        <input type="text" name="fullName" value="${customer.username}">
                    </div>

                    <div class="form-group">
                        <label>Tên hiển thị:</label>
                        <input type="text" name="displayName" value="${customer.displayName}">
                    </div>

                    <div class="form-group">
                        <label>Giới tính:</label>
                        <div class="radio-group">
                            <input type="radio" id="nam" name="gender" value="Nam" ${customer.gender == 'Nam' ? 'checked' : ''}>
                            <label for="nam">Nam</label>
                            <input type="radio" id="nu" name="gender" value="Nữ" ${customer.gender == 'Nữ' ? 'checked' : ''}>
                            <label for="nu">Nữ</label>
                            <input type="radio" id="khac" name="gender" value="Khác" ${customer.gender == 'Khác' ? 'checked' : ''}>
                            <label for="khac">Khác</label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Ngày sinh:</label>
                        <input type="date" name="birthDate" value="<fmt:formatDate value='${customer.birthDate}' pattern='yyyy-MM-dd'/>">
                    </div>

                    <div class="form-group">
                        <label>Số điện thoại:</label>
                        <input type="text" name="phone" value="${customer.phone}">
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="cancel-btn" onclick="closeEditForm()">Hủy</button>
                    <button type="submit" class="save-btn-custom">Lưu thay đổi</button>
                </div>
            </div>
        </form>
    </div>
</div>
<div id="changePasswordModal" class="modal-overlay" style="display:none;">
    <div class="modal-content">
        <div class="modal-header">
            <h3>Đổi mật khẩu: ${customer.username}</h3>
            <span class="close-btn" onclick="closePassForm()">&times;</span>
        </div>

        <form action="${pageContext.request.contextPath}/AdminChangePasswordController" method="post">
            <input type="hidden" name="userId" value="${customer.id}">

            <div class="form-grid">
                <div class="form-group">
                    <label>Mật khẩu mới *</label>
                    <input type="password" name="newPassword" id="newPassword" required placeholder="Nhập mật khẩu mới">
                </div>

                <div class="form-group">
                    <label>Xác nhận mật khẩu mới *</label>
                    <input type="password" name="confirmPassword" id="confirmPassword" required placeholder="Nhập lại mật khẩu mới">
                </div>
                <small id="passError" style="color: red; display: none;">Mật khẩu xác nhận không khớp!</small>
            </div>

            <div class="modal-footer">
                <button type="button" class="cancel-btn" onclick="closePassForm()">Hủy</button>
                <button type="submit" class="save-btn-custom">Cập nhật mật khẩu</button>
            </div>
        </form>
    </div>
</div>

</body>

<script>
    const contextPath = "${pageContext.request.contextPath}";
</script>

<script src="${pageContext.request.contextPath}/ckfinder/ckfinder.js"></script>

<script src="${pageContext.request.contextPath}/js/admin_customer_detail.js"></script>
</html>