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
    // Sử dụng request thay vì pageContext để tránh lỗi resolve method
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
    <style>
        /* Tổng thể container */
        .contact-wrapper {
            max-width: 1100px;
            margin: 50px auto;
            padding: 0 20px;
        }

        .contact-box {
            background: #fff;
            border-radius: 8px;
            overflow: hidden;
        }

        /* Chia layout form */
        .contact-box form {
            display: flex;
            gap: 40px;
            flex-wrap: wrap;
        }

        .form-left {
            flex: 1.5;
            min-width: 300px;
        }

        .form-right {
            flex: 1;
            min-width: 300px;
            padding-left: 40px;
            border-left: 1px solid #eee; /* Đường kẻ dọc ngăn cách */
        }

        /* Styling các ô nhập liệu */
        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 20px;
            width: 100%;
        }

        .form-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 8px;
            font-size: 15px;
        }

        .form-group input,
        .form-group textarea {
            width: 100%;
            padding: 12px 15px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 14px;
            background-color: #f9f9f9; /* Màu nền nhẹ cho input */
            transition: all 0.3s;
            box-sizing: border-box;
        }

        .form-group input:focus,
        .form-group textarea:focus {
            border-color: #8b5e3c; /* Màu thương hiệu khi focus */
            outline: none;
            background-color: #fff;
        }

        /* Nút gửi */
        .btn-submit {
            background-color: #8b5e3c; /* Màu nâu gỗ theo ảnh */
            color: white;
            padding: 12px 35px;
            border: none;
            border-radius: 6px;
            font-weight: bold;
            cursor: pointer;
            font-size: 16px;
            transition: background 0.3s;
        }

        .btn-submit:hover {
            background-color: #6f4a30;
        }

        /* Thông tin bên phải */
        .form-right h3 {
            font-size: 24px;
            margin-bottom: 15px;
        }

        .form-right p {
            color: #666;
            line-height: 1.6;
            margin-bottom: 10px;
        }

        .contact-details p {
            margin-bottom: 15px;
            font-size: 15px;
        }

        .contact-details strong {
            color: #333;
            display: inline-block;
            width: 110px;
        }

        /* Social links styling */
        .social h4 {
            margin-top: 25px;
            margin-bottom: 15px;
        }

        .social-links a {
            color: #555;
            font-size: 15px;
            display: flex;
            align-items: center;
            padding: 5px 0;
        }

        /* Responsive cho mobile */
        @media (max-width: 768px) {
            .contact-box form {
                flex-direction: column;
            }
            .form-right {
                padding-left: 0;
                border-left: none;
                border-top: 1px solid #eee;
                padding-top: 30px;
            }
            .form-row {
                flex-direction: column;
                gap: 0;
            }
        }
        /* Container danh sách mạng xã hội */
        .social-links {
            list-style: none;
            padding: 0;
            margin: 15px 0 0 0;
        }

        .social-links li {
            margin-bottom: 12px; /* Khoảng cách giữa các dòng */
        }

        .social-links a {
            display: flex;
            align-items: center;
            color: #444; /* Màu chữ trung tính */
            text-decoration: none;
            font-size: 15px;
            transition: all 0.3s ease;
        }

        /* Hiệu ứng khi di chuột qua */
        .social-links a:hover {
            color: #8b5e3c; /* Đổi sang màu nâu thương hiệu khi hover */
            transform: translateX(5px); /* Nhích nhẹ sang phải tạo cảm giác mượt mà */
        }

        /* Định dạng icon FontAwesome */
        .social-links i {
            font-size: 18px;
            margin-right: 12px;
            width: 25px; /* Giữ các icon thẳng hàng dọc */
            text-align: center;
        }

        /* Màu riêng cho từng icon nếu bạn muốn chuyên nghiệp hơn */
        .social-links .fa-facebook-f { color: #3b5998; }
        .social-links .fa-instagram { color: #e4405f; }
        .social-links .fa-twitter { color: #1da1f2; }
        .social-links .fa-google { color: #db4437; }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container">
    <!-- Thông báo success từ redirect -->
    <c:if test="${param.success eq 'true'}">
        <div style="background: #f0fdf4; color: #166534; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #bbf7d0;">
            <i class="fas fa-check-circle"></i> Gửi liên hệ thành công! Chúng tôi sẽ phản hồi sớm nhất có thể.
        </div>
    </c:if>
    <!-- Thông báo lỗi từ servlet (nếu forward) -->
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