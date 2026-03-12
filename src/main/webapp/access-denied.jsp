<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Truy cập bị từ chối</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/access-denied.css">
</head>
<body>
<div class="card"> <div class="icon">
    <i class="fas fa-user-lock"></i>
</div>
    <h1>Dừng lại!</h1>
    <p>Bạn không có quyền để thực hiện hành động này. Vui lòng kiểm tra tài khoản tại đây.</p>
    <a href="${pageContext.request.contextPath}/AdminProfileServlet" class="btn">Tiếp tục</a>
</div>
</body>
