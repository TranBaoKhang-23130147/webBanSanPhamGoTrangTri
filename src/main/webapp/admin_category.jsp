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

</head>
<body>

<div class="admin-container">
    <header class="header">
        <div class="logo-placeholder">
            <img src="../img/logo.png" alt="Logo Modern Homes">
            <p class="logo">HOME DECOR</p>
        </div>

        <div class="header-icons">

            <div class="gmail-dropdown">
                <i class="fa-solid fa-envelope gmail-icon"></i>

                <div id="gmailMenuContent" class="dropdown-content gmail-content">
                    <div class="dropdown-header">Gmail</div>
                    <p class="no-messages-text">Không có Gmail nào.</p>
                    <div class="dropdown-divider"></div>
                    <a href="#" class="view-all-link">Mở Gmail</a>
                </div>
            </div>
            <div class="notification-dropdown">
                <i class="fa-solid fa-bell notification-icon"></i>

                <div id="notificationMenuContent" class="dropdown-content notification-content">
                    <div class="dropdown-header">Thông Báo Mới (5)</div>
                    <a href="#">Đơn hàng mới #1001</a>
                    <a href="#">Sản phẩm hết hàng</a>
                    <a href="#">Khách hàng mới đăng ký</a>
                    <a href="#">Đơn hàng #1005 vừa được hủy bỏ</a>
                    <a href="#">Cần duyệt 3 đánh giá sản phẩm mới</a>
                    <div class="dropdown-divider"></div>
                    <a href="#" class="view-all-link">Xem tất cả</a>
                </div>
            </div>

            <div class="user-dropdown">
                <i class="fas fa-user-circle user-logo" ></i>

                <div id="userMenuContent" class="dropdown-content">
                    <a href="admin_thong_tin_tai_khoan.html"> Thông tin tài khoản</a>
                    <a href="admin_doi_mat_khau.html"> Đổi mật khẩu</a>
                    <div class="dropdown-divider"></div>
                    <a href="#" class="logout-link"> Đăng xuất</a>
                </div>
            </div>
        </div>
    </header>
    <div class="main-wrapper">
        <aside class="sidebar">
            <nav class="sidebar-nav">
                <ul>
                    <li ><a href="admin_homepage.html">Tổng quan</a></li>
                    <li class="active"><a href="#"> Sản phẩm</a></li>
                    <li><a href="admin_product_type.html">Loại sản phẩm</a></li>
                    <li><a href="admin_category.html"> Danh mục</a></li>
                    <li><a href="admin_order.html"> Đơn hàng</a></li>
                    <li><a href="admin_customer.html"> Khách hàng</a></li>
                    <li><a href="admin_profile.html"> Hồ sơ</a></li>
                    <li><a href="admin_setting.html"> Cài đặt</a></li>
                </ul>
            </nav>
        </aside>
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

                <div class="category-table-wrapper">
                    <table class="category-table">
                        <thead>
                        <tr>
                            <th class="col-id">ID</th>
                            <th class="col-name">Tên Danh Mục</th>

                            <th class="col-product-count">Số SP</th>

                            <th class="col-actions">Thao tác</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${listC}" var="c">
                            <tr>
                                <td class="col-id">${c.id}</td>
                                <td class="col-name">${c.categoryName}</td>.
                                <td class="col-product-count">0</td>
                                <td class="col-actions">
                                    <i class="fa-solid fa-pen-to-square" onclick="openCategoryModal('edit', '${c.id}', '${c.categoryName}')"></i>
                                    <i class="fa-solid fa-trash-can"></i>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>

                        <c:if test="${not empty sessionScope.msg}">
                            <script>
                                alert("${sessionScope.msg}");
                            </script>
                            <c:remove var="msg" scope="session" />
                        </c:if>
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

<script src="js/admin_category.js"></script>
</body>
</html>