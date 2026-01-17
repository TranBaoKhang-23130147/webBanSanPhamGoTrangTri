<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<aside class="sidebar">
    <nav class="sidebar-nav">
        <ul>

            <li class="${activePage == '#' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/AdminOverviewOrderServlet">Tổng quan</a>
            </li>

            <li class="${activePage == 'products' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/products">sản phẩm</a>
            </li>
            <li class="${activePage == 'productType' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/product-type-manager">Loại sản phẩm</a>
            </li>
            <li class="${activePage == 'category' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/category-manager"> Danh mục</a>
            </li>

            <li class="${activePage == 'source' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/source-manager"> Nhà cung cấp</a>
            </li>
            <li><a href="admin_order.html"> Đơn hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/customers"> Khách hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin-management"> Quan tri vien</a></li>
            <li><a href="${pageContext.request.contextPath}/AdminProfileServlet"> Hồ sơ</a></li>

            <li class="${activePage == 'setting' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/UpdateSettingServlet"> Cài đặt</a>
            </li>
        </ul>
    </nav>
</aside>