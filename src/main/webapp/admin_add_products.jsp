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

    </body>
</html>