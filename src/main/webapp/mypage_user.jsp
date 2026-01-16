<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="model.User,model.Order,model.OrderDetail,java.util.List" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // Ngăn chặn trình duyệt lưu cache trang này
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
%>
<%
    // Lấy đúng tên biến LOGGED_USER từ Session
    User user = (User) session.getAttribute("LOGGED_USER");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;

    }
    System.out.println(user.getId());
%>
<%
    String activeTab = (String) request.getAttribute("activeTab");
    if (activeTab == null) {
        activeTab = "ho-so";
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
                    <a href="MyPageServlet?tab=ho-so"
                       class="menu-link ${activeTab == 'ho-so' ? 'active' : ''}"
                       data-tab="ho-so">
                        <i class="fas fa-id-card"></i> Hồ sơ
                    </a>

                    <a href="MyPageServlet?tab=thanh-toan"
                       class="menu-link ${activeTab == 'thanh-toan' ? 'active' : ''}"
                       data-tab="thanh-toan">
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
        <div id="ho-so" class="tab-content <%= "ho-so".equals(activeTab) ? "active" : "" %>">
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
<%--        <div id="thanh-toan" class="tab-content <%= "thanh-toan".equals(activeTab) ? "active" : "" %>">--%>
<%--            <div class="header-with-button">--%>
<%--                <h2>Quản lý thanh toán</h2>--%>

<%--                <button class="add-btn" onclick="document.getElementById('addForm').style.display='block'">--%>
<%--                    <i class="fas fa-plus"></i> Thêm thẻ--%>
<%--                </button>--%>
<%--            </div>--%>

<%--            <div id="addForm" style="display:none; background: #fff; padding: 20px; border: 1px solid #eee; margin-bottom: 20px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05);">--%>
<%--                <form action="AddPaymentServlet" method="post">--%>
<%--                    <h3 style="margin-bottom: 20px; color: #333;"><i class="fas fa-plus-circle"></i> Thêm phương thức thanh toán</h3>--%>
<%--                    <div class="form-group">--%>
<%--                        <label>Loại thẻ:</label>--%>
<%--                        <select name="type" class="input-style">--%>
<%--                            <option value="Visa">Visa</option>--%>
<%--                            <option value="MasterCard">MasterCard</option>--%>
<%--                            <option value="JCB">JCB</option>--%>
<%--                        </select>--%>
<%--                    </div>--%>
<%--                    <div class="form-group">--%>
<%--                        <label>Số thẻ:</label>--%>
<%--                        <input type="text" name="cardNumber" placeholder="**** **** **** ****" required class="input-style">--%>
<%--                    </div>--%>
<%--                    <div class="form-group">--%>
<%--                        <label>Ngày hết hạn:</label>--%>
<%--                        <input type="date" name="duration" required class="input-style">--%>
<%--                    </div>--%>
<%--                    <div style="margin-top: 20px;">--%>
<%--                        <button type="submit" class="save-btn">Xác nhận thêm</button>--%>
<%--                        <button type="button" class="delete-btn" onclick="document.getElementById('addForm').style.display='none'">Hủy bỏ</button>--%>
<%--                    </div>--%>
<%--                </form>--%>
<%--            </div>--%>

<%--            <div class="payment-management">--%>
<%--                <c:if test="${empty listPayments}">--%>
<%--                    <p>Chưa có thẻ thanh toán</p>--%>
<%--                </c:if>--%>

<%--                <c:forEach items="${listPayments}" var="p">--%>
<%--                    <div class="card-item" style="background: linear-gradient(135deg, #2c3e50, #4ca1af); color: white; border-radius: 15px; padding: 20px; position: relative; overflow: hidden;">--%>
<%--                        <div class="card-display">--%>
<%--                            <div style="display: flex; justify-content: space-between; align-items: flex-start;">--%>
<%--                                <span style="font-size: 1.2em; font-weight: bold; letter-spacing: 2px;">${p.type}</span>--%>
<%--                                <i class="fas fa-microchip" style="font-size: 2em; color: #f1c40f;"></i>--%>
<%--                            </div>--%>

<%--                            <p class="card-number" style="font-size: 1.4em; margin: 20px 0; letter-spacing: 4px;">--%>
<%--                                **** **** **** ${p.cardNumber.substring(p.cardNumber.length() - 4)}--%>
<%--                            </p>--%>

<%--                            <div style="display: flex; justify-content: space-between;">--%>
<%--                                <div>--%>
<%--                                    <small style="opacity: 0.8; display: block;">Chủ thẻ</small>--%>
<%--                                    <span style="text-transform: uppercase;">${user.getUsername()}</span>--%>
<%--                                </div>--%>
<%--                                <div>--%>
<%--                                    <small style="opacity: 0.8; display: block;">Hết hạn</small>--%>
<%--                                    <span><fmt:formatDate value="${p.duration}" pattern="MM/yy"/></span>--%>
<%--                                </div>--%>
<%--                            </div>--%>
<%--                        </div>--%>

<%--                        <div class="card-actions" style="margin-top: 20px; display: flex; justify-content: flex-end;">--%>
<%--                            <a href="DeletePaymentServlet?id=${p.id}" onclick="return confirm('Xóa thẻ này?')" class="btn-small-delete">--%>
<%--                                <i class="fas fa-trash"></i> Xóa--%>
<%--                            </a>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </c:forEach>--%>
<%--            </div>--%>
<%--        </div>--%>
        <div id="thanh-toan" class="tab-content <%= "thanh-toan".equals(activeTab) ? "active" : "" %>">
            <div class="header-with-button">
                <h2>Quản lý thanh toán</h2>
                <button class="add-btn" onclick="document.getElementById('addForm').style.display='block'">
                    <i class="fas fa-plus"></i> Thêm thẻ
                </button>
            </div>

            <div id="addForm" style="display:none; background: #fff; padding: 20px; border: 1px solid #eee; margin-bottom: 20px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05);">
                <form action="AddPaymentServlet" method="post">
                    <h3 style="margin-bottom: 20px; color: #333;"><i class="fas fa-plus-circle"></i> Thêm phương thức thanh toán</h3>
                    <div class="form-group">
                        <label>Loại thẻ:</label>
                        <select name="type" class="input-style">
                            <option value="Visa">Visa</option>
                            <option value="MasterCard">MasterCard</option>
                            <option value="JCB">JCB</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Số thẻ:</label>
                        <input type="text" name="cardNumber" placeholder="**** **** **** ****" required class="input-style">
                    </div>
                    <div class="form-group">
                        <label>Ngày hết hạn:</label>
                        <input type="date" name="duration" required class="input-style">
                    </div>
                    <div style="margin-top: 20px; display: flex; gap: 10px;">
                        <button type="submit" class="save-btn">Xác nhận thêm</button>
                        <button type="button" class="delete-btn" onclick="document.getElementById('addForm').style.display='none'">Hủy bỏ</button>
                    </div>
                </form>
            </div>

            <div class="payment-management" style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 25px; align-items: stretch;">
                <c:if test="${empty listPayments}">
                    <p style="grid-column: span 2; text-align: center; color: #888;">Chưa có thẻ thanh toán</p>
                </c:if>

                <c:forEach items="${listPayments}" var="p">
                    <div class="card-item" style="background: linear-gradient(135deg, #2c3e50, #4ca1af); color: white; border-radius: 15px; padding: 25px; box-shadow: 0 8px 16px rgba(0,0,0,0.15); display: flex; flex-direction: column; justify-content: space-between; min-height: 230px;">

                        <div class="card-display">
                            <div style="display: flex; justify-content: space-between; align-items: center;">
                                <span style="font-size: 1.3em; font-weight: bold; letter-spacing: 2px;">${p.type}</span>
                                <i class="fas fa-microchip" style="font-size: 2.2em; color: #f1c40f;"></i>
                            </div>

                            <p class="card-number" style="font-size: 1.5em; margin: 30px 0; letter-spacing: 4px; text-align: center; font-family: 'Courier New', monospace;">
                                <c:choose>
                                    <c:when test="${p.cardNumber.length() > 4}">
                                        **** **** **** ${p.cardNumber.substring(p.cardNumber.length() - 4)}
                                    </c:when>
                                    <c:otherwise>${p.cardNumber}</c:otherwise>
                                </c:choose>
                            </p>

                            <div style="display: flex; justify-content: space-between; font-size: 0.85em;">
                                <div>
                                    <small style="display: block; opacity: 0.7; margin-bottom: 5px; text-transform: uppercase;">Chủ thẻ</small>
                                    <span style="text-transform: uppercase; font-weight: bold; font-size: 1.1em;"><%= user.getUsername() %></span>
                                </div>
                                <div style="text-align: right;">
                                    <small style="display: block; opacity: 0.7; margin-bottom: 5px; text-transform: uppercase;">Hết hạn</small>
                                    <span style="font-size: 1.1em;"><fmt:formatDate value="${p.duration}" pattern="MM/yy"/></span>
                                </div>
                            </div>
                        </div>

                        <div class="card-actions" style="margin-top: 20px; display: flex; justify-content: flex-end; gap: 12px; border-top: 1px solid rgba(255,255,255,0.2); padding-top: 15px;">
                            <a href="javascript:void(0)"
                               onclick="openEditModal('${p.id}', '${p.type}', '${p.cardNumber}', '<fmt:formatDate value="${p.duration}" pattern="yyyy-MM-dd"/>')"
                               class="btn-action-card"
                               style="background: rgba(255,255,255,0.2); color: white; padding: 6px 15px; border-radius: 6px; text-decoration: none; font-size: 0.85em; border: 1px solid rgba(255,255,255,0.3); transition: 0.3s;">
                                <i class="fas fa-edit"></i> Sửa
                            </a>
                            <a href="DeletePaymentServlet?id=${p.id}"
                               onclick="return confirm('Bạn có chắc chắn muốn xóa thẻ này?')"
                               style="background: #e74c3c; color: white; padding: 7px 15px; border-radius: 6px; text-decoration: none;">
                                <i class="fas fa-trash"></i> Xóa
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

            <div id="addForm" style="display:none; background: #fff; padding: 20px; border: 1px solid #eee; margin-bottom: 20px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05);">
                <form action="AddPaymentServlet" method="post">
                    <h3 style="margin-bottom: 20px; color: #333;"><i class="fas fa-plus-circle"></i> Thêm phương thức thanh toán</h3>
                    <div class="form-group">
                        <label>Loại thẻ:</label>
                        <select name="type" class="input-style">
                            <option value="Visa">Visa</option>
                            <option value="MasterCard">MasterCard</option>
                            <option value="JCB">JCB</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Số thẻ:</label>
                        <input type="text" name="cardNumber" placeholder="**** **** **** ****" required class="input-style">
                    </div>
                    <div class="form-group">
                        <label>Ngày hết hạn:</label>
                        <input type="date" name="duration" required class="input-style">
                    </div>
                    <div style="margin-top: 20px;">
                        <button type="submit" class="save-btn">Xác nhận thêm</button>
                        <button type="button" class="delete-btn" onclick="document.getElementById('addForm').style.display='none'">Hủy bỏ</button>
                    </div>
                </form>
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
<div id="editCardModal" style="display:none; position: fixed; z-index: 9999; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); backdrop-filter: blur(3px);">
    <div style="background: #fff; margin: 10% auto; padding: 30px; border-radius: 12px; width: 400px; position: relative; box-shadow: 0 10px 30px rgba(0,0,0,0.2);">
        <h3 style="margin-top: 0; border-bottom: 1px solid #eee; padding-bottom: 15px; color: var(--text-color);">
            <i class="fas fa-credit-card" style="color: var(--primary-color);"></i> Chỉnh sửa thẻ
        </h3>

        <form action="EditPaymentServlet" method="post" style="margin-top: 20px;">
            <input type="hidden" name="id" id="edit-id">

            <div class="form-group" style="flex-direction: column; align-items: flex-start;">
                <label style="margin-bottom: 8px; width: 100%;">Loại thẻ:</label>
                <select name="type" id="edit-type" style="width:100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
                    <option value="Visa">Visa</option>
                    <option value="MasterCard">MasterCard</option>
                    <option value="JCB">JCB</option>
                </select>
            </div>

            <div class="form-group" style="flex-direction: column; align-items: flex-start; margin-top: 15px;">
                <label style="margin-bottom: 8px; width: 100%;">Số thẻ:</label>
                <input type="text" name="cardNumber" id="edit-number" style="width:100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
            </div>

            <div class="form-group" style="flex-direction: column; align-items: flex-start; margin-top: 15px;">
                <label style="margin-bottom: 8px; width: 100%;">Ngày hết hạn:</label>
                <input type="date" name="duration" id="edit-duration" style="width:100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
            </div>

            <div style="text-align: right; margin-top: 25px; display: flex; justify-content: flex-end; gap: 10px;">
                <button type="button" class="support-btn" onclick="document.getElementById('editCardModal').style.display='none'" style="margin:0; width: 80px;">Hủy</button>
                <button type="submit" class="save-btn" style="margin:0; width: 120px;">Lưu lại</button>
            </div>
        </form>
    </div>
</div></body>
</html>