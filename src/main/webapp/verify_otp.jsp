<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 12/27/2025
  Time: 12:33 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Xác nhận OTP</title>
</head>
<body>

<form action="<%=request.getContextPath()%>/VerifyOtpServlet" method="post">
    <h2>Xác nhận OTP</h2>
    <input type="text" name="otp" placeholder="Nhập mã OTP" required>
    <button>Xác nhận</button>
</form>

<% String err = (String) request.getAttribute("ERROR"); %>
<% if (err != null) { %>
<p style="color:red"><%= err %></p>
<% } %>

</body>
</html>
