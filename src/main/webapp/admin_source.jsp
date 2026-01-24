<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ NGUỒN HÀNG</title>
    <link rel="icon" type="image/png"  href="../img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="css/admin_products.css">
    <link rel="stylesheet" href="css/admin_profile_style.css">
    <link rel="stylesheet" href="css/admin_category.css">
    <link href="https://cdn.jsdelivr.net/npm/@sweetalert2/theme-bootstrap-4/bootstrap-4.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>

<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="content">

            <div class="category-management-container">
                <h2 class="page-title">Quản Lý Nguồn Hàng</h2>

                <div class="search-filter-bar">
                    <div class="search-input-group" style="flex-grow: 1;">
                        <input type="text" placeholder="Tìm kiếm nguồn hàng..." class="search-input" id="searchInput" value="${keyword}">
                    </div>
                    <%-- Cập nhật hàm gọi: openSourceModal --%>
                    <button class="add-new-category-btn" onclick="openSourceModal()">
                        <i class="fa-solid fa-plus"></i> Thêm Nguồn Hàng Mới
                    </button>
                </div>

                <div class="category-table-wrapper">
                    <table class="category-table">
                        <thead>
                        <tr>
                            <th class="col-id">ID</th>
                            <th class="col-name">Tên Nguồn Hàng</th>
                            <th class="col-product">Số Sản Phẩm</th>
                            <th class="col-product-count">Tổng tồn kho </th>
                            <th class="col-actions">Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${listS}" var="s">
                            <tr>
                                <td class="col-id">${s.id}</td>
                                <td class="col-name">${s.sourceName}</td>
                                <td class="col-product"> 0</td>

                                <td class="col-product-count">${s.totalInventory}</td>   <!-- ← sửa ở đây -->                                <td class="col-actions">
                                        <%-- Cập nhật hàm gọi: openSourceModal và deleteSource --%>
                                    <i class="fa-solid fa-pen-to-square"
                                       onclick="openSourceModal('edit', '${s.id}', '${s.sourceName}')"
                                       title="Chỉnh sửa"></i>
                                    <i class="fa-solid fa-trash-can"
                                       onclick="deleteSource('${s.id}', '${s.sourceName}')"
                                       style="cursor:pointer; color:red; margin-left:10px;"
                                       title="Xóa"></i>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <%-- Cập nhật ID Modal: sourceModal --%>
            <div id="sourceModal" class="modal">
                <div class="modal-content">
                    <span class="close-btn" onclick="closeSourceModal()">&times;</span>
                    <h3 id="modalTitle">Thêm Nguồn Hàng Mới</h3>
                    <form id="sourceForm" action="add-source" method="POST">
                        <div class="form-group">
                            <label for="sourceName">Tên Nguồn Hàng:</label>
                            <input type="text" name="sourceName" id="sourceName" required>
                        </div>
                        <input type="hidden" id="sourceId" name="id">
                        <button type="submit" class="submit-btn">Lưu Thông Tin</button>
                    </form>
                </div>
            </div>

        </main>
    </div>
</div>

<c:if test="${not empty sessionScope.msg}">
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const message = "${sessionScope.msg}";
            const type = ("${sessionScope.msgType}").trim().toLowerCase();

            Swal.fire({
                title: type === 'success' ? 'Thành công!' : 'Thông báo lỗi',
                text: message,
                icon: type === 'success' ? 'success' : 'error',
                confirmButtonColor: type === 'success' ? '#28a745' : '#d33',
                confirmButtonText: 'Đồng ý'
            });
        });
    </script>
    <c:remove var="msg" scope="session" />
    <c:remove var="msgType" scope="session" />
</c:if>

<script src="js/admin_source.js"></script>
</body>
</html>