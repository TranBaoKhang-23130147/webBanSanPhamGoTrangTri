function getShippingFee(subTotal) {
    if (subTotal < 100000) return 0;
    if (subTotal < 1000000) return 50000;
    if (subTotal < 3000000) return 100000;
    if (subTotal < 5000000) return 200000;
    if (subTotal < 10000000) return 500000;
    return 1000000;
}
function updateTotal() {

    let subTotal = 0;
    const taxRate = 0.08;

    document.querySelectorAll('.cart-item').forEach(item => {

        const checkbox = item.querySelector('.cart-check');

        if (checkbox && checkbox.checked) {

            const price = parseFloat(
                item.querySelector('.item-price').dataset.price
            ) || 0;

            const qty = parseInt(
                item.querySelector('.item-qty').value
            ) || 0;

            subTotal += price * qty;
        }
    });

    const taxAmount = subTotal * taxRate;
    const shippingFee = getShippingFee(subTotal);
    const finalTotal = subTotal > 0 ? subTotal + taxAmount + shippingFee : 0;

    document.getElementById('sub-total').innerText =
        subTotal.toLocaleString('vi-VN') + " VND";

    document.getElementById('tax-amount').innerText =
        taxAmount.toLocaleString('vi-VN') + " VND";

    document.getElementById('shipping-fee').innerText =
        shippingFee.toLocaleString('vi-VN') + " VND";

    document.getElementById('cart-total').innerText =
        finalTotal.toLocaleString('vi-VN') + " VND";
}

function changeQty(btn, delta) {

    const cartItem = btn.closest('.cart-item');
    const qtyInput = cartItem.querySelector(".item-qty");

    const variantId = cartItem.dataset.variantId;

    let qty = (parseInt(qtyInput.value) || 0) + delta;

    if (qty < 1) return;

    const oldQty = qtyInput.value;

    qtyInput.value = qty;

    updateTotal();

    const url =
        "CartServlet?action=updateQtyAjax&variantId=" +
        variantId +
        "&quantity=" +
        qty;

    fetch(url, {
        method: 'GET',
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
    })
        .then(res => res.json())
        .then(data => {

            if (!data.success) {

                alert("Lỗi: " + data.message);

                qtyInput.value = oldQty;

                updateTotal();
            }
        })
        .catch(err => {

            console.error("AJAX Error:", err);

            alert("Không thể kết nối đến máy chủ");

            qtyInput.value = oldQty;

            updateTotal();
        });
}
function openCheckoutModal() {

    const totalText =
        document.getElementById('cart-total').innerText.trim();

    const selectedCheckboxes =
        document.querySelectorAll('.cart-check:checked');

    if (selectedCheckboxes.length === 0 || totalText === "0 VND") {

        alert("Vui lòng tích chọn ít nhất một sản phẩm để thanh toán!");

        return;
    }

    const form = document.getElementById('checkoutForm');

    const oldHiddenInputs =
        form.querySelectorAll('input[name="selectedItems"]');

    oldHiddenInputs.forEach(input => input.remove());

    selectedCheckboxes.forEach(cb => {

        const hiddenInput = document.createElement('input');

        hiddenInput.type = 'hidden';
        hiddenInput.name = 'selectedItems';
        hiddenInput.value = cb.value;

        form.appendChild(hiddenInput);
    });

    document.getElementById('modalTotal').innerText = totalText;

    document.getElementById('checkoutModal').style.display = 'flex';
}


function closeCheckoutModal() {

    document.getElementById('checkoutModal').style.display = 'none';
}

function toggleBankSelection() {

    const selectedMethod =
        document.querySelector('input[name="paymentMethod"]:checked')?.value;

    const bankSection = document.getElementById('bankSelection');

    const cardRadios =
        document.querySelectorAll('input[name="cardId"]');

    if (selectedMethod === 'BANK') {

        bankSection.style.display = 'block';

        cardRadios.forEach(r => r.required = true);

    } else {

        bankSection.style.display = 'none';

        cardRadios.forEach(r => r.required = false);
    }
}

window.onload = function () {

    updateTotal();

    document
        .querySelectorAll('input[name="paymentMethod"]')
        .forEach(radio => {

            radio.addEventListener(
                'change',
                toggleBankSelection
            );
        });

    toggleBankSelection();
};
window.onclick = function(event) {

    const modal = document.getElementById('checkoutModal');

    if (event.target === modal) {

        closeCheckoutModal();
    }
};


document
    .getElementById('checkoutForm')
    ?.addEventListener('submit', function () {

        const btn = document.getElementById('submitOrderBtn');

        if (btn) {

            btn.disabled = true;

            btn.innerHTML = 'Đang xử lý...';
        }
    });