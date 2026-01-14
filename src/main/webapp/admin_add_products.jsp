<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - THÊM SẢN PHẨM</title>
    <link rel="icon" type="image/png" href="../img/logo.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="css/user_admin.css">
    <link rel="stylesheet" href="css/admin_add_products.css">
    <link rel="stylesheet" href="css/admin_profile_style.css">
    <style>
        .variant-item { margin-bottom: 20px; position: relative; }
        .remove-variant { color: red; cursor: pointer; position: absolute; top: 10px; right: 10px; }
        select { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; margin-bottom: 10px; }
    </style>
</head>
<body>

<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="page-content-wrapper">
            <form action="admin-add-product" method="POST" enctype="multipart/form-data">
                <div class="add-product-container">
                    <h1 class="page-title">Thêm sản phẩm mới</h1>

                    <div class="product-form-layout">
                        <div class="form-column-left">
                            <div class="card product-info-section">
                                <h2 class="card-title">Thông tin cơ bản</h2>
                                <div class="form-group">
                                    <label for="productName">Tên sản phẩm</label>
                                    <input type="text" name="productName" id="productName" required placeholder="Ví dụ: Giường gỗ sồi...">
                                </div>

                                <div class="form-group">
                                    <label>Danh mục</label>
                                    <select name="categoryId" required>
                                        <c:forEach var="cat" items="${listCategories}">
                                            <option value="${cat.id}">${cat.categoryName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Loại sản phẩm</label>
                                    <select name="typeId" required>
                                        <c:forEach var="t" items="${listTypes}">
                                            <option value="${t.id}">${t.typeName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Nhà cung cấp (Nguồn)</label>
                                    <select name="sourceId" required>
                                        <c:forEach var="s" items="${listSources}">
                                            <option value="${s.id}">${s.sourceName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Ngày sản xuất</label>
                                    <input type="date" name="mfgDate" required>
                                </div>
                            </div>

                            <div class="card">
                                <h2 class="card-title">Mô tả sản phẩm</h2>
                                <div class="form-group">
                                    <label>Giới thiệu ngắn</label>
                                    <textarea name="introduce" rows="3" placeholder="Mô tả tổng quan..."></textarea>
                                </div>
                                <div class="form-group">
                                    <label>Đặc điểm nổi bật (Dùng dấu | để ngăn cách các ý)</label>
                                    <input type="text" name="highlights" placeholder="Bền bỉ | Thẩm mỹ | Chống mối mọt">
                                </div>
                            </div>

                            <div class="card">
                                <h2 class="card-title">Thông số chi tiết</h2>
                                <div class="form-group"><label>Chất liệu</label><input type="text" name="material"></div>
                                <div class="form-group"><label>Kích thước tổng quát</label><input type="text" name="infoSize"></div>
                                <div class="form-group"><label>Màu sắc tổng quát</label><input type="text" name="infoColor"></div>
                                <div class="form-group"><label>Bảo hành</label><input type="text" name="guarantee"></div>
                            </div>

                            <div class="card image-upload-section">
                                <h2 class="card-title">Hình ảnh (Tối đa 5 ảnh)</h2>
                                <div class="drop-area" onclick="document.getElementById('fileInput').click()">
                                    <i class="fas fa-box-open"></i>
                                    <p>Click để chọn tối đa 5 ảnh</p>
                                    <input type="file" id="fileInput" name="productImages" multiple accept="image/*" style="display:none" onchange="previewImages(this)">
                                </div>
                                <div id="image-preview" style="display: flex; gap: 10px; margin-top: 10px; flex-wrap: wrap;"></div>
                            </div>
                        </div>

                        <div class="form-column-right">
                            <div id="variant-container">
                                <div class="card variant-item">
                                    <h2 class="card-title">Biến thể 1</h2>
                                    <div class="attribute-item form-row">
                                        <div class="form-group">
                                            <label>Màu sắc</label>
                                            <select name="colorId[]">
                                                <c:forEach var="c" items="${listColors}">
                                                    <option value="${c.id}">${c.colorName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Kích thước</label>
                                            <select name="sizeId[]">
                                                <c:forEach var="sz" items="${listSizes}">
                                                    <option value="${sz.id}">${sz.size_name}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label>Giá biến thể (VND)</label>
                                        <input type="number" name="variantPrice[]" required placeholder="0.00">
                                    </div>
                                    <div class="form-group">
                                        <label>Số lượng kho</label>
                                        <input type="number" name="variantStock[]" required placeholder="0">
                                    </div>
                                </div>
                            </div>

                            <button type="button" class="btn-link-add" onclick="addVariant()" style="width: 100%; margin-bottom: 20px;">
                                <i class="fas fa-plus"></i> Thêm biến thể mới
                            </button>

                            <button type="submit" class="btn-primary btn-save" style="width: 100%;">LƯU SẢN PHẨM</button>
                        </div>
                    </div>
                </div>
            </form>
        </main>
    </div>
</div>

<script>
    // Hàm thêm biến thể động
    function addVariant() {
        const container = document.getElementById('variant-container');
        const variantCount = container.getElementsByClassName('variant-item').length + 1;

        const firstVariant = container.querySelector('.variant-item');
        const newVariant = firstVariant.cloneNode(true);

        // Cập nhật tiêu đề
        newVariant.querySelector('.card-title').innerHTML = `Biến thể ${variantCount} <span class="remove-variant" onclick="this.parentElement.parentElement.remove()"><i class="fas fa-trash"></i></span>`;

        // Xóa trắng dữ liệu trong biến thể mới
        newVariant.querySelectorAll('input').forEach(input => input.value = "");

        container.appendChild(newVariant);
    }

    // Hàm xem trước nhiều ảnh
    function previewImages(input) {
        const preview = document.getElementById('image-preview');
        preview.innerHTML = "";
        if (input.files) {
            const filesArray = Array.from(input.files).slice(0, 5);
            filesArray.forEach(file => {
                const reader = new FileReader();
                reader.onload = (e) => {
                    const div = document.createElement("div");
                    div.style.position = "relative";
                    div.innerHTML = `
                        <img src="${e.target.result}" style="width: 80px; height: 80px; object-fit: cover; border-radius: 4px; border: 1px solid #ddd;">
                    `;
                    preview.appendChild(div);
                };
                reader.readAsDataURL(file);
            });
        }
    }
</script>
</body>
</html>