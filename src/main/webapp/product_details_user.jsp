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
        <div class="rating-price-wrapper">
            <div class="rating">
                <%-- Hiển thị sao dựa trên số điểm trung bình --%>
                <c:forEach begin="1" end="5" var="i">
                    <i class="${i <= p.averageRating ? 'ri-star-s-fill' : 'ri-star-s-line'}"></i>
                </c:forEach>
                <span class="rating-text">${p.averageRating}</span>
                <span class="review-count">| ${p.totalReviews} đánh giá</span>
            </div>
            <div class="product-price-section">
                <%-- Hiển thị giá (Nếu có biến thể thì lấy giá biến thể đầu tiên làm mặc định) --%>
                <div class="product-price">
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
            <%-- Phần hiển thị Màu sắc --%>
                <div class="color-select">
                    <p>Màu Sắc:</p>
                    <c:forEach var="variant" items="${p.variants}" varStatus="status">
                        <button class="color-btn ${status.first ? 'active' : ''}"
                                style="background-color: ${variant.color.colorCode}; border: 1px solid #ddd;">
                                <%-- Nếu bạn muốn hiện cả chữ thì để dòng dưới, nếu chỉ muốn hiện ô màu thì xóa đi --%>
                                ${variant.color.colorName}
                        </button>
                    </c:forEach>
                </div>

                <div class="capacity-select option-group">
                    <p>Kích thước:</p>
                    <c:forEach var="variant" items="${p.variants}" varStatus="status">
                        <button class="${status.first ? 'active' : ''}">
                                ${variant.size.size_name} <%-- Gọi thông qua object size --%>
                        </button>
                    </c:forEach>
                </div>
        </div>

        <div class="purchase-actions">
            <div class="quantity-select">
                <div class=" quantity-row">
                    <p>Số Lượng:</p>
                    <div class="quantity-controls">
                        <button class="qty-btn" onclick="this.parentNode.querySelector('input[type=number]').stepDown()">-</button>
                        <input type="number" value="1" min="1" max="100" />
                        <button class="qty-btn" onclick="this.parentNode.querySelector('input[type=number]').stepUp()">+</button>
                    </div>
                </div>
            </div>

            <div class="button-group">
                <button class="add-to-cart">Thêm vào giỏ hàng</button>
                <button class="buy-now">Mua Ngay</button>
            </div>
        </div>
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
<section class="review-section">

    <h2 class="review-title">Đánh giá</h2>

    <!-- TỔNG QUAN ĐÁNH GIÁ -->
    <div class="review-summary">
        <div class="score-box">
            <div class="rating">
                <i class="ri-star-s-fill"></i>
                <i class="ri-star-s-fill"></i>
                <i class="ri-star-s-fill"></i>
                <i class="ri-star-s-fill"></i>
                <i class="ri-star-s-fill"></i>

                <span>(${p.averageRating})</span>
            </div>
            <p>Tổng ${p.reviewList.size()} đánh giá của khách hàng</p>
        </div>

        <div class="score-bars">
            <div class="bar-item"><span>5 sao</span><div class="bar"><div style="width:85%"></div></div></div>
            <div class="bar-item"><span>4 sao</span><div class="bar"><div style="width:10%"></div></div></div>
            <div class="bar-item"><span>3 sao</span><div class="bar"><div style="width:3%"></div></div></div>
            <div class="bar-item"><span>2 sao</span><div class="bar"><div style="width:1%"></div></div></div>
            <div class="bar-item"><span>1 sao</span><div class="bar"><div style="width:1%"></div></div></div>
        </div>
    </div>

    <!-- LỌC ĐÁNH GIÁ -->
    <div class="review-filter">
        <label>Lọc đánh giá: </label>
        <select>
            <option>Tất cả</option>
            <option>5 sao</option>
            <option>4 sao</option>
            <option>3 sao</option>
            <option>2 sao</option>
            <option>1 sao</option>
        </select>
    </div>

    <!-- LIST REVIEW -->
    <div class="review-list">
        <c:forEach var="rev" items="${p.reviewList}">
            <div class="review-item">
                <div class="review-user">
                    <img src="img/default-avatar.png" class="avatar">
                    <div>
                        <h4>Người dùng #${rev.userId}</h4>
                        <div class="rating stars small">
                            <c:forEach begin="1" end="5" var="i">
                                <i class="${i <= rev.rating ? 'ri-star-s-fill' : 'ri-star-s-line'}"></i>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <p class="review-text">${rev.comment}</p>
            </div>
        </c:forEach>
    </div>
</section>

<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
