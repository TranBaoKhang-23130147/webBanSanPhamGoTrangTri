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

                <!-- ================= COLORS ================= -->

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

                <!-- ================= SIZE ================= -->

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

            <!-- ================= MODAL COLOR ================= -->

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

            <!-- ================= MODAL SIZE ================= -->

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
<style>
    .attribute-wrapper{
        display:flex;
        gap:30px;
    }

    .attribute-card{
        flex:1;
        background:white;
        border-radius:10px;
        padding:20px;
        box-shadow:0 4px 12px rgba(0,0,0,.08);
    }

    .attribute-card h3{
        margin-bottom:15px;
    }

    .attribute-card table{
        width:100%;
        border-collapse:collapse;
    }

    .attribute-card th,td{
        padding:10px;
        border-bottom:1px solid #eee;
    }

    .attribute-card th{
        background:#f8f9fa;
    }

    .btn-add{
        background:#4e73df;
        color:white;
        padding:6px 12px;
        border:none;
        border-radius:5px;
        cursor:pointer;
        margin-bottom:10px;
    }

    .btn-add:hover{
        opacity:.8;
    }

    .action i{
        cursor:pointer;
        margin-right:8px;
    }

    .modal{
        display:none;
        position:fixed;
        top:0;
        left:0;
        width:100%;
        height:100%;
        background:rgba(0,0,0,.5);
        justify-content:center;
        align-items:center;
    }

    .modal-box{
        background:white;
        padding:25px;
        border-radius:10px;
        width:320px;
    }
    .modal-box input{
        width:100%;
        padding:8px;
        margin:10px 0;
    }
    .content-area{
        width:100%;
        padding:30px;
        box-sizing:border-box;
    }

    .attribute-wrapper{
        display:flex;
        gap:40px;
        max-width:1000px;
        margin:auto;
    }

    .attribute-card{
        flex:1;
        background:white;
        border-radius:12px;
        padding:25px;
        box-shadow:0 6px 15px rgba(0,0,0,.08);
    }

    .attribute-card h3{
        margin-bottom:15px;
        font-size:20px;
    }

    .attribute-card table{
        width:100%;
        border-collapse:collapse;
    }

    .attribute-card th,
    .attribute-card td{
        padding:10px;
        border-bottom:1px solid #eee;
        text-align:left;
    }

    .attribute-card th{
        background:#f5f6fa;
    }

    .btn-add{
        background:#4e73df;
        color:white;
        padding:8px 14px;
        border:none;
        border-radius:6px;
        cursor:pointer;
        margin-bottom:12px;
    }

    .btn-add:hover{
        opacity:.85;
    }

    .action i{
        cursor:pointer;
        margin-right:10px;
        color:#444;
    }

    .action i:hover{
        color:#4e73df;
    }

    /* ===== MODAL ===== */

    .modal{
        display:none;
        position:fixed;
        inset:0;
        background:rgba(0,0,0,.45);
        justify-content:center;
        align-items:center;
        z-index:999;
    }
h2{
    margin-left:260px;
}
    .modal-box{
        background:white;
        padding:25px;
        border-radius:12px;
        width:320px;
    }

    .modal-box input{
        width:100%;
        padding:10px;
        margin:10px 0;
        border:1px solid #ddd;
        border-radius:6px;
    }
    .attribute-wrapper{
        display:flex;
        gap:40px;
        max-width:1000px;
        margin-left:380px;   /* đẩy sang phải */
    }
    .attribute-card{
        max-height:500px;
        overflow-y:auto;
    }

</style>
<script>

    const colorModal = document.getElementById("colorModal");
    const sizeModal = document.getElementById("sizeModal");

    const colorId = document.getElementById("colorId");
    const colorName = document.getElementById("colorName");
    const colorCode = document.getElementById("colorCode");

    const sizeId = document.getElementById("sizeId");
    const sizeName = document.getElementById("sizeName");

    function openColor(){
        colorModal.style.display='flex';
        colorId.value='';
        colorName.value='';
        colorCode.value='';
    }

    function editColor(id,name,code){
        openColor();
        colorId.value=id;
        colorName.value=name;
        colorCode.value=code;
    }

    function openSize(){
        sizeModal.style.display='flex';
        sizeId.value='';
        sizeName.value='';
    }

    function editSize(id,name){
        openSize();
        sizeId.value=id;
        sizeName.value=name;
    }

    window.onclick=e=>{
        if(e.target===colorModal) colorModal.style.display='none';
        if(e.target===sizeModal) sizeModal.style.display='none';
    }

    function deleteColor(id){
        Swal.fire({
            title:'Xóa màu?',
            icon:'warning',
            showCancelButton:true
        }).then(r=>{
            if(r.isConfirmed)
                location='admin-attribute?delete=color&id='+id;

        });
    }

    function deleteSize(id){
        Swal.fire({
            title:'Xóa size?',
            icon:'warning',
            showCancelButton:true
        }).then(r=>{
            if(r.isConfirmed)
                location='admin-attribute?delete=size&id='+id;

        });
    }

</script>

</html>