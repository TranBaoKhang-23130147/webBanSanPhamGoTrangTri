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
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="page-content-wrapper">
            <div class="add-product-container">
                <h1 class="page-title">Thêm sản phẩm</h1>

                <div class="product-form-layout">

                    <div class="form-column-left">

                        <div class="card product-info-section">
                            <h2 class="card-title">Thông tin sản phẩm</h2>


                            <div class="form-group">
                                <label for="productName">Tên</label>
                                <input type="text" id="productName" placeholder="Giường, tủ, kệ,...">
                            </div>



                            <div class="form-group ">
                                <label for="category">Danh mục</label>
                                <input type="text" id="category" placeholder=" TRANG TRÍ PHÒNG KHÁCH">
                            </div>
                            <div class="form-group">
                                <label for="productName">Loại sản phẩm</label>
                                <input type="text" id=" " placeholder="Giường, tủ, kệ,...">
                            </div>
                            <div class="form-group">
                                <label for="supplier">Tên</label>
                                <input type="text" id="source" placeholder="Xưỡng gỗ XuanMai,..">
                            </div>
                            <div class="form-group">
                                <label for="supplier">Ngày sản xất</label>
                                <input type="text" id="" placeholder="...">
                            </div>

                            <div class="form-group">
                                <label for="description" class="mota">Mô tả (Tùy chọn)</label>
                                <div class="richtext-toolbar">
                                    <button><i class="fas fa-bold"></i></button>
                                    <button><i class="fas fa-italic"></i></button>
                                    <button><i class="fas fa-underline"></i></button>
                                    <button><i class="fas fa-strikethrough"></i></button>
                                    <button><i class="fas fa-link"></i></button>
                                    <button><i class="fas fa-quote-left"></i></button>
                                    <button><i class="fas fa-list-ul"></i></button>
                                    <button><i class="fas fa-list-ol"></i></button>
                                    <button><i class="fas fa-code"></i></button>
                                </div>
                                <textarea id="description" rows="5" placeholder="Nhập mô tả của bạn..."></textarea>
                            </div>
                        </div>

                        <div class="card image-upload-section">
                            <h2 class="card-title">Hình ảnh</h2>
                            <div class="drop-area">
                                <i class="fas fa-box-open"></i>
                                <p>Kéo và thả tệp của bạn vào đây hoặc</p>
                                <button class="btn-secondary">Tải ảnh lên</button>
                            </div>
                        </div>

                    </div>

                    <div class="form-column-right">

                        <div class="card discount-section">
                            <h2 class="card-title">Biến thể</h2>
                            <div class="form-group ">
                                <label for="skuCode">Mã SKU</label>
                                <input type="text" id="skuCode" placeholder="Ví dụ: 348121032">
                            </div>
                            <div class="attribute-item form-row">
                                <select>
                                    <option>Màu sắc</option>
                                </select>
                                <label for="discountPrice"></label>
                                <select>
                                    <option>Kích thước</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="discountPrice">Giá</label>
                                <input type="text" id="discountPrice" placeholder="0.00">
                            </div>
                            <div class="form-group">
                                <label for="discountQuantity">Số lượng</label>
                                <input type="text" id="discountQuantity" placeholder="0">
                            </div>

                            <button class="btn-link-add">+ Thêm biến thể</button>
                        </div>

                        <button class="btn-primary btn-save">Lưu</button>
                    </div>

                </div>
            </div>
        </main>

    </div>
</div>
<script src="js/order_admin.js"></script>

</body>
</html>