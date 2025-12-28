<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <title>HOME DECOR - SẢN PHẨM</title>
    <link rel="icon" type="image/png" sizes="9992x9992" href="img/p.png" class="lo">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/product_all_style.css">
    <link
            href="https://cdn.jsdelivr.net/npm/remixicon@4.7.0/fonts/remixicon.css"
            rel="stylesheet"
    />
</head>
<body>
<div id="header">
    <div id="logo"><img src="../img/logo.png" class="image"/>
        <div class="brand"> <div id="name-web">HOME DECOR</div>
            <div id="sub-slogan">Nét mộc trong từng góc nhỏ</div></div>
    </div>

    <nav class="menu-bar">

        <a class="menu" id="home" href="homepage_user.jsp"> TRANG CHỦ</a>
        <div class="menu product-menu">
            <a id="product" href="product_all_user.jsp">SẢN PHẨM</a>
            <div class="submenu">
                <a href="decorate_livingroom_user.html">TRANG TRÍ PHÒNG KHÁCH</a>
                <a href="decorate_bedroom_user.html">TRANG TRÍ PHÒNG NGỦ</a>
                <a href="decorate_kitchen_user.html">TRANG TRÍ PHÒNG BẾP</a>
                <a href="decorate_homeoffice_user.html">TRANG TRÍ PHÒNG LÀM VIỆC</a>
                <a href="decorate_miniitem_user.html">ĐỒ TRANG TRÍ MINI</a>
                <a href="souvenirs_user.html">QUÀ LƯU NIỆM</a>
            </div>
        </div>
        <a class="menu" id=" " href="../purchasing_policy_user.jsp" >CHÍNH SÁCH MUA HÀNG</a>

        <a class="menu" id="introduce" href="../introduce_user.jsp" >GIỚI THIỆU</a>
        <a class="menu" id="contact" href="../contact_user.jsp">LIÊN HỆ</a>


    </nav>
    <div class="icons">
        <a class="nav_item" href="shopping-cart.html" id="cart-link" >
            <i class="fas fa-shopping-cart"></i>
        </a>

        <a class="nav-item" href="search.html" id="search-link">
            <i class="fas fa-search"></i>
        </a>

        <div class="user-login">
            <i class="fas fa-user"></i>
            <div class="user">
                <a class="nav_item" href="mypage_user.jsp" id="login">Trang của tôi</a>
                <a class="nav-item" href="index.jsp" id="register">Đăng xuất</a>
            </div>

        </div>
    </div>
</div>
<section class="overlay-section">
    <div class="overlay-card " style="background-image: url('https://i.pinimg.com/1200x/4d/16/07/4d16076bd71f77a7b5f69963e875cac6.jpg');">
        <h3>TRANG TRÍ PHÒNG KHÁCH</h3>
        <a href="decorate_livingroom_user.html"><button >Xem chi tiết</button></a>
    </div>
    <div class="overlay-card" style="background-image: url('https://i.pinimg.com/736x/22/bc/ce/22bcce5d6c7b7412d817bb51a6daaf23.jpg');">
        <h3>TRANG TRÍ PHÒNG NGỦ</h3>
        <a href="decorate_bedroom_user.html"><button>Xem chi tiết</button></a>
    </div>
    <div class="overlay-card" style="background-image: url('https://i.pinimg.com/1200x/dc/29/25/dc2925db1bfab4c46a2c1392247fa682.jpg');">
        <h3>TRANG TRÍ PHÒNG BẾP</h3>
        <a href="decorate_kitchen_user.html"><button>Xem chi tiết</button></a>
    </div>
    <div class="overlay-card" style="background-image: url('https://i.pinimg.com/1200x/53/82/9a/53829ac906f0539b852666eb726f7278.jpg');">
        <h3>TRANG TRÍ PHÒNG LÀM VIỆC</h3>
        <a href="decorate_homeoffice_user.html"><button>Xem chi tiết</button></a>
    </div>
    <div class="overlay-card" style="background-image: url('https://i.pinimg.com/1200x/b7/f1/eb/b7f1eb1afde268f136926ca69c3b53fd.jpg');">
        <h3>ĐỒ TRANG TRÍ MINI</h3>
        <a href="decorate_miniitem_user.html"><button>Xem chi tiết</button></a>
    </div>
    <div class="overlay-card" style="background-image: url('https://i.pinimg.com/1200x/b2/84/fa/b284fab9541221dff3b9e5f9b49af1ad.jpg');">
        <h3>QUÀ LƯU NIỆM</h3>
        <a href="souvenirs_user.html"><button>Xem chi tiết</button></a>
    </div>
</section>
<section class="product-all">
    <h2 class="titleProduct">TẤT CẢ SẢN PHẨM</h2>
    <div class="product-container">

        <aside class="filter-sidebar">
            <h3>Bộ lọc</h3>

            <div class="filter-group">
                <h4>Loại</h4>
                <label><input type="checkbox" name="type" value="ban"> Bàn</label>
                <label><input type="checkbox" name="type" value="ghe"> Ghế</label>
                <label><input type="checkbox" name="type" value="tu"> Tủ</label>
                <label><input type="checkbox" name="type" value="giuong"> Giường</label>
                <label><input type="checkbox" name="type" value="ke"> Kệ</label>
                <label><input type="checkbox" name="type" value="khac"> Khác</label>
            </div>

            <div class="filter-group">
                <h4>Giá tiền</h4>
                <label><input type="checkbox" name="price" value="duoi1"> Dưới 1 triệu</label>
                <label><input type="checkbox" name="price" value="1-3"> 1 - 3 triệu</label>
                <label><input type="checkbox" name="price" value="3-5"> 3 - 5 triệu</label>
                <label><input type="checkbox" name="price" value="5-10"> 5 - 10 triệu</label>
                <label><input type="checkbox" name="price" value="tren10"> Trên 10 triệu</label>
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
            </div>`
        </aside>

        <div class="products">
            <c:forEach items="${listP}" var="p">
                <div class="product-card">
                    <a href="product-detail?id=${p.id}" class="product-link">
                        <div class="set">
                            <img src="${p.imageUrl}" alt="${p.nameProduct}">
                            <h2>${p.nameProduct}</h2>
                            <div class="rating">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="${i <= p.rating ? 'ri-star-s-fill' : 'ri-star-s-line'}"></i>
                                </c:forEach>
                                <span>(${p.rating})</span>
                            </div>

                            <div class="price">
                                <fmt:formatNumber value="${p.price}" type="number"/> VNĐ                            </div>
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
            <a href="../introduce_user.jsp">Về tụi mình</a>
            <a href="../purchasing_policy_user.jsp">Chính sách Thanh toán</a>
            <a href="../purchasing_policy_user.jsp">Chính sách Giao hàng</a>
            <a href="../purchasing_policy_user.jsp">Chính sách Đổi trả</a>
        </div>


        <div class="footer-col">
            <h3>Hỗ trợ khách hàng</h3>
            <a href="product_all_user.html">Tất cả sản phẩm</a>
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

</body>
<script src="../js/decorate.js"></script>
</html>