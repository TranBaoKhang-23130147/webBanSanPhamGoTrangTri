<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HOME DECOR - SẢN PHẨM</title>
    <link rel="icon" type="image/png"  href="../img/logo.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/product_all_style.css">

    <link rel="stylesheet" href="css/product_details_user.css">
    <link
            href="https://cdn.jsdelivr.net/npm/remixicon@4.7.0/fonts/remixicon.css"
            rel="stylesheet"
    />

</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<c:if test="${not empty sessionScope.ADD_CART_SUCCESS}">
    <div id="cartAlert"
    >
        <i class="ri-checkbox-circle-fill"></i>
            ${sessionScope.ADD_CART_SUCCESS}
    </div>

    <%-- Xóa sau khi hiển thị --%>
    <c:remove var="ADD_CART_SUCCESS" scope="session"/>
</c:if>

<div class="product-detail">
    <div class="product-gallery">
        <div class="main-image">
            <%-- Thêm ID để JavaScript có thể tìm thấy ảnh này --%>
            <img id="main-product-img" src="${p.imageUrl}" alt="${p.nameProduct}" style="width: 100%;">
        </div>

        <div class="thumb-list" style="display: flex; gap: 5px; margin-top: 10px;">
            <%-- Dùng p.subImages là đúng vì Servlet bạn gán vào đối tượng p --%>
            <c:forEach var="img" items="${p.subImages}">
                <img src="${img.urlImage}"
                     onclick="changeMainImage(this.src)"
                     style="width: 70px; height: 70px; cursor: pointer; border: 1px solid #ddd; object-fit: cover;">
            </c:forEach>
        </div>
    </div>

    <script>
        // Hàm này sẽ chạy khi bạn click vào ảnh nhỏ
        function changeMainImage(newSrc) {
            document.getElementById('main-product-img').src = newSrc;
        }
    </script>

    <div class="product-info">
        <h2 class="product-title">${p.nameProduct}</h2>
        <%-- Trong vòng lặp c:forEach --%>
        <p>Số lượng còn lại: <strong>${p.totalQuantity}</strong> sản phẩm</p>        <div class="rating-price-wrapper">
        <div class="rating">
            <c:forEach begin="1" end="5" var="i">
                <i class="${i <= p.averageRating ? 'ri-star-s-fill' : 'ri-star-s-line'}" style="color: #ffcc00;"></i>
            </c:forEach>
            <span>(<fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1"/>)</span>
            <span>| ${p.reviewList.size()}đánh giá</span>


        </div>
        <div class="product-price-section">
            <%-- Hiển thị giá (Nếu có biến thể thì lấy giá biến thể đầu tiên làm mặc định) --%>
                <div class="product-price" id="productPrice">

                <fmt:formatNumber value="${p.price}" pattern="#,###"/> VND
            </div>
        </div>
    </div>

        <ul class="benefit-list">
            <li><span class="label">Thanh toán:</span> Gía sản phẩm chưa bao gồm chi phí vận chuyển và lắp đặt</li>
            <li><span class="label">Vận Chuyển:</span> Thời gian giao hàng từ 2-5 ngày tùy thuộc vào khu vực</li>
            <li><span class="label">Đổi trả:</span> Trả hàng miễn phí 15 ngày</li>
        </ul>

        <div class="product-options">
            <div class="color-select option-group">
                <p>Màu Sắc:</p>
                <div class="button-list">
                    <c:set var="usedColors" value="" />
                    <c:forEach var="v" items="${p.variants}">
                        <c:if test="${!usedColors.contains(v.color.id.toString())}">
                            <button type="button"
                                    class="variant-btn color-btn"
                                    data-color-id="${v.color.id}"
                                    onclick="selectColor('${v.color.id}')"
                                    style="background-color: ${not empty v.color.colorCode ? v.color.colorCode : '#ccc'}; color: #fff; border: 1px solid #ddd;">
                                    ${not empty v.color.colorName ? v.color.colorName : 'Chưa có tên'}
                            </button>
                            <c:set var="usedColors" value="${usedColors},${v.color.id}" />
                        </c:if>
                    </c:forEach>
                </div>
            </div>

            <div class="capacity-select option-group">
                <p>Kích thước:</p>
                <div class="button-list">
                    <c:set var="usedSizes" value="" />
                    <c:forEach var="v" items="${p.variants}">
                        <c:if test="${!usedSizes.contains(v.size.id.toString())}">
                            <button type="button"
                                    class="variant-btn size-btn"
                                    data-size-id="${v.size.id}"
                                    onclick="selectSize('${v.size.id}')">
                                    ${v.size.size_name}
                            </button>
                            <c:set var="usedSizes" value="${usedSizes},${v.size.id}" />
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </div>
        <form action="<c:url value='/CartServlet' />"
              method="post"
              onsubmit="return submitAddToCart();">

            <input type="hidden" name="action" value="add">
            <input type="hidden" name="productId" value="${p.id}">
            <input type="hidden" name="variantId" id="variantIdInput">
            <input type="hidden" name="quantity" id="quantityInput">


            <div class="purchase-actions">
                <div class="quantity-select">
                    <div class=" quantity-row">
                        <p>Số Lượng:</p>
                        <div class="quantity-controls">
                            <button type="button" class="qty-btn"
                                    onclick="this.parentNode.querySelector('input[type=number]').stepDown()">-</button>
                            <input type="number" id="qtyInput" value="1" min="1" max="100" />

                            <button type="button" class="qty-btn"
                                    onclick="this.parentNode.querySelector('input[type=number]').stepUp()">+</button>
                        </div>
                    </div>
                </div>

                <div class="button-group">
                    <button type="submit" class="add-to-cart">
                        Thêm vào giỏ hàng
                    </button>
                    <button class="buy-now">Mua Ngay</button>
                </div>
            </div>
        </form>
    </div>
</div>
</div>
<div class="product-description-container">
    <h2 class="description-title">Mô tả chi tiết sản phẩm</h2>
    <div class="description-content">
        <p><strong>${p.nameProduct}</strong></p>

        <%-- Lấy từ bảng Descriptions --%>
        <p>${p.detailDescription.introduce}</p>

        <h3>Đặc Điểm Nổi Bật:</h3>
        <ul>
            ${p.detailDescription.highlights}
        </ul>

        <h3>Thông tin chi tiết:</h3>
        <ul>
            <%-- Lấy từ bảng Informations thông qua Description --%>
            <li><strong>Chất liệu:</strong> ${p.detailDescription.information.material}</li>
            <li><strong>Kích thước:</strong> ${p.detailDescription.information.size}</li>
            <li><strong>Màu sắc:</strong> ${p.detailDescription.information.color}</li>
            <li><strong>Bảo hành:</strong> ${p.detailDescription.information.guarantee}</li>
        </ul>

        <h3>Nguồn gốc & ngày sản xuất:</h3>
        <ul>
            <li><strong>Nhà cung cấp:</strong> ${p.source.sourceName}</li>
            <li><strong>Ngày sản xuất:</strong> <fmt:formatDate value="${p.mfgDate}" pattern="dd/MM/yyyy" /></li>
        </ul>
    </div>
</div>
<section class="review-section" id="reviewArea">
    <h2 class="review-title">Đánh giá sản phẩm</h2>

    <!-- ===== MESSAGE ===== -->

    <c:if test="${not empty sessionScope.successMessage}">
        <div style="color:green;font-weight:bold;margin-bottom:15px">
                ${sessionScope.successMessage}
        </div>
        <c:remove var="successMessage" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div style="color:red;font-weight:bold;margin-bottom:15px">
                ${sessionScope.errorMessage}
        </div>
        <c:remove var="errorMessage" scope="session"/>
    </c:if>

    <!-- ===== SUMMARY ===== -->

    <div class="review-summary">
        <div class="score-box">
                <i class='ri-star-s-fill'></i>

            <strong style="margin-left:10px">
                <fmt:formatNumber value="${p.averageRating}" maxFractionDigits="1"/> / 5
            </strong>

            <p>${p.reviewList.size()} đánh giá</p>
        </div>
    </div>

    <!-- ===== NÚT VIẾT ĐÁNH GIÁ ===== -->

    <c:if test="${not empty sessionScope.LOGGED_USER}">

    </c:if>

    <!-- ===== FORM REVIEW (CHỈ HIỆN KHI review=true) ===== -->

    <c:if test="${not empty sessionScope.LOGGED_USER && param.review == 'true'}">

        <div style="margin:25px 0;background:#fff;padding:20px;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,.08)">

            <h3>Viết đánh giá của bạn</h3>

            <form method="post" action="${pageContext.request.contextPath}/ReviewServlet">

                <input type="hidden" name="productId" value="${p.id}">

                <div style="margin-bottom:10px">
                    <select name="rating" required style="padding:8px;width:100%">
                        <option value="">-- Chọn sao --</option>
                        <option value="5">⭐⭐⭐⭐⭐</option>
                        <option value="4">⭐⭐⭐⭐</option>
                        <option value="3">⭐⭐⭐</option>
                        <option value="2">⭐⭐</option>
                        <option value="1">⭐</option>
                    </select>
                </div>

                <textarea name="comment" rows="4" required
                          placeholder="Nhập tối thiểu 10 ký tự..."
                          style="width:100%;padding:10px;border:1px solid #ddd;border-radius:6px"></textarea>

                <button type="submit"
                        style="margin-top:10px;background:#8B5E3C;color:white;padding:10px 25px;border:none;border-radius:6px">
                    Gửi đánh giá
                </button>

            </form>
        </div>
    </c:if>

    <!-- ===== CHƯA LOGIN ===== -->

    <c:if test="${empty sessionScope.LOGGED_USER}">
        <p>
            <a href="${pageContext.request.contextPath}/login.jsp">
               Đăng nhập mua hàng để đánh giá sản phẩm !
            </a>
        </p>
    </c:if>

    <!-- ===== LIST REVIEW ===== -->

    <div class="review-list">

        <c:choose>
            <c:when test="${not empty p.reviewList}">
                <c:forEach var="rev" items="${p.reviewList}">

                    <div class="review-item">

                        <h4>${userNames[rev.userId]}</h4>

                        <c:forEach begin="1" end="5" var="i">
                            <i class="${i <= rev.rating ? 'ri-star-s-fill' : 'ri-star-s-line'}"></i>
                        </c:forEach>

                        <span style="font-size:12px;color:#999">
<fmt:formatDate value="${rev.createAt}" pattern="dd/MM/yyyy"/>
</span>

                        <p>${rev.comment}</p>

                    </div>

                </c:forEach>
            </c:when>

            <c:otherwise>
                <p>Chưa có đánh giá nào.</p>
            </c:otherwise>

        </c:choose>

    </div>

</section>

<!-- AUTO SCROLL -->

<c:if test="${param.review == 'true'}">
    <script>
        document.getElementById("reviewArea")?.scrollIntoView({behavior:"smooth"});
    </script>
</c:if>

<jsp:include page="footer.jsp"></jsp:include>
<script>
    // 1. Chuyển dữ liệu biến thể từ Java sang JavaScript JSON
    // Thay thế đoạn từ dòng 186 đến 195 bằng đoạn này:
    const allVariants = [
        <c:forEach var="v" items="${p.variants}" varStatus="status">
        {
            id: "${v.id}",
            colorId: "${v.color.id}",
            sizeId: "${v.size.id}",
            price: ${v.variant_price},
            // Đảm bảo lấy đúng trường inventory_quantity từ database
            stock: ${v.inventory_quantity}
        }${!status.last ? ',' : ''}
        </c:forEach>
    ];
    let selectedColorId = null;
    let selectedSizeId = null;

    function selectColor(colorId) {
        // Nếu nhấn lại chính nó thì bỏ chọn
        selectedColorId = (selectedColorId === colorId) ? null : colorId;
        updateUI();
    }

    function selectSize(sizeId) {
        selectedSizeId = (selectedSizeId === sizeId) ? null : sizeId;
        updateUI();
    }

    // Thay thế hàm updateUI() cũ bằng đoạn này:
    function updateUI() {
        // 1. Cập nhật class Active cho nút
        document.querySelectorAll('.color-btn').forEach(btn => {
            btn.classList.toggle('active', btn.getAttribute('data-color-id') === selectedColorId);
        });
        document.querySelectorAll('.size-btn').forEach(btn => {
            btn.classList.toggle('active', btn.getAttribute('data-size-id') === selectedSizeId);
        });

        // 2. Vô hiệu hóa các nút không hợp lệ (Logic cũ của bạn)
        document.querySelectorAll('.size-btn').forEach(btn => {
            const sizeId = btn.getAttribute('data-size-id');
            const exists = selectedColorId ? allVariants.some(v => v.colorId === selectedColorId && v.sizeId === sizeId) : true;
            btn.disabled = !exists;
            btn.style.opacity = exists ? "1" : "0.3";
        });

        document.querySelectorAll('.color-btn').forEach(btn => {
            const colorId = btn.getAttribute('data-color-id');
            const exists = selectedSizeId ? allVariants.some(v => v.sizeId === selectedSizeId && v.colorId === colorId) : true;
            btn.disabled = !exists;
            btn.style.opacity = exists ? "1" : "0.3";
        });

        const activeVariant = allVariants.find(v => v.colorId === selectedColorId && v.sizeId === selectedSizeId);
        const stockDisplay = document.querySelector('.product-info p strong');
        const qtyInput = document.getElementById('qtyInput'); // Ô nhập số lượng
        const priceDisplay = document.getElementById('productPrice');

        if (activeVariant) {
            stockDisplay.innerText = activeVariant.stock;

            // ✅ SET GIÁ THEO BIẾN THỂ
            priceDisplay.innerText =
                new Intl.NumberFormat('vi-VN').format(activeVariant.price) + " VND";

            qtyInput.max = activeVariant.stock;

            if (parseInt(qtyInput.value) > activeVariant.stock) {
                qtyInput.value = activeVariant.stock;
            }

            if (activeVariant.stock <= 0) {
                stockDisplay.style.color = "red";
                stockDisplay.innerText = "Hết hàng";
                qtyInput.value = 0;
                document.querySelector('.add-to-cart').disabled = true;
            } else {
                stockDisplay.style.color = "inherit";
                document.querySelector('.add-to-cart').disabled = false;
            }

        } else {
            stockDisplay.innerText = "${p.totalQuantity}";

            // 🔁 RESET GIÁ VỀ GIÁ GỐC
            priceDisplay.innerText =
                new Intl.NumberFormat('vi-VN').format(${p.price}) + " VND";

            qtyInput.max = 100;
        }

    }
    function submitAddToCart() {

        if (!selectedColorId || !selectedSizeId) {
            alert("Vui lòng chọn đầy đủ Màu sắc và Kích thước");
            return false;
        }

        const variant = allVariants.find(v =>
            v.colorId === selectedColorId && v.sizeId === selectedSizeId
        );

        if (!variant) {
            alert("Biến thể không hợp lệ");
            return false;
        }
        // Thêm đoạn này vào trong hàm submitAddToCart() trước dòng return true;
        if (variant.stock <= 0) {
            alert("Sản phẩm này hiện đã hết hàng, vui lòng chọn mẫu khác!");
            return false;
        }

        document.getElementById("variantIdInput").value = variant.id;
        document.getElementById("quantityInput").value =
            document.getElementById("qtyInput").value;

        return true;
    }

</script>
</div>
<script>
    function filterReviews(star) {
        document.querySelectorAll('.review-item').forEach(item => {
            const rating = item.querySelectorAll('.ri-star-s-fill').length;

            if (star === "all" || rating == star) {
                item.style.display = "block";
            } else {
                item.style.display = "none";
            }
        });
    }
</script>

</body>
</html>