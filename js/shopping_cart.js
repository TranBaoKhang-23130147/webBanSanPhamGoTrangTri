// Lấy các phần tử modal
var modal = document.getElementById("checkoutModal");
var btn = document.getElementById("openCheckoutModal");
// Lấy nút đóng (x)
var span = document.getElementsByClassName("close-btn")[0];

// Khi người dùng nhấp vào nút "TIẾN HÀNH THANH TOÁN", mở modal
btn.onclick = function(e) {
    e.preventDefault();
    modal.style.display = "block";
    document.body.classList.add("modal-open"); // Thêm class để ẩn scrollbar

    // Cập nhật Tổng tiền trong Modal từ bảng tóm tắt giỏ hàng
    document.getElementById('modal-subtotal').textContent = document.querySelector('.summary-amount').textContent;
    // Sử dụng regex để loại bỏ dấu ** và lấy giá trị tổng cộng
    document.getElementById('modal-total').textContent = document.querySelector('.summary-total-amount').textContent.replace(/\*/g, '');
    document.getElementById('modal-shipping').textContent = '30.000 VNĐ'; // Giữ cố định phí ship
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