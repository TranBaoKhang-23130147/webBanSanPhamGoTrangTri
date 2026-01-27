<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - SẢN PHẨM</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/decorate_style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product_all_style.css">

    <link
            href="https://cdn.jsdelivr.net/npm/remixicon@4.7.0/fonts/remixicon.css"
            rel="stylesheet"
    />

</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<section class="banner-product">
    <img
            src="${not empty categoryBanner ? categoryBanner : 'https://i.pinimg.com/1200x/4d/16/07/4d16076bd71f77a7b5f69963e875cac6.jpg'}"
            alt="Banner danh mục"
            class="banner-image"
    />
    <div class="banner-overlay">
        <div class="banner-content">
            <h2>${not empty categoryName ? categoryName.toUpperCase() : 'BỘ SƯU TẬP DECOR'}</h2>
            <p>Nâng tầm không gian sống với những sản phẩm decor tinh tế và hiện đại.</p>
            <button id="scrollToProducts">Khám Phá Ngay</button>
        </div>
    </div>
</section>
<section id="productSection">
    <div class="product-container">
        <jsp:include page="filter.jsp"></jsp:include>
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
                <p style="text-align: center; width: 100%;">Không tìm thấy sản phẩm nào phù hợp.</p>
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

        <c:if test="${currentPage > 1}">
            <%-- Quan trọng: Phải giữ lại cid=${param.cid} --%>
            <a href="CategoryController?cid=${param.cid}&page=${currentPage - 1}" class="page-btn">«</a>
        </c:if>

        <c:forEach begin="${startPage}" end="${endPage}" var="i">
            <a href="CategoryController?cid=${param.cid}&page=${i}"
               class="page-btn ${i == currentPage ? 'active' : ''}">
                    ${i}
            </a>
        </c:forEach>

        <c:if test="${currentPage < totalPages}">
            <a href="CategoryController?cid=${param.cid}&page=${currentPage + 1}" class="page-btn">»</a>
        </c:if>

    </div>
</div>

<jsp:include page="footer.jsp"></jsp:include>


</body>
<script src="${pageContext.request.contextPath}/js/decorate.js"></script>

</html>