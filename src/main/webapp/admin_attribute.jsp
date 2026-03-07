<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - QUẢN LÝ MÀU SẮC VÀ KÍCH THƯỚC</title>
    <link rel="icon" type="image/png"  href="../img/logo.png" >
    <link rel="stylesheet" href="../css/admin_customer_style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user_admin.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_add_products.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_profile_style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin_attribute.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

</head>
<body>
<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>
        <main class="content-area">

            <h2>Quản lý màu sắc và kích thước</h2>

            <div class="attribute-wrapper">
                <div class="attribute-card">

                    <h3>Màu sắc</h3>

                    <button class="btn-add" onclick="openColor()">+ Thêm màu</button>

                    <table>
                        <tr>
                            <th>ID</th>
                            <th>Tên</th>
                            <th>Mã</th>
                            <th></th>
                        </tr>

                        <c:forEach items="${listColors}" var="c">
                            <tr>
                                <td>${c.id}</td>
                                <td>${c.colorName}</td>

                                <td>
<span style="background:${c.colorCode};color:white;padding:4px 10px;border-radius:6px">
        ${c.colorCode}
</span>
                                </td>

                                <td class="action">
                                    <i class="fa fa-edit" onclick="editColor(${c.id},'${c.colorName}','${c.colorCode}')"></i>
                                    <i class="fa fa-trash" onclick="deleteColor(${c.id})"></i>
                                </td>

                            </tr>
                        </c:forEach>

                    </table>

                </div>

                <div class="attribute-card">

                    <h3>Kích thước</h3>

                    <button class="btn-add" onclick="openSize()">+ Thêm size</button>

                    <table>
                        <tr>
                            <th>ID</th>
                            <th>Size</th>
                            <th></th>
                        </tr>

                        <c:forEach items="${listSizes}" var="s">
                            <tr>
                                <td>${s.id}</td>
                                <td>${s.sizeName}</td>

                                <td class="action">
                                    <i class="fa fa-edit" onclick="editSize(${s.id},'${s.sizeName}')"></i>
                                    <i class="fa fa-trash" onclick="deleteSize(${s.id})"></i>
                                </td>
                            </tr>
                        </c:forEach>

                    </table>

                </div>

            </div>

            <div class="modal" id="colorModal">
                <div class="modal-box">

                    <form action="${pageContext.request.contextPath}/admin-attribute" method="post">
                        <input type="hidden" name="type" value="color">


                        <input type="hidden" name="id" id="colorId">

                        <input name="name" id="colorName" placeholder="Tên màu" required>
                        <input name="code" id="colorCode" placeholder="#ffffff" required>

                        <button class="btn-add">Lưu</button>

                    </form>

                </div>
            </div>

            <div class="modal" id="sizeModal">
                <div class="modal-box">

                    <form action="${pageContext.request.contextPath}/admin-attribute" method="post">
                        <input type="hidden" name="type" value="size">


                        <input type="hidden" name="id" id="sizeId">
                        <input name="name" id="sizeName" placeholder="VD: XL" required>

                        <button class="btn-add">Lưu</button>

                    </form>

                </div>
            </div>

        </main>

    </div>
</div>

</body>
<script src="${pageContext.request.contextPath}/js/admin_attribute.js"></script>
</html>