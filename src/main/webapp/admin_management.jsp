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
                        <button class="action-btn customize-btn" onclick="location.href='add-admin.jsp'">
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