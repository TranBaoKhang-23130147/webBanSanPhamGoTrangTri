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
                        <button class="action-btn customize-btn"> <i class="fa-solid fa-plus"></i> Thêm khách hàng</button>
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