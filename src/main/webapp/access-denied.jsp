<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Truy cập bị từ chối</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        body { font-family: sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; background: #f4f7f6; margin: 0; }
        .card { background: white; padding: 40px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); text-align: center; max-width: 400px; }
        .icon { font-size: 60px; color: #e74c3c; margin-bottom: 20px; }
        h1 { color: #2c3e50; font-size: 24px; }
        p { color: #7f8c8d; line-height: 1.6; }
        .btn { display: inline-block; margin-top: 25px; padding: 12px 25px; background: #3498db; color: white; text-decoration: none; border-radius: 6px; transition: 0.3s; }
        .btn:hover { background: #2980b9; }
    </style>
</head>
<body>
<div class="card">
    <div class="icon"><i class="fas fa-user-lock"></i></div>
    <h1>Dừng lại!</h1>
    <p>Bạn không có quyền quản trị để thực hiện hành động này. Vui lòng kiểm tra tài khoản tại đây.</p>
    <a href="${pageContext.request.contextPath}/AdminProfileServlet" class="btn">Tiếp tục</a>
</div>
</body>
</html>