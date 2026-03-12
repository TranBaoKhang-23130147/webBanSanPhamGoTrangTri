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
                <div class="customer-container">
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
                                        <i class="fa-solid fa-trash-can"
                                           onclick="deleteUser('${u.id}', '${u.displayName}')"
                                           style="cursor:pointer; color:red; margin-left:10px;"></i>
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
<div id="customerModal" class="modal-overlay">
    <div class="modal-box">

        <div class="modal-header">
            <h3><i class="fas fa-user-circle"></i> Thông Tin Chi Tiết Khách Hàng</h3>
            <span onclick="closeCustomerModal()" class="modal-close">&times;</span>
        </div>

        <div id="modalContent" class="modal-content">
            <div class="modal-loading">
                <i class="fas fa-spinner fa-spin"></i>
                <p>Đang tải dữ liệu khách hàng...</p>
            </div>
        </div>

        <div class="modal-footer">
            <button onclick="closeCustomerModal()" class="modal-close-btn">
                Đóng
            </button>
        </div>

    </div>
</div>
<div id="addUserModal" class="modal-overlay add-user-modal">
    <div class="modal-box add-user-box">

        <div class="add-user-header">
            <h3>Thêm Người Dùng Mới</h3>
            <span onclick="closeAddUserModal()" class="close-icon">&times;</span>
        </div>

        <form id="addUserForm" class="add-user-form">
            <div class="add-user-grid">

                <div class="form-group">
                    <label>Họ và tên:</label>
                    <input type="text" name="username" id="new_username" required class="input-style">
                </div>

                <div class="form-group">
                    <label>Phân quyền:</label>
                    <select name="role" id="new_role" class="input-style">
                        <option value="User">User</option>
                        <option value="Staff">Staff</option>
                        <option value="Admin">Admin</option>
                    </select>
                </div>

                <div class="form-group full-width">
                    <label>Email:</label>
                    <div class="flex-group">
                        <input type="email" name="email" id="new_email" required class="input-style flex-1">
                        <button type="button" onclick="sendOtpAdmin()" id="btnSendOtp" class="btn-otp-send">
                            Gửi mã
                        </button>
                    </div>
                </div>

                <div class="form-group full-width">
                    <label>Mã xác thực OTP:</label>
                    <div class="flex-group">
                        <input type="text" name="otp" id="new_otp" placeholder="Nhập mã OTP" class="input-style flex-1">
                        <button type="button" onclick="verifyOtpOnly()" id="btnVerifyOtp" class="btn-otp-verify">
                            Xác nhận mã
                        </button>
                    </div>
                </div>

                <div class="form-group">
                    <label>Mật khẩu:</label>
                    <input type="password" name="password" id="new_password" required class="input-style">
                </div>

                <div class="form-group">
                    <label>Xác nhận mật khẩu:</label>
                    <input type="password" id="confirm_password" class="input-style">
                </div>

            </div>

            <div class="add-user-footer">
                <button type="button" onclick="closeAddUserModal()" class="btn-cancel">Hủy</button>
                <button type="button" id="btnSubmitUser" onclick="submitAddUser()" class="btn-save-user" disabled>
                    Lưu Người Dùng
                </button>
            </div>
        </form>

    </div>
</div>
<script>
    const contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/admin_customer.js"></script>
</body>
</html>