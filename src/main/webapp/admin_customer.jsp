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
                    <div class="header-actions">
                        <button class="action-btn customize-btn"><i class="fas fa-cog"></i> Them khach hang</button>
                    </div>
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
                                <th>Thao Tac</th>

                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${listUsers}" var="u">
                                <tr>
                                    <td><input type="checkbox" value="${u.id}"></td>
                                    <td class="customer-name-cell">
                                        <a href="customer-detail?id=${u.id}" class="customer-name-link">
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