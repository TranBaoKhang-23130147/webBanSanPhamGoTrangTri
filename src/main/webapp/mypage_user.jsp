<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="model.User,model.Order,model.OrderDetail,java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    // Lấy đúng tên biến LOGGED_USER từ Session
    User user = (User) session.getAttribute("LOGGED_USER");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - TRANG CỦA TÔI</title>
    <link rel="icon" type="image/png" href="img/logo.png" class="lo">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link rel="stylesheet" href="css/homepage_style.css">
    <link rel="stylesheet" href="css/mypage_style.css">
</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

<div class="dashboard-container">

    <div class="sidebar">
        <div class="user-info">
            <div class="avatar-container">
                <img src="https://i.pinimg.com/474x/f1/7a/28/f17a28e82524a427ea89fd3c1b5f9266.jpg" alt="Avatar" class="avatar-img"/>
            </div>
            <div class="user-name"><%= user.getUsername() %></div>
        </div>

        <div class="menu-list">
            <div class="menu-item-dropdown active" id="tai-khoan-menu">
                <a href="#" class="menu-link dropdown-toggle">
                    <i class="fas fa-user"></i> Tài Khoản
                    <i class="fas fa-chevron-down toggle-icon"></i>
                </a>
                <div class="dropdown-content">
                    <a href="#" class="tab-link active" data-tab="ho-so"><i class="fas fa-id-card"></i> Hồ sơ</a>
                    <a href="MyPageServlet?tab=thanh-toan" class="tab-link" data-tab="thanh-toan">
                        <i class="fas fa-credit-card"></i> Thanh toán
                    </a>
                    <a href="#" class="tab-link" data-tab="dia-chi"><i class="fas fa-map-marker-alt"></i> Địa chỉ</a>
                    <a href="#" class="tab-link" data-tab="bao-mat"><i class="fas fa-shield-alt"></i> Bảo mật</a>
                    <a href="#" class="tab-link" data-tab="thong-bao"><i class="fas fa-bell"></i> Thông báo</a>
                </div>
            </div>
            <a href="#" class="tab-link menu-link" data-tab="don-hang"><i class="fas fa-shopping-cart"></i> Đơn hàng</a>
            <a href="#" class="tab-link menu-link" data-tab="tin-nhan"><i class="fas fa-comment-dots"></i> Tin nhắn</a>
        </div>
    </div>

    <main class="main-content">
        <div id="ho-so" class="tab-content active">
            <h2>Hồ sơ</h2>
            <p class="subtitle">Quản lý thông tin hồ sơ để giữ an toàn cho tài khoản của bạn</p>

            <%-- Thêm thẻ form bao toàn bộ container --%>
            <form action="UpdateProfileController" method="post">
                <div class="profile-container">
                    <div class="profile-left">
                        <div class="avatar-edit">
                            <img src="${not empty user.avatarUrl ? user.avatarUrl : 'https://i.pinimg.com/474x/f1/7a/28/f17a28e82524a427ea89fd3c1b5f9266.jpg'}" class="main-avatar"/>
                            <button type="button" class="camera-btn"><i class="fas fa-camera"></i></button>
                        </div>
                        <div class="form-group">
                            <label for="ho-ten">Tên hiển thị:</label>
                            <%-- Thêm name="fullName" --%>
                            <input type="text" name="fullName" value="<%= user.getUsername() != null ? user.getUsername() : "" %>">
                        </div>
                        <div class="form-group">
                            <label for="ten-hien-thi">Tên khác:</label>
                            <%-- Thêm name="displayName" --%>
                            <input type="text" name="displayName" id="ten-hien-thi" value="<%= user.getDisplayName() != null ? user.getDisplayName() : "" %>">
                        </div>
                        <div class="form-group">
                            <label>Giới tính :</label>
                            <div class="radio-group">
                                <%-- Sửa name="gender" cho cả 3 cái --%>
                                <input type="radio" id="nam" name="gender" value="Nam" <%= "Nam".equals(user.getGender()) ? "checked" : "" %>>
                                <label for="nam">Nam</label>
                                <input type="radio" id="nu" name="gender" value="Nữ" <%= "Nữ".equals(user.getGender()) || user.getGender() == null ? "checked" : "" %>>
                                <label for="nu">Nữ</label>
                                <input type="radio" id="khac" name="gender" value="Khác" <%= "Khác".equals(user.getGender()) ? "checked" : "" %>>
                                <label for="khac">Khác</label>
                            </div>
                        </div>
                    <div class="form-group">
                        <label>Ngày sinh :</label>
                        <%-- Để đơn giản, dùng date input sẽ tự động có Ngày/Tháng/Năm mà không cần code phức tạp --%>
                        <input type="date" name="birthDate" value="<%= user.getBirthDate() %>" style="padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                    </div>
                    <button type="submit" class="save-btn">Lưu</button>
                </div>
                    <div class="profile-right">
                        <h3>Thông tin liên hệ</h3>
                        <div class="contact-item">
                            <label>Số điện thoại :</label>

                        <%-- Thay span thành input để người dùng nhập được sđt mới --%>
                            <input type="text" name="phone" value="<%= user.getPhone() != null ? user.getPhone() : "" %>" placeholder="Số điện thoại">
                        </div>
                        <div class="contact-item">
                            <label>Email :</label>

                        <%-- Email thường cố định, để readonly --%>
                            <input type="email" name="email" value="<%= user.getEmail() %>" readonly style="border:none; background:none;">
                        </div>
                        <h3>Liên kết</h3>
                        <div class="contact-item link-item">
                            <i class="fab fa-facebook-square"></i>
                            <span>Facebook</span>
                            <button class="link-btn">Liên kết</button>
                        </div>
                        <div class="contact-item link-item">
                            <i class="fab fa-google"></i>
                            <span>Google</span>
                            <button class="delete-btn">Xóa</button>
                        </div>
                        <div class="contact-item link-item">
                            <i class="fas fa-comments"></i>
                            <span>Zalo</span>
                            <button class="delete-btn">Xóa</button>
                        </div>
                    </div>
                </div>
            </form>


        </div>
        <div id="thanh-toan" class="tab-content">
<%--            <div class="header-with-button">--%>

<%--            <h2>Quản lý thanh toán</h2>--%>
<%--            <button class="add-btn"><i class="fas fa-plus"></i> Thêm</button>--%>
<%--            </div>--%>
    <div class="header-with-button">
        <h2>Quản lý thanh toán</h2>

        <button class="add-btn" onclick="document.getElementById('addForm').style.display='block'">
            <i class="fas fa-plus"></i> Thêm thẻ
        </button>
    </div>

    <div id="addForm" style="display:none; background: #f9f9f9; padding: 20px; border: 1px solid #ddd; margin-bottom: 20px; border-radius: 8px;">
        <h3 style="margin-bottom: 15px;">Nhập thông tin thẻ mới</h3>
        <form action="AddPaymentServlet" method="post">
            <div class="form-group">
                <label>Loại thẻ:</label>
                <select name="type" style="width: 100%; padding: 8px; margin-bottom: 10px;">
                    <option value="Visa">Visa</option>
                    <option value="MasterCard">MasterCard</option>
                    <option value="JCB">JCB</option>
                </select>
            </div>
            <div class="form-group">
                <label>Ngày hết hạn:</label>
                <input type="date" name="duration" required style="width: 100%; padding: 8px; margin-bottom: 15px;">
            </div>
            <button type="submit" class="save-btn">Lưu thẻ</button>
            <button type="button" class="delete-btn" onclick="document.getElementById('addForm').style.display='none'">Hủy</button>
        </form>
    </div>
            <div class="payment-management">

<%--                <div class="card-item">--%>
<%--                    <div class="card-display">--%>
<%--                        <p class="card-number">Số thẻ: **** **** **** 9999</p>--%>
<%--                        <p class="card-name">TRAN THI THUY KIEU</p>--%>
<%--                        <p class="card-date">Date: 31/12/2016</p>--%>
<%--                        <img src="https://i.pinimg.com/474x/f1/7a/28/f17a28e82524a427ea89fd3c1b5f9266.jpg" alt="VISA" class="visa-logo">--%>
<%--                    </div>--%>
<%--                    <div class="card-actions">--%>
<%--                        <button class="default-btn">Mặc định</button>--%>
<%--                        <button class="edit-btn">Sửa</button>--%>
<%--                        <button class="delete-btn">Xóa</button>--%>
<%--                    </div>--%>
<%--                </div>--%>
    <c:forEach items="${listPayments}" var="p">
        <div class="card-item">
            <div class="card-display">
                <p class="card-number">Phương thức: ${p.type}</p>
                <p class="card-name">${user.username}</p>
                <p class="card-date">Hết hạn: ${p.duration}</p>
                <img src="img/visa.png" class="visa-logo">
            </div>

            <div class="card-actions" style="margin-top: 15px; display: flex; gap: 10px; justify-content: flex-end;">
                <button class="add-btn" style="background-color: #2ecc71; padding: 5px 15px; font-size: 13px;"
                        onclick="editCard('${p.id}', '${p.type}', '${p.duration}')">
                    <i class="fas fa-edit"></i> Sửa
                </button>

                <a href="DeletePaymentServlet?id=${p.id}"
                   class="add-btn"
                   style="background-color: #e74c3c; padding: 5px 15px; font-size: 13px; text-decoration: none; color: white;"
                   onclick="return confirm('Bạn có chắc muốn xóa thẻ này?')">
                    <i class="fas fa-trash"></i> Xóa
                </a>
            </div>
        </div>
    </c:forEach>
            </div>
        </div>

        <div id="dia-chi" class="tab-content">
            <div class="header-with-button">
            <h2>Địa Chỉ</h2>
            <button class="add-btn float-right"><i class="fas fa-plus"></i> Thêm</button>
            </div>
            <div class="address-list">
                <div class="address-item default-address">
                    <div class="address-icon"><i class="fas fa-home"></i></div>
                    <div class="address-details">
                        <p class="address-name">Trần Thị Thúy Kiều <span>0399999970</span></p>
                        <p class="address-line">Thủ Đức, TP.HCM</p>
                    </div>
                    <div class="address-actions">
                        <span class="default-tag">Mặc định</span>
                        <button class="change-btn">Thay đổi</button>
                        <button class="set-default-btn">Đặt làm mặc định</button>
                        <button class="delete-btn">Xóa</button>
                    </div>
                </div>
                <div class="address-item">
                    <div class="address-icon"><i class="fas fa-building"></i></div>
                    <div class="address-details">
                        <p class="address-name">Trần Thị Thúy Kiều <span>0399999970</span></p>
                        <p class="address-line">Thủ Đức, TP.HCM</p>
                    </div>
                    <div class="address-actions">
                        <button class="set-default-btn">Đặt làm mặc định</button>

                        <button class="change-btn">Thay đổi</button>
                        <button class="delete-btn">Xóa</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="bao-mat" class="tab-content">
            <h2>Mật khẩu</h2>
            <h3>Đổi mật khẩu</h3>
            <div class="password-change-form">
                <div class="form-group">
                    <label for="current-pass">Mật khẩu hiện tại <span class="required">*</span></label>
                    <input type="password" id="current-pass">
                    <i class="fas fa-eye password-toggle"></i>
                </div>
                <div class="form-group">
                    <label for="new-pass">Mật khẩu mới <span class="required">*</span></label>
                    <input type="password" id="new-pass">
                    <i class="fas fa-eye password-toggle"></i>
                </div>
                <div class="form-group">
                    <label for="confirm-pass">Xác nhận mật khẩu <span class="required">*</span></label>
                    <input type="password" id="confirm-pass">
                    <i class="fas fa-eye password-toggle"></i>
                </div>
                <div class="password-requirements">
                    <h4>Yêu cầu mật khẩu:</h4>
                    <p>Đảm bảo các yêu cầu sau được đáp ứng:</p>
                    <ul>
                        <li>Tối thiểu 8 ký tự - càng dài càng tốt</li>
                        <li>Ít nhất một ký tự viết thường</li>
                        <li>Ít nhất một ký tự viết hoa</li>
                        <li>Ít nhất một ký tự số, ký hiệu hoặc khoảng trắng</li>
                    </ul>
                </div>
                <button class="save-btn float-right">Lưu</button>
            </div>
        </div>


        <div id="don-hang" class="tab-content">
            <h2>Đơn hàng</h2>
            <div class="order-summary">
                <div class="summary-box">
                    <div class="summary-number">${countOrder}</div>
                    <div class="summary-label">Đơn hàng</div>
                </div>
                <div class="summary-separator">|</div>
                <div class="summary-box">
                    <div class="summary-number">
                        <fmt:formatNumber value="${totalSpent / 1000000}" maxFractionDigits="1"/> Tr
                    </div>
                    <div class="summary-label">Tổng tiền tích lũy</div>
                </div>
            </div>

            <div class="order-list">
                <c:forEach items="${listO}" var="order">
                    <c:forEach items="${order.details}" var="d">
                        <div class="order-item">
                            <div class="product-info">
                                <img src="${d.productImg}" alt="Product" class="product-img">
                                <div class="product-details">
                                    <p class="product-name">${d.productName}</p>
                                    <div class="product-specs">
                                        <p class="product-color"><span>Màu Sắc:</span> ${d.color}</p>
                                        <p class="product-size"><span>Kích thước:</span> ${d.size}</p>
                                        <p class="quantity"><span>Số lượng:</span> ${d.quantity}</p>
                                    </div>
                                    <span class="delivery-status">${order.status}</span>
                                </div>
                            </div>
                            <div class="order-total">
                                <div class="order-date">
                                    <fmt:formatDate value="${order.createAt}" pattern="dd/MM/yyyy"/>
                                </div>
                                <p>Tổng thanh toán: <fmt:formatNumber value="${d.total}" pattern="#,###"/> VNĐ</p>
                                <div class="order-actions-group">
                                    <button class="view-details-btn">Xem chi tiết</button>
                                    <button class="support-btn">Hỗ trợ</button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:forEach>
            </div>
        </div>
        <div id="thong-bao" class="tab-content">
            <h2>Thông báo</h2>
            <p>Không có thông báo mới.</p>
        </div>
        <div id="tin-nhan" class="tab-content">
            <h2>Tin nhắn</h2>
            <p>Không có tin nhắn nào.</p>
        </div>

    </main>
</div>

<jsp:include page="footer.jsp"></jsp:include>

<script src="js/mypage_script.js"></script>
</body>
</html>