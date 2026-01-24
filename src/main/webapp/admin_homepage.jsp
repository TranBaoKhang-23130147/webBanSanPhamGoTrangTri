<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
    OrderDao orderDao = new OrderDao();
    double totalRevenue = orderDao.getTotalRevenue();

    dao.ProductDao productDao = new dao.ProductDao();
    List<model.Product> topProducts = productDao.getTop8BestSellers();
    request.setAttribute("topProducts", topProducts);
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
            <h2 class="page-title">Tổng quan</h2>

            <div class="kpi-row-modern">
                <div class="kpi-card-modern">
                    <div class="kpi-label-modern">Tổng Doanh Thu </div>
                    <div class="kpi-value-modern">
                        <fmt:formatNumber value="<%= totalRevenue %>" pattern="#,###"/>
                        <span class="unit">VND</span></div>
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
                    <div class="kpi-label-modern">Khách Hàng Mới </div>
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
                            <th>Đánh Giá</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${topProducts}" var="p" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td>
                                    <div style="display: flex; align-items: center; gap: 10px;">
                                        <img src="${p.imageUrl}" alt="${p.nameProduct}" style="width: 40px; height: 40px; border-radius: 4px; object-fit: cover;">
                                            ${p.nameProduct}
                                    </div>
                                </td>
                                <td>${p.price} VND</td>
                                <td>
                                    <fmt:formatNumber value="${p.averageRating}" pattern="#.#"/>
                                    <i class="fas fa-star text-yellow"></i>
                                </td>
                            </tr>
                        </c:forEach>

                        <%-- Hiển thị thông báo nếu không có sản phẩm nào --%>
                        <c:if test="${empty topProducts}">
                            <tr>
                                <td colspan="4" style="text-align: center;">Chưa có dữ liệu bán hàng</td>
                            </tr>
                        </c:if>
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
