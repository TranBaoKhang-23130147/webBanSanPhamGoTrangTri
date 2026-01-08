<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="model.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
            <input type="text" name="txtSearch"
                   value="${txtS}"
                   placeholder="Nhập tên sản phẩm...">

            <select name="category">
                <option value="all" ${catS == 'all' ? 'selected' : ''}>Phân loại</option>
                <option value="Bàn ghế" ${catS == 'Bàn ghế' ? 'selected' : ''}>Bàn ghế</option>
                <option value="Kệ" ${catS == 'Kệ' ? 'selected' : ''}>Kệ</option>
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
    <c:choose>
        <c:when test="${not empty listP}">
            <h2>Sản phẩm tìm được cho: "${txtS}"</h2>

            <div class="products">
                <c:forEach var="p" items="${listP}">
                    <div class="product-card">
                        <div class="set">
                            <a href="detail?id=${p.id}">
                                <img src="${p.imageUrl}">
                                <h2>${p.nameProduct}</h2>
                            </a>

                            <div class="rating">
                                <c:forEach begin="1" end="5" var="i">
                                    <i class="${i <= p.averageRating ? 'ri-star-s-fill' : 'ri-star-s-line'}"></i>
                                </c:forEach>
                                <span>(${p.averageRating})</span>
                            </div>

                            <div class="price">
                                <fmt:formatNumber value="${p.price}" pattern="#,###"/>₫
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>

        <c:otherwise>
            <h2>Sản phẩm gợi ý cho bạn</h2>
        </c:otherwise>
    </c:choose>

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
