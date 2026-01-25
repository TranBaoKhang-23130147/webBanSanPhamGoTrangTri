<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
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
                <a href="${pageContext.request.contextPath}/AdminProductTypeServlet">Loại sản phẩm</a>
            </li>
            <li class="${activePage == 'category' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/AdminCountProductCategoryServlet"> Danh mục</a>
            </li>

            <li class="${activePage == 'source' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/AdminCountProductSourceServlet"> Nhà cung cấp</a>
            </li>
            <li class="${activePage == ' ' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin-orders">don hang </a>
            </li>
            <li><a href="${pageContext.request.contextPath}/admin/customers"> Khách hàng</a></li>
            <li><a href="${pageContext.request.contextPath}/admin-management"> Quan tri vien</a></li>
            <li><a href="${pageContext.request.contextPath}/AdminProfileServlet"> Hồ sơ</a></li>

            <li class="${activePage == 'setting' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/UpdateSettingServlet"> Cài đặt</a>
            </li>
        </ul>
    </nav>
</aside>