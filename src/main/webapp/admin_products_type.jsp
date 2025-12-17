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
</head>
<body>

<div class="admin-container">
    <header class="header">
        <div class="logo-placeholder">
            <img src="../img/logo.png" alt="Logo Modern Homes">
            <p class="logo">HOME DECOR</p>
        </div>

        <div class="header-icons">
            <div class="user-dropdown">
                <i class="fas fa-user-circle user-logo"></i>
                <div id="userMenuContent" class="dropdown-content">
                    <a href="admin_thong_tin_tai_khoan.html">Thông tin tài khoản</a>
                    <div class="dropdown-divider"></div>
                    <a href="#" class="logout-link">Đăng xuất</a>
                </div>
            </div>
        </div>
    </header>

    <div class="main-wrapper">
        <aside class="sidebar">
            <nav class="sidebar-nav">
                <ul>
                    <li><a href="admin_homepage.html">Tổng quan</a></li>
                    <li><a href="admin_product.jsp">Sản phẩm</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/product-type-manager">Loại sản phẩm</a></li>
                    <li><a href="${pageContext.request.contextPath}/category-manager">Danh mục</a></li>
                    <li><a href="admin_order.html">Đơn hàng</a></li>
                    <li><a href="admin_customer.html">Khách hàng</a></li>
                    <li><a href="admin_profile.html"> Hồ sơ</a></li>
                    <li><a href="admin_setting.html"> Cài đặt</a></li>
                </ul>
            </nav>
        </aside>

        <main class="content">
            <div class="category-management-container">
                <h2 class="page-title">Quản Lý Loại Sản Phẩm</h2>

                <div class="search-filter-bar">
                    <div class="search-input-group" style="flex-grow: 1;">
                        <input type="text" placeholder="Tìm kiếm loại sản phẩm..."
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
                            <th class="col-id">ID Danh Mục</th>
                            <th class="col-actions">Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%-- Giả sử Servlet gửi listPT --%>
                        <c:forEach items="${listPT}" var="pt">
                            <tr>
                                <td class="col-id">${pt.id}</td>
                                <td class="col-name">${pt.productTypeName}</td>
                                <td class="col-id">${pt.categoryId}</td>
                                <td class="col-actions">
                                    <i class="fa-solid fa-pen-to-square"
                                       onclick="editProductType('${pt.id}', '${pt.productTypeName}', '${pt.categoryId}')"></i>
                                    <i class="fa-solid fa-trash-can"></i>
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

                        <div class="form-group">
                            <label for="categoryId">Thuộc Danh Mục:</label>
                            <select name="categoryId" id="categoryId" required
                                    style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px;">
                                <option value="">-- Chọn danh mục --</option>
                                <c:forEach items="${listC}" var="c">
                                    <%-- value gửi về Servlet là ID, nhưng hiển thị cho người dùng là Tên --%>
                                    <option value="${c.id}">ID: ${c.id} - ${c.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <button type="submit" class="submit-btn">Lưu Thông Tin</button>
                    </form>
                </div>
            </div>
        </main>
    </div>
</div>

<script src="js/admin_product_type.js"></script>
</body>
</html>