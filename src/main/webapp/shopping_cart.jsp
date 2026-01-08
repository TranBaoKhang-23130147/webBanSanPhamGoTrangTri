
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - GIỎ HÀNG</title>

    <link rel="icon" href="img/logo.png">
    <link rel="stylesheet" href="css/shopping_cart_style.css">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>

<body>

<jsp:include page="header.jsp"/>

<c:set var="cart" value="${sessionScope.CART}"/>

<div class="main-content">
    <div class="cart-page-content-new">

        <!-- ================= LEFT: CART LIST ================= -->
        <div class="shopping-cart-list">

            <div class="cart-header-text">
                Bạn đang có ${fn:length(cart)} sản phẩm trong giỏ hàng
            </div>

            <c:forEach var="item" items="${cart}">
                <div class="cart-item">

                    <input type="checkbox"
                           class="cart-check"
                           checked
                           onchange="updateTotal()">

                    <div class="product-details">
                        <img src="${item.product.imageUrl}" class="product-image-list">

                        <div class="product-info-list">
                            <div class="product-name-list">
                                    ${item.product.nameProduct}
                            </div>

                            <div class="product-options">
                                Màu: ${item.variant.color.colorName} |
                                Size: ${item.variant.size.size_name}
                            </div>
                        </div>
                    </div>

                    <div class="product-actions-price">

                        <!-- đơn giá -->
                        <span class="item-price"
                              data-price="${item.variant.variant_price}">
            <fmt:formatNumber value="${item.variant.variant_price}" pattern="#,###"/> đ
        </span>

                        <!-- số lượng -->
                        <input type="number"
                               class="item-qty"
                               value="${item.quantity}"
                               min="1"
                               onchange="updateTotal()">

                    </div>
                    <a href="RemoveFromCartServlet?variantId=${item.variant.id}">
                        <i class="fas fa-trash-alt remove-icon-list"></i>
                    </a>
                </div>


            </c:forEach>

        </div>

        <!-- ================= RIGHT: SUMMARY ================= -->
        <div class="cart-summary-container">

            <div class="voucher-input">
                <span class="input-title">Nhập mã khuyến mãi</span>
                <input type="text" placeholder="Voucher hoặc gift code">
                <button class="apply-btn">Áp dụng</button>
            </div>

            <!-- TÍNH TỔNG -->
            <c:set var="total" value="0"/>
            <c:forEach var="item" items="${cart}">
                <c:set var="total" value="${total + item.totalPrice}"/>
            </c:forEach>

            <div class="summary-title">Bản tóm tắt</div>

            <div class="summary-line-new">
                <span>Tổng giá</span>
                <span>
                    <fmt:formatNumber value="${total}" pattern="#,###"/> VND
                </span>
            </div>

            <div class="summary-line-new">
                <span>Thuế GTGT</span>
                <span>Đã bao gồm</span>
            </div>

            <div class="summary-line-new total-new">
                <span>Tổng cộng</span>
                <span id="cart-total">0 VND</span>
            </div>

            <a href="#" class="checkout-btn-new">
                Thanh toán
            </a>

        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>

<script src="js/shopping_cart.js"></script>
</body>
</html>
