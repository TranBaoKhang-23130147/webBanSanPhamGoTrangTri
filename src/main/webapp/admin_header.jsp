<%@ page import="dao.NotificationDao" %>
<%@ page import="model.Notification" %>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<header class="header">
    <div class="logo-placeholder">
        <img src="${pageContext.request.contextPath}/img/logo.png" alt="Logo Modern Homes">
        <p class="logo">HOME DECOR</p>
    </div>

    <div class="header-icons">

    <div class="user-logout-wrapper">
        <a href="login.jsp" class="logout-btn">
           Đăng xuất
        </a>
    </div>
    </div>
        <style>
            .user-logout-wrapper {
                display: flex;
                align-items: center;
                padding: 10px;
            }

            .logout-btn {
                display: inline-flex;
                align-items: center;
                gap: 8px;
                color: #000000 !important;
                padding: 10px 20px;
                border-radius: 8px;
                text-decoration: none !important;
                font-weight: 600;
                font-size: 14px;
                transition: all 0.3s ease;
            }
        </style>


</header>