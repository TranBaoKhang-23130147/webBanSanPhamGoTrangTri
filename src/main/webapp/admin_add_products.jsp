<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - THÊM SẢN PHẨM</title>
    <link rel="icon" type="image/png" href="img/logo.png">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user_admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_profile_style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_add_products.css">
</head>
<body>
<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="content-wrapper">
            <form action="admin-add-product" method="POST">
                <div class="add-product-container">
                    <c:if test="${param.status == 'success'}">
                        <div class="alert success">
                        <i class="fas fa-check-circle"></i> Thêm sản phẩm thành công!
                        </div>
                    </c:if>
                    <c:if test="${not empty message}">
                <div class="alert error">
                <i class="fas fa-exclamation-triangle"></i> ${message}
                        </div>
                    </c:if>
                    <h1 class="page-title">Thêm sản phẩm mới</h1>
                    <div class="product-form">
                        <div class="form-column-left">
                            <div class="card product-info">
                                <h2 class="card-title">Thông tin cơ bản</h2>
                                <div class="form-group">
                                    <label for="productName">Tên sản phẩm</label>
                                    <input type="text" name="productName" id="productName" required >
                                </div>
                                <div class="form-group">
                                    <label>Giá sản phẩm</label>
                                    <input type="number" name="basePrice" required>
                                </div>
                                <div class="form-group">
                                    <label>Danh mục</label>
                                    <select name="categoryId">
                                        <option value="">Chọn danh mục</option>
                                        <c:forEach var="cat" items="${listCategories}">
                                            <option value="${cat.id}">${cat.categoryName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Loại sản phẩm</label>
                                    <select name="typeId">
                                        <option value="">Chọn loại</option>
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
                                    <textarea name="introduce" rows="3" ></textarea>
                                </div>
                                <div class="form-group">
                                    <label>Đặc điểm nổi bật </label>
                                    <input type="text" name="highlights">
                                </div>
                            </div>
                            <div class="card">
                                <h2 class="card-title">Thông tin chi tiết sản phẩm</h2>

                                <input type="hidden" name="infoId" value="${productInfo.id}">

                                <div class="form-group">
                                    <label for="material">Chất liệu</label>
                                    <input type="text" id="material" name="material"required>
                                </div>

                                <div class="form-group">
                                    <label for="colorInfo">Màu sắc chi tiết</label>
                                    <input type="text" id="colorInfo" name="colorInfo">
                                </div>

                                <div class="form-group">
                                    <label for="sizeInfo">Kích thước tổng quát</label>
                                    <input type="text" id="sizeInfo" name="sizeInfo">
                                </div>

                                <div class="form-group">
                                    <label for="guarantee">Bảo hành</label>
                                    <input type="text" id="guarantee" name="guarantee">
                                </div>

                            </div>
                            <div class="card image-upload">
                                <h2 class="card-title">Hình ảnh</h2>

                                <div class="drop-area" onclick="selectImagesWithCKFinder('imageUrls', 'image-preview')">
                                    <i class="fas fa-box-open"></i>
                                    <p>Click để chọn ảnh</p>
                                    <input type="hidden" id="imageUrls" name="productImages">
                                </div>

                                <div id="image-preview"></div>
                            </div>


                            <div class="form-column-right">
                                <div id="variant-container">
                                    <div class="card variant-item">
                                        <h2 class="card-title">Biến thể</h2>

                                        <span class="remove-variant" onclick="this.parentElement.remove()" style="display:none;">
                <i class="fas fa-trash"></i>
            </span>

                                        <div class="variant-grid">

                                            <div class="form-group sku-group">
                                                <label>Mã SKU:</label>
                                                <input type="text" name="variantSKU[]" required>
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
                                                <input type="number" name="variantPrice[]">
                                            </div>

                                            <div class="form-group">
                                                <label>Số lượng kho:</label>
                                                <input type="number" name="variantStock[]" required>
                                            </div>

                                        </div>
                                    </div>
                                </div>

                                <button type="button" class="btn-link-add" onclick="addVariant()">
                                    <i class="fas fa-plus"></i> Thêm biến thể mới
                                </button>

                                <button type="submit" class="btn-primary btn-save">
                                    LƯU SẢN PHẨM
                                </button>
                            </div>  </div>  </div>
            </form>
        </main>
    </div>
</div>


<script src="${pageContext.request.contextPath}/ckfinder/ckfinder.js"></script>
<script src="${pageContext.request.contextPath}/js/admin_add_product.js"></script>
<script>
    function addVariant() {
        const container = document.getElementById('variant-container');
        const newVariant = document.createElement('div');
        newVariant.className = 'card variant-item';
        newVariant.innerHTML = `
            <h2 class="card-title">Biến thể</h2>
            <span class="remove-variant" onclick="this.parentElement.remove(); checkDuplicateLive();">
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
        container.appendChild(newVariant);
    }
</script>
</body>
</html>