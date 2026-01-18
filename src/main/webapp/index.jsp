<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - TRANG CHỦ</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/homepage_style.css">
    <link
            href="https://cdn.jsdelivr.net/npm/remixicon@4.7.0/fonts/remixicon.css"
            rel="stylesheet"
    />



</head>
<body>

<jsp:include page="header.jsp"></jsp:include>

<div class="banner">
    <button class="pause-play-btn" id="pausePlayBtn">
        <i class="fas fa-pause"></i>
    </button>
    <div class="indicator" id="indicator1">
        <div class="progress-loader" >

            <div class="progress"></div>
        </div>
        <div class="progress-loader" >

            <div class="progress"></div>
        </div>
        <div class="progress-loader" >

            <div class="progress"></div>
        </div>
        <div class="progress-loader" >

            <div class="progress"></div>
        </div>
    </div>
    <div class="banner-container">
        <div class="banner-slide active" id="slide-1"
             style="background-image: url('https://i.pinimg.com/1200x/c1/9d/df/c19ddf496eadac9b51cfd9c56c719686.jpg');" >
            <div class="overlay"></div>

        </div>

        <div class="banner-slide" id="slide-2" style="background-image: url('https://woodbests.com/cdn/shop/files/banner_02_1.png?v=1757746115&width=1920');">
            <div class="overlay"></div>

        </div>

        <div class="banner-slide" id="slide-3" style="background-image: url('https://images.pexels.com/photos/4050318/pexels-photo-4050318.jpeg');">
            <div class="overlay"></div>

        </div>
        <div class="banner-slide" id="slide-4" style="background-image: url('https://images.pexels.com/photos/6739226/pexels-photo-6739226.jpeg');">
            <div class="overlay"></div>

        </div>


    </div>
    <div class="banner-slogan">
        <h2>Nơi gỗ kể câu chuyện của không gian.</h2>
        <p>Từng đường vân, sắc màu và cảm hứng từ thiên nhiên được chúng tôi tỉ mỉ chọn lọc và chăm chút, mang đến cảm giác ấm áp, tinh tế và gần gũi – tạo nên không gian hài hòa, chan hòa với thiên nhiên.</p>
    </div>

    <div class="products">
        <c:forEach items="${top3Products}" var="p">
            <div class="product-card">
                <a href="detail?id=${p.id}" class="product-link">
                    <img src="${p.imageUrl}" alt="${p.nameProduct}">
                    <h2>${p.nameProduct}</h2>
                </a>

                <div class="rating">
                    <i class="ri-star-s-fill"></i>
                    <span>(${p.averageRating})</span>
                </div>

                <div class="price">
                    <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/> VNĐ
                </div>

                <div class="action-buttons">
                    <a href="add-to-cart?id=${p.id}" class="add-cart">Thêm giỏ hàng</a>
                    <a href="buy-now?id=${p.id}" class="buy-now">Mua hàng</a>
                </div>
            </div>
        </c:forEach>
    </div>


    <section class="intro-section">
        <div class="intro-content">
            <div class="intro-text">
                <h2>An tâm mua sắm với chính sách hỗ trợ toàn diện</h2>
                <p>Chúng tôi luôn đặt lợi ích khách hàng lên hàng đầu. Tìm hiểu về Chính sách Bảo hành, Vận chuyển và Đổi trả để yên tâm lựa chọn sản phẩm ưng ý.</p>
                <button class="intro-btn">Tìm hiểu ngay</button>
            </div>

            <div class="intro-image">
                <img src="https://i.pinimg.com/1200x/f3/01/41/f3014120ee3158232a4285f3695663c1.jpg" alt="sp">
                <!--                <div class="image-number">1</div>-->
            </div>
        </div>
    </section>

    <button class="nav-btn prev-btn">❮</button>
    <button class="nav-btn next-btn">❯</button>
</div>


<section class="san-pham-moi">
    <h2 class="title">SẢN PHẨM BÁN CHẠY</h2>
    <p class="section-desc">Những sản phẩm được khách hàng yêu thích nhất tại Home Decor.</p>

    <div class="san-pham-nb">
        <c:forEach items="${products}" var="p">
            <div class="product-card">
                <a href="detail?id=${p.id}" class="product-link">
                    <img src="${p.imageUrl}" alt="${p.nameProduct}">
                    <h2>${p.nameProduct}</h2>
                </a>

                <div class="rating">
                    <i class="ri-star-s-fill"></i>
                    <span>(${p.averageRating})</span>
                </div>

                <div class="price">
                    <fmt:formatNumber value="${p.price}" type="number" groupingUsed="true"/> VNĐ
                </div>

                <div class="action-buttons">
                    <a href="add-to-cart?id=${p.id}" class="add-cart">Thêm giỏ hàng</a>
                    <a href="buy-now?id=${p.id}" class="buy-now">Mua hàng</a>
                </div>
            </div>
        </c:forEach>
    </div>
</section>

<!--<section class="blog-section">-->
<!--    <div class="blog-header">-->
<!--        <h2>Bài Viết Gần Đây</h2>-->
<!--        <p>Khám phá những câu chuyện, mẹo trang trí và cảm hứng thiết kế nội thất mới nhất từ WIS Decor.</p>-->
<!--    </div>-->

<!--    <div class="blog-slider">-->
<!--        <div class="blog-card">-->
<!--            <img src="https://images.pexels.com/photos/1866149/pexels-photo-1866149.jpeg" alt="Decor Chair">-->
<!--            <div class="blog-info">-->
<!--                <h3>Ghế Mộc Tinh Tế Cho Không Gian Tối Giản</h3>-->
<!--                <p>Chiếc ghế đơn giản nhưng mang lại điểm nhấn tinh tế cho căn phòng của bạn.</p>-->
<!--            </div>-->
<!--        </div>-->

<!--        <div class="blog-card">-->
<!--            <img src="https://images.pexels.com/photos/1571460/pexels-photo-1571460.jpeg" alt="Living Room">-->
<!--            <div class="blog-info">-->
<!--                <h3>Trang Trí Phòng Khách Với Tông Màu Ấm</h3>-->
<!--                <p>Sự kết hợp giữa gỗ tự nhiên và ánh sáng vàng mang lại cảm giác ấm áp, gần gũi.</p>-->
<!--            </div>-->
<!--        </div>-->

<!--        <div class="blog-card">-->
<!--            <img src="https://images.pexels.com/photos/1571463/pexels-photo-1571463.jpeg" alt="Bedroom Decor">-->
<!--            <div class="blog-info">-->
<!--                <h3>Phòng Ngủ Thư Giãn Với Chất Liệu Tự Nhiên</h3>-->
<!--                <p>Không gian nghỉ ngơi nhẹ nhàng, mang đậm hơi thở của thiên nhiên.</p>-->
<!--            </div>-->
<!--        </div>-->

<!--        <div class="blog-card">-->
<!--            <img src="https://images.pexels.com/photos/1457841/pexels-photo-1457841.jpeg" alt="Dining Room">-->
<!--            <div class="blog-info">-->
<!--                <h3>Góc Ăn Uống Ấm Cúng Cho Gia Đình</h3>-->
<!--                <p>Tạo cảm giác sum vầy bằng cách lựa chọn nội thất phù hợp với phong cách sống.</p>-->
<!--            </div>-->
<!--        </div>-->
<!--    </div>-->
<!--</section>-->

<section class="about-section">
    <div class="about-content">
        <div class="about-text">
            <h2>Về <span>HOME DECOR</span></h2>
            <h3>Không Gian Sống – Nơi Cảm Xúc Bắt Đầu</h3>
            <p>
                Home Decor mang đến giải pháp thiết kế nội thất độc đáo, kết hợp giữa phong cách hiện đại
                và tinh thần mộc mạc. Chúng tôi tin rằng mỗi món đồ gỗ đều kể một câu chuyện –
                câu chuyện về gu thẩm mỹ và dấu ấn riêng của bạn.
            </p>
            <div class="about-signature">
                <p><strong>Tran Thi Thuy Kieu</strong><br>Founder & CEO, HOME Decor</p>
            </div>
            <button class="about-btn">Tìm Hiểu Thêm</button>
        </div>

        <div class="about-images">
            <div class="img-top">
                <img src="https://images.pexels.com/photos/1571460/pexels-photo-1571460.jpeg" alt="Living room decor">
                <img src="https://images.pexels.com/photos/1457841/pexels-photo-1457841.jpeg" alt="Bedroom decor">
            </div>

            <div class="about-card">
                <div class="card-header">
                    <img src="https://i.pinimg.com/736x/ac/ee/d5/aceed58b2efee46f597864ed43a8c291.jpg" alt="user">
                    <div class="card-info">
                        <h4>Xuân Mai</h4>
                        <p>Khách hàng thân thiết</p>
                    </div>

                    <i class="ri-star-s-fill rating"></i><span>(4.9) </span>
                </div>
                <p>
                    “Tôi thật sự ấn tượng với sự tinh tế trong từng chi tiết.
                    Không gian trở nên ấm cúng và sang trọng hơn rất nhiều.”
                </p>
            </div>
        </div>
    </div>
</section>

<%--<div class="footer">--%>
<%--    <div class="footer-container">--%>

<%--        <div class="footer-col">--%>
<%--            <h3>Về chúng tôi</h3>--%>


<%--            <p>HOME DECOR</p>--%>

<%--            <p><i class="fa-solid"></i> Địa chỉ: Trường Đại học Nông Lâm Thành phố Hồ Chí Minh</p>--%>
<%--            <p><i class="fa-solid"></i> Số điện thoại: 0944459364</p>--%>
<%--            <p><i class="fa-solid"></i> Email: 23130082@st.hcmuaf.edu.vn</p>--%>

<%--            <div class="social-icons">--%>
<%--                <a href="#"><i class="fab fa-facebook"></i></a>--%>
<%--                <a href="#"><i class="fab fa-instagram"></i></a>--%>
<%--                <a href="#"><i class="fab fa-tiktok"></i></a>--%>
<%--            </div>--%>
<%--        </div>--%>


<%--        <div class="footer-col">--%>
<%--            <h3>Chính sách</h3>--%>
<%--            <a href="introduce_user.html">Về tụi mình</a>--%>
<%--            <a href="purchasing_policy_user.html">Chính sách Thanh toán</a>--%>
<%--            <a href="purchasing_policy_user.html">Chính sách Giao hàng</a>--%>
<%--            <a href="purchasing_policy_user.html">Chính sách Đổi trả</a>--%>
<%--        </div>--%>


<%--        <div class="footer-col">--%>
<%--            <h3>Hỗ trợ khách hàng</h3>--%>
<%--            <a href="product_all_user.html">Tất cả sản phẩm</a>--%>
<%--        </div>--%>


<%--        <div class="footer-col">--%>
<%--            <h3>Liên kiết nhanh</h3>--%>
<%--            <a href="homepage_user.jsp">Trang chủ</a>--%>
<%--            <a href="purchasing_policy_user.html">Chính sách mua hàng</a>--%>
<%--            <a href="introduce_user.html">Giới thiệu</a>--%>
<%--            <a href="contact_user.html">Liên hệ</a>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>
<jsp:include page="footer.jsp"></jsp:include>
</body>
<script src="js/homepage.js"></script>
</html>