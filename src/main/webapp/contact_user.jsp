<%@ page import="model.User" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    User user = (User) session.getAttribute("LOGGED_USER");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - LIÊN HỆ</title>
    <link rel="icon" type="image/png" sizes="9992x9992" href="../img/logo.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/contact_style.css">

</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<section class="contact-wrapper">
    <div class="contact-box">

        <!-- Form liên hệ -->
        <div class="form-container">
            <form>
                <div class="row">
                    <div class="input-group">
                        <label>Họ</label>
                        <input type="text" placeholder="Nhập họ của bạn">
                    </div>

                    <div class="input-group">
                        <label>Tên</label>
                        <input type="text" placeholder="Nhập tên của bạn">
                    </div>
                </div>

                <div class="row">
                    <div class="input-group">
                        <label>Email</label>
                        <input type="email" placeholder="Nhập email">
                    </div>

                    <div class="input-group">
                        <label>Số điện thoại</label>
                        <input type="text" placeholder="Nhập số điện thoại">
                    </div>
                </div>

                <div class="input-group">
                    <label>Nội dung cần liên hệ</label>
                    <textarea placeholder="Vui lòng nhập nội dung..."></textarea>
                </div>

                <button type="submit" class="btn-submit">Gửi liên hệ</button>
            </form>
        </div>

        <!-- Info liên hệ -->
        <div class="info-container">
            <h2>Liên hệ với chúng tôi</h2>
            <p>Vui lòng để lại thông tin và nội dung liên hệ, chúng tôi sẽ phản hồi sớm nhất có thể.</p>

            <div class="contact-info">
                <p><b>Số điện thoại:</b> +84 123 456 789</p>
                <p><b>Email:</b> contact@company.com</p>
                <p><b>Địa chỉ:</b> Đại học Nông Lâm TP.HCM</p>
            </div>

            <div class="follow-us">
                <b>Theo dõi chúng tôi:</b>
                <p>Facebook — Instagram — Twitter — Google</p>
            </div>
        </div>

    </div>
</section>


<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>