function confirmDeleteAdmin(id, name) {
    Swal.fire({
        title: 'Xác nhận xóa?',
        text: "Bạn đang xóa tài khoản Admin '" + name + "'. Dữ liệu liên quan cũng sẽ bị xóa!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Đúng, xóa ngay!',
        cancelButtonText: 'Hủy'
    }).then((result) => {

        if (result.isConfirmed) {
            window.location.href = contextPath + "/AdminDeleteCustomerServlet?id=" + id + "&type=admin";
        }

    });
}
function deleteUser(id, name) {

    Swal.fire({
        title: 'Xác nhận xóa?',
        text: "Bạn có chắc chắn muốn xóa khách hàng '" + name + "' không? Hành động này không thể hoàn tác!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Đồng ý, xóa!',
        cancelButtonText: 'Hủy'
    }).then((result) => {

        if (result.isConfirmed) {
            window.location.href = contextPath + "/DeleteCustomerServlet?id=" + id;
        }

    });

}


function openAddUserModal() {

    const modal = document.getElementById('addUserModal');

    if (modal) modal.style.display = 'flex';

}


function closeAddUserModal() {

    const modal = document.getElementById('addUserModal');

    if (modal) modal.style.display = 'none';

}


function sendOtpAdmin() {

    const email = document.getElementById('new_email').value;
    const btn = document.getElementById('btnSendOtp');

    if (!email) {
        Swal.fire('Chú ý', 'Vui lòng nhập email!', 'warning');
        return;
    }

    btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
    btn.disabled = true;

    const url = contextPath + "/RegisterServlet?action=sendOtp&email=" + encodeURIComponent(email);

    fetch(url, { method: 'POST' })
        .then(res => {

            if (res.ok) {

                Swal.fire('Thành công', 'Mã xác thực đã gửi!', 'success');

                btn.innerHTML = 'Gửi lại mã';
                btn.style.background = "#1cc88a";

            } else {

                Swal.fire('Lỗi', 'Không thể gửi mail!', 'error');
                btn.innerHTML = 'Gửi mã';

            }

        })
        .finally(() => btn.disabled = false);

}


function verifyOtpOnly() {

    const otp = document.getElementById('new_otp').value;

    const btnVerify = document.getElementById('btnVerifyOtp');

    const btnSubmit = document.getElementById('btnSubmitUser');

    if (!otp) {
        Swal.fire('Lỗi', 'Vui lòng nhập mã OTP!', 'error');
        return;
    }

    const url = contextPath + "/AdminAddCustomerServlet?action=verifyOnly&otp=" + otp;

    fetch(url, { method: 'POST' })
        .then(res => res.json())
        .then(data => {

            if (data.status === "success") {

                Swal.fire('Thành công', 'Mã OTP hợp lệ!', 'success');

                btnVerify.innerHTML = '<i class="fas fa-check"></i> Đã xác nhận';

                btnVerify.style.background = "#28a745";

                btnVerify.disabled = true;

                btnSubmit.disabled = false;

                btnSubmit.style.opacity = "1";

            } else {

                Swal.fire('Lỗi', 'Mã OTP không chính xác hoặc hết hạn!', 'error');

            }

        });

}


function submitAddUser() {

    const btnSubmit = document.getElementById('btnSubmitUser');

    const form = document.getElementById('addUserForm');

    const pass = document.getElementById('new_password').value;

    const confirmPass = document.getElementById('confirm_password').value;

    if (pass !== confirmPass) {

        Swal.fire('Lỗi', 'Mật khẩu xác nhận không khớp!', 'error');

        return;

    }

    const passRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;

    if (!passRegex.test(pass)) {

        Swal.fire('Mật khẩu yếu', 'Mật khẩu phải có ít nhất 8 ký tự, 1 chữ hoa và 1 ký tự đặc biệt!', 'warning');

        return;

    }

    const formData = new URLSearchParams(new FormData(form));

    const url = contextPath + "/AdminAddCustomerServlet";

    btnSubmit.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang lưu...';

    btnSubmit.disabled = true;

    fetch(url, {

        method: 'POST',

        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },

        body: formData

    })
        .then(response => {

            if (!response.ok) throw new Error('Lỗi kết nối server');

            return response.json();

        })
        .then(data => {

            if (data.status === "success") {

                closeAddUserModal();

                Swal.fire({
                    title: 'Thành công!',
                    text: 'Đã thêm khách hàng mới.',
                    icon: 'success',
                    confirmButtonColor: '#4e73df'
                }).then(() => location.reload());

            } else {

                Swal.fire('Lỗi', data.message, 'error');

                btnSubmit.innerHTML = 'Lưu Người Dùng';

                btnSubmit.disabled = false;

            }

        })
        .catch(() => {

            Swal.fire('Lỗi', 'Không thể lưu dữ liệu!', 'error');

            btnSubmit.innerHTML = 'Lưu Người Dùng';

            btnSubmit.disabled = false;

        });

}