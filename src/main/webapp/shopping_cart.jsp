
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

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">


    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/shopping_cart_style.css">

</head>

<body>

<jsp:include page="header.jsp"/>

<c:set var="cart" value="${sessionScope.CART}"/>

<div class="main-content">
    <div class="cart-page-content-new">

        <div class="shopping-cart-list">

            <div class="cart-header-text">
                Bạn đang có ${fn:length(cart)} sản phẩm trong giỏ hàng
            </div>

            <c:forEach var="item" items="${cart}">
                <div class="cart-item" data-variant-id="${item.variant.id}">

                    <input type="checkbox"
                           name="selectedItems"
                           value="${item.variant.id}"
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

                        <span class="item-price"
                              data-price="${item.variant.variant_price}">
            <fmt:formatNumber value="${item.variant.variant_price}" pattern="#,###"/> đ
        </span>

                        <div class="quantity-control-list">
                            <button type="button"
                                    class="quantity-btn-list"
                                    onclick="changeQty(this, -1)">
                                −
                            </button>

                            <input type="text"
                                   class="quantity-input-list item-qty"
                                   data-id="${item.variant.id}"
                                   value="${item.quantity}"
                                   readonly>

                            <button type="button"
                                    class="quantity-btn-list"
                                    onclick="changeQty(this, 1)">
                                +
                            </button>
                        </div>


                    </div>
                    <a href="RemoveFromCartServlet?variantId=${item.variant.id}"
                       data-variant-id="${item.variant.id}"
                       class="remove-link">
                        <i class="fas fa-trash-alt remove-icon-list"></i>
                    </a>
                </div>


            </c:forEach>

        </div>


        <div class="cart-summary-container">

    <div class="voucher-input">
        <span class="input-title">Nhập Mã Khuyến Mãi</span>
        <input type="text" placeholder="Voucher hoặc gift code" >
        <button class="apply-btn">Áp dụng</button>
    </div>


    <c:set var="total" value="0"/>
    <c:forEach var="item" items="${cart}">
        <c:set var="total" value="${total + item.totalPrice}"/>
    </c:forEach>

    <div class="summary-title">Thông tin đơn hàng</div>

    <div class="summary-line-new">
        <span>Tạm tính</span>
        <span id="sub-total">
        <fmt:formatNumber value="${total}" pattern="#,###"/> VND
    </span>
    </div>

    <div class="summary-line-new">
        <span>Thuế GTGT (8%)</span>
        <span id="tax-amount">
        <fmt:formatNumber value="${total * 0.08}" pattern="#,###"/> VND
    </span>
    </div>

    <div class="summary-line-new">
        <span>Phí vận chuyển</span>
        <span id="shipping-fee">0 VND</span>
    </div>


    <div class="summary-line-new total-new">
        <span>Tổng cộng</span>
        <span id="cart-total"
              style="color: #e54d42; font-weight: bold; font-size: 1.2em;">
        <c:choose>
            <c:when test="${not empty CART and total > 0}">
                <fmt:formatNumber value="${total}" pattern="#,###"/> VND
            </c:when>
            <c:otherwise>
                0 VND
            </c:otherwise>
        </c:choose>
    </span>
    </div>

    <a href="javascript:void(0)" class="checkout-btn-new" onclick="openCheckoutModal()">
        Thanh toán
    </a>

        </div>
    </div>
</div>
<div id="checkoutModal" class="modal" style="display: none;">
    <div class="modal-content">
        <span class="close-btn" onclick="closeCheckoutModal()">×</span>
        <h2>Thông tin thanh toán</h2>

        <form id="checkoutForm" action="CheckoutServlet" method="post">

            <div class="form-group">
                <label>Họ và tên người nhận:</label>
                <input type="text" name="fullName" required value="${LOGGED_USER.username}">
            </div>

            <div class="form-group">
                <label>Số điện thoại:</label>
                <input type="tel" name="phone" required value="${LOGGED_USER.phone}">
            </div>

            <div class="form-group">
                <label>Địa chỉ nhận hàng: </label>
                <select name="address_id" id="addressSelect" required>
                    <c:forEach var="a" items="${addresses}">
                        <option value="${a.id}">
                                ${a.detail}, ${a.commune}, ${a.district}, ${a.province}
                                ${a.isDefault == 1 ? ' (Mặc định)' : ''}
                            - ${a.name} (${a.phone})
                        </option>
                    </c:forEach>
                </select>
                <c:if test="${empty addresses}">
                    <p style="color:#e74c3c; margin-top:8px; font-size:0.95em;">
                        Bạn chưa có địa chỉ nào. Vui lòng thêm địa chỉ trong trang cá nhân trước khi đặt hàng.
                    </p>
                </c:if>
            </div>

            <div class="form-group">
                <label>Phương thức thanh toán:</label>
                <div class="payment-options">
                    <label>
                        <input type="radio" name="paymentMethod" value="COD" checked onclick="toggleCardList(false)">
                        Thanh toán khi nhận hàng (COD)
                    </label>
                    <br>
                    <label><input type="radio" name="paymentMethod" value="BANK"> Thẻ ngân hàng / Chuyển khoản (BANK)</label>
                </div>
            </div>

            <div id="bankSelection" style="display:none; background:#f4f4f4; padding:12px; border-radius:8px; margin:10px 0;">
                <c:choose>
                    <c:when test="${not empty listPayments}">
                        <c:forEach var="p" items="${listPayments}">
                            <div style="margin-bottom:8px;">
                                <input type="radio" name="cardId" value="${p.id}" id="card-${p.id}" required>
                                <label for="card-${p.id}">${p.type} - ****${p.cardNumber.substring(p.cardNumber.length()-4)}</label>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p style="color:#e74c3c; font-size:13px;">
                            Bạn chưa liên kết thẻ. Vui lòng chọn COD hoặc thêm thẻ trong trang cá nhân.
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="form-group">
                <label>Ghi chú đơn hàng (tùy chọn):</label>
                <textarea name="note" placeholder="Ghi chú cho shipper..." style="width:100%; height:80px;"></textarea>
            </div>

            <div class="total-confirm">
                <strong>Tổng cộng: </strong> <span id="modalTotal">0 VND</span>
            </div>

            <button type="submit" class="submit-btn" id="submitOrderBtn">Xác nhận đặt hàng</button>
        </form>
    </div>
</div>
<jsp:include page="footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/shopping_cart.js"></script>

</body>
</html>
