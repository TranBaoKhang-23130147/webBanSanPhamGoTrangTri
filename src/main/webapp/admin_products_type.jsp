<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ LOẠI SẢN PHẨM</title>
    <link rel="icon" type="image/png" href="../img/logo.png">
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
                <h2 class="page-title">Quản Lý Loại Sản Phẩm</h2>

                <div class="search-filter-bar">
                    <div class="search-input-group" style="flex-grow: 1;">
                        <input type="text" placeholder="Tìm kiếm loại sản phẩm"
                               class="search-input" id="searchInput" value="${keyword}">
                    </div>
                    <button class="add-new-category-btn" onclick="openProductTypeModal()">
                        <i class="fa-solid fa-plus"></i> Thêm Loại SP Mới
                    </button>
                </div>

                <div class="category-table-wrapper">
                    <table class="category-table">
                        <thead>
                        <tr>
                            <th class="col-id">ID</th>
                            <th class="col-name">Tên Loại Sản Phẩm</th>
                            <th class="col-product-count">Số Sản Phẩm</th>
                            <th class="col-actions">Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%-- Giả sử Servlet gửi listPT --%>
                        <c:forEach items="${listPT}" var="pt">
                            <tr>
                                <td class="col-id">${pt.id}</td>
                                <td class="col-name">${pt.productTypeName}</td>
                                <td class="col-product-count">0</td>

                                <td class="col-actions">
                                    <i class="fa-solid fa-pen-to-square"
                                       onclick="editProductType('${pt.id}', '${pt.productTypeName}')"></i>

                                    <i class="fa-solid fa-trash-can"
                                       onclick="deleteProductType('${pt.id}', '${pt.productTypeName}')"
                                       style="cursor: pointer; color: #e74c3c; margin-left: 10px;"></i>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
            <div id="productTypeModal" class="modal">
                <div class="modal-content">
                    <span class="close-btn" onclick="closeProductTypeModal()">&times;</span>
                    <h3 id="modalTitle">Thêm Loại Sản Phẩm Mới</h3>

                    <form id="productTypeForm" action="${pageContext.request.contextPath}/add-product-type" method="post">
                        <div class="form-group">
                            <label for="productTypeName">Tên Loại Sản Phẩm:</label>
                            <input type="text" name="productTypeName" id="productTypeName" required>
                        </div>
                        <button type="submit" class="submit-btn">Lưu Thông Tin</button>
                    </form>
                </div>
            </div>
        </main>
    </div>
</div>
<%-- Kiểm tra và hiển thị thông báo từ Servlet --%>
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
<script src="js/admin_product_type.js"></script>
</body>
</html>