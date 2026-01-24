<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ KHÁCH HÀNG</title>
    <link rel="icon" type="image/png"  href="../img/logo.png" >
    <link rel="stylesheet" href="../css/admin_customer_style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="../css/admin_profile_style.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

</head>
<body>
<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="content-area">

            <section class="admin-content-card">
                <h2 class="page-title">Quản Lý Khách Hàng</h2>

                <div class="controls-bar">
                    <div class="search-bar">
                        <i class="fas fa-search"></i>
                        <input type="text" placeholder="Tìm kiếm">
                    </div>
                    <button class="action-btn customize-btn" onclick="openAddUserModal()">
                        <i class="fa-solid fa-plus"></i> Thêm khách hàng
                    </button>
                </div>
                <div class="customer-list-container">
<!--                    <div class="search-bar">-->
<!--                        <i class="fas fa-search"></i>-->
<!--                        <input type="text" placeholder="Tìm kiếm">-->
<!--                    </div>-->
                    <div class="table-responsive">
                        <table class="data-table">
                            <thead>
                            <tr>
                                <th><input type="checkbox" class="select-all"></th>
                                <th>Khách Hàng</th>
                                <th>Số Điện Thoại</th>
                                <th>Email</th>
                                <th>Ngày Tạo</th>
                                <th>Trạng Thái</th>
                                <th>Thao Tác</th>

                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${listUsers}" var="u">
                                <tr>
                                    <td><input type="checkbox" value="${u.id}"></td>
                                    <td class="customer-name-cell">
                                        <a href="${pageContext.request.contextPath}/admin/customer-detail?id=${u.id}"
                                           class="customer-name-link"
                                           style="font-weight: bold; color: #4e73df; text-decoration: none;">
                                                ${u.displayName != null ? u.displayName : u.username}
                                        </a>
                                        <br><small style="color: #888;">(${u.username})</small>
                                    </td>
                                    <td>${u.phone != null ? u.phone : 'Chưa có'}</td>
                                    <td>${u.email}</td>
                                    <td>
                                        <c:if test="${u.createAt != null}">
                                            <fmt:formatDate value="${u.createAt}" pattern="dd/MM/yyyy"/>
                                        </c:if>
                                        <c:if test="${u.createAt == null}">
                                            Chưa có ngày
                                        </c:if>
                                    </td>
                                </td>
                                    <td>
                <span class="status ${u.status == 'Active' ? 'active-status' : 'inactive-status'}">
                    ${u.status == 'Active' ? 'Hoạt Động' : 'Khóa'}
                </span>
                                    </td>
                                    <td class="col-actions">
                                        <i class="fa-solid fa-pen-to-square"
                                           onclick="location.href='edit-customer?id=${u.id}'"
                                           style="cursor:pointer;"></i>
                                        <i class="fa-solid fa-trash-can"
                                           onclick="deleteUser('${u.id}', '${u.displayName}')"
                                           style="cursor:pointer; color:red; margin-left:10px;"></i>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
<!--                    <div class="pagination">-->
<!--                        <button class="page-nav prev">Quay lại</button>-->
<!--                        <div class="page-numbers">-->
<!--                            <button class="page-num active">1</button>-->
<!--                            <button class="page-num">2</button>-->
<!--                            <button class="page-num">3</button>-->
<!--                        </div>-->
<!--                        <button class="page-nav next">Tiếp theo</button>-->
<!--                    </div>-->
                </div>
            </section>
        </main>
    </div>
    </div>
<div id="customerModal" class="modal-overlay" style="display:none; position: fixed; z-index: 9999; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); backdrop-filter: blur(3px); align-items: center; justify-content: center;">
    <div class="modal-box" style="background: #fff; width: 850px; border-radius: 15px; position: relative; box-shadow: 0 10px 40px rgba(0,0,0,0.2); overflow: hidden;">
        <div style="background: #4e73df; color: white; padding: 20px; display: flex; justify-content: space-between; align-items: center;">
            <h3 style="margin: 0;"><i class="fas fa-user-circle"></i> Thông Tin Chi Tiết Khách Hàng</h3>
            <span onclick="closeCustomerModal()" style="font-size: 30px; cursor: pointer; line-height: 1;">&times;</span>
        </div>

        <div id="modalContent" style="padding: 30px; max-height: 550px; overflow-y: auto;">
            <div style="text-align: center; padding: 50px;">
                <i class="fas fa-spinner fa-spin" style="font-size: 30px; color: #4e73df;"></i>
                <p>Đang tải dữ liệu khách hàng...</p>
            </div>
        </div>

        <div style="padding: 15px 30px; background: #f8f9fc; text-align: right; border-top: 1px solid #eee;">
            <button onclick="closeCustomerModal()" class="action-btn" style="background: #6c757d; border: none; padding: 8px 20px; color: white; border-radius: 5px; cursor: pointer;">Đóng</button>
        </div>
    </div>
</div>
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
                // Chuyển hướng đến Servlet xử lý xóa
                window.location.href = "${pageContext.request.contextPath}/DeleteCustomerServlet?id=" + id;            }
        })
    }

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