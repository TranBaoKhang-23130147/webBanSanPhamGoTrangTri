<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - HỒ SƠ</title>
    <link rel="icon" type="image/png"  href="../img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_profile_style.css">



</head>
<body>
<div class="admin-container">


        <%@ include file="admin_header.jsp" %>
        <div class="main-wrapper">
            <%@ include file="admin_sidebar.jsp" %>

        <main class="main-content">
            <div class="profile-header-section">
                <div class="cover-image-container">
                    <button  type="button" onclick="openAdminFinder()" style="position: absolute; bottom: 0; right: 0; cursor: pointer;">
                        <i class="fas fa-camera"></i>Thay đổi ảnh đại diện</i>
                    </button>
                    <img src="https://i.pinimg.com/736x/68/7a/95/687a9516347d14f79b5b3246070fa77c.jpg" alt="Cover Image" class="cover-image">


                </div>

                <div class="profile-info-bar">
                    <div class="profile-avatar-container" style="position: relative; display: inline-block;">
                        <img id="admin-avatar-display"
                             src="${pageContext.request.contextPath}${not empty admin.avatarUrl ? admin.avatarUrl : '/img/logo.png'}"
                             class="profile-avatar" style="width: 150px; height: 150px; border-radius: 50%; object-fit: cover;">


                    </div>

                    <form id="adminAvatarForm" action="AdminProfileServlet" method="POST" style="display: none;">
                        <input type="hidden" name="adminAvatarUrl" id="adminAvatarInput">
                    </form>


                    <div class="user-details">
                        <h1 class="user-name">
                            <c:out value="${admin.username}" default="Admin Name" />
                            <i class="fas fa-check-circle verified-icon"></i>
                        </h1>
                        <p class="user-meta">
                            <i class="fas fa-map-marker-alt"></i>
                            ${not empty admin.address ? admin.address.province : "Việt Nam"} |
                            <i class="fas fa-calendar-alt"></i>
                            Tham gia <fmt:formatDate value="${admin.createAt}" pattern="MMMM 'năm' yyyy" />
                        </p>
                        <p><i class="fas fa-clock"></i> Đăng nhập lúc: <span id="loginTime"></span></p>

                        </span>
                    </div>
                </div>
            </div>

            <div class="profile-content-section">







                <div class="profile-details-wrapper">

                    <div class="profile-details-left">
                        <h3 class="section-title">Hồ Sơ</h3>
                        <div class="info-group">
                            <label>Họ và tên:</label>
                            <p class="info-text">${admin.username}</p>
                        </div>
                        <div class="info-group">  <label>Chức vụ: </label>
                             <p class="info-text">${admin.role}</p></div>
                        <div class="info-group">
                            <label>Điện thoại</label>
                            <p class="info-text">${admin.phone}</p>
                        </div>

                        <div class="info-group">
                            <label>Email</label>
                            <p class="info-text">${admin.email}</p>
                        </div>
                        <div class="info-group">

                            <label>Ngày tạo tài khoản</label>
                            <p class="info-text">
                                <fmt:formatDate value="${admin.createAt}" pattern="dd.MM.yyyy" />
                            </p>

                    </div>
                        <div class="info-group">
                            <label>Địa chỉ</label>
                            <p class="info-text">
                                <c:choose>
                                    <c:when test="${not empty admin.address}">
                                        <%-- Gọi hàm getFullAddress() từ model Address --%>
                                        ${admin.address.fullAddress}
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #999;">Chưa cập nhật địa chỉ</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div> </div>

                    <div class="profile-details-right">
                        <h3 class="section-title">Quyền Hạn Hệ Thống</h3>

                        <div class="role-status-message ${admin.role eq 'Admin' ? 'msg-admin' : 'msg-staff'}">
                            <c:choose>
                                <%-- Sử dụng 'eq' và viết hoa đúng như Database --%>
                                <c:when test="${admin.role eq 'Admin'}">
                                    <i class="fas fa-crown"></i> <strong>Chào Admin!</strong> Bạn có tất cả mọi quyền hạn trong hệ thống.
                                </c:when>
                                <c:otherwise>
                                    <i class="fas fa-info-circle"></i> <strong>Chào Staff!</strong> Bạn chỉ được phép xem và cập nhật trạng thái đơn hàng.
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="permissions-container">
                            <div class="perm-item granted">
                                <i class="fas fa-check-circle"></i>
                                <span>Xem danh sách các mục trong hệ thống</span>
                            </div>

                            <div class="perm-item granted">
                                <i class="fas fa-check-circle"></i>
                                <span>Cập nhật trạng thái đơn hàng</span>
                            </div>

                            <c:choose>
                                <c:when test="${admin.role == 'Admin'}">
                                    <div class="perm-item granted">
                                        <i class="fas fa-check-circle"></i>
                                        <span>Quản lý nhân sự (Thêm/Sửa/Xóa)</span>
                                    </div>
                                    <div class="perm-item granted">
                                        <i class="fas fa-check-circle"></i>
                                        <span>Xem báo cáo & Tổng doanh thu</span>
                                    </div>
                                    <div class="perm-item granted">
                                        <i class="fas fa-check-circle"></i>
                                        <span>Toàn quyền Xóa dữ liệu </span>
                                    </div>
                                </c:when>

                                <c:otherwise>
                                    <div class="perm-item restricted">
                                        <i class="fas fa-lock"></i>
                                        <span>Quản lý nhân sự (Bị giới hạn)</span>
                                    </div>
                                    <div class="perm-item restricted">
                                        <i class="fas fa-lock"></i>
                                        <span>Quản lý sản phẩm & đơn hàng (Bị giới hạn)</span>
                                    </div>
                                    <div class="perm-item restricted">
                                        <i class="fas fa-lock"></i>
                                        <span>Quản lý loại sản phẩm, danh mục, nhà sản xuất (Bị giới hạn)</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>


                    </div>

                    <script>
                        document.getElementById('loginTime').innerText = new Date().toLocaleTimeString();
                    </script>


                </div>
            </div>
        </main>
    </div>
</div>
</body>
<script src="${pageContext.request.contextPath}/ckfinder/ckfinder.js"></script>
<script>
    function openAdminFinder() { // Đổi tên hàm cho khớp với onclick của button
        var finder = new CKFinder();
        finder.basePath = '${pageContext.request.contextPath}/ckfinder/';

        finder.selectActionFunction = function(fileUrl) {
            var contextPath = "${pageContext.request.contextPath}" || "";
            var relativeUrl = fileUrl || "";

            // 1. Xử lý đường dẫn tương đối
            if (contextPath && relativeUrl.startsWith(contextPath)) {
                relativeUrl = relativeUrl.substring(contextPath.length);
            }

            // 2. Cập nhật preview ảnh ngay lập tức
            var preview = document.getElementById('admin-avatar-display');
            if (preview) {
                preview.src = contextPath + relativeUrl;
            }

            // 3. Gán vào input hidden và tự động SUBMIT form
            var input = document.getElementById('adminAvatarInput');
            var form = document.getElementById('adminAvatarForm');
            if (input && form) {
                input.value = relativeUrl;
                form.submit(); // Tự động gửi lên Servlet doPost
            }
        };
        finder.popup();
    }
</script>
</html>