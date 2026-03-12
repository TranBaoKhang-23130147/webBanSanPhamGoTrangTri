<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ SẢN PHẨM</title>

    <link rel="icon" type="image/png" href="img/logo.png">

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_products.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_profile_style.css">

</head>

<body>

<div class="admin-container">

    <%@ include file="admin_header.jsp" %>

    <div class="main-wrapper">

        <%@ include file="admin_sidebar.jsp" %>

        <main class="content">

            <div class="product-management-container">

                <h2 class="page-title">Quản Lý Sản Phẩm</h2>

                <div class="search-filter">

                    <form action="products"
                          method="GET"
                          class="product-search-form">

                        <div class="search-input">

                            <input type="text"
                                   name="keyword"
                                   value="${param.keyword}"
                                   placeholder="Tìm kiếm sản phẩm"
                                   class="search-input">

                        </div>

                        <select name="typeId"
                                class="filter-select"
                                onchange="this.form.submit()">

                            <option value="">Loại sản phẩm</option>

                            <c:forEach var="type" items="${typeList}">

                                <option value="${type.id}"
                                    ${param.typeId == type.id ? 'selected' : ''}>

                                        ${type.productTypeName}

                                </option>

                            </c:forEach>

                        </select>

                        <select name="categoryId"
                                class="filter-select"
                                onchange="this.form.submit()">

                            <option value="">Danh mục</option>

                            <c:forEach var="cat" items="${categoryList}">

                                <option value="${cat.id}"
                                    ${param.categoryId == cat.id ? 'selected' : ''}>

                                        ${cat.categoryName}

                                </option>

                            </c:forEach>

                        </select>

                        <button type="submit" class="btn-search"></button>

                    </form>


                    <div class="action-buttons">

                        <a href="${pageContext.request.contextPath}/admin-attribute"
                           class="attribute-link">

                            <button class="export-product-btn">

                                <i class="fa-solid fa-file-export"></i>

                                Thêm màu sắc và kích thước

                            </button>

                        </a>

                    </div>

                    <button class="add-new-product-btn">

                        <i class="fa-solid fa-plus"></i>

                        Thêm Sản Phẩm Mới

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
                            <th class="col-stock">Giá</th>
                            <th class="col-stock">Số lượng</th>
                            <th class="col-status">Trạng thái</th>
                            <th class="col-actions">Thao tác</th>

                        </tr>

                        </thead>

                        <tbody>

                        <c:forEach var="p" items="${productList}">

                            <tr>

                                <td class="col-image">

                                    <img src="${not empty p.imageUrl ? p.imageUrl : 'img/default-product.png'}"
                                         class="product-icon">

                                </td>


                                <td class="col-name">

                                    <a href="${pageContext.request.contextPath}/admin/product-detail?id=${p.id}"
                                       class="product-detail-link">

                                            ${p.nameProduct}

                                    </a>

                                </td>


                                <td class="col-category">

                                        ${not empty p.typeName ? p.typeName : 'Chưa phân loại'}

                                </td>


                                <td class="col-category">

                                        ${not empty p.categoryName ? p.categoryName : 'Chưa có danh mục'}

                                </td>


                                <td class="col-date-added">

                                    <fmt:formatDate value="${p.mfgDate}" pattern="dd/MM/yyyy"/>

                                </td>


                                <td class="col-price">

                                    <fmt:formatNumber value="${p.price}" pattern="#,###"/> đ

                                </td>


                                <td class="col-stock">

                                        ${p.totalStock != null ? p.totalStock : 0}

                                </td>


                                <td class="col-status">

                                    <label class="switch">

                                        <input type="checkbox"
                                            ${p.isActive == 1 ? 'checked' : ''}
                                               onchange="toggleActive(${p.id}, this.checked)">

                                        <span class="slider round"></span>

                                    </label>

                                </td>


                                <td class="col-actions">

                                    <a href="${pageContext.request.contextPath}/admin-edit-product?id=${p.id}"
                                       title="Sửa">

                                        <i class="fa-solid fa-pen-to-square edit-icon"></i>

                                    </a>

                                    <a href="#"
                                       onclick="confirmDelete(${p.id}); return false;"
                                       title="Xóa">

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

<script>
    const contextPath = "${pageContext.request.contextPath}";
</script>

<script src="${pageContext.request.contextPath}/js/admin_products.js"></script>

</body>
</html>