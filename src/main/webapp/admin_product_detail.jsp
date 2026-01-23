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
<style>
    :root {
        --primary-color: #4e73df;
        --secondary-color: #e67e22;
        --text-color: #333;
        --bg-gray: #f8f9fc;
        --border-color: #eaecf4;
    }

    /* Tổng thể container */
    .product-management-container {
        background: #fff;
        padding: 30px;
        border-radius: 15px;
        box-shadow: 0 0.15rem 1.75rem 0 rgba(58, 59, 69, 0.15);
    }

    .page-title {
        font-weight: 700;
        color: var(--text-color);
        margin-bottom: 25px;
        font-size: 1.6rem;
    }

    /* Bố cục chính 2 cột */
    .product-detail-admin {
        display: grid;
        grid-template-columns: 450px 1fr;
        gap: 40px;
    }

    /* Thư viện ảnh */
    .product-gallery {
        position: sticky;
        top: 20px;
    }

    .main-image {
        border: 1px solid var(--border-color);
        border-radius: 12px;
        overflow: hidden;
        background: #fff;
        aspect-ratio: 1/1;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .main-image img {
        width: 100%;
        height: 100%;
        object-fit: contain;
        transition: transform 0.3s ease;
    }

    .thumb-list {
        display: flex;
        gap: 12px;
        margin-top: 15px;
        overflow-x: auto;
        padding-bottom: 5px;
    }

    .thumb-list img {
        width: 80px;
        height: 80px;
        object-fit: cover;
        border-radius: 8px;
        cursor: pointer;
        border: 2px solid transparent;
        transition: all 0.2s;
    }

    .thumb-list img:hover {
        border-color: var(--primary-color);
        opacity: 0.8;
    }

    /* Thông tin sản phẩm */
    .product-info h1 {
        font-size: 1.8rem;
        font-weight: 700;
        margin-bottom: 10px;
        line-height: 1.3;
    }

    .price-section {
        background: #fff5eb;
        padding: 15px 20px;
        border-radius: 10px;
        display: inline-block;
        width: 100%;
    }

    /* Khối thống kê kho hàng - Làm mới lại */
    .inventory-summary-box {
        background: var(--bg-gray);
        border-radius: 12px;
        padding: 20px;
        margin: 25px 0;
        border: 1px solid var(--border-color);
    }

    .stats-grid {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 15px;
        text-align: center;
    }

    .stat-item {
        background: #fff;
        padding: 15px;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    }

    .stat-item .label {
        display: block;
        font-size: 0.8rem;
        color: #858796;
        text-transform: uppercase;
        margin-bottom: 5px;
    }

    .stat-item .value {
        font-size: 1.2rem;
        font-weight: 700;
        color: var(--primary-color);
    }

    /* Biến thể: Màu sắc & Size */
    .variant-selection label {
        font-weight: 600;
        margin-bottom: 10px;
        display: block;
        color: #444;
    }

    .color-btn {
        width: 35px !important;
        height: 35px !important;
        border-radius: 50%;
        transition: transform 0.2s, border-color 0.2s;
        position: relative;
    }

    .color-btn.active {
        transform: scale(1.2);
        border: 2px solid #333 !important;
        box-shadow: 0 0 10px rgba(0,0,0,0.2);
    }

    .size-btn {
        padding: 8px 18px;
        border: 1px solid #ddd;
        background: #fff;
        border-radius: 6px;
        cursor: pointer;
        transition: all 0.2s;
        font-weight: 500;
    }

    .size-btn.active {
        background: var(--primary-color);
        color: #fff;
        border-color: var(--primary-color);
    }

    /* Nút bấm hành động */
    .admin-action-buttons {
        margin-top: 30px;
        padding-top: 20px;
        border-top: 1px dashed #ddd;
    }

    .add-new-product-btn {
        background: var(--primary-color);
        color: #fff;
        padding: 12px 25px;
        border-radius: 8px;
        font-weight: 600;
        transition: all 0.3s;
        display: inline-flex;
        align-items: center;
        gap: 8px;
    }

    .export-product-btn {
        background: #fff;
        color: #e74c3c;
        border: 1px solid #e74c3c;
        padding: 12px 25px;
        border-radius: 8px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s;
    }

    .add-new-product-btn:hover {
        background: #2e59d9;
        box-shadow: 0 4px 12px rgba(78, 115, 223, 0.3);
    }

    .export-product-btn:hover {
        background: #e74c3c;
        color: #fff;
    }

    /* Mô tả chi tiết */
    .product-description {
        background: #fff;
        border: 1px solid var(--border-color);
        border-radius: 12px;
        padding: 25px;
    }
</style>
</html>





















