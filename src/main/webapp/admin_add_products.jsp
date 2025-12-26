<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR</title>
    <link rel="icon" type="image/png"  href="../img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="css/user_admin.css">
    <link rel="stylesheet" href="css/admin_add_products.css">
    <link rel="stylesheet" href="css/admin_profile_style.css">

</head>
<body>

<div class="admin-container">
    <header class="header">
        <div class="logo-placeholder">
            <img src="../img/logo.png" alt="Logo">
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
                    <li ><a href="../admin_homepage.jsp">Tổng quan</a></li>
                    <li class="active"><a href="admin_products.html"> Sản phẩm</a></li>
                    <li><a href="../admin_product_type.jsp">Loại sản phẩm</a></li>
                    <li><a href="admin_category.html"> Danh mục</a></li>
                    <li ><a href="admin_order.html"> Đơn hàng</a></li>
                    <li><a href="admin_customer.html"> Khách hàng</a></li>
                    <li><a href="admin_profile.html"> Hồ sơ</a></li>
                    <li><a href="../admin_setting.jsp"> Cài đặt</a></li>
                </ul>
            </nav>
        </aside>
        <main class="page-content-wrapper">
            <div class="add-product-container">
                <h1 class="page-title">Thêm sản phẩm mới</h1>

                <form action="${pageContext.request.contextPath}/add-product" method="POST" enctype="multipart/form-data">
                    <div class="product-form-layout">
                        <div class="form-column-left">
                            <div class="card product-info-section">
                                <h2 class="card-title">Thông tin cơ bản</h2>

                                <div class="form-group">
                                    <label for="productName">Tên sản phẩm</label>
                                    <input type="text" name="productName" id="productName" placeholder="Giường, tủ, kệ,..." required>
                                </div>

                                <div class="form-group">
                                    <label for="categoryId">Danh mục</label>
                                    <select name="categoryId" id="categoryId" required>
                                        <option value="">-- Chọn danh mục --</option>
                                        <c:forEach items="${listC}" var="c">
                                            <option value="${c.id}">${c.categoryName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="productTypeId">Loại sản phẩm</label>
                                    <select name="productTypeId" id="productTypeId" required>
                                        <option value="">-- Chọn loại sản phẩm --</option>
                                        <c:forEach items="${listPT}" var="pt">
                                            <option value="${pt.id}">${pt.productTypeName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="description">Mô tả chi tiết</label>
                                    <textarea name="description" id="description" rows="5" placeholder="Nhập mô tả sản phẩm..."></textarea>
                                </div>
                            </div>

                            <div class="card image-upload-section">
                                <h2 class="card-title">Hình ảnh (primary_image_id)</h2>
                                <div class="drop-area">
                                    <i class="fas fa-box-open"></i>
                                    <p>Kéo thả hoặc tải ảnh sản phẩm lên</p>
                                    <input type="file" name="productImage" accept="image/*">
                                </div>
                            </div>
                        </div>

                        <div class="form-column-right">
                            <div class="card pricing-section">
                                <h2 class="card-title">Giá & Tồn kho</h2>

                                <div class="form-group">
                                    <label for="price">Giá bán (VNĐ)</label>
                                    <input type="number" step="0.01" name="price" id="price" placeholder="0.00" required>
                                </div>

                                <div class="form-group">
                                    <label>Trạng thái kinh doanh</label>
                                    <select name="isActive">
                                        <option value="1">Đang kinh doanh</option>
                                        <option value="0">Ngừng kinh doanh</option>
                                    </select>
                                </div>
                            </div>

                            <div class="card organization-section">
                                <h2 class="card-title">Thông tin bổ sung</h2>

                                <div class="form-group">
                                    <label for="sourceId">Nhà cung cấp</label>
                                    <select name="sourceId" id="sourceId">
                                        <c:forEach items="${listSource}" var="s">
                                            <option value="${s.id}">${s.sourceName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label for="mfgDate">Ngày sản xuất</label>
                                    <input type="date" name="mfgDate" id="mfgDate" required>
                                </div>
                            </div>

                            <button type="submit" class="btn-primary btn-save">Lưu sản phẩm</button>
                        </div>
                    </div>
                </form>
            </div>
        </main>

    </div>
</div>
<script src="js/order_admin.js"></script>

</body>
</html>