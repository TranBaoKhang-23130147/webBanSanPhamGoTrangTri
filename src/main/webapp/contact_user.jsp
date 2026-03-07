<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ page import="dao.ContactSettingsDao, model.ContactSettings" %>
<%
    dao.ContactSettingsDao settingsDao = new dao.ContactSettingsDao();
    model.ContactSettings settings = settingsDao.getSettings();
    if (settings == null) {
        settings = new model.ContactSettings();
    }
    request.setAttribute("settings", settings);
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HOME DECOR - LIÊN HỆ</title>
    <link rel="icon" type="image/png" sizes="32x32" href="../img/logo.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/contact_style.css">

</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container">
    <c:if test="${param.success eq 'true'}">
        <div style="background: #f0fdf4; color: #166534; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #bbf7d0;">
            <i class="fas fa-check-circle"></i> Gửi liên hệ thành công! Chúng tôi sẽ phản hồi sớm nhất có thể.
        </div>
    </c:if>
    <c:if test="${not empty requestScope.error}">
        <div class="alert alert-danger">
            Lỗi: ${requestScope.error}
        </div>
    </c:if>

    <section class="contact-wrapper">
        <div class="contact-box">
            <form action="${pageContext.request.contextPath}/contact-user" method="post">
                <div class="form-left">
                    <div class="form-row">
                        <div class="form-group">
                            <label>Họ</label>
                            <input type="text" name="lastName" placeholder="Nhập họ của bạn" required>
                        </div>
                        <div class="form-group">
                            <label>Tên</label>
                            <input type="text" name="firstName" placeholder="Nhập tên của bạn" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" name="email" placeholder="Nhập email của bạn" required>
                    </div>

                    <div class="form-group">
                        <label>Số điện thoại</label>
                        <input type="tel" name="phone" placeholder="Nhập số điện thoại" required>
                    </div>

                    <div class="form-group">
                        <label>Nội dung cần liên hệ</label>
                        <textarea name="message" rows="6" placeholder="Vui lòng nhập nội dung cần hỗ trợ..." required></textarea>
                    </div>

                    <button type="submit" class="btn-submit">Gửi liên hệ</button>
                </div>

                <div class="form-right">
                    <h3>Liên hệ với chúng tôi</h3>
                    <p>Vui lòng để lại thông tin và nội dung liên hệ, chúng tôi sẽ phản hồi sớm nhất có thể.</p>

                    <div class="contact-details">
                        <p><strong>Số điện thoại:</strong> <%= settings.getPhone() != null ? settings.getPhone() : "Chưa cập nhật" %></p>
                        <p><strong>Email:</strong> <%= settings.getEmail() != null ? settings.getEmail() : "Chưa cập nhật" %></p>
                        <p><strong>Địa chỉ:</strong> <%= settings.getAddress() != null ? settings.getAddress() : "Chưa cập nhật" %></p>
                    </div>

                    <div class="social">
                        <h4 style="margin-top: 25px; font-size: 18px; color: #333;">Theo dõi chúng tôi:</h4>
                        <ul class="social-links">
                            <c:if test="${not empty settings.facebookUrl}">
                                <li><a href="${settings.facebookUrl}" target="_blank"><i class="fab fa-facebook-f"></i> Facebook</a></li>
                            </c:if>
                            <c:if test="${not empty settings.instagramUrl}">
                                <li><a href="${settings.instagramUrl}" target="_blank"><i class="fab fa-instagram"></i> Instagram</a></li>
                            </c:if>
                            <c:if test="${not empty settings.twitterUrl}">
                                <li><a href="${settings.twitterUrl}" target="_blank"><i class="fab fa-twitter"></i> Twitter (X)</a></li>
                            </c:if>
                            <c:if test="${not empty settings.googleUrl}">
                                <li><a href="${settings.googleUrl}" target="_blank"><i class="fab fa-google"></i> Google (Tìm kiếm)</a></li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </form>
        </div>
    </section>
</div>

<jsp:include page="footer.jsp"/>

</body>
</html>