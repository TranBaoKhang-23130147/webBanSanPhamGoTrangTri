<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - SẢN PHẨM</title>
    <link rel="icon" type="image/png" href="img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/decorate_style.css">
    <link
            href="https://cdn.jsdelivr.net/npm/remixicon@4.7.0/fonts/remixicon.css"
            rel="stylesheet"
    />

</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<%--<div id="header">--%>
<%--    <div id="logo"><img src="img/logo.png" class="image"/>--%>
<%--        <div class="brand"> <div id="name-web">HOME DECOR</div>--%>
<%--            <div id="sub-slogan">Nét mộc trong từng góc nhỏ</div></div>--%>
<%--    </div>--%>

<%--    <nav class="menu-bar">--%>

<%--        <a class="menu" id="home" href="homepage_user.jsp"> TRANG CHỦ</a>--%>
<%--        <div class="menu product-menu">--%>
<%--            <a id="product" href="product_all_user.jsp">SẢN PHẨM</a>--%>
<%--            <div class="submenu">--%>
<%--                <a href="LivingroomDecorateServlet?category=trang-tri-phong-khach">--%>
<%--                    TRANG TRÍ PHÒNG KHÁCH--%>
<%--                </a>--%>
<%--                <a href="BedroomDecorateServlet?category=trang-tri-phong-ngu">TRANG TRÍ PHÒNG NGỦ</a>--%>
<%--                <a href="KitchenDecorateServlet?category=trang-tri-phong-bep">TRANG TRÍ PHÒNG BẾP</a>--%>
<%--                <a href="HomeofficeDecorateServlet?category=trang-tri-phong-lam-viec">TRANG TRÍ PHÒNG LÀM VIỆC</a>--%>
<%--                <a href="MiniitemDecorateServlet?category=do-trang-tri-mini">ĐỒ TRANG TRÍ MINI</a>--%>
<%--                <a href="SouvenirServlet?category=qua-luu-niem">QUÀ LƯU NIỆM</a>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--        <a class="menu" id=" " href="purchasing_policy_user.jsp" >CHÍNH SÁCH MUA HÀNG</a>--%>

<%--        <a class="menu" id="introduce" href="introduce_user.jsp" >GIỚI THIỆU</a>--%>
<%--        <a class="menu" id="contact" href="contact_user.jsp">LIÊN HỆ</a>--%>


<%--    </nav>--%>
<%--    <div class="icons">--%>
<%--        <a class="nav_item" href="html/shopping-cart.html" id="cart-link" >--%>
<%--            <i class="fas fa-shopping-cart"></i>--%>
<%--        </a>--%>

<%--        <a class="nav-item" href="html/search.html" id="search-link">--%>
<%--            <i class="fas fa-search"></i>--%>
<%--        </a>--%>

<%--        <div class="user-login">--%>
<%--            <i class="fas fa-user"></i>--%>
<%--            <div class="user">--%>
<%--                <a class="nav_item" href="mypage_user.jsp" id="login">Trang của tôi</a>--%>
<%--                <a class="nav-item" href="homepage.html" id="register">Đăng xuất</a>--%>
<%--            </div>--%>

<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>

<section class="banner-product">

    <img
            src="https://i.pinimg.com/1200x/dc/29/25/dc2925db1bfab4c46a2c1392247fa682.jpg"
            alt="Ảnh trang trí phòng bếp"
            class="banner-image"
    />
    <div class="banner-overlay">
        <div class="banner-content">
            <h2>TRANG TRÍ PHÒNG BẾP</h2>
            <p>Tạo điểm nhấn cho nơi giữ lửa gia đình bằng những sản phẩm decor tiện nghi và phong cách.</p>
            <button id="scrollToProducts">Khám Phá Ngay</button>
        </div>
    </div>
</section>
<section id="productSection">
<!--    <h3 class="titleProduct">Sản phẩm dành cho phòng bếp</h3>-->
    <div class="product-container">

        <form action="ProductFilterServlet" method="get">
            <input type="hidden" name="page" value="kitchen">
            <input type="hidden" name="category" value="trang-tri-phong-bep">
            <aside class="filter-sidebar">
                <h3>Bộ lọc</h3>

                <div class="filter-group">
                    <h4>Loại</h4>
                    <label><input type="checkbox" name="type" value="8"> Bàn</label>
                    <label><input type="checkbox" name="type" value="2"> Ghế</label>
                    <label><input type="checkbox" name="type" value="4"> Tủ</label>
                    <label><input type="checkbox" name="type" value="7"> Giường</label>
                    <label><input type="checkbox" name="type" value="9"> Kệ</label>
                    <label><input type="checkbox" name="type" value=""> Khác</label>
                </div>

                <div class="filter-group">
                    <h4>Giá tiền</h4>
                    <label><input type="checkbox" name="price" value="1"> Dưới 1 triệu</label>
                    <label><input type="checkbox" name="price" value="2"> 1 - 3 triệu</label>
                    <label><input type="checkbox" name="price" value="3"> 3 - 5 triệu</label>
                    <label><input type="checkbox" name="price" value="4"> 5 - 10 triệu</label>
                    <label><input type="checkbox" name="price" value="5"> Trên 10 triệu</label>
                </div>


                <div class="filter-group">
                    <h4>Đánh giá</h4>
                    <label><input type="checkbox" name="rating" value="5">
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                    </label>
                    <label><input type="checkbox" name="rating" value="4">
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                    </label>
                    <label><input type="checkbox" name="rating" value="3">
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                    </label>
                    <label><input type="checkbox" name="rating" value="2">
                        <i class="ri-star-s-fill rating"></i>
                        <i class="ri-star-s-fill rating"></i>
                    </label>
                    <label><input type="checkbox" name="rating" value="1">
                        <i class="ri-star-s-fill rating"></i>
                    </label>
                </div>
                <br><br>
                <!-- 🔥 NÚT LỌC BẮT BUỘC -->
                <button type="submit">LỌC SẢN PHẨM</button>
            </aside>
        </form>

        <div class="product">

            <c:forEach items="${listP}" var="p">
                <div class="product-card">
                    <a href="detail?id=${p.id}" class="product-link">
                        <div class="set">
                            <img src="${pageContext.request.contextPath}/${p.imageUrl}"
                                 onerror="this.src='${pageContext.request.contextPath}/img/logo.png'"
                                 alt="${p.nameProduct}">
                            <h2>${p.nameProduct}</h2>

                            <div class="rating">
                                    <%-- Sửa logic hiển thị sao dựa trên số thực --%>
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="${i <= p.averageRating ? 'ri-star-s-fill' : 'ri-star-s-line'}"></i>
                                </c:forEach>
                                <span>(<fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1"/>)</span>
                            </div>

                            <div class="price">
                                <fmt:formatNumber value="${p.price}" type="number"/> VNĐ
                            </div>
                        </div>
                    </a>

                    <div class="action-buttons">
                        <button class="add-cart" onclick="addToCart(${p.id})">Thêm giỏ hàng</button>
                        <button class="buy-now">Mua hàng</button>
                    </div>
                </div>
            </c:forEach>

            <%-- Hiển thị thông báo nếu không có sản phẩm nào --%>
            <c:if test="${empty listP}">
                <p style="text-align: center; width: 100%;">Không tìm thấy sản phẩm nào phù hợp.</p>
            </c:if>
        </div>
    </div>
</section>
<div class="footer">
    <div class="footer-container">


        <div class="footer-col">
            <h3>Về chúng tôi</h3>


            <p>HOME DECOR</p>

            <p><i class="fa-solid"></i> Địa chỉ: Trường Đại học Nông Lâm Thành phố Hồ Chí Minh</p>
            <p><i class="fa-solid"></i> Số điện thoại: 0944459364</p>
            <p><i class="fa-solid"></i> Email: 23130082@st.hcmuaf.edu.vn</p>

            <div class="social-icons">
                <a href="#"><i class="fab fa-facebook"></i></a>
                <a href="#"><i class="fab fa-instagram"></i></a>
                <a href="#"><i class="fab fa-tiktok"></i></a>
            </div>
        </div>


        <div class="footer-col">
            <h3>Chính sách</h3>
            <a href="introduce_user.jsp">Về tụi mình</a>
            <a href="purchasing_policy_user.jsp">Chính sách Thanh toán</a>
            <a href="purchasing_policy_user.jsp">Chính sách Giao hàng</a>
            <a href="purchasing_policy_user.jsp">Chính sách Đổi trả</a>
        </div>


        <div class="footer-col">
            <h3>Hỗ trợ khách hàng</h3>
            <a href="product_all_user.jsp">Tất cả sản phẩm</a>
        </div>


        <div class="footer-col">
            <h3>Liên kiết nhanh</h3>
            <a href="homepage_user.jsp">Trang chủ</a>
            <a href="purchasing_policy_user.jsp">Chính sách mua hàng</a>
            <a href="introduce_user.jsp">Giới thiệu</a>
            <a href="contact_user.jsp">Liên hệ</a>
        </div>

    </div>
</div>

</body>
<script src="js/decorate.js"></script>
</html>