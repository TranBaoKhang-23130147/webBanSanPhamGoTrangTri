<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<aside class="sidebar">
    <nav class="sidebar-nav">
        <ul>
            <li><a href="admin_homepage.html">Tổng quan</a></li>
            <li><a href="#"> Sản phẩm</a></li>
            <li class="${activePage == 'productType' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/product-type-manager">Loại sản phẩm</a>
            </li>
            <li class="${activePage == 'category' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/category-manager"> Danh mục</a>
            </li>
            <li><a href="admin_order.html"> Đơn hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/customers"> Khách hàng</a></li>
            <li><a href="admin_profile.html"> Hồ sơ</a></li>
            <li><a href="admin_setting.html"> Cài đặt</a></li>
        </ul>
    </nav>
</aside>