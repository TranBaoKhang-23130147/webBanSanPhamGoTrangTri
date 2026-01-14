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
        <link rel="stylesheet" href="css/decorate_style.css">
        <link
                href="https://cdn.jsdelivr.net/npm/remixicon@4.7.0/fonts/remixicon.css"
                rel="stylesheet"
        />

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
                <h2>TRANG TRÍ PHÒNG KHÁCH</h2>
                <p>Nâng tầm không gian sống với những sản phẩm decor tinh tế và hiện đại.</p>
                <button id="scrollToProducts">Khám Phá Ngay</button>
            </div>
        </div>
    </section>
    <section id="productSection">
        <!--    <h3 class="titleProduct">Sản phẩm dành cho phòng khách</h3>-->
        <div class="product-container">

            <form action="ProductFilterServlet" method="get">
                <input type="hidden" name="page" value="livingroom">
                <input type="hidden" name="category" value="trang-tri-phong-khach">
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
    <script src="js/decorate.js"></script>
    </html>