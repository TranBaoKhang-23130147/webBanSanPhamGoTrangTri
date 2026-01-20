<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - THÊM SẢN PHẨM</title>
    <link rel="icon" type="image/png" href="img/logo.png">
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
            <form action="admin-add-product" method="POST">
                <div class="add-product-container">
                    <%-- Kiểm tra thông báo từ Redirect (status=success) --%>
                    <c:if test="${param.status == 'success'}">
                        <div style="padding: 15px; background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; border-radius: 4px; margin-bottom: 20px;">
                            <i class="fas fa-check-circle"></i> Thêm sản phẩm thành công!
                        </div>
                    </c:if>

                    <%-- Kiểm tra thông báo từ Request Attribute (Lỗi) --%>
                    <c:if test="${not empty message}">
                        <div style="padding: 15px; background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; border-radius: 4px; margin-bottom: 20px;">
                            <i class="fas fa-exclamation-triangle"></i> ${message}
                        </div>
                    </c:if>
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
                                    <label>Giá sản phẩm</label>
                                    <input type="number" name="basePrice" required>
                                </div>

                                <!-- Danh mục -->
                                <div class="form-group">
                                    <label>Danh mục</label>
                                    <select name="categoryId">
                                        <option value="">-- Chọn danh mục (tùy chọn) --</option>
                                        <c:forEach var="cat" items="${listCategories}">
                                            <option value="${cat.id}">${cat.categoryName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <!-- Loại sản phẩm -->
                                <div class="form-group">
                                    <label>Loại sản phẩm</label>
                                    <select name="typeId">
                                        <option value="">-- Chọn loại (tùy chọn) --</option>
                                        <c:forEach var="t" items="${listTypes}">
                                            <option value="${t.id}">${t.productTypeName}</option>
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
                                    <input type="date" name="mfgDate" required class="form-control">
                                </div>
                            </div>

                            <div class="card">
                                <h2 class="card-title">Mô tả sản phẩm</h2>
                                <div class="form-group">
                                    <label>Giới thiệu ngắn</label>
                                    <textarea name="introduce" rows="3" placeholder="Mô tả tổng quan..."></textarea>
                                </div>
                                <div class="form-group">
                                    <label>Đặc điểm nổi bật </label>
                                    <input type="text" name="highlights" placeholder="Bền bỉ, Thẩm mỹ, Chống mối mọt">
                                </div>
                            </div>
                            <div class="card">
                                <h2 class="card-title">Thông tin chi tiết sản phẩm</h2>

                                <input type="hidden" name="infoId" value="${productInfo.id}">

                                <div class="form-group">
                                    <label for="material">Chất liệu</label>
                                    <input type="text" id="material" name="material" placeholder="Ví dụ: Gỗ sồi tự nhiên, Nệm da..." required>
                                </div>

                                <div class="form-group">
                                    <label for="colorInfo">Màu sắc chi tiết</label>
                                    <input type="text" id="colorInfo" name="colorInfo" placeholder="Ví dụ: Nâu cánh gián, Trắng kem...">
                                </div>

                                <div class="form-group">
                                    <label for="sizeInfo">Kích thước tổng quát</label>
                                    <input type="text" id="sizeInfo" name="sizeInfo" placeholder="Ví dụ: 200cm x 180cm x 40cm">
                                </div>

                                <div class="form-group">
                                    <label for="guarantee">Bảo hành</label>
                                    <input type="text" id="guarantee" name="guarantee" placeholder="Ví dụ: 12 tháng, 2 năm...">
                                </div>

                            </div>

                            <div class="card image-upload-section">
                                <h2 class="card-title">Hình ảnh (Sử dụng CKFinder)</h2>
                                <div class="drop-area" onclick="selectImagesWithCKFinder('imageUrls', 'image-preview')" style="cursor: pointer; border: 2px dashed #ccc; padding: 20px; text-align: center;">
                                    <i class="fas fa-box-open"></i>
                                    <p>Click để chọn ảnh từ Server</p>
                                    <input type="hidden" id="imageUrls" name="productImages">
                                </div>
                                <div id="image-preview" style="display: flex; gap: 10px; margin-top: 10px; flex-wrap: wrap;"></div>
                            </div>
                        </div> <div class="form-column-right">
                        <div id="variant-container">
                            <div class="card variant-item">
                                <h2 class="card-title">Biến thể</h2>
                                <span class="remove-variant" onclick="this.parentElement.remove()" style="display:none;">
                                        <i class="fas fa-trash"></i>
                                    </span>

                                <div class="variant-grid">
                                    <div class="form-group sku-group">
                                        <label>Mã SKU:</label>
                                        <input type="text" name="variantSKU[]" required placeholder="Ví dụ: BG-PB-01">
                                    </div>

                                    <div class="form-group">
                                        <label>Màu sắc:</label>
                                        <select name="colorId[]">
                                            <c:forEach var="c" items="${listColors}">
                                                <option value="${c.id}">${c.colorName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>Kích thước:</label>
                                        <select name="sizeId[]">
                                            <c:forEach var="sz" items="${listSizes}">
                                                <option value="${sz.id}">${sz.size_name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="form-group">
                                        <label>Giá biến thể (VND):</label>
                                        <input type="number" name="variantPrice[]" required placeholder="0.00">
                                    </div>
                                    <div class="form-group">
                                        <label>Số lượng kho:</label>
                                        <input type="number" name="variantStock[]" required placeholder="0">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <button type="button" class="btn-link-add" onclick="addVariant()" style="width: 100%; margin-bottom: 20px;">
                            <i class="fas fa-plus"></i> Thêm biến thể mới
                        </button>

                        <button type="submit" class="btn-primary btn-save" style="width: 100%;">LƯU SẢN PHẨM</button>                    </div>
                    </div>
                </div>
            </form>
        </main>
    </div>
</div>

<script src="${pageContext.request.contextPath}/ckfinder/ckfinder.js"></script>
<script>
    function selectImagesWithCKFinder(targetInputId, targetPreviewId) {
        var finder = new CKFinder();
        // Giữ nguyên basePath có contextPath để mở được cửa sổ CKFinder
        finder.basePath = '${pageContext.request.contextPath}/ckfinder/';

        finder.selectActionFunction = function(fileUrl, data, allFiles) {
            let urls = [];
            let html = '';

            if (allFiles && allFiles.length > 0) {
                allFiles.forEach(file => {
                    // KHÔNG nối thêm contextPath vì file.url đã có sẵn rồi
                    var finalUrl = file.url;
                    urls.push(finalUrl);
                    html += `<img src="${finalUrl}" style="width:100px;height:100px;object-fit:cover;border:1px solid #ddd;border-radius:5px;margin-right:5px;">`;
                });
            } else {
                // Tương tự cho trường hợp chọn 1 file
                var finalUrl = fileUrl;
                urls.push(finalUrl);
                html += `<img src="${finalUrl}" style="width:100px;height:100px;object-fit:cover;border:1px solid #ddd;border-radius:5px;margin-right:5px;">`;
            }

            document.getElementById(targetInputId).value = urls.join(',');
            document.getElementById(targetPreviewId).innerHTML = html;
        };
        finder.popup();
    }
</script>
<script>
    function addVariant() {
        const container = document.getElementById('variant-container');

        // Tạo một khối variant-item mới (copy cấu trúc từ khối mẫu)
        const newVariant = document.createElement('div');
        newVariant.className = 'card variant-item';
        newVariant.innerHTML = `
            <h2 class="card-title">Biến thể</h2>
            <span class="remove-variant" onclick="this.parentElement.remove()">
                <i class="fas fa-trash"></i>
            </span>

            <div class="variant-grid">
                <div class="form-group sku-group">
                    <label>Mã SKU:</label>
                    <input type="text" name="variantSKU[]" required placeholder="Ví dụ: BG-PB-01">
                </div>

                <div class="form-group">
                    <label>Màu sắc:</label>
                    <select name="colorId[]">
                        <c:forEach var="c" items="${listColors}">
                            <option value="${c.id}">${c.colorName}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>Kích thước:</label>
                    <select name="sizeId[]">
                        <c:forEach var="sz" items="${listSizes}">
                            <option value="${sz.id}">${sz.size_name}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label>Giá biến thể (VND):</label>
                    <input type="number" name="variantPrice[]" required placeholder="0.00">
                </div>

                <div class="form-group">
                    <label>Số lượng kho:</label>
                    <input type="number" name="variantStock[]" required placeholder="0">
                </div>
            </div>
        `;

        // Thêm khối mới vào container
        container.appendChild(newVariant);

        // Hiện nút xóa cho khối mới (khối đầu tiên có display:none)
        const removeBtn = newVariant.querySelector('.remove-variant');
        if (removeBtn) removeBtn.style.display = 'block';
    }

    // Tùy chọn: Hiện nút xóa cho khối biến thể đầu tiên (nếu muốn cho phép xóa luôn khối mặc định)
    window.onload = function() {
        const firstRemove = document.querySelector('.variant-item .remove-variant');
        if (firstRemove) firstRemove.style.display = 'block';
    };
</script>
<script>
    // Hàm kiểm tra trùng variant (color + size) - gọi trước khi submit
    function validateVariants() {
        const variantItems = document.querySelectorAll('.variant-item');
        const seenCombinations = new Set(); // Lưu {colorId-sizeId}
        let hasDuplicate = false;
        let errorMsg = '';

        for (let i = 0; i < variantItems.length; i++) {
            const colorSelect = variantItems[i].querySelector('select[name="colorId[]"]');
            const sizeSelect = variantItems[i].querySelector('select[name="sizeId[]"]');

            if (colorSelect && sizeSelect) {
                const colorId = colorSelect.value;
                const sizeId = sizeSelect.value;

                if (colorId && sizeId) { // Chỉ check nếu đã chọn cả 2
                    const combination = `${colorId}-${sizeId}`;

                    if (seenCombinations.has(combination)) {
                        hasDuplicate = true;
                        errorMsg += `Biến thể ${i+1}: Màu "${colorSelect.options[colorSelect.selectedIndex].text}" + Kích thước "${sizeSelect.options[sizeSelect.selectedIndex].text}" đã trùng!\n`;
                    } else {
                        seenCombinations.add(combination);
                    }
                }
            }
        }

        if (hasDuplicate) {
            alert('LỖI: Các biến thể sau bị TRÙNG (cùng màu + kích thước):\n\n' + errorMsg + '\n\nVui lòng sửa lại!');
            return false;
        }

        // Check thêm: bắt buộc ít nhất 1 variant
        if (variantItems.length === 0) {
            alert('Vui lòng thêm ít nhất 1 biến thể!');
            return false;
        }

        return true;
    }

    // Gắn event cho form submit
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.querySelector('form[action="admin-add-product"]');
        if (form) {
            form.addEventListener('submit', function(e) {
                if (!validateVariants()) {
                    e.preventDefault(); // Ngăn submit
                }
            });
        }
    });

    // Real-time warning khi user thay đổi color/size (optional - UX tốt hơn)
    function checkDuplicateLive(container) {
        const items = container.querySelectorAll('.variant-item');
        const seen = new Set();

        items.forEach(item => {
            const colorSel = item.querySelector('select[name="colorId[]"]');
            const sizeSel = item.querySelector('select[name="sizeId[]"]');
            if (colorSel && sizeSel && colorSel.value && sizeSel.value) {
                const combo = `${colorSel.value}-${sizeSel.value}`;
                if (seen.has(combo)) {
                    item.style.border = '2px solid red';
                    item.title = 'Trùng màu + kích thước!';
                } else {
                    seen.add(combo);
                    item.style.border = '';
                    item.title = '';
                }
            }
        });
    }

    // Gắn real-time check cho tất cả select color/size
    document.addEventListener('change', function(e) {
        if (e.target.name === 'colorId[]' || e.target.name === 'sizeId[]') {
            checkDuplicateLive(document.getElementById('variant-container'));
        }
    });

    // Hàm addVariant() (đã có từ trước, cải tiến thêm)
    function addVariant() {
        const container = document.getElementById('variant-container');
        const newVariant = document.createElement('div');
        newVariant.className = 'card variant-item';
        newVariant.innerHTML = `
            <h2 class="card-title">Biến thể</h2>
            <span class="remove-variant" onclick="this.parentElement.remove(); checkDuplicateLive(document.getElementById('variant-container'));">
                <i class="fas fa-trash"></i>
            </span>
            <div class="variant-grid">
                <div class="form-group sku-group">
                    <label>Mã SKU:</label>
                    <input type="text" name="variantSKU[]" required placeholder="Ví dụ: BG-PB-01">
                </div>
                <div class="form-group">
                    <label>Màu sắc:</label>
                    <select name="colorId[]" onchange="checkDuplicateLive(document.getElementById('variant-container'));">
                        <option value="">-- Chọn màu --</option>
                        <c:forEach var="c" items="${listColors}">
                            <option value="${c.id}">${c.colorName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Kích thước:</label>
                    <select name="sizeId[]" onchange="checkDuplicateLive(document.getElementById('variant-container'));">
                        <option value="">-- Chọn size --</option>
                        <c:forEach var="sz" items="${listSizes}">
                            <option value="${sz.id}">${sz.size_name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label>Giá biến thể (VND):</label>
                    <input type="number" name="variantPrice[]" required placeholder="0.00" step="0.01" min="0">
                </div>
                <div class="form-group">
                    <label>Số lượng kho:</label>
                    <input type="number" name="variantStock[]" required placeholder="0" min="0">
                </div>
            </div>
        `;
        container.appendChild(newVariant);

        // Check duplicate ngay sau khi add
        setTimeout(() => checkDuplicateLive(container), 100);
    }

    // Cho phép xóa variant đầu tiên
    document.addEventListener('DOMContentLoaded', function() {
        const firstRemove = document.querySelector('.remove-variant');
        if (firstRemove) firstRemove.style.display = 'block';
    });
</script>
    </body>
</html>