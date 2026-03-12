<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="icon" type="image/png" href="img/logo.png">
    <title>HOME DECOR - ĐĂNG NHẬP</title>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css">

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/login_style.css">
</head>

<body>

<div class="messages">

    <c:if test="${not empty Error}">
        <div class="error">${Error}</div>
    </c:if>

    <c:if test="${not empty MESS_REGISTER}">
        <div class="error">${MESS_REGISTER}</div>
    </c:if>

    <c:if test="${not empty MESS_SUCCESS}">
        <div class="success">${MESS_SUCCESS}</div>
    </c:if>

</div>

<div class="container" id="container">
    <div class="form-container sign-up">

        <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">

            <h1>Đăng ký</h1>

            <div class="social-container">
                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile
                &redirect_uri=http://localhost:8080/webBanSanPhamGoTrangTri_war_exploded/google-login
                &response_type=code
                &client_id=1089942878583-pq5ui5eubco8s2lav650ln4gn19gogfe.apps.googleusercontent.com
                &approval_prompt=force" class="social">
                    <i class="fab fa-google-plus-g"></i>
                </a>
            </div>

            <span>hoặc sử dụng email của bạn để đăng ký</span>

            <div class="infield">
                <input type="text" name="username" placeholder="Tên" required>
            </div>

            <div class="infield">
                <input type="email" name="email" placeholder="Email" required>
            </div>

            <div class="infield">
                <input type="password" name="password" placeholder="Mật khẩu" required minlength="8">
                <small class="password-note">
                    Mật khẩu phải chứa ít nhất 8 ký tự, 1 chữ hoa, 1 số và 1 ký tự đặc biệt
                </small>
            </div>

            <div class="infield">
                <input type="password" name="re_password" placeholder="Xác nhận mật khẩu" required>
            </div>

            <button>Đăng ký</button>

        </form>
    </div>

    <div class="form-container sign-in">

        <form action="${pageContext.request.contextPath}/LoginServlet" method="post">

            <h1>Đăng nhập</h1>

            <span>hoặc sử dụng tài khoản của bạn</span>

            <div class="infield">
                <input type="email" name="email" placeholder="Email" required>
            </div>

            <div class="infield">
                <input type="password" name="password" placeholder="Mật khẩu" required>
            </div>

            <div class="remember-forgot">
                <a href="login_forgot_password.jsp" class="forgot">Quên mật khẩu?</a>
            </div>

            <button>Đăng nhập</button>

        </form>

    </div>

    <div class="overlay-container" id="overlayCon">

        <div class="overlay">

            <div class="overlay-panel overlay-left">
                <h1>HOME DECOR!</h1>
                <p>Để tiếp tục kết nối với chúng tôi, vui lòng đăng nhập bằng thông tin cá nhân của bạn</p>
                <button>Đăng nhập</button>
            </div>

            <div class="overlay-panel overlay-right">
                <h1>HOME DECOR!</h1>
                <p>Hãy nhập thông tin cá nhân của bạn và bắt đầu hành trình cùng chúng tôi</p>
                <button>Đăng ký</button>
            </div>

        </div>

        <button id="overlayBtn"></button>

    </div>

</div>
<div id="otpModal" class="otp-modal ${SHOW_OTP ? 'show' : ''}">

    <div class="otp-box">

        <h3>Nhập mã OTP</h3>

        <c:if test="${not empty ERROR}">
            <div class="error">${ERROR}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/VerifyOtpServlet" method="post">
            <input type="text" name="otp" placeholder="Nhập mã OTP" required>
            <button type="submit">XÁC NHẬN</button>
        </form>

        <p class="note">Mã OTP đã được gửi về email của bạn</p>

    </div>

</div>


<script src="js/login_JS.js"></script>

</body>
</html>