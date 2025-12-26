<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HOME DECOR - NHẬP MÃ XÁC NHẬP</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >

    <link rel="stylesheet" href="css/otp_pass.css">
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

<!--<script>-->
<!--    const inputs = document.querySelectorAll('.otp-input');-->
<!--    inputs.forEach((input, index) => {-->
<!--        input.addEventListener('keyup', (e) => {-->
<!--            if (input.value.length === 1 && index < inputs.length - 1) {-->
<!--                inputs[index + 1].focus();-->
<!--            }-->
<!--        });-->
<!--    });-->

<!--    let time = 59;-->
<!--    const countdownEl = document.getElementById('countdown');-->

<!--    const interval = setInterval(() => {-->
<!--        countdownEl.textContent = time + ' giây';-->
<!--        time&#45;&#45;;-->
<!--        if (time < 0) {-->
<!--            clearInterval(interval);-->
<!--            countdownEl.innerHTML = '<a href="#" class="resend-link">Gửi lại mã</a>';-->
<!--        }-->
<!--    }, 1000);-->
<!--</script>-->

</body>
</html>