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
            <a href="${pageContext.request.contextPath}/AdminOrderHistoryController?userId=${customer.id}" class="history-btn">
                <i class="fas fa-shopping-cart"></i> Xem lịch sử mua hàng
            </a>
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
<style>
    .modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); z-index: 9999; display: flex; align-items: center; justify-content: center; backdrop-filter: blur(4px); }
    .modal-content { background: white; width: 450px; padding: 25px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.3); animation: slideDown 0.3s ease; }
    .modal-header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eee; padding-bottom: 15px; margin-bottom: 20px; }
    .close-btn { font-size: 28px; cursor: pointer; color: #aaa; }
    .close-btn:hover { color: red; }

    .avatar-edit { text-align: center; position: relative; margin-bottom: 20px; }
    .camera-btn { position: absolute; bottom: 0; right: 40%; background: #4e73df; color: white; border: none; border-radius: 50%; width: 30px; height: 30px; cursor: pointer; }

    .form-grid { display: flex; flex-direction: column; gap: 15px; }
    .form-group label { display: block; font-weight: bold; margin-bottom: 5px; color: #555; font-size: 14px; }
    .form-group input[type="text"], .form-group input[type="date"] { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px; box-sizing: border-box; }

    .radio-group { display: flex; gap: 20px; padding: 5px 0; }
    .modal-footer { margin-top: 25px; display: flex; justify-content: flex-end; gap: 10px; }

    .save-btn-custom { background: #4e73df; color: white; border: none; padding: 10px 20px; border-radius: 8px; cursor: pointer; font-weight: bold; }
    .cancel-btn { background: #eee; color: #333; border: none; padding: 10px 20px; border-radius: 8px; cursor: pointer; }

    @keyframes slideDown { from { transform: translateY(-50px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
</style>
</body>
<script>
    function openEditForm() {
        document.getElementById('editProfileModal').style.display = 'flex';
    }

    function closeEditForm() {
        document.getElementById('editProfileModal').style.display = 'none';
    }

    // Đóng khi click ra ngoài
    window.onclick = function(event) {
        let modal = document.getElementById('editProfileModal');
        if (event.target == modal) closeEditForm();
    }
</script>
<%-- 1. Khai báo thư viện CKFinder (đảm bảo đúng đường dẫn) --%>
<script src="${pageContext.request.contextPath}/ckfinder/ckfinder.js"></script>

<script>
    function selectAvatarWithCKFinder() {
        var finder = new CKFinder();
        // Đường dẫn đến thư mục ckfinder trong project của bạn
        finder.basePath = '${pageContext.request.contextPath}/ckfinder/';

        finder.selectActionFunction = function(fileUrl) {
            var contextPath = "${pageContext.request.contextPath}" || "";
            var relativeUrl = fileUrl || "";

            // Xử lý cắt Context Path để lấy đường dẫn tương đối
            if (contextPath && relativeUrl.startsWith(contextPath)) {
                relativeUrl = relativeUrl.substring(contextPath.length);
            }

            // Đảm bảo bắt đầu bằng dấu /
            if (relativeUrl && !relativeUrl.startsWith('/')) {
                relativeUrl = '/' + relativeUrl;
            }

            // Cập nhật giá trị vào input hidden để gửi form POST
            var input = document.getElementById('user-avatar-url');
            if (input) {
                input.value = relativeUrl;
            }

            // Cập nhật ảnh hiển thị ngay lập tức trên giao diện
            var preview = document.getElementById('user-avatar-display');
            if (preview) {
                preview.src = contextPath + relativeUrl;
            }
        };

        finder.popup();
    }
    function openPassForm() {
        document.getElementById('changePasswordModal').style.display = 'flex';
    }

    function closePassForm() {
        document.getElementById('changePasswordModal').style.display = 'none';
    }

    // Bổ sung vào sự kiện window.onclick đã có của bạn
    window.onclick = function(event) {
        let editModal = document.getElementById('editProfileModal');
        let passModal = document.getElementById('changePasswordModal');

        if (event.target == editModal) closeEditForm();
        if (event.target == passModal) closePassForm();
    }

    // Kiểm tra mật khẩu khớp nhau trước khi submit (Tùy chọn)
    document.querySelector('#changePasswordModal form').onsubmit = function(e) {
        let pass = document.getElementById('newPassword').value;
        let confirm = document.getElementById('confirmPassword').value;
        if (pass !== confirm) {
            document.getElementById('passError').style.display = 'block';
            e.preventDefault();
            return false;
        }
    };
</script>
</html>