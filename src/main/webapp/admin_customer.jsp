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
    const contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/js/admin_customer.js"></script>
</body>
</html>