<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ SẢN PHẨM</title>

    <link rel="icon" type="image/png" href="img/logo.png">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_profile_style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product_details_user.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_product_detail.css">

</head>

<body>

<div class="admin-container">

    <%@ include file="admin_header.jsp" %>

    <div class="main-wrapper">

        <%@ include file="admin_sidebar.jsp" %>

        <main class="content">

            <div class="product-management-container">

                <div class="breadcrumb">
                    <a href="${pageContext.request.contextPath}/products" class="breadcrumb-link">
                        Quản lý sản phẩm
                    </a>

                    <span class="breadcrumb-separator">/</span>

                    Chi tiết sản phẩm
                </div>


                <h2 class="page-title">
                    Chi Tiết Sản Phẩm #${p.id}
                </h2>


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

                        <h1 class="product-title">
                            ${p.nameProduct}
                        </h1>


                        <div class="rating-wrapper">

                            <c:forEach begin="1" end="5" var="i">

                                <i class="${i <= p.averageRating ? 'ri-star-s-fill' : 'ri-star-s-line'}"></i>

                            </c:forEach>

                            <span class="rating-value">
                                (${p.averageRating})
                            </span>

                            <span class="review-count">
                                | ${p.reviewList.size()} đánh giá
                            </span>

                        </div>


                        <div class="price-section">

                        <span class="current-price" id="currentPrice">

                            <fmt:formatNumber value="${p.price}" pattern="#,###"/> ₫

                        </span>

                        </div>


                        <div class="inventory-summary-box">

                            <h3 class="inventory-title">
                                Thống kê kho hàng tổng
                            </h3>

                            <div class="stats-grid">

                                <div class="stat-item">
                                    <span class="label">Tổng nhập</span>
                                    <span class="value">
                                        <fmt:formatNumber value="${p.totalImported}" pattern="#,###"/>
                                    </span>
                                </div>

                                <div class="stat-item">
                                    <span class="label">Đã bán</span>
                                    <span class="value">
                                        <fmt:formatNumber value="${p.totalSold}" pattern="#,###"/>
                                    </span>
                                </div>

                                <div class="stat-item">
                                    <span class="label">Tồn kho</span>
                                    <span class="value">
                                        <fmt:formatNumber value="${p.totalRemaining}" pattern="#,###"/>
                                    </span>
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

                                            <button type="button"
                                                    class="color-btn"
                                                    data-color-id="${v.color.id}"
                                                    onclick="selectColor('${v.color.id}')"
                                                    style="background-color: ${v.color.colorCode};">

                                            </button>

                                            <c:set var="usedColors"
                                                   value="${usedColors},${v.color.id}" />

                                        </c:if>

                                    </c:forEach>

                                </div>

                            </div>


                            <div class="size-group">

                                <label>Kích thước:</label>

                                <div class="size-buttons">

                                    <c:set var="usedSizes" value="" />

                                    <c:forEach var="v" items="${p.variants}">

                                        <c:if test="${!usedSizes.contains(v.size.id.toString())}">

                                            <button type="button"
                                                    class="size-btn"
                                                    data-size-id="${v.size.id}"
                                                    onclick="selectSize('${v.size.id}')">

                                                    ${v.size.size_name}

                                            </button>

                                            <c:set var="usedSizes"
                                                   value="${usedSizes},${v.size.id}" />

                                        </c:if>

                                    </c:forEach>

                                </div>

                            </div>

                        </div>


                        <div class="current-variant-info">

                            <p>

                                <strong>Trạng thái kho:</strong>

                                <span id="inventoryDisplay">
                                    Chưa chọn biến thể
                                </span>

                            </p>

                        </div>

                    </div>

                </div>


                <div class="product-description">

                    <h2 class="description-title">
                        Mô tả chi tiết
                    </h2>

                    <div class="description-content">

                        <p>
                            ${p.detailDescription.introduce}
                        </p>

                    </div>

                </div>

            </div>

        </main>

    </div>

</div>


<script>

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

    function selectColor(colorId){
        selectedColor = (selectedColor === colorId) ? null : colorId;
        updateVariantDisplay();
    }

    function selectSize(sizeId){
        selectedSize = (selectedSize === sizeId) ? null : sizeId;
        updateVariantDisplay();
    }

    function updateVariantDisplay(){

        const variant = allVariants.find(v =>
            v.colorId === selectedColor &&
            v.sizeId === selectedSize
        );

        document.querySelectorAll('.color-btn').forEach(btn=>{
            btn.classList.toggle('active',btn.dataset.colorId===selectedColor);
        });

        document.querySelectorAll('.size-btn').forEach(btn=>{
            btn.classList.toggle('active',btn.dataset.sizeId===selectedSize);
        });

        const priceDisplay = document.getElementById('currentPrice');
        const inventoryDisplay = document.getElementById('inventoryDisplay');

        if(variant){

            priceDisplay.innerHTML =
                new Intl.NumberFormat('vi-VN').format(variant.price)+' ₫';

            inventoryDisplay.innerHTML = variant.inventory + ' cái';

            inventoryDisplay.style.color =
                variant.inventory>0 ? "green" : "red";

        }
        else{

            priceDisplay.innerHTML =
                new Intl.NumberFormat('vi-VN').format(${p.price})+' ₫';

            inventoryDisplay.innerHTML =
                'Vui lòng chọn đủ Màu và Size';

            inventoryDisplay.style.color="#666";

        }

    }

    function changeMainImage(src){
        document.getElementById('main-product-img').src = src;
    }

</script>

</body>
</html>