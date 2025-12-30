<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="model.User" %>
<%
    User user = (User) session.getAttribute("LOGGED_USER");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - TÌM KIẾM</title>
    <link rel="icon" type="image/png" href="img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/search_style.css">
    <link
            href="https://cdn.jsdelivr.net/npm/remixicon@4.7.0/fonts/remixicon.css"
            rel="stylesheet"
    />
</head>
<body>


<jsp:include page="header.jsp"></jsp:include>


<section class="hero">
    <div class="hero-content">
        <h1>Tìm sản phẩm <br> trang trí hoàn hảo cho ngôi nhà bạn</h1>
        <p>Khám phá hàng nghìn sản phẩm nội thất cao cấp, thiết kế tinh tế</p>

        <form action="search" method="get" class="search-bar">
            <input type="text" name="txtSearch" placeholder="Nhập tên sản phẩm...">
            <select name="category">
                <option value="all">Phân loại</option>
                <option value="Bàn ghế">Bàn ghế</option>
                <option value="Kệ">Kệ</option>
            </select>
            <button type="submit"><i class="fas fa-search"></i></button>
        </form>

    </div>

    <div class="hero-image">
        <img src="https://images.unsplash.com/photo-1615874959474-d609969a20ed" alt="Trang trí nội thất">
        <div class="decor-stats">
            <p><strong>Với đa dạng</strong> sản phẩm<br></p>
            <p><strong>Phù hợp</strong> cho <br><span>ngôi nhà của bạn</span></p>
            <p><strong>Home Decor</strong> rất vui<br><span>khi bạn tin chọn</span></p>
        </div>
    </div>
</section>
<section class="featured">
    <h2>Sản phẩm gợi ý cho bạn</h2>
    <p>Khám phá những sản phẩm được yêu thích nhất hiện nay</p>

    <div class="products">
        <div class="product-card">
            <!--            <span class="badge">HOT</span>-->
            <div class="set">
                <img src="https://i.pinimg.com/736x/0b/e6/ab/0be6ab843569781d6a78bd3786e736de.jpg" alt="ke treo tuong">
                <h2>Kệ treo tường</h2>
                <div class="rating">
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <span>(4.0)</span></div>
                <div class="price">129.000₫</div>
                <div class="action-buttons">
                    <button class="add-cart">Thêm giỏ hàng</button>
                    <button class="buy-now">Mua hàng</button>
                </div>
            </div>
        </div>

        <div class="product-card">
            <div class="set">
                <img src="https://i.pinimg.com/736x/68/1c/46/681c469884d60a1a27b0b3686c589f3e.jpg" alt="Bộ bàn ghế gỗ tự nhiên">
                <h2>Bộ bàn ghế gỗ tự nhiên</h2>
                <div class="rating">
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <span>(5.0)</span></div>
                <div class="price">99.000₫</div>
                <div class="action-buttons">
                    <button class="add-cart">Thêm giỏ hàng</button>
                    <button class="buy-now">Mua hàng</button>
                </div>
            </div>
        </div>

        <div class="product-card">
            <div class="set">
                <!--                <span class="badge">SALE</span>-->

                <img src="https://i.pinimg.com/1200x/9b/81/7e/9b817e378dc66a4c57a75c9072c98f7e.jpg" alt="Bình hoa gỗ">
                <h2>Bình hoa bằng gỗ</h2>
                <div class="rating">
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-half-line"></i>
                    <span>(4.5)</span></div>
                <div class="price">453.000₫</div>
                <div class="action-buttons">
                    <button class="add-cart">Thêm giỏ hàng</button>
                    <button class="buy-now">Mua hàng</button>
                </div>
            </div>
        </div>
        <div class="product-card">
            <div class="set">
                <img src="https://i.pinimg.com/736x/23/15/a0/2315a0ec69375b795b3736ef05f59b40.jpg" alt="Kệ đựng gia vị">
                <h2>Kệ đựng gia vị</h2>
                <div class="rating">
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-s-fill"></i>
                    <i class="ri-star-half-line"></i>
                    <span>(4.5)</span></div>
                <div class="price">300.000₫</div>
                <div class="action-buttons">
                    <button class="add-cart">Thêm giỏ hàng</button>
                    <button class="buy-now">Mua hàng</button>
                </div>
            </div>
        </div>
    </div>

</section>
<section class="categories">
    <h2>Danh mục nổi bật</h2>
    <p>Khám phá không gian nội thất theo từng phong cách</p>

    <div class="category-list">
        <div class="category-card"><i class="fas fa-couch"></i><p>Phòng khách</p><span>124 sản phẩm</span></div>
        <div class="category-card"><i class="fas fa-bed"></i><p>Phòng ngủ</p><span>89 sản phẩm</span></div>
        <div class="category-card"><i class="fas fa-utensils"></i><p>Phòng bếp</p><span>56 sản phẩm</span></div>
        <div class="category-card"><i class="fas fa-lightbulb"></i><p>Phòng làm việc</p><span>77 sản phẩm</span></div>
        <div class="category-card"><i class="fas fa-paint-brush"></i><p>Đồ trang trí mini</p><span>102 sản phẩm</span></div>
        <div class="category-card"><i class="fas fa-chair"></i><p>Quà lưu niệm</p><span>65 sản phẩm</span></div>
    </div>
</section>



<jsp:include page="footer.jsp"></jsp:include>

</body>
</html>
