// Lấy các phần tử modal
var modal = document.getElementById("checkoutModal");
// CHỈNH SỬA: Thay đổi ID để khớp với ID trong HTML đã cập nhật.
var btn = document.getElementById("openCheckoutModalBtn");
// Lấy nút đóng (x)
var span = document.getElementsByClassName("close-btn")[0];

// Khi người dùng nhấp vào nút "TIẾN HÀNH THANH TOÁN", mở modal
btn.onclick = function(e) {
    e.preventDefault();
    modal.style.display = "block";
    document.body.classList.add("modal-open"); // Thêm class để ẩn scrollbar

    // Cập nhật Tổng tiền trong Modal từ bảng tóm tắt giỏ hàng (sử dụng class mới)
    // Dùng .summary-amount-new vì HTML tóm tắt đã được cập nhật
    document.getElementById('modal-subtotal').textContent = document.querySelector('.summary-line-new:nth-child(2) .summary-amount-new').textContent;
    // Lấy giá trị tổng cộng (dòng thứ 4 của .cart-summary-new)
    document.getElementById('modal-total').textContent = document.querySelector('.summary-line-new.total-new .summary-total-amount-new').textContent;

    // Giữ cố định phí ship (Nếu phí ship không hiển thị riêng trong HTML Tóm tắt mới)
    document.getElementById('modal-shipping').textContent = '30.000 VNĐ';
}

// Khi người dùng nhấp vào (x), đóng modal
span.onclick = function() {
    modal.style.display = "none";
    document.body.classList.remove("modal-open");
}

// Khi người dùng nhấp vào bất cứ đâu bên ngoài modal, đóng nó
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
        document.body.classList.remove("modal-open");
    }
}

// Xử lý sự kiện Submit Form (ví dụ)
document.querySelector('.checkout-form').onsubmit = function(e) {
    e.preventDefault(); // Ngăn form submit thực tế
    alert("Đơn hàng đã được đặt thành công! Cảm ơn quý khách.");
    modal.style.display = "none";
    document.body.classList.remove("modal-open");
}
function updateTotal() {
    let total = 0;

    document.querySelectorAll('.cart-item').forEach(item => {

        const checkbox = item.querySelector('.cart-check');
        if (!checkbox.checked) return;

        const price = parseFloat(
            item.querySelector('.item-price').dataset.price
        );

        const qty = parseInt(
            item.querySelector('.item-qty').value
        );

        total += price * qty;
    });

    document.getElementById('cart-total').innerText =
        total.toLocaleString('vi-VN') + " VND";
}

// chạy ngay khi load trang
updateTotal();
function changeQty(btn, delta) {
    const input = btn.parentElement.querySelector('.qty-input');
    let value = parseInt(input.value) || 1;

    value += delta;
    if (value < 1) value = 1;
    if (value > 100) value = 100;

    input.value = value;

    // nếu bạn có update tổng tiền:
    updateTotal();
}
function changeQty(btn, delta) {
    const qtyInput = btn.parentElement.querySelector(".item-qty");
    let qty = parseInt(qtyInput.value);

    qty += delta;
    if (qty < 1) qty = 1;

    qtyInput.value = qty;
    updateTotal();
}
