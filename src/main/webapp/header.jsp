<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("LOGGED_USER");
%>
<div id="header">
    <div id="logo"><img src="img/logo.png" class="image" alt=""/>
        <div class="brand"> <div id="name-web">HOME DECOR</div>
            <div id="sub-slogan">Nét mộc trong từng góc nhỏ</div></div>
    </div>

    <nav class="menu-bar">

        <a class="menu" id="home" href="homepage_user.jsp"> TRANG CHỦ</a>
        <div class="menu product-menu">

            <a id="product" href="${pageContext.request.contextPath}/ProductAllServlet">SẢN PHẨM</a>
            <div class="submenu">
                <a href="html/decorate_livingroom_user.html">TRANG TRÍ PHÒNG KHÁCH</a>
                <a href="html/decorate_bedroom_user.html">TRANG TRÍ PHÒNG NGỦ</a>
                <a href="html/decorate_kitchen_user.html">TRANG TRÍ PHÒNG BẾP</a>
                <a href="html/decorate_homeoffice_user.html">TRANG TRÍ PHÒNG LÀM VIỆC</a>
                <a href="html/decorate_miniitem_user.html">ĐỒ TRANG TRÍ MINI</a>
                <a href="html/souvenirs_user.html">QUÀ LƯU NIỆM</a>
            </div>
        </div>
        <a class="menu" id=" " href="purchasing_policy_user.jsp" >CHÍNH SÁCH MUA HÀNG</a>
        <a class="menu" id="introduce" href="introduce_user.jsp" >GIỚI THIỆU</a>
        <a class="menu" id="contact" href="contact_user.jsp">LIÊN HỆ</a>

    </nav>
    <div class="icons">
        <a class="nav_item" href="html/shopping-cart.html" id="cart-link">
            <i class="fas fa-shopping-cart"></i>
        </a>

        <a class="nav-item" href="search.jsp" id="search-link">
            <i class="fas fa-search"></i>
        </a>

        <div class="user-login">
            <i class="fas fa-user"></i>
            <div class="user">
                <% if (user != null) { %>
<%--                <span style="display:block; padding: 5px 10px; font-weight:bold; color:#8B4513;">--%>
<%--                Chào, <%= user.getUsername() %>--%>
            </span>
                <a class="nav_item" href="mypage_user.jsp" id="myPage">Trang của tôi</a>
                <a class="nav-item" href="LogoutServlet" id="login-register">Đăng xuất</a>
                <% } else { %>
                <a class="nav-item" href="login.jsp" id="login-register">Đăng nhập</a>
                <a class="nav-item" href="login.jsp" >Đăng ký</a>
                <% } %>
            </div>
        </div>
    </div>



</div>