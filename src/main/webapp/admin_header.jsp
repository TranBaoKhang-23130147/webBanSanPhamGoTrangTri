<%@ page import="dao.NotificationDao" %>
<%@ page import="model.Notification" %>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // 1. Lấy user từ session
    User admin = (User) session.getAttribute("LOGGED_USER");
    if (admin != null) {
        NotificationDao notiDAO = new NotificationDao();
        // 2. Lấy danh sách từ DB
        List<Notification> notifications = notiDAO.getTopNotifications(admin.getId());
        // 3. Đẩy vào request để JSTL bên dưới sử dụng
        request.setAttribute("notifications", notifications);
    }
%>
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

        <%-- Phần hiển thị thông báo trong header.jsp --%>
        <div class="notification-dropdown">
            <i class="fa-solid fa-bell notification-icon"></i>
            <span class="badge">${notifications.size()}</span> <div id="notificationMenuContent" class="dropdown-content notification-content">
            <div class="dropdown-header">
                Thông Báo Mới (${notifications != null ? notifications.size() : 0})
            </div>

            <c:forEach var="n" items="${notifications}">
                <a href="ViewContactDetail?id=${n.relatedId}">
                    <div class="noti-item ${n.isRead() ? '' : 'unread'}">
                        📩 ${n.content}
                        <br><small>${n.createAt}</small>
                    </div>
                </a>
            </c:forEach>

            <c:if test="${empty notifications}">
                <p class="no-messages-text">Không có thông báo mới.</p>
            </c:if>
        </div>
        </div>

        <div class="user-dropdown">
            <i class="fas fa-user-circle user-logo" ></i>
            <div id="userMenuContent" class="dropdown-content">
                <a href="admin_thong_tin_tai_khoan.html"> Thông tin tài khoản</a>
                <a href="#"> Đổi mật khẩu</a>
                <div class="dropdown-divider"></div>
                <a href="login.jsp" class="logout-link"> Đăng xuất</a>
            </div>
        </div>
    </div>
</header>