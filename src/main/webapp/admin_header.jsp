<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="header">
    <div class="logo-placeholder">
        <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo Modern Homes">
        <p class="logo">HOME DECOR</p>
    </div>

    <div class="header-icons">
        <div class="gmail-dropdown">
            <i class="fa-solid fa-envelope gmail-icon"></i>
            <div id="gmailMenuContent" class="dropdown-content gmail-content">
                <div class="dropdown-header">Gmail</div>
                <p class="no-messages-text">Không có Gmail nào.</p>
                <div class="dropdown-divider"></div>
                <a href="#" class="view-all-link">Mở Gmail</a>
            </div>
        </div>

        <div class="notification-dropdown">
            <i class="fa-solid fa-bell notification-icon"></i>
            <div id="notificationMenuContent" class="dropdown-content notification-content">
                <div class="dropdown-header">Thông Báo Mới (5)</div>
                <a href="#">Đơn hàng mới #1001</a>
                <a href="#">Sản phẩm hết hàng</a>
                <a href="#">Khách hàng mới đăng ký</a>
                <a href="#">Đơn hàng #1005 vừa được hủy bỏ</a>
                <a href="#">Cần duyệt 3 đánh giá sản phẩm mới</a>
                <div class="dropdown-divider"></div>
                <a href="#" class="view-all-link">Xem tất cả</a>
            </div>
        </div>

        <div class="user-dropdown">
            <i class="fas fa-user-circle user-logo" ></i>
            <div id="userMenuContent" class="dropdown-content">
                <a href="admin_thong_tin_tai_khoan.html"> Thông tin tài khoản</a>
                <a href="#"> Đổi mật khẩu</a>
                <div class="dropdown-divider"></div>
                <a href="#" class="logout-link"> Đăng xuất</a>
            </div>
        </div>
    </div>
</header>