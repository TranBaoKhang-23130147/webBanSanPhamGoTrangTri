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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage_style.css">

</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class="dashboard-container">

    <div class="sidebar">
        <div class="user-info">
            <div class="avatar-container">
                <img
                <%-- Dùng LOGGED_USER để đồng bộ ngay sau khi nhấn Lưu --%>
                        src="${pageContext.request.contextPath}${not empty LOGGED_USER.avatarUrl ? LOGGED_USER.avatarUrl : '/img/logo.png'}"
                        alt="Avatar"
                        class="avatar-img"
                        onerror="this.src='${pageContext.request.contextPath}/img/logo.png';"
                        style="width: 60px; height: 60px; object-fit: cover; border-radius: 50%;" />
            </div>
            <%-- Hiển thị tên từ Session --%>
            <div class="user-name">${LOGGED_USER.username}</div>
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

                    <a href="MyPageServlet?tab=dia-chi"
                       class="menu-link ${activeTab == 'dia-chi' ? 'active' : ''}">
                        <i class="fas fa-map-marker-alt"></i> Địa chỉ
                    </a>
                    <a href="MyPageServlet?tab=bao-mat" class="menu-link" ${activeTab == 'bao-mat' ? 'active' : ''}><i class="fas fa-shield-alt"></i> Bảo mật</a>
                    <a href="#" class="tab-link" data-tab="thong-bao"><i class="fas fa-bell"></i> Thông báo</a>
                </div>
            </div>
            <a href="MyPageServlet?tab=don-hang" class="menu-link ${activeTab == 'don-hang' ? 'active' : ''}">
                <i class="fas fa-shopping-cart"></i> Đơn hàng
            </a>

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
                            <img id="user-avatar-display"
                                 src="${pageContext.request.contextPath}${not empty LOGGED_USER.avatarUrl ? LOGGED_USER.avatarUrl : '/img/logo.png'}"
                                 alt="Avatar"
                                 class="avatar-img"
                                 style="width:150px; height:150px; object-fit:cover; border-radius:50%;"
                                 onerror="this.src='${pageContext.request.contextPath}/img/logo.png';" />

                            <button type="button" class="camera-btn" onclick="selectAvatarWithCKFinder()">
                                <i class="fas fa-camera"></i>
                            </button>

                            <input type="hidden" name="avatar_id" id="user-avatar-url" value="${LOGGED_USER.avatarUrl}">
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



        <div id="dia-chi" class="tab-content <%= "dia-chi".equals(activeTab) ? "active" : "" %>">

            <div class="header-with-button">
                <h2>Địa Chỉ</h2>
                <button class="add-btn" onclick="openAddAddressModal()">
                    <i class="fas fa-plus"></i> Thêm
                </button>
            </div>

            <div class="address-list">

                <c:if test="${empty addresses}">
                    <p style="color:#888;">Bạn chưa có địa chỉ nào</p>
                </c:if>

                <c:forEach var="a" items="${addresses}">
                    <div class="address-item ${a.isDefault == 1 ? 'default-address' : ''}">

                        <div class="address-icon">
                            <i class="fas fa-map-marker-alt"></i>
                        </div>

                        <div class="address-details">
                            <p class="address-name">
                                    ${a.name}
                                <span>${a.phone}</span>
                            </p>
                            <p class="address-line">${a.fullAddress}</p>
                        </div>

                        <div class="address-actions">

                            <c:if test="${a.isDefault == 1}">
                                <span class="default-tag">Mặc định</span>
                            </c:if>

                            <form action="AddressServlet" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="${a.id}">
                                <button class="set-default-btn"
                                        name="action" value="default"
                                        <c:if test="${a.isDefault == 1}">disabled</c:if>>
                                    Đặt làm mặc định
                                </button>
                            </form>

                            <button class="change-btn"
                                    onclick="openEditAddressModal(
                                            '${a.id}',
                                            '${a.name}',
                                            '${a.phone}',
                                            '${a.detail}',
                                            '${a.commune}',
                                            '${a.district}',
                                            '${a.province}'
                                            )">
                                Thay đổi
                            </button>


                            <form action="AddressServlet" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="${a.id}">
                                <button class="delete-btn"
                                        name="action" value="delete"
                                        onclick="return confirm('Xóa địa chỉ này?')">
                                    Xóa
                                </button>
                            </form>

                        </div>
                    </div>
                </c:forEach>

            </div>
        </div>

        <div id="bao-mat" class="tab-content"<%= "bao-mat".equals(activeTab) ? "active" : "" %>">
        <h2>Bảo mật</h2>
        <h3>Đổi mật khẩu</h3>

        <form action="ChangePasswordServlet" method="post" class="password-change-form">
            <div class="form-group">
                <label>Mật khẩu hiện tại *</label>
                <input type="password" name="currentPassword" required>
            </div>

            <div class="form-group">
                <label>Mật khẩu mới *</label>
                <input type="password" name="newPassword" required>
            </div>

            <div class="form-group">
                <label>Xác nhận mật khẩu *</label>
                <input type="password" name="confirmPassword" required>
            </div>

            <button type="submit" class="save-btn">Lưu</button>
        </form>

        <c:if test="${not empty msg}">
        <p style="color: green">${msg}</p>
        </c:if>

        <c:if test="${not empty error}">
        <p style="color: red">${error}</p>
        </c:if>
        <c:remove var="msg" scope="session"/>
        <c:remove var="error" scope="session"/>

</div>

<div id="don-hang" class="tab-content ${activeTab == 'don-hang' ? 'active' : ''}">
    <h2 style="margin-bottom: 20px; color: #333;">Đơn hàng của tôi</h2>
    <a href="#" class="tab-link menu-link" data-tab="tin-nhan">Đánh giá sản phẩm </a>
    <div class="order-dashboard-summary" style="display: flex; gap: 20px; margin-bottom: 30px;">
        <div class="summary-item" style="background: #e3f2fd; padding: 20px; border-radius: 10px; flex: 1; text-align: center;">
            <span class="num" style="display: block; font-size: 1.5em; font-weight: bold; color: #1976d2;">${countOrder}</span>
            <span class="label" style="color: #555;">Tổng đơn hàng</span>
        </div>
        <div class="summary-item" style="background: #f1f8e9; padding: 20px; border-radius: 10px; flex: 1; text-align: center;">
            <span class="num" style="display: block; font-size: 1.5em; font-weight: bold; color: #388e3c;">
                <fmt:formatNumber value="${totalSpent}" pattern="#,###"/> VND
            </span>
            <span class="label" style="color: #555;">Tổng tích lũy</span>
        </div>
    </div>
    <div class="order-status-filter">
        <c:set var="currStatus" value="${param.status == null ? 'tat-ca' : param.status}" />

        <a href="MyPageServlet?tab=don-hang" class="filter-btn ${currStatus == 'tat-ca' ? 'active' : ''}">Tất cả</a>
        <a href="MyPageServlet?tab=don-hang&status=Chờ xác nhận" class="filter-btn ${currStatus == 'Chờ xác nhận' ? 'active' : ''}">Chờ xác nhận</a>
        <a href="MyPageServlet?tab=don-hang&status=Đang giao" class="filter-btn ${currStatus == 'Đang giao' ? 'active' : ''}">Đang giao</a>
        <a href="MyPageServlet?tab=don-hang&status=Đã giao" class="filter-btn ${currStatus == 'Đã giao' ? 'active' : ''}">Đã giao</a>
        <a href="MyPageServlet?tab=don-hang&status=Đã hủy" class="filter-btn ${currStatus == 'Đã hủy' ? 'active' : ''}">Đã hủy</a>
        <a href="MyPageServlet?tab=don-hang&status=Hoàn hàng" class="filter-btn ${currStatus == 'Hoàn hàng' ? 'active' : ''}">Đã hoàn hàng</a>
    </div>

    <style>
        .filter-item {
            padding: 10px 20px;
            text-decoration: none;
            color: #666;
            font-weight: 500;
            white-space: nowrap;
            border-bottom: 3px solid transparent;
            transition: all 0.3s;
        }
        .filter-item:hover { color: #1976d2; }
        .filter-item.active {
            color: #1976d2;
            border-bottom-color: #1976d2;
            background: #f0f7ff;
            border-radius: 4px 4px 0 0;
        }
    </style>
    <div class="orders-grid">

        <c:forEach items="${listO}" var="order">
            <div class="order-item-card">
                <div class="card-header">
                    <span class="order-id">#${order.id}</span>
                    <span class="order-status status-${order.status}">${order.status}</span>
                </div>

                <div class="card-body">
                    <div class="info-row">
                        <i class="far fa-calendar-alt"></i>
                        <span><fmt:formatDate value="${order.createAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                    </div>
                    <div class="info-row">
                        <i class="fas fa-credit-card"></i>
                        <span>${order.paymentStatus}</span>
                    </div>
                    <div class="price-box">
                        <span class="label">Tổng thanh toán:</span>
                        <span class="amount"><fmt:formatNumber value="${order.totalOrder}" pattern="#,###"/> VND</span>
                    </div>
                </div>

                <div class="card-footer">
                    <a href="javascript:void(0)" class="btn-detail" onclick="showUserOrderDetail(${order.id})">
                        Xem chi tiết <i class="fas fa-chevron-right"></i>
                    </a>
                </div>

                <div id="data-order-${order.id}" style="display: none;">
                    <h3 style="border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 15px;">
                        Chi tiết đơn hàng #${order.id}
                    </h3>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 15px; font-size: 0.9em;">
                        <div>
                            <p><strong>Người nhận:</strong> <%= user.getUsername() %></p>
                            <p><strong>Số điện thoại:</strong> <%= user.getPhone() %></p>
                        </div>
                        <div style="text-align: right;">
                            <p><strong>Ngày đặt:</strong> <fmt:formatDate value="${order.createAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                            <p><strong>Trạng thái:</strong> <span class="order-status status-${order.status}">${order.status}</span></p>
                        </div>
                    </div>

                    <div class="modal-body-scroll" style="max-height: 400px; overflow-y: auto;">
                        <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px;">
                            <thead>
                            <tr style="background: #f8f9fa; border-bottom: 2px solid #dee2e6;">
                                <th style="padding: 10px; text-align: left;">Sản phẩm</th>
                                <th style="padding: 10px; text-align: center;">SL</th>
                                <th style="padding: 10px; text-align: right;">Đơn giá</th>
                                <th style="padding: 10px; text-align: right;">Thành tiền</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${order.details}" var="d">
                                <tr style="border-bottom: 1px solid #eee;" >

                                    <td style="padding: 10px;">${d.productName}</td>

                                    <td style="padding: 10px; text-align: center;">x${d.quantity}</td>
                                    <td style="padding: 10px; text-align: right;">
                                        <fmt:formatNumber value="${d.total / d.quantity}" pattern="#,###"/>
                                    </td>
                                    <td style="padding: 10px; text-align: right; font-weight: bold;">
                                        <fmt:formatNumber value="${d.total}" pattern="#,###"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="summary-horizontal-card">
                        <div class="summary-section">
                            <span class="summary-title">Tạm tính</span>
                            <span class="summary-amount"><fmt:formatNumber value="${order.subTotal}" pattern="#,###"/></span>
                        </div>

                        <div class="summary-section">
                            <span class="summary-title">Thuế (8%)</span>
                            <span class="summary-amount"><fmt:formatNumber value="${order.taxAmount}" pattern="#,###"/></span>
                        </div>

                        <div class="summary-section">
                            <span class="summary-title">Phí ship</span>
                            <span class="summary-amount"><fmt:formatNumber value="${order.shippingFee}" pattern="#,###"/></span>
                        </div>

                        <div class="summary-section total-box">
                            <span class="summary-title">Tổng cộng</span>
                            <span class="summary-amount total-final"><fmt:formatNumber value="${order.totalOrder}" pattern="#,###"/> VND</span>
                        </div>
                    </div>
                    <div class="order-actions" style="margin-top: 20px; display: flex; justify-content: flex-end; gap: 10px;">
                            <%-- 1. Nút HỦY ĐƠN: Hiện khi trạng thái là 'Chờ xác nhận' --%>
                        <c:if test="${order.status == 'Chờ xác nhận'}">
                            <form action="MyPageServlet" method="post" onsubmit="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')">
                                <input type="hidden" name="action" value="cancelOrder">
                                <input type="hidden" name="orderId" value="${order.id}">
                                <button type="submit" style="background: #e74c3c; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-weight: bold;">
                                    Hủy Đơn Hàng
                                </button>
                            </form>
                        </c:if>

                            <%-- 2. Nút HOÀN HÀNG: Hiện khi 'Đã giao' và trong vòng 7 ngày --%>
                            <%-- Kiểm tra Đơn đã giao VÀ đã thanh toán --%>
                        <div class="order-actions" style="margin-top: 20px; display: flex; justify-content: flex-end; gap: 10px;">
                                <%-- 1. Nút HỦY ĐƠN: Hiện khi trạng thái là 'Chờ xác nhận' --%>
                            <c:if test="${order.status == 'Chờ xác nhận'}">
                                <form action="MyPageServlet" method="post" onsubmit="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này?')">
                                    <input type="hidden" name="action" value="cancelOrder">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <button type="submit" style="background: #e74c3c; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-weight: bold;">
                                        Hủy Đơn Hàng
                                    </button>
                                </form>
                            </c:if>

                            <c:if test="${order.status == 'Đã giao'}">
                                <jsp:useBean id="now" class="java.util.Date" />
                                <c:set var="diff" value="${now.time - order.createAt.time}" />
                                <c:set var="days" value="${diff / (1000 * 60 * 60 * 24)}" />

                                <div style="display: flex; gap: 10px; align-items: center;">
                                        <%-- Nút VIẾT ĐÁNH GIÁ: Luôn hiện khi đã giao --%>
                                        <%-- Nút VIẾT ĐÁNH GIÁ: Gọi hàm JS truyền vào Order ID --%>
                                    <button type="button"
                                            onclick="openReviewModal(${order.id})"
                                            style="background: #27ae60; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-weight: bold;">
                                        Viết Đánh Giá
                                    </button>
                                        <%-- Nút HOÀN HÀNG: Chỉ hiện trong vòng 7 ngày --%>
                                    <c:choose>
                                        <c:when test="${days <= 7}">
                                            <form action="MyPageServlet" method="post" onsubmit="return confirm('Bạn muốn yêu cầu hoàn hàng cho đơn hàng này?')" style="margin: 0;">
                                                <input type="hidden" name="action" value="returnOrder">
                                                <input type="hidden" name="orderId" value="${order.id}">
                                                <button type="submit" style="background: #f39c12; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; font-weight: bold;">
                                                    Yêu Cầu Hoàn Hàng
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: #888; font-style: italic; font-size: 0.9em;">(Hết hạn hoàn hàng)</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:if>
                        </div>
                    </div>

                </div>
            </div>
        </c:forEach>
    </div>
</div>


<div id="thong-bao" class="tab-content">
    <h2>Thông báo</h2>
    <p>Không có thông báo mới.</p>
</div>
<div id="tin-nhan" class="tab-content">
    <h2 style="color: #333; border-bottom: 2px solid #27ae60; padding-bottom: 10px;">Sản phẩm cần đánh giá</h2>
    <p style="color: #666; font-size: 14px; margin-bottom: 20px;">Danh sách sản phẩm từ đơn hàng đã giao và thanh toán xong</p>
    
    <%
        java.util.Map<String, model.OrderDetail> reviewProducts = new java.util.LinkedHashMap<>();
        List<Order> allOrders = (List<Order>) request.getAttribute("allOrders");
        
        if (allOrders != null) {
            for (Order order : allOrders) {
                // Chỉ lấy đơn hàng đã giao (Đã giao) và thanh toán (Đã thanh toán)
                if ("Đã giao".equals(order.getStatus()) && "Đã thanh toán".equals(order.getPaymentStatus())) {
                    List<OrderDetail> details = order.getDetails();
                    if (details != null) {
                        for (OrderDetail detail : details) {
                            // Tạo key unique cho mỗi sản phẩm + variant + order
                            String key = detail.getProductVariantId() + "_" + order.getId();
                            if (!reviewProducts.containsKey(key)) {
                                reviewProducts.put(key, detail);
                            }
                        }
                    }
                }
            }
        }
    %>
    
    <% if (reviewProducts.isEmpty()) { %>
        <div style="text-align: center; padding: 40px 20px; background: #f9f9f9; border-radius: 8px;">
            <i class="fas fa-check-circle" style="font-size: 48px; color: #27ae60; margin-bottom: 20px; display: block;"></i>
            <p style="font-size: 1.1em; color: #666;">Bạn đã đánh giá hết tất cả sản phẩm!</p>
        </div>
    <% } else { %>
        <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 15px;">
            <%
                for (String key : reviewProducts.keySet()) {
                    OrderDetail detail = reviewProducts.get(key);
                    String[] parts = key.split("_");
                    int orderId = Integer.parseInt(parts[parts.length - 1]);
            %>
                <div style="background: white; border: 1px solid #ddd; border-radius: 8px; padding: 15px; box-shadow: 0 2px 8px rgba(0,0,0,0.08);">
                    <div style="margin-bottom: 10px;">
                        <strong style="color: #8B5E3C; font-size: 1.05em;">📦 <%= detail.getProductName() %></strong>
                    </div>
                    <div style="color: #666; font-size: 0.9em; margin-bottom: 8px;">
                        <p style="margin: 5px 0;"><strong>Đơn hàng:</strong> #<%= orderId %></p>
                        <p style="margin: 5px 0;"><strong>Số lượng:</strong> <%= detail.getQuantity() %> cái</p>
                        <p style="margin: 5px 0;"><strong>Giá:</strong> <fmt:formatNumber value="<%= detail.getTotal() / detail.getQuantity() %>" pattern="#,###"/> VND</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/detail?id=<%= detail.getProductId() %>&review=true&orderId=<%= orderId %>">
    <span style="display: inline-block; background: #8B5E3C; color: white; padding: 8px 15px; border-radius: 4px; text-decoration: none; font-weight: bold; width: 100%; text-align: center;">
        ✍️ Viết đánh giá
    </span>
                    </a>

                </div>
            <% } %>
        </div>
    <% } %>
</div>

</main>
</div>

<jsp:include page="footer.jsp"></jsp:include>

<script src="js/mypage_script.js"></script>
<div id="addressModal" class="modal-overlay">
    <div class="modal-box">
        <h3 id="modalTitle">
            <i class="fas fa-plus-circle"></i> Thêm địa chỉ
        </h3>

        <form action="AddressServlet" method="post" class="modal-form">
            <input type="hidden" name="id" id="addr-id">
            <input type="hidden" name="action" id="addr-action" value="add">

            <div class="form-group">
                <label>Họ tên</label>
                <input name="name" id="addr-name" placeholder="Nhập tên người nhận" required>
            </div>

            <div class="form-group">
                <label>Số điện thoại</label>
                <input name="phone" id="addr-phone" placeholder="Nhập số điện thoại" required>
            </div>

            <div class="form-group">
                <label>Địa chỉ chi tiết</label>
                <input name="detail" id="addr-detail" placeholder="Số nhà, tên đường" required>
            </div>


            <div class="form-group">
                <label>Phường / Xã</label>
                <input name="commune" id="addr-commune" placeholder="Nhập tên Phường / Xã" required>
            </div>

            <div class="form-group">
                <label>Quận / Huyện</label>
                <input name="district" id="addr-district" placeholder="Nhập tên Quận / Huyện " required>
            </div>


            <div class="form-group">
                <label>Tỉnh / Thành phố</label>
                <input name="province" id="addr-province" placeholder="Nhập tên Tỉnh / Thành phố" required>
            </div>

            <div class="modal-actions">
                <button type="submit" class="save-btn">Xác nhận</button>
                <button type="button" class="cancel-btn" onclick="closeAddressModal()">Hủy bỏ</button>
            </div>
        </form>
    </div>
</div>

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
</div>
<div id="orderDetailModal" class="order-modal" style="display: none;">
    <div class="order-modal-content">
    </div>
</div>

</body>
<div id="reviewModal" class="modal" style="display:none; position: fixed; z-index: 10000; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); overflow-y: auto;">
    <div class="modal-content" style="background: #fff; margin: 2% auto; padding: 25px; width: 50%; border-radius: 12px; position: relative; box-shadow: 0 5px 15px rgba(0,0,0,0.3);">
        <span onclick="closeReviewModal()" style="position: absolute; right: 20px; top: 10px; cursor: pointer; font-size: 28px; color: #888;">&times;</span>
        <h2 style="margin-top: 0; color: #333; border-bottom: 2px solid #27ae60; padding-bottom: 10px;">Đánh giá sản phẩm</h2>

        <form action="ReviewServlet" method="post" id="mainReviewForm">
            <input type="hidden" name="orderId" id="reviewOrderId">

            <div id="reviewProductList"></div>

            <div style="text-align: right; margin-top: 15px; border-top: 1px solid #eee; padding-top: 15px;">
                <button type="button" onclick="closeReviewModal()" style="background: #95a5a6; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer; margin-right: 10px;">Hủy</button>
                <button type="submit" style="background: #27ae60; color: white; border: none; padding: 10px 30px; border-radius: 5px; cursor: pointer; font-weight: bold;">Gửi Đánh Giá</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/ckfinder/ckfinder.js"></script>

<script>
    function selectAvatarWithCKFinder() {
        var finder = new CKFinder();
        finder.basePath = '${pageContext.request.contextPath}/ckfinder/';

        finder.selectActionFunction = function(fileUrl) {
            console.log("CKFinder gốc trả về:", fileUrl);  // debug để xem fileUrl thực tế là gì

            var contextPath = "${pageContext.request.contextPath}" || "";  // tránh lỗi nếu contextPath rỗng
            var relativeUrl = fileUrl || "";

            // Loại bỏ context path nếu có ở đầu
            if (contextPath && relativeUrl.startsWith(contextPath)) {
                relativeUrl = relativeUrl.substring(contextPath.length);
            }

            // Đảm bảo luôn bắt đầu bằng dấu /
            if (relativeUrl && !relativeUrl.startsWith('/')) {
                relativeUrl = '/' + relativeUrl;
            }

            // Nếu CKFinder trả về đường dẫn thiếu phần /img/... thì thêm lại (tùy cấu trúc upload của bạn)
            // Ví dụ: nếu bạn biết ảnh luôn nằm trong /img/products/images/
            if (!relativeUrl.includes('/img/') && !relativeUrl.includes('/products/')) {
                var fileName = relativeUrl.split('/').pop();  // lấy tên file cuối cùng
                relativeUrl = '/img/products/images/' + fileName;  // chỉnh đường dẫn gốc của bạn nếu khác
            }

            console.log("Context Path:", contextPath);
            console.log("Relative URL sau khi xử lý:", relativeUrl);

            // Cập nhật input hidden để gửi về server
            var input = document.getElementById('user-avatar-url');
            if (input) {
                input.value = relativeUrl;
            } else {
                console.error("Không tìm thấy input #user-avatar-url");
            }

            // Cập nhật preview ảnh ngay lập tức
            var preview = document.getElementById('user-avatar-display');
            if (preview) {
                preview.src = contextPath + relativeUrl;
                preview.onerror = function() {
                    this.src = contextPath + '/img/logo.png';  // fallback nếu ảnh lỗi
                    console.warn("Preview lỗi, fallback về logo");
                };
            } else {
                console.error("Không tìm thấy img #user-avatar-display");
            }
        };

        finder.popup();
    }

</script>
<script>
    // --- 1. HIỆN CHI TIẾT ĐƠN HÀNG ---

    function showUserOrderDetail(id) {

        const modal = document.getElementById('orderDetailModal');
        const container = modal.querySelector('.order-modal-content');
        const dataDiv = document.getElementById('data-order-' + id);

        if (dataDiv && modal) {
            container.innerHTML =
                '<span class="order-close" onclick="closeOrderDetail()">&times;</span>' +
                dataDiv.innerHTML;

            modal.style.display = 'block';
            document.body.style.overflow = 'hidden';
        }
    }



    // --- 2. NÚT "VIẾT ĐÁNH GIÁ" (JSP GỌI HÀM NÀY) ---

    function openReviewModal(orderId) {
        switchToReviewForm(orderId);
    }



    // --- 3. CHUYỂN SANG FORM ĐÁNH GIÁ ---





    // --- 4. ĐÓNG MODAL ---

    function closeOrderDetail() {
        document.getElementById('orderDetailModal').style.display = 'none';
        document.body.style.overflow = 'auto';
    }



    // --- 5. CLICK NGOÀI MODAL ---

    window.onclick = function(e) {
        const modal = document.getElementById('orderDetailModal');
        if (e.target === modal) closeOrderDetail();
    };

    document.querySelectorAll("[data-rated='true']").forEach(btn=>{
        btn.innerText="✔ Đã đánh giá";
        btn.disabled=true;
    });

</script>
</html>