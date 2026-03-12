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
    <link rel="icon" href="${pageContext.request.contextPath}/img/logo.png">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_contact_settings.css">
    <link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-bootstrap-4/bootstrap-4.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="content">
            <div class="admin-contact">
                <div class="form-container">
                    <h2>Chỉnh sửa thông tin</h2>
                    <form action="${pageContext.request.contextPath}/update-contact-settings" method="POST">
                        <div class="form-group">
                            <label>Số điện thoại</label>
                            <input type="text" name="phone"
                                   value="${settings.phone}">
                        </div>
                        <div class="form-group">
                            <label>Email</label>
                            <input type="email" name="email"
                                   value="${settings.email}">
                        </div>

                        <div class="form-group">
                            <label>Địa chỉ</label>
                            <input type="text" name="address"
                                   value="${settings.address}">
                        </div>

                        <div class="form-group">
                            <label>Facebook</label>
                            <input type="url" name="facebook_url"
                                   value="${settings.facebookUrl}">
                        </div>

                        <div class="form-group">
                            <label>Instagram</label>
                            <input type="url" name="instagram_url"
                                   value="${settings.instagramUrl}">
                        </div>

                        <div class="form-group">
                            <label>Twitter</label>
                            <input type="url" name="twitter_url"
                                   value="${settings.twitterUrl}">
                        </div>

                        <div class="form-group">
                            <label>Google</label>
                            <input type="url" name="google_url"
                                   value="${settings.googleUrl}">
                        </div>

                        <button class="btn-submit">
                            Cập nhật
                        </button>

                    </form>
                </div>
            </div>

        </main>
    </div>
</div>
</body>
</html>