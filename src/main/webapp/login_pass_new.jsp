<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HOME DECOR - NHẬP MẬT KHẨU MỚI</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >

    <link rel="stylesheet" href="css/pass_new.css">
</head>
<body>
<div class="container">
    <form action="${pageContext.request.contextPath}/ResetPasswordServlet" method="post" class="reset-form">
        <h1>Nhập mật khẩu mới</h1>

        <c:if test="${not empty ERROR_MESSAGE}">
            <div class="error-msg">
                    ${ERROR_MESSAGE}
            </div>
        </c:if>

        <input type="password" name="password" placeholder="Mật khẩu mới" required>
        <input type="password" name="re_password" placeholder="Nhập lại mật khẩu" required>

        <button type="submit">Đặt lại mật khẩu</button>
    </form>
</div>


</body>
</html>