<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HOME DECOR - QUÊN MẬT KHẨU</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >

    <link rel="stylesheet" href="css/forgot_style.css">

</head>
<body>

<div class="container">
    <form action="${pageContext.request.contextPath}/ForgotPasswordServlet" method="post" class="forgot-form">
        <h1>Quên mật khẩu</h1>
        <p>Nhập email của bạn để nhận mã OTP.</p>

        <c:if test="${not empty ERROR_MESSAGE}">
            <div class="error-msg">
                    ${ERROR_MESSAGE}
            </div>
        </c:if>

        <div class="infield">
            <input type="email" name="email" placeholder="Nhập email của bạn" required>
        </div>

        <button type="submit">Tiếp tục</button>

        <div class="back-login">
            <a href="login.jsp">Quay lại đăng nhập</a>
        </div>
    </form>

</div>
<script src="js/login_JS.js"></script>

</body>
</html>
