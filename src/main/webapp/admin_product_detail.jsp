<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ SẢN PHẨM</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_profile_style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product_details_user.css">

<%--    <img src="${pageContext.request.contextPath}/${p.imageUrl}" ...>--%>

</head>
<body>


        <div class="admin-container">
            <%@ include file="admin_header.jsp" %>
            <div class="main-wrapper">
                <%@ include file="admin_sidebar.jsp" %>

                <main class="content">
                    <div class="product-management-container">

                        <div class="breadcrumb" style="margin-bottom: 20px; font-size: 0.9rem;">
                            <a href="${pageContext.request.contextPath}/products" style="color: var(--primary-color); text-decoration: none;">Quản lý sản phẩm</a>
                            <span style="color: #ccc; margin: 0 10px;">/</span> Chi tiết sản phẩm
                        </div>

                        <h2 class="page-title">Chi Tiết Sản Phẩm #${p.id}</h2>

                        <div class="product-detail-admin">
                            <div class="product-gallery">
                                <div class="main-image">
                                    <img id="main-product-img"
                                         src="${pageContext.request.contextPath}/${p.imageUrl}"
                                         alt="${p.nameProduct}">
                                </div>
                                <div class="thumb-list">
                                    <c:forEach var="img" items="${p.subImages}">
                                        <img src="${pageContext.request.contextPath}/${img.urlImage}"
                                             onclick="changeMainImage(this.src)"
                                             alt="Thumbnail">
                                    </c:forEach>
                                </div>
                            </div>

                            <div class="product-info">
                                <h1 class="product-title">${p.nameProduct}</h1>

                                <div class="rating-wrapper" style="margin-bottom: 15px; color: #f1c40f;">
                                    <c:forEach begin="1" end="5" var="i">
                                        <i class="${i <= p.averageRating ? 'ri-star-s-fill' : 'ri-star-s-line'}"></i>
                                    </c:forEach>
                                    <span class="rating-value" style="color: #666; font-size: 0.9rem;">(${p.averageRating})</span>
                                    <span class="review-count" style="margin-left: 10px; color: #999;">| ${p.reviewList.size()} đánh giá</span>
                                </div>

                                <div class="price-section" style="margin-bottom: 20px;">
                            <span class="current-price" id="currentPrice" style="font-size: 2rem; font-weight: 700; color: #e67e22;">
                                <fmt:formatNumber value="${p.price}" pattern="#,###"/> ₫
                            </span>
                                </div>

                                <div class="inventory-summary-box">
                                    <h3 style="font-size: 1rem; margin-bottom: 15px; color: var(--text-color);">Thống kê kho hàng tổng</h3>
                                    <div class="stats-grid">
                                        <div class="stat-item">
                                            <span class="label">Tổng nhập</span>
                                            <span class="value"><fmt:formatNumber value="${p.totalImported}" pattern="#,###"/></span>
                                        </div>
                                        <div class="stat-item">
                                            <span class="label">Đã bán</span>
                                            <span class="value"><fmt:formatNumber value="${p.totalSold}" pattern="#,###"/></span>
                                        </div>
                                        <div class="stat-item">
                                            <span class="label">Tồn kho</span>
                                            <span class="value"><fmt:formatNumber value="${p.totalRemaining}" pattern="#,###"/></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="variant-selection">
                                    <div class="color-group">
                                        <label>Màu sắc:</label>
                                        <div class="color-buttons">
                                            <c:set var="usedColors" value="" />
                                            <c:forEach var="v" items="${p.variants}">
                                                <c:if test="${!usedColors.contains(v.color.id.toString())}">
                                                    <button type="button" class="color-btn" data-color-id="${v.color.id}" onclick="selectColor('${v.color.id}')" style="background-color: ${v.color.colorCode}; border: 1px solid #ddd; width: 30px; height: 30px; border-radius: 50%; cursor: pointer; margin-right: 5px;"></button>
                                                    <c:set var="usedColors" value="${usedColors},${v.color.id}" />
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <div class="size-group" style="margin-top: 15px;">
                                        <label>Kích thước:</label>
                                        <div class="size-buttons" style="display: flex; gap: 10px; margin-top: 5px;">
                                            <c:set var="usedSizes" value="" />
                                            <c:forEach var="v" items="${p.variants}">
                                                <c:if test="${!usedSizes.contains(v.size.id.toString())}">
                                                    <button type="button" class="size-btn" data-size-id="${v.size.id}" onclick="selectSize('${v.size.id}')">${v.size.size_name}</button>
                                                    <c:set var="usedSizes" value="${usedSizes},${v.size.id}" />
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>

                                <div class="current-variant-info" style="margin: 20px 0; padding: 10px; background: #f9f9f9; border-radius: 5px;">
                                    <p style="margin: 0;"><strong>Trạng thái kho:</strong> <span id="inventoryDisplay" style="color: var(--primary-color);">Chưa chọn biến thể</span></p>
                                </div>

                                <div class="admin-action-buttons" style="display: flex; gap: 15px;">
                                    <a href="${pageContext.request.contextPath}/admin/edit-product?id=${p.id}" class="add-new-product-btn" style="text-decoration: none;">
                                        <i class="ri-edit-2-line"></i> Sửa sản phẩm
                                    </a>
                                    <button class="export-product-btn" onclick="deleteSelectedVariant()">
                                        <i class="ri-delete-bin-6-line"></i> Xóa biến thể
                                    </button>
                                </div>
                            </div>
                        </div>

                        <div class="product-description" style="margin-top: 40px; border-top: 1px solid #eee; padding-top: 30px;">
                            <h2 style="font-size: 1.4rem; margin-bottom: 20px;">Mô tả chi tiết</h2>
                            <div class="description-content" style="line-height: 1.6; color: #555;">
                                <p>${p.detailDescription.introduce}</p>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
        </div>
        <script>
            // 1. Khởi tạo mảng dữ liệu biến thể từ JSP
            const allVariants = [
                <c:forEach var="v" items="${p.variants}" varStatus="loop">
                {
                    id: "${v.id}",
                    colorId: "${v.color.id}",
                    sizeId: "${v.size.id}",
                    price: ${v.variant_price},
                    inventory: ${v.inventory_quantity != null ? v.inventory_quantity : 0}
                }${!loop.last ? ',' : ''}
                </c:forEach>
            ];

            let selectedColor = null;
            let selectedSize = null;

            // 2. Hàm xử lý khi chọn màu
            function selectColor(colorId) {
                selectedColor = (selectedColor === colorId) ? null : colorId;
                updateVariantDisplay();
            }

            // 3. Hàm xử lý khi chọn kích thước
            function selectSize(sizeId) {
                selectedSize = (selectedSize === sizeId) ? null : sizeId;
                updateVariantDisplay();
            }

            // 4. Hàm cập nhật giao diện (Giá, Kho, Trạng thái nút)
            function updateVariantDisplay() {
                // Tìm biến thể khớp với cả màu và size đã chọn
                const variant = allVariants.find(v => v.colorId === selectedColor && v.sizeId === selectedSize);

                // Cập nhật trạng thái Active cho các nút
                document.querySelectorAll('.color-btn').forEach(btn => {
                    btn.classList.toggle('active', btn.dataset.colorId === selectedColor);
                });
                document.querySelectorAll('.size-btn').forEach(btn => {
                    btn.classList.toggle('active', btn.dataset.sizeId === selectedSize);
                });

                const priceDisplay = document.getElementById('currentPrice');
                const inventoryDisplay = document.getElementById('inventoryDisplay');

                if (variant) {
                    priceDisplay.innerHTML = new Intl.NumberFormat('vi-VN').format(variant.price) + ' ₫';
                    inventoryDisplay.innerHTML = variant.inventory + ' cái';
                    inventoryDisplay.style.color = variant.inventory > 0 ? "green" : "red";
                } else {
                    // Trả về giá mặc định nếu chưa chọn đủ bộ
                    priceDisplay.innerHTML = new Intl.NumberFormat('vi-VN').format(${p.price}) + ' ₫';
                    inventoryDisplay.innerHTML = 'Vui lòng chọn đủ Màu và Size';
                    inventoryDisplay.style.color = "#666";
                }
            }

            // 5. Hàm đổi ảnh chính
            function changeMainImage(src) {
                document.getElementById('main-product-img').src = src;
            }
        </script>
</body>
</html>





















