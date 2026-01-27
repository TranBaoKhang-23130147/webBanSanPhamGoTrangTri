<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - CHỈNH SỬA SẢN PHẨM</title>
    <link rel="stylesheet" href="css/user_admin.css">
    <link rel="stylesheet" href="css/admin_add_products.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        .variant-item { margin-bottom: 20px; position: relative; border: 1px solid #eee; padding: 15px; border-radius: 8px; transition: all 0.3s; }
        .variant-item.duplicate-error { border: 2px solid #e74c3c !important; background-color: #fdf2f2; }
        .remove-variant { color: #e74c3c; cursor: pointer; position: absolute; top: 15px; right: 15px; z-index: 10; font-size: 1.2rem; }
        .remove-variant:hover { color: #c0392b; }
        #image-preview img { width: 100px; height: 100px; object-fit: cover; border: 1px solid #ddd; border-radius: 5px; margin: 5px; }
        .page-title { margin-bottom: 20px; color: #2c3e50; border-bottom: 2px solid #27ae60; padding-bottom: 10px; }
    </style>
</head>
<body>

<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="page-content-wrapper">
            <form action="admin-edit-product" method="POST" id="editProductForm">

                <input type="hidden" name="productId" value="${product.id}">
                <input type="hidden" name="infoId" value="${product.information.id}">
                <input type="hidden" name="descId" value="${product.detailDescription.id}">

                <div class="add-product-container">
                    <h1 class="page-title">Chỉnh sửa: ${product.nameProduct}</h1>

                    <div class="product-form-layout">
                        <div class="form-column-left">
                            <div class="card product-info-section">
                                <h2 class="card-title">Thông tin cơ bản</h2>
                                <div class="form-group">
                                    <label for="productName">Tên sản phẩm</label>
                                    <input type="text" name="productName" id="productName" required value="${product.nameProduct}">
                                </div>
                                <div class="form-group">
                                    <label>Giá cơ bản (VND)</label>
                                    <input type="number" name="price" required value="${product.price}">
                                </div>

                                <div class="form-group">
                                    <label>Danh mục</label>
                                    <select name="categoryId">
                                        <c:forEach var="cat" items="${listCategories}">
                                            <option value="${cat.id}" ${cat.id == product.categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Loại sản phẩm</label>
                                    <select name="productTypeId">
                                        <c:forEach var="t" items="${listTypes}">
                                            <option value="${t.id}" ${t.id == product.productTypeId ? 'selected' : ''}>${t.productTypeName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Nhà cung cấp (Source)</label>
                                    <select name="sourceId">
                                        <c:forEach var="s" items="${listSources}">
                                            <option value="${s.id}" ${s.id == product.sourceId ? 'selected' : ''}>${s.sourceName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="form-group">
                                    <label>Ngày sản xuất</label>
                                    <input type="date" name="mfgDate" required value="${product.mfgDate}">
                                </div>
                            </div>

                            <div class="card">
                                <h2 class="card-title">Mô tả & Chi tiết</h2>
                                <div class="form-group">
                                    <label>Giới thiệu ngắn</label>
                                    <textarea name="introduce" rows="4" class="form-control">${product.detailDescription.introduce}</textarea>
                                </div>
                                <div class="form-group">
                                    <label>Điểm nổi bật (Highlights)</label>
                                    <textarea name="highlights" rows="3" class="form-control">${product.detailDescription.highlights}</textarea>
                                </div>
                                <div class="form-group">
                                    <label>Chất liệu</label>
                                    <input type="text" name="material" value="${product.information.material}">
                                </div>
                                <div class="form-group">
                                    <label>Bảo hành</label>
                                    <input type="text" name="guarantee" value="${product.information.guarantee}">
                                </div>
                            </div>

                            <div class="card image-upload-section">
                                <h2 class="card-title">Hình ảnh sản phẩm</h2>
                                <div class="drop-area" onclick="selectImagesWithCKFinder('imageUrls', 'image-preview')" style="border: 2px dashed #ccc; padding: 20px; text-align: center; cursor: pointer;">
                                    <i class="fas fa-cloud-upload-alt" style="font-size: 2rem; color: #7f8c8d;"></i>
                                    <p>Click để quản lý bộ sưu tập ảnh</p>
                                    <input type="hidden" id="imageUrls" name="productImages" value="${product.imagesRaw}">
                                </div>
                                <div id="image-preview">
                                    <c:forEach var="img" items="${product.listImages}">
                                        <img src="${img}" alt="Product Image">
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <div class="form-column-right">
                            <div id="variant-container">
                                <c:forEach var="v" items="${product.variants}" varStatus="vStatus">
                                    <div class="card variant-item">
                                        <input type="hidden" name="variantId[]" value="${v.id}">  <!-- THÊM DÒNG NÀY -->
                                        <h2 class="card-title">Biến thể #${vStatus.count}</h2>
                                        <span class="remove-variant" onclick="removeVariant(this)">
                                            <i class="fas fa-trash"></i>
                                        </span>
                                        <div class="variant-grid">
                                            <div class="form-group">
                                                <label>Mã SKU:</label>
                                                <input type="text" name="variantSKU[]" required value="${v.sku}">
                                            </div>
                                            <div class="form-group">
                                                <label>Màu sắc:</label>
                                                <select name="variantColor[]" class="val-change">
                                                    <c:forEach var="c" items="${listColors}">
                                                        <option value="${c.id}" ${c.id == v.color_id ? 'selected' : ''}>${c.colorName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <label>Kích thước:</label>
                                                <select name="variantSize[]" class="val-change">
                                                    <c:forEach var="sz" items="${listSizes}">
                                                        <option value="${sz.id}" ${sz.id == v.size_id ? 'selected' : ''}>${sz.size_name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <label>Giá (VND):</label>
                                                <input type="number" name="variantPrice[]" required value="${v.variant_price}">
                                            </div>
                                            <div class="form-group">
                                                <label>Kho:</label>
                                                <input type="number" name="variantStock[]" required value="${v.inventory_quantity}">
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <button type="button" class="btn-link-add" onclick="addVariant()" style="width: 100%; margin-bottom: 20px; padding: 12px; border: 1px dashed #27ae60; color: #27ae60; background: none; cursor: pointer;">
                                <i class="fas fa-plus"></i> Thêm biến thể mới
                            </button>

                            <button type="submit" class="btn-primary btn-save" style="width: 100%; background-color: #27ae60; color: white; border: none; padding: 15px; font-weight: bold; border-radius: 5px; cursor: pointer;">
                                CẬP NHẬT SẢN PHẨM
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </main>
    </div>
</div>

<script src="${pageContext.request.contextPath}/ckfinder/ckfinder.js"></script>
<script>
    // 1. Quản lý ảnh với CKFinder
    function selectImagesWithCKFinder(targetInputId, targetPreviewId) {
        var finder = new CKFinder();
        finder.basePath = '${pageContext.request.contextPath}/ckfinder/';
        finder.selectActionFunction = function(fileUrl, data, allFiles) {
            let urls = [];
            let html = '';
            if (allFiles && allFiles.length > 0) {
                allFiles.forEach(file => {
                    urls.push(file.url);
                    html += `<img src="${file.url}">`;
                });
            } else {
                urls.push(fileUrl);
                html += `<img src="${fileUrl}">`;
            }
            document.getElementById(targetInputId).value = urls.join(',');
            document.getElementById(targetPreviewId).innerHTML = html;
        };
        finder.popup();
    }

    // 2. Thêm biến thể mới - Lưu ý name phải khớp variantColor[], variantSize[]...
    function addVariant() {
        const container = document.getElementById('variant-container');
        const count = container.querySelectorAll('.variant-item').length + 1;
        const newVariant = document.createElement('div');
        newVariant.className = 'card variant-item';
        newVariant.innerHTML = `
        <input type="hidden" name="variantId[]" value="0">
            <h2 class="card-title">Biến thể #${count}</h2>
            <span class="remove-variant" onclick="removeVariant(this)">
                <i class="fas fa-trash"></i>
            </span>
            <div class="variant-grid">
                <div class="form-group">
                    <label>Mã SKU:</label>
                    <input type="text" name="variantSKU[]" required placeholder="SKU">
                </div>
                <div class="form-group">
                    <label>Màu sắc:</label>
                    <select name="variantColor[]" class="val-change">
                        <option value="">-- Chọn màu --</option>
                        <c:forEach var="c" items="${listColors}">
                            <option value="${c.id}">${c.colorName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Kích thước:</label>
                    <select name="variantSize[]" class="val-change">
                        <option value="">-- Chọn size --</option>
                        <c:forEach var="sz" items="${listSizes}">
                            <option value="${sz.id}">${sz.size_name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Giá biến thể:</label>
                    <input type="number" name="variantPrice[]" required placeholder="0">
                </div>
                <div class="form-group">
                    <label>Số lượng kho:</label>
                    <input type="number" name="variantStock[]" required placeholder="0">
                </div>
            </div>
        `;
        container.appendChild(newVariant);
    }

    function removeVariant(btn) {
        if(confirm('Bạn có chắc chắn muốn xóa biến thể này?')) {
            btn.parentElement.remove();
            checkDuplicateLive();
        }
    }

    // 4. Kiểm tra trùng lặp
    function checkDuplicateLive() {
        const container = document.getElementById('variant-container');
        const items = container.querySelectorAll('.variant-item');
        const seen = new Set();

        items.forEach(item => {
            const colorSel = item.querySelector('select[name="variantColor[]"]');
            const sizeSel = item.querySelector('select[name="variantSize[]"]');

            if (colorSel && sizeSel && colorSel.value && sizeSel.value) {
                const combo = colorSel.value + "-" + sizeSel.value;
                if (seen.has(combo)) {
                    item.classList.add('duplicate-error');
                } else {
                    seen.add(combo);
                    item.classList.remove('duplicate-error');
                }
            }
        });
    }

    // 5. Submit validation
    document.getElementById('editProductForm').addEventListener('submit', function(e) {
        const items = document.querySelectorAll('.variant-item');
        const seen = new Set();
        let hasError = false;

        if (items.length === 0) {
            alert('Lỗi: Phải có ít nhất 1 biến thể!');
            e.preventDefault();
            return;
        }

        items.forEach((item, index) => {
            const colorSel = item.querySelector('select[name="variantColor[]"]');
            const sizeSel = item.querySelector('select[name="variantSize[]"]');

            if (!colorSel.value || !sizeSel.value) {
                alert('Vui lòng chọn Màu và Size cho biến thể #' + (index + 1));
                hasError = true;
                return;
            }

            const combo = colorSel.value + "-" + sizeSel.value;
            if (seen.has(combo)) {
                alert('Lỗi: Biến thể bị trùng Màu và Kích thước!');
                item.classList.add('duplicate-error');
                hasError = true;
            } else {
                seen.add(combo);
            }
        });

        if (hasError) e.preventDefault();
    });

    document.addEventListener('change', function(e) {
        if (e.target.classList.contains('val-change')) {
            checkDuplicateLive();
        }
    });
</script>
</body>
</html>