<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HOME DECOR - QUẢN LÝ THÔNG TIN LIÊN HỆ</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_profile_style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_category.css">


    <link rel="icon" href="${pageContext.request.contextPath}/img/logo.png">

    <link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-bootstrap-4/bootstrap-4.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        .admin-form-container {
            max-width: 700px;
            margin: 40px auto;
            padding: 30px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
        }
        .admin-form-container h2 {
            text-align: center;
            color: #2c3e50;
            margin-bottom: 30px;
            border-bottom: 2px solid #27ae60;
            padding-bottom: 10px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #34495e;
        }
        .form-group input[type="text"],
        .form-group input[type="email"],
        .form-group input[type="url"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
            transition: border-color 0.3s;
        }
        .form-group input:focus {
            outline: none;
            border-color: #27ae60;
            box-shadow: 0 0 5px rgba(39,174,96,0.3);
        }
        .btn-submit {
            width: 100%;
            padding: 14px;
            background: #27ae60;
            color: white;
            border: none;
            border-radius: 6px;
            font-size: 18px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
        }
        .btn-submit:hover {
            background: #219653;
        }
    </style>
</head>
<body>

<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="content">

            <div class="admin-form-container">
                <h2>Chỉnh sửa thông tin liên hệ</h2>

                <form action="${pageContext.request.contextPath}/update-contact-settings" method="POST">
                    <div class="form-group">
                        <label>Số điện thoại:</label>
                        <input type="text" name="phone" value="${settings.phone != null ? settings.phone : ''}" placeholder="+84 123 456 789" required>
                    </div>

                    <div class="form-group">
                        <label>Email nhận liên hệ:</label>
                        <input type="email" name="email" value="${settings.email != null ? settings.email : ''}" placeholder="contact@company.com" required>
                    </div>

                    <div class="form-group">
                        <label>Địa chỉ:</label>
                        <input type="text" name="address" value="${settings.address != null ? settings.address : ''}" placeholder="Đại học Nông Lâm TP.HCM">
                    </div>

                    <div class="form-group">
                        <label>Link Facebook:</label>
                        <input type="url" name="facebook_url" value="${settings.facebookUrl != null ? settings.facebookUrl : ''}" placeholder="https://facebook.com/yourshop">
                    </div>

                    <div class="form-group">
                        <label>Link Instagram:</label>
                        <input type="url" name="instagram_url" value="${settings.instagramUrl != null ? settings.instagramUrl : ''}" placeholder="https://instagram.com/yourshop">
                    </div>

                    <div class="form-group">
                        <label>Link Twitter (X):</label>
                        <input type="url" name="twitter_url" value="${settings.twitterUrl != null ? settings.twitterUrl : ''}" placeholder="https://twitter.com/yourshop">
                    </div>

                    <div class="form-group">
                        <label>Link Google (Tìm kiếm):</label>
                        <input type="url" name="google_url" value="${settings.googleUrl != null ? settings.googleUrl : ''}" placeholder="https://google.com/search?q=yourshop">
                    </div>

                    <button type="submit" class="btn-submit">Cập nhật thông tin</button>
                </form>
            </div>

        </main>
    </div>
</div>

<!-- SweetAlert2 thông báo từ session (nếu servlet set msg) -->
<c:if test="${not empty sessionScope.msg}">
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const message = "${sessionScope.msg}";
            const type = ("${sessionScope.msgType}").trim().toLowerCase();

            Swal.fire({
                title: type === 'success' ? 'Thành công!' : 'Thông báo lỗi',
                text: message,
                icon: type === 'success' ? 'success' : 'error',
                confirmButtonColor: type === 'success' ? '#28a745' : '#d33',
                confirmButtonText: 'Đồng ý'
            });
        });
    </script>
    <c:remove var="msg" scope="session" />
    <c:remove var="msgType" scope="session" />
</c:if>

<script src="${pageContext.request.contextPath}/js/admin_category.js"></script>
</body>
</html>