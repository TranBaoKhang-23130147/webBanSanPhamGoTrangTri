<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HOME DECOR - NHẬP MÃ XÁC NHẬP</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/otp_pass.css">

</head>
<body>

<div class="container">
    <form action="${pageContext.request.contextPath}/VerifyOtpResetServlet" method="post" class="otp-form">
        <h1>Nhập mã xác nhận</h1>

        <c:if test="${not empty ERROR_MESSAGE}">
            <div class="error-msg">
                    ${ERROR_MESSAGE}
            </div>
        </c:if>

        <input type="text" name="otp" placeholder="Nhập mã OTP" required>

        <button type="submit">Tiếp tục</button>

        <a href="login_forgot_password.jsp">Quay lại</a>
    </form>
</div>

</body>
</html>