
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


        <!-- ================= LEFT: CART LIST ================= -->
        <div class="shopping-cart-list">

            <div class="cart-header-text">
                Bạn đang có ${fn:length(cart)} sản phẩm trong giỏ hàng
            </div>

            <c:forEach var="item" items="${cart}">
                <div class="cart-item">

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

                        <!-- đơn giá -->
                        <span class="item-price"
                              data-price="${item.variant.variant_price}">
            <fmt:formatNumber value="${item.variant.variant_price}" pattern="#,###"/> đ
        </span>

                        <!-- số lượng -->
                        <div class="quantity-control-list">
                            <button type="button"
                                    class="quantity-btn-list"
                                    onclick="changeQty(this, -1)">
                                −
                            </button>

                            <input type="text"
                                   class="quantity-input-list item-qty"
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


        <!-- ================= RIGHT: SUMMARY ================= -->
        <div class="cart-summary-container">

<%--    <div class="voucher-input">--%>
<%--        <span class="input-title">...</span>--%>
<%--        <input type="text" placeholder="  ...">--%>
<%--        <button class="apply-btn">Áp dụng</button>--%>
<%--    </div>--%>


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
<!-- Modal Thanh toán -->
<!-- Modal Thanh toán -->
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
                <label>Địa chỉ nhận hàng: <span style="color:#e74c3c; font-size:0.9em;">(chọn từ danh sách đã lưu)</span></label>
                <select name="address_id" id="addressSelect" required>
                    <c:forEach var="a" items="${addresses}">
                        <option value="${a.id}">
                                ${a.detail}, ${a.commune}, ${a.district}, ${a.province}
                                ${a.isDefault == 1 ? ' (Mặc định)' : ''}
                            - ${a.name} (${a.phone})
                        </option>
                    </c:forEach>
                    <!-- KHÔNG có option "Nhập địa chỉ khác" nữa -->
                </select>
                <!-- Nếu danh sách rỗng, có thể thêm thông báo -->
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
                <!-- phần chọn thẻ giữ nguyên như cũ -->
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
<script>
    // Tính phí ship (giữ nguyên)
    function getShippingFee(subTotal) {
        if (subTotal < 100000) return 0;
        if (subTotal < 1000000) return 50000;
        if (subTotal < 3000000) return 100000;
        if (subTotal < 5000000) return 200000;
        if (subTotal < 10000000) return 500000;
        return 1000000;
    }

    // Cập nhật tổng tiền (giữ nguyên, nhưng thêm format đẹp hơn)
    function updateTotal() {
        let subTotal = 0;
        const taxRate = 0.08;

        document.querySelectorAll('.cart-item').forEach(item => {
            const checkbox = item.querySelector('.cart-check');
            if (checkbox && checkbox.checked) {
                const price = parseFloat(item.querySelector('.item-price').getAttribute('data-price')) || 0;
                const qty = parseInt(item.querySelector('.item-qty').value) || 0;
                subTotal += price * qty;
            }
        });

        const taxAmount = subTotal * taxRate;
        const shippingFee = getShippingFee(subTotal);
        const finalTotal = subTotal > 0 ? subTotal + taxAmount + shippingFee : 0;

        document.getElementById('sub-total').innerText = subTotal.toLocaleString('vi-VN') + " VND";
        document.getElementById('tax-amount').innerText = taxAmount.toLocaleString('vi-VN') + " VND";
        document.getElementById('shipping-fee').innerText = shippingFee.toLocaleString('vi-VN') + " VND";
        document.getElementById('cart-total').innerText = finalTotal.toLocaleString('vi-VN') + " VND";
    }

    // Thay đổi số lượng + gửi AJAX (giữ nguyên, nhưng thêm kiểm tra an toàn hơn)
    function changeQty(btn, delta) {
        const qtyInput = btn.parentElement.querySelector(".item-qty");
        let qty = parseInt(qtyInput.value) || 1;
        qty += delta;
        if (qty < 1) return;

        const oldQty = qtyInput.value;
        qtyInput.value = qty;
        updateTotal();

        const cartItem = btn.closest('.cart-item');
        const removeLink = cartItem.querySelector('a.remove-link');
        const variantId = removeLink?.dataset.variantId?.trim();

        if (!variantId) {
            console.error("Không tìm thấy variantId");
            alert("Lỗi: Không xác định sản phẩm. Vui lòng tải lại trang.");
            qtyInput.value = oldQty;
            updateTotal();
            return;
        }

        fetch(`CartServlet?action=updateQtyAjax&variantId=${variantId}&quantity=${qty}`, {
            method: 'GET',
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => {
                if (!response.ok) throw new Error(`HTTP ${response.status}`);
                console.log("Cập nhật số lượng thành công");
            })
            .catch(error => {
                console.error("Lỗi cập nhật số lượng:", error);
                alert("Không thể cập nhật số lượng. Vui lòng thử lại!");
                qtyInput.value = oldQty;
                updateTotal();
            });
    }

    // Mở modal thanh toán (gộp 2 hàm thành 1)
    function openCheckoutModal() {
        const totalText = document.getElementById('cart-total').innerText.trim();

        // 1. Kiểm tra xem có tích chọn món nào không
        const selectedCheckboxes = document.querySelectorAll('.cart-check:checked');

        if (selectedCheckboxes.length === 0 || totalText === "0 VND") {
            alert("Vui lòng tích chọn ít nhất một sản phẩm để thanh toán!");
            return;
        }

        // 2. Xóa các ID cũ trong form (nếu có) để tránh bị lặp dữ liệu
        const form = document.getElementById('checkoutForm');
        const oldHiddenInputs = form.querySelectorAll('input[name="selectedItems"]');
        oldHiddenInputs.forEach(input => input.remove());

        // 3. Tạo các input hidden mới dựa trên những gì người dùng vừa tích chọn
        selectedCheckboxes.forEach(cb => {
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'selectedItems';
            hiddenInput.value = cb.value; // Đây chính là variant.id
            form.appendChild(hiddenInput);
        });

        // 4. Hiển thị tổng tiền lên modal và mở modal
        document.getElementById('modalTotal').innerText = totalText;
        document.getElementById('checkoutModal').style.display = 'flex';
    }

    function closeCheckoutModal() {
        document.getElementById('checkoutModal').style.display = 'none';
    }

    // Đóng modal khi click bên ngoài
    window.onclick = function(event) {
        const modal = document.getElementById('checkoutModal');
        if (event.target === modal) {
            closeCheckoutModal();
        }
    };

    // Hiển thị/ẩn phần chọn thẻ ngân hàng khi thay đổi phương thức thanh toán
    function toggleBankSelection() {
        const selectedMethod = document.querySelector('input[name="paymentMethod"]:checked')?.value;
        const bankSection = document.getElementById('bankSelection');
        const cardRadios = document.querySelectorAll('input[name="cardId"]');

        if (selectedMethod === 'BANK') {
            bankSection.style.display = 'block';
            cardRadios.forEach(r => r.required = true); // Bắt buộc chọn thẻ
            // ... (phần kiểm tra thẻ rỗng của bạn giữ nguyên)
        } else {
            bankSection.style.display = 'none';
            cardRadios.forEach(r => r.required = false); // Không bắt buộc khi chọn COD
        }
    }
    // Disable nút submit khi đang xử lý
    document.getElementById('checkoutForm')?.addEventListener('submit', function(e) {
        const btn = document.getElementById('submitOrderBtn');
        if (btn) {
            btn.disabled = true;
            btn.innerHTML = 'Đang xử lý...';
        }
    });

    // Khởi tạo sự kiện khi trang load
    window.onload = function () {
        updateTotal();

        // Gắn sự kiện change cho tất cả radio paymentMethod
        document.querySelectorAll('input[name="paymentMethod"]').forEach(radio => {
            radio.addEventListener('change', toggleBankSelection);
        });

        // Gọi lần đầu để hiển thị đúng trạng thái ban đầu (COD)
        toggleBankSelection();
    };
</script>


</body>
</html>
