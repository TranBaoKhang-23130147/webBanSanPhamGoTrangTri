<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<aside class="sidebar">
    <nav class="sidebar-nav">
        <ul>

            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/AdminOverviewOrderServlet">Tổng quan</a>
            </li>

            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/products">sản phẩm</a>
            </li>
            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/AdminProductTypeServlet">Loại sản phẩm</a>
            </li>
                <li class="${activePage == '' ? 'active' : ''}">
                    <a href="${pageContext.request.contextPath}/AdminCountProductCategoryServlet"> Danh mục</a>
                </li>

            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/AdminProductTypeServlet"> Nhà cung cấp</a>
            </li>
            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin-orders">Đơn hàng</a>
            </li>
            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin/customers"> Khách hàng</a>
            </li>
            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/admin-management"> Quản trị viên</a>
            </li>
            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/update-contact-settings"> Liên hệ</a>
            </li>
            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/AdminProfileServlet"> Hồ sơ</a>
            </li>


            <li class="${activePage == '' ? 'active' : ''}">
                <a href="${pageContext.request.contextPath}/UpdateSettingServlet"> Cài đặt</a>
            </li>

        </ul>
    </nav>
</aside>