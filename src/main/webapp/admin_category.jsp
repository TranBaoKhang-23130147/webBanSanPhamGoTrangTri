<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ SẢN PHẨM</title>
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
                <h2 class="page-title">Quản Lý Danh Mục Sản Phẩm</h2>

                <div class="search-filter-bar">
                    <div class="search-input-group" style="flex-grow: 1;">
                        <input type="text" placeholder="Tìm kiếm danh mục" class="search-input" id="searchInput" value="${keyword}">
                    </div>
                    <button class="add-new-category-btn" onclick="openCategoryModal()">
                        <i class="fa-solid fa-plus"></i> Thêm Danh Mục Mới
                    </button>

                </div>
<%--                <div class="category-table-wrapper">--%>
<%--                    <table class="category-table">--%>

                        <div class="category-table-wrapper">
                            <table class="category-table">
                        <thead>
                        <tr>
                            <th class="col-id">ID</th>
                            <th class="col-name">Tên Danh Mục</th>
                            <th class="col-product">Số Sản Phẩm</th>
                            <th class="col-product-count">Tổng tồn kho</th>

                            <th class="col-actions">Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${listC}" var="c">
                            <tr>
                                <td class="col-id">${c.id}</td>
                                <td class="col-name">${c.categoryName}</td>
                                <td class="col-product">
                                        ${productCountMap[c.id] != null ? productCountMap[c.id] : 0}
                                </td>
                                <td class="col-product-count">${c.totalInventory}</td>   <!-- ← sửa ở đây -->                                <td class="col-actions">
                                    <i class="fa-solid fa-pen-to-square" onclick="openCategoryModal('edit', '${c.id}', '${c.categoryName}')"></i>
                                    <i class="fa-solid fa-trash-can" onclick="deleteCategory('${c.id}', '${c.categoryName}')" style="cursor:pointer; color:red; margin-left:10px;"></i>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>

<%--                        <c:if test="${not empty sessionScope.msg}">--%>
<%--                            <script>--%>
<%--                                alert("${sessionScope.msg}");--%>
<%--                            </script>--%>
<%--                            <c:remove var="msg" scope="session" />--%>
<%--                        </c:if>--%>
                    </table>
                </div>
            </div>

            <div id="categoryModal" class="modal">
                <div class="modal-content">
                    <span class="close-btn" onclick="closeCategoryModal()">&times;</span>
                    <h3 id="modalTitle">Thêm Danh Mục Mới</h3>
                    <form id="categoryForm" action="add-category" method="POST">                        <div class="form-group">
                        <label for="categoryName">Tên Danh Mục:</label>
                        <input type="text" name="categoryName" id="categoryName" required>
                    </div>

                        <input type="hidden" id="categoryId">
                        <button type="submit" class="submit-btn">Lưu Danh Mục</button>
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
<script src="js/admin_category.js"></script>
</body>
</html>