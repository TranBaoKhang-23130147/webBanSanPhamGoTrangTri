<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("LOGGED_USER");
%>
<div class="footer">
    <div class="footer-container">


        <div class="footer-col">
            <h3>Về chúng tôi</h3>


            <p>HOME DECOR</p>

            <p>
                Số điện thoại: ${contactSettings.phone}
            </p>

            <p>
                Email: ${contactSettings.email}
            </p>

            <p>
                Địa chỉ: ${contactSettings.address}
            </p>


            <div class="social-icons">
                <c:if test="${not empty contactSettings.facebookUrl}">
                    <a href="${contactSettings.facebookUrl}" target="_blank">
                        <i class="fab fa-facebook"></i>
                    </a>
                </c:if>

                <c:if test="${not empty contactSettings.instagramUrl}">
                    <a href="${contactSettings.instagramUrl}" target="_blank">
                        <i class="fab fa-instagram"></i>
                    </a>
                </c:if>

                <c:if test="${not empty contactSettings.twitterUrl}">
                    <a href="${contactSettings.twitterUrl}" target="_blank">
                        <i class="fab fa-twitter"></i>
                    </a>
                </c:if>
                <c:if test="${not empty contactSettings.googleUrl}">
                    <a href="${contactSettings.googleUrl}" target="_blank" title="Google">
                        <i class="fab fa-google"></i>
                    </a>
                </c:if>
            </div>

        </div>


        <div class="footer-col">
            <h3>Chính sách</h3>
            <a href="../introduce_user.jsp">Về tụi mình</a>
            <a href="../purchasing_policy_user.jsp">Chính sách Thanh toán</a>
            <a href="../purchasing_policy_user.jsp">Chính sách Giao hàng</a>
            <a href="../purchasing_policy_user.jsp">Chính sách Đổi trả</a>
        </div>


        <div class="footer-col">
            <h3>Hỗ trợ khách hàng</h3>
            <a href="../product_all_user.jsp">Tất cả sản phẩm</a>
        </div>


        <div class="footer-col">
            <h3>Liên kiết nhanh</h3>
            <a href="../homepage_user.jsp">Trang chủ</a>
            <a href="../purchasing_policy_user.jsp">Chính sách mua hàng</a>
            <a href="../introduce_user.jsp">Giới thiệu</a>
            <a href="../contact_user.jsp">Liên hệ</a>
        </div>

    </div>
</div>