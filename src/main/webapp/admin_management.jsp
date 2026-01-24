<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ ADMIN</title>
    <link rel="icon" type="image/png"  href="../img/logo.png" >
    <link rel="stylesheet" href="css/admin_customer_style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="css/admin_profile_style.css">

    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <style>
        .role-admin-tag {
            background-color: #ebf5ff;
            color: #007bff;
            padding: 4px 10px;
            border-radius: 4px;
            font-weight: 600;
            font-size: 12px;
        }
        .self-account {
            background-color: #fff9db;
        }
        .col-actions i:hover {
            transform: scale(1.2);
            transition: 0.2s;
        }
    </style>
</head>
<body>
<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="content-area">

            <section class="admin-content-card">
                <h2 class="page-title">Danh Sách Quản Trị Viên</h2>

                <div class="controls-bar">
                    <div class="search-bar">
                        <i class="fas fa-search"></i>
                        <input type="text" placeholder="Tìm kiếm admin...">
                    </div>
                    <div class="header-actions">
                        <button class="action-btn customize-btn" onclick="openAddUserModal()">
                            <i class="fas fa-user-shield"></i> Thêm Admin Mới
                        </button>

                    </div>
                </div>

                <div class="customer-list-container">
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Họ Tên Quản Trị</th>
                                <th>Email</th>
                                <th>Vai Trò</th>
                                <th>Trạng Thái</th>
                                <th>Thao Tác</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${listAdmins}" var="a">
                                <tr class="${a.id == sessionScope.LOGGED_USER.id ? 'self-account' : ''}">
                                    <td>${a.id}</td>
                                    <td class="customer-name-cell">
                                        <strong>${a.displayName != null ? a.displayName : a.username}</strong>
                                        <c:if test="${a.id == sessionScope.LOGGED_USER.id}">
                                            <span style="color: #28a745; font-size: 11px;">(Bạn)</span>
                                        </c:if>
                                        <br><small style="color: #888;">@${a.username}</small>
                                    </td>
                                    <td>${a.email}</td>
                                    <td><span class="role-admin-tag">Administrator</span></td>
                                    <td>
                                        <span class="status ${a.status == 'Active' ? 'active-status' : 'inactive-status'}">
                                                ${a.status == 'Active' ? 'Hoạt Động' : 'Đang Khóa'}
                                        </span>
                                    </td>
                                    <td class="col-actions">
                                        <i class="fa-solid fa-user-pen"
                                           title="Sửa quyền hạn"
                                           onclick="location.href='edit-admin?id=${a.id}'"
                                           style="cursor:pointer; color: #4e73df;"></i>

                                        <c:if test="${a.id != sessionScope.LOGGED_USER.id}">
                                            <i class="fa-solid fa-trash-can"
                                               title="Xóa tài khoản admin"
                                               onclick="confirmDeleteAdmin('${a.id}', '${a.displayName != null ? a.displayName : a.username}')"
                                               style="cursor:pointer; color:red; margin-left:15px;"></i>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
        </main>
    </div>
</div>

<script>
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
                // Gọi đến Servlet bạn đã tạo
                window.location.href = "AdminDeleteCustomerServlet?id=" + id + "&type=admin";
            }
        })
    }
</script>
<div id="addUserModal" class="modal-overlay" style="display:none; position: fixed; z-index: 9999; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); backdrop-filter: blur(3px); align-items: center; justify-content: center;">
    <div class="modal-box" style="background: #fff; width: 600px; border-radius: 15px; overflow: hidden; box-shadow: 0 10px 40px rgba(0,0,0,0.2);">
        <div style="background: #7f462e; color: white; padding: 15px 20px; display: flex; justify-content: space-between;">
            <h3 style="margin: 0;">Thêm Người Dùng Mới</h3>
            <span onclick="closeAddUserModal()" style="cursor: pointer; font-size: 24px;">&times;</span>
        </div>

        <form id="addUserForm" style="padding: 20px;">
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                <div class="form-group">
                    <label>Họ và tên:</label>
                    <input type="text" name="username" id="new_username" required style="width:100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                </div>
                <div class="form-group">
                    <label>Phân quyền:</label>
                    <select name="role" id="new_role" style="width:100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                        <option value="User">User</option>
                        <option value="Staff">Staff</option>
                        <option value="Admin">Admin</option>
                    </select>
                </div>
                <div class="form-group" style="grid-column: span 2;">
                    <label>Email:</label>
                    <div style="display: flex; gap: 5px;">
                        <input type="email" name="email" id="new_email" required style="flex: 1; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                        <button type="button" onclick="sendOtpAdmin()" id="btnSendOtp" style="padding: 0 15px; background: #1cc88a; color: white; border: none; border-radius: 4px; cursor: pointer;">Gửi mã</button>
                    </div>
                </div>

                <div class="form-group" style="grid-column: span 2;">
                    <label>Mã xác thực OTP:</label>
                    <div style="display: flex; gap: 5px;">
                        <input type="text" name="otp" id="new_otp" placeholder="Nhập mã OTP" style="flex: 1; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                        <button type="button" onclick="verifyOtpOnly()" id="btnVerifyOtp" style="padding: 0 15px; background: #e74a3b; color: white; border: none; border-radius: 4px; cursor: pointer;">Xác nhận mã</button>
                    </div>
                </div>

                <div class="form-group">
                    <label>Mật khẩu:</label>
                    <input type="password" name="password" id="new_password" required style="width:100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                </div>
                <div class="form-group">
                    <label>Xác nhận mật khẩu:</label>
                    <input type="password" id="confirm_password" style="width:100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
                </div>
            </div>

            <div style="margin-top: 20px; text-align: right; border-top: 1px solid #eee; padding-top: 15px;">
                <button type="button" onclick="closeAddUserModal()" style="padding: 10px 20px; background: #858796; color: white; border: none; border-radius: 5px; cursor:pointer;">Hủy</button>
                <button type="button" id="btnSubmitUser" onclick="submitAddUser()" disabled style="padding: 10px 20px; background: #4e73df; color: white; border: none; border-radius: 5px; cursor:pointer; opacity: 0.5;">Lưu Người Dùng</button>
            </div>
        </form>
    </div>
</div>
<script>


</script>
<script>
    function openAddUserModal() {
        const modal = document.getElementById('addUserModal');
        if(modal) modal.style.display = 'flex';
    }

    function closeAddUserModal() {
        const modal = document.getElementById('addUserModal');
        if(modal) modal.style.display = 'none';
    }

    // Gửi mã OTP - Sửa lỗi 404 bằng contextPath
    function sendOtpAdmin() {
        const email = document.getElementById('new_email').value;
        const btn = document.getElementById('btnSendOtp');

        if (!email) {
            Swal.fire('Chú ý', 'Vui lòng nhập email!', 'warning');
            return;
        }

        btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
        btn.disabled = true;

        const url = "${pageContext.request.contextPath}/RegisterServlet?action=sendOtp&email=" + encodeURIComponent(email);

        fetch(url, { method: 'POST' })
            .then(res => {
                if (res.ok) {
                    Swal.fire('Thành công', 'Mã xác thực đã gửi!', 'success');
                    btn.innerHTML = 'Gửi lại mã';
                    btn.style.background = "#1cc88a";
                } else {
                    Swal.fire('Lỗi', 'Không thể gửi mail. Kiểm tra lại đường dẫn!', 'error');
                    btn.innerHTML = 'Gửi mã';
                }
            })
            .finally(() => btn.disabled = false);
    }

    // Xác nhận mã OTP ngay tại chỗ
    function verifyOtpOnly() {
        const otp = document.getElementById('new_otp').value;
        const btnVerify = document.getElementById('btnVerifyOtp');
        const btnSubmit = document.getElementById('btnSubmitUser');

        if (!otp) {
            Swal.fire('Lỗi', 'Vui lòng nhập mã OTP!', 'error');
            return;
        }

        // Gọi Servlet để check mã (Bạn cần thêm case "verifyOnly" trong AdminAddCustomerServlet)
        const url = "${pageContext.request.contextPath}/AdminAddCustomerServlet?action=verifyOnly&otp=" + otp;

        fetch(url, { method: 'POST' })
            .then(res => res.json())
            .then(data => {
                if (data.status === "success") {
                    Swal.fire('Thành công', 'Mã OTP hợp lệ!', 'success');
                    btnVerify.innerHTML = '<i class="fas fa-check"></i> Đã xác nhận';
                    btnVerify.style.background = "#28a745";
                    btnVerify.disabled = true;

                    // Mở khóa nút Lưu
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

        // 1. Kiểm tra khớp mật khẩu
        if (pass !== confirmPass) {
            Swal.fire('Lỗi', 'Mật khẩu xác nhận không khớp!', 'error');
            return;
        }

        // 2. Kiểm tra định dạng mật khẩu (Client-side)
        const passRegex = /^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?":{}|<>]).{8,}$/;
        if (!passRegex.test(pass)) {
            Swal.fire('Mật khẩu yếu', 'Mật khẩu phải có ít nhất 8 ký tự, 1 chữ hoa và 1 ký tự đặc biệt!', 'warning');
            return;
        }

        // 3. Chuẩn bị gửi dữ liệu
        const formData = new URLSearchParams(new FormData(form));
        const url = "${pageContext.request.contextPath}/AdminAddCustomerServlet";

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
                    // --- BƯỚC QUAN TRỌNG: Đóng form ngay lập tức ---
                    closeAddUserModal();

                    Swal.fire({
                        title: 'Thành công!',
                        text: 'Đã thêm khách hàng mới.',
                        icon: 'success',
                        confirmButtonColor: '#4e73df'
                    }).then(() => {
                        location.reload();
                    });
                } else {
                    // Nếu lỗi (trùng email, sai OTP...) thì giữ form để sửa
                    Swal.fire('Lỗi', data.message, 'error');
                    btnSubmit.innerHTML = 'Lưu Người Dùng';
                    btnSubmit.disabled = false;
                }
            })
            .catch(err => {
                console.error(err);
                Swal.fire('Lỗi', 'Không thể lưu dữ liệu. Vui lòng thử lại!', 'error');
                btnSubmit.innerHTML = 'Lưu Người Dùng';
                btnSubmit.disabled = false;
            });
    }
</script>
<c:if test="${not empty sessionScope.msg}">
    <script>
        Swal.fire({
            title: "${sessionScope.msgType == 'success' ? 'Thành công!' : 'Lỗi!'}",
            text: "${sessionScope.msg}",
            icon: "${sessionScope.msgType}",
            confirmButtonColor: '#4e73df'
        });
    </script>
    <c:remove var="msg" scope="session" />
    <c:remove var="msgType" scope="session" />
</c:if>

</body>
</html>