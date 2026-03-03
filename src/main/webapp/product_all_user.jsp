
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">

    <title>HOME DECOR - SẢN PHẨM</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product_all_style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/decorate_style.css">
    <link
            href="https://cdn.jsdelivr.net/npm/remixicon@4.7.0/fonts/remixicon.css"
            rel="stylesheet"
    />
    <link rel="stylesheet" href="css/search_style.css">

</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<section class="banner-product">

    <img
            src="https://i.pinimg.com/1200x/4d/16/07/4d16076bd71f77a7b5f69963e875cac6.jpg"
            alt="Ảnh trang trí phòng khách"
            class="banner-image"
    />
    <div class="banner-overlay">
        <div class="banner-content">
            <h2>BỘ SƯU TẬP SẢN PHẨM</h2>
            <p>Nâng tầm không gian sống với những sản phẩm decor tinh tế và hiện đại.</p>
        </div>
    </div>
</section>
<jsp:include page="danh_muc_loc.jsp"></jsp:include>
<section class="product-all">
    <h2 class="titleProduct">TẤT CẢ SẢN PHẨM</h2>
    <div class="product-container">

        <jsp:include page="filter.jsp"></jsp:include>
        <div class="products">
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
                        <a href="detail?id=${p.id}" class="add-cart">Thêm giỏ hàng</a>
                        <a href="detail?id=${p.id}" class="buy-now">Mua hàng</a>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty listP}">
                <div style="grid-column: 1 / -1; width: 100%; text-align: center; padding: 100px 0;">
                    <p style="font-size: 18px; color: #888;">
                        <i class="ri-search-line"></i> Không tìm thấy sản phẩm nào phù hợp.
                    </p>
                </div>
            </c:if>
        </div>
    </div>
</section>
<c:set var="range" value="3" />
<c:set var="startPage" value="${currentPage - range}" />
<c:set var="endPage" value="${currentPage + range}" />

<c:if test="${startPage < 1}">
    <c:set var="startPage" value="1" />
</c:if>

<c:if test="${endPage > totalPages}">
    <c:set var="endPage" value="${totalPages}" />
</c:if>

<div class="pagination-wrapper">
    <div class="pagination">

        <!-- Prev -->
        <c:if test="${currentPage > 1}">
            <a href="?page=${currentPage - 1}" class="page-btn">«</a>
        </c:if>

        <!-- Pages -->
        <c:forEach begin="${startPage}" end="${endPage}" var="i">
            <a href="?page=${i}"
               class="page-btn ${i == currentPage ? 'active' : ''}">
                    ${i}
            </a>
        </c:forEach>

        <!-- Next -->
        <c:if test="${currentPage < totalPages}">
            <a href="?page=${currentPage + 1}" class="page-btn">»</a>
        </c:if>

    </div>
</div>



<jsp:include page="footer.jsp"></jsp:include>


</body>
<script src="${pageContext.request.contextPath}/js/decorate.js"></script>

</html>