<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

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
                    <%-- Gửi dữ liệu về @WebServlet("/products") --%>
                    <form action="products" method="GET" style="display: flex; gap: 10px; flex-grow: 1; align-items: center;">

                        <div class="search-input-group">
                            <%-- Giữ lại từ khóa tìm kiếm bằng param.keyword --%>
                            <input type="text" name="keyword" value="${param.keyword}"
                                   placeholder="Tìm kiếm sản phẩm" class="search-input">
                        </div>

                        <%-- Lọc theo Loại sản phẩm: Tự submit khi chọn --%>
                        <select name="typeId" class="filter-select" onchange="this.form.submit()">
                            <option value="">Loại sản phẩm</option>
                            <c:forEach var="type" items="${typeList}">
                                <option value="${type.id}" ${param.typeId == type.id ? 'selected' : ''}>
                                        ${type.productTypeName}
                                </option>
                            </c:forEach>
                        </select>

                        <%-- Lọc theo Danh mục: Tự submit khi chọn --%>
                        <select name="categoryId" class="filter-select" onchange="this.form.submit()">
                            <option value="">Danh mục</option>
                            <c:forEach var="cat" items="${categoryList}">
                                <option value="${cat.id}" ${param.categoryId == cat.id ? 'selected' : ''}>
                                        ${cat.categoryName}
                                </option>
                            </c:forEach>
                        </select>

                        <%-- Nút tìm kiếm icon --%>
                        <button type="submit" style="background: none; border: none; cursor: pointer; color: #666;">
                        </button>
                    </form>

                    <div class="action-buttons" style="display: flex; gap: 10px;">
                        <button class="export-product-btn">
                            <i class="fa-solid fa-file-export"></i> Thêm màu sắc và kích thướt
                        </button>
                        <button class="add-new-product-btn">
                            <i class="fa-solid fa-plus"></i> Thêm Sản Phẩm Mới
                        </button>
                    </div>
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
                                         class="product-icon" style="width: 50px; height: 50px; object-fit: cover;">
                                </td>

                                <td class="col-name">
                                    <a href="${pageContext.request.contextPath}/admin/product-detail?id=${p.id}"
                                       style="color: #007bff; text-decoration: none; cursor: pointer;">
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
                                    <fmt:formatDate value="${p.mfgDate}" pattern="dd/MM/yyyy" />
                                </td>

                                <td class="col-price">
                                    <fmt:formatNumber value="${p.price}" pattern="#,###"/> đ
                                </td>

                                <td class="col-stock">
                                        ${p.totalStock != null ? p.totalStock : 0}
                                </td>

                                <td class="col-status">
                                    <label class="switch">
                                        <input type="checkbox" ${p.isActive == 1 ? 'checked' : ''}
                                               onchange="toggleActive(${p.id}, this.checked)">
                                        <span class="slider round"></span>
                                    </label>
                                </td>

                                <td class="col-actions">
                                    <a href="admin/edit-product?id=${p.id}" title="Sửa">
                                        <i class="fa-solid fa-pen-to-square edit-icon"></i>
                                    </a>
                                    <a href="#" onclick="confirmDelete(${p.id}); return false;" title="Xóa">
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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    // Confirm xóa sản phẩm


    // Bật/tắt trạng thái active (nếu bạn muốn implement)
    function toggleActive(productId, isActive) {
        const status = isActive ? 1 : 0;
        Swal.fire({
            title: isActive ? 'Kích hoạt sản phẩm?' : 'Tắt sản phẩm?',
            text: isActive ? "Sản phẩm sẽ hiển thị cho khách hàng." : "Sản phẩm sẽ ẩn đi.",
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Đồng ý'
        }).then((result) => {
            if (result.isConfirmed) {
                // Gửi AJAX hoặc redirect đến servlet update status
                // Ví dụ dùng fetch (AJAX đơn giản)
                fetch('${pageContext.request.contextPath}/admin/update-product-status', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'id=' + productId + '&active=' + status
                })
                    .then(response => {
                        if (response.ok) {
                            Swal.fire('Thành công!', 'Trạng thái đã cập nhật.', 'success');
                        } else {
                            throw new Error('Cập nhật thất bại');
                        }
                    })
                    .catch(error => {
                        Swal.fire('Lỗi!', 'Không thể cập nhật trạng thái.', 'error');
                        // Hoàn nguyên checkbox nếu lỗi
                        document.querySelector(`input[onchange="toggleActive(${productId}, ${!isActive})"]`).checked = !isActive;
                    });
            } else {
                // Hoàn nguyên checkbox nếu hủy
                this.checked = !isActive;
            }
        });
    }

    // Gắn event cho nút "Thêm Sản Phẩm Mới" (ví dụ)
    document.addEventListener('DOMContentLoaded', function() {
        const addBtn = document.querySelector('.add-new-product-btn');
        if (addBtn) {
            addBtn.addEventListener('click', function() {
                window.location.href = '${pageContext.request.contextPath}/admin-add-product';
            });
        }

        // Nút "Thêm màu sắc và kích thước" - tùy theo chức năng bạn muốn
        const colorSizeBtn = document.querySelector('.export-product-btn');
        if (colorSizeBtn) {
            colorSizeBtn.addEventListener('click', function() {
                Swal.fire({
                    title: 'Chức năng đang phát triển',
                    text: 'Tính năng quản lý màu sắc & kích thước sẽ được thêm sau.',
                    icon: 'info'
                });
            });
        }
    });
    function confirmDelete(productId) {
        Swal.fire({
            title: 'Xác nhận xóa?',
            text: "Sản phẩm sẽ bị xóa vĩnh viễn!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Xóa ngay',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                fetch('${pageContext.request.contextPath}/admin/delete-product', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'id=' + productId
                    // Nếu có CSRF: + '&csrfToken=' + document.querySelector('[name="csrfToken"]').value
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            Swal.fire('Thành công!', data.message, 'success')
                                .then(() => location.reload());
                        } else {
                            Swal.fire('Lỗi!', data.message, 'error');
                        }
                    })
                    .catch(error => {
                        Swal.fire('Lỗi!', 'Không thể xóa sản phẩm. Lỗi: ' + error, 'error');
                    });
            }
        });
    }
</script>
</body>
</html>