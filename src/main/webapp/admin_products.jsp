<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ SẢN PHẨM</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="css/admin_products.css">
    <link rel="stylesheet" href="css/admin_profile_style.css">
</head>
<body>

<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="content">

            <div class="product-management-container">
                <h2 class="page-title">Quản Lý Sản Phẩm</h2>

                <div class="search-filter-bar">
                    <div class="search-input-group">
                        <input type="text" placeholder="Tìm kiếm sản phẩm" class="search-input">
                    </div>
                    <select class="filter-select">
                        <option value="">Loại sản phẩm</option>
                    </select>
                    <select class="filter-select">
                        <option value="">Danh mục</option>
                    </select>

                    <button class="export-product-btn">
                        <i class="fa-solid fa-file-export"></i> Thêm màu sắc và kích thướt
                    </button>
                    <button class="add-new-product-btn">
                        <i class="fa-solid fa-plus"></i> Thêm Sản Phẩm Mới
                    </button>
                </div>

                <div class="product-table-wrapper">
                    <table class="product-table">
                        <thead>
                        <tr>
                            <th class="col-image">Ảnh</th>
                            <th class="col-name">Sản phẩm</th>
                            <th class="col-category">Loại sản phẩm</th>
                            <th class="col-category">Danh mục</th>
                            <th class="col-date-added">Ngày thêm</th>
                            <th class="col-price">Giá (VND)</th>
                            <th class="col-stock">Số lượng</th>
                            <th class="col-status">Trạng thái</th>
                            <th class="col-actions">Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="p" items="${productList}">
                            <tr>
                                <td class="col-image">
                                    <img src="${p.imageUrl}" class="product-icon">
                                </td>

                                <td class="col-name">${p.nameProduct}</td>

                                <td class="col-category">
                                        ${p.productTypeId}
                                </td>

                                <td class="col-category">
                                        ${p.categoryId}
                                </td>

                                <td class="col-date-added">
                                        ${p.mfgDate}
                                </td>

                                <td class="col-price">
                                    <fmt:formatNumber value="${p.price}" type="number"/> đ
                                </td>

                                <td class="col-stock">
                                    10
                                </td>

                                <td class="col-status">
                                    <label class="switch">
                                        <input type="checkbox" ${p.isActive == 1 ? "checked" : ""}>
                                        <span class="slider round"></span>
                                    </label>
                                </td>

                                <td class="col-actions">
                                    <a href="admin/edit-product?id=${p.id}">
                                        <i class="fa-solid fa-pen-to-square edit-icon"></i>
                                    </a>
                                    <a href="admin/delete-product?id=${p.id}"
                                       onclick="return confirm('Xóa sản phẩm này?')">
                                        <i class="fa-solid fa-trash-can delete-icon"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>

                    </table>
                </div>
            </div>
        </main>

    </div>
</div>
<script src="js/admin_products.js"></script>

</body>
</html>