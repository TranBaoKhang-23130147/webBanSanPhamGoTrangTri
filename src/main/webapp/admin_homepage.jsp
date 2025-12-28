<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="dao.UserDao" %>
<%@ page import="dao.OrderDao" %>

<%
    // 1. Kiểm tra session
    User user = (User) session.getAttribute("LOGGED_USER");

// 2. Nếu chưa đăng nhập hoặc không phải Admin -> Đuổi về trang login
    if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

// 3. Lấy dữ liệu từ request attributes (được set bởi servlet)
    UserDao userDao = new UserDao();
    int newUsersLast30Days = userDao.countNewUsersLast30Days();

// Lấy orderCount từ request attribute
    Integer orderCountObj = (Integer) request.getAttribute("orderCount");
    int orderCount = 0;

// Nếu không có từ servlet (truy cập trực tiếp JSP), lấy từ database
    if (orderCountObj == null) {
        OrderDao orderDao = new OrderDao();
        try {
            orderCount = orderDao.getOrderCount();
        } catch (Exception e) {
            e.printStackTrace();
            orderCount = 0;
        }
    } else {
        orderCount = orderCountObj;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>HOME DECOR - TỔNG QUAN</title>
    <link rel="icon" type="image/png"  href="img/logo.png" >
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="css/homepage_admin.css">
    <link rel="stylesheet" href="css/admin_profile_style.css">

</head>
<body>

<div class="admin-container">
    <%@ include file="admin_header.jsp" %>
    <div class="main-wrapper">
        <%@ include file="admin_sidebar.jsp" %>


        <main class="content">
            <h2 class="page-title">Tổng quan tháng 11</h2>

            <div class="kpi-row-modern">
                <div class="kpi-card-modern">
                    <div class="kpi-label-modern">Tổng Doanh Thu <span class="trend up">▲ 12.2%</span></div>
                    <div class="kpi-value-modern">720,700,000 <span class="unit">VND</span></div>
                </div>
                <!--                <div class="kpi-card-modern">-->
                <!--                    <div class="kpi-label-modern">Tổng Lượt Truy Cập <span class="trend down">▼ 8.2%</span></div>-->
                <!--                    <div class="kpi-value-modern">320</div>-->
                <!--                </div>-->
                <%
                    System.out.println("Order Count (JSP): " + request.getAttribute("orderCount"));
                %>
                <div class="kpi-card-modern">
                    <div class="kpi-label-modern">Số Lượng Đơn Hàng</div>
                    <div class="kpi-value-modern">

                        <%= orderCount %>
                    </div>
                </div>
                <div class="kpi-card-modern">
                    <div class="kpi-label-modern">Khách Hàng Mới <span class="trend up">▲ 10.0%</span></div>
                    <div class="kpi-value-modern"><%= newUsersLast30Days %></div>
                </div>
            </div>

            <div class="widget-full-width">
                <div class="widget-card">
                    <table class="data-table-modern">
                        <thead>
                        <tr>
                            <th>Top</th>
                            <th>Sản Phẩm</th>
                            <th>Lượt Mua</th>
                            <!--                            <th>Còn Lại</th>-->
                            <!--                            <th>Số Lượt Xem</th>-->
                            <th>Đánh Giá</th>
                            <!--                            <th>Xu Hướng</th>-->
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>1</td>
                            <td>Kệ Treo Tường Gỗ Sồi 3 Tầng</td>
                            <!--                            <td>530</td>-->
                            <!--                            <td>10</td>-->
                            <td>622</td>
                            <td>4.8 <i class="fas fa-star text-yellow"></i></td>
                            <!--                            <td><span class="trend up">▲ 4.5%</span></td>-->
                        </tr>
                        <tr>
                            <td>2</td>
                            <td>Đồng Hồ Treo Tường Gỗ Phong Cách Retro</td>
                            <!--                            <td>330</td>-->
                            <!--                            <td>30</td>-->
                            <td>422</td>
                            <td>4.8 <i class="fas fa-star text-yellow"></i></td>
                            <!--                            <td><span class="trend up">▲ 4.5%</span></td>-->
                        </tr>
                        <tr>
                            <td>3</td>
                            <td>Chậu Cây Để Bàn Gỗ Tần Bì (Set 3)</td>
                            <!--                            <td>230</td>-->
                            <!--                            <td>93</td>-->
                            <td>325</td>
                            <td>4.8 <i class="fas fa-star text-yellow"></i></td>
                            <!--                            <td><span class="trend up">▲ 4.5%</span></td>-->
                        </tr>
                        <tr>
                            <td>4</td>
                            <td>Tượng Gỗ Trang Trí Hình Hươu (Lớn)</td>
                            <!--                            <td>230</td>-->
                            <!--                            <td>40</td>-->
                            <td>322</td>
                            <td>4.8 <i class="fas fa-star text-yellow"></i></td>
                            <!--                            <td><span class="trend up">▲ 4.5%</span></td>-->
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>

<script src="js/homepage_admin.js"></script>
</body>
</html>
