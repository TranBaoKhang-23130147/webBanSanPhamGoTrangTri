<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="categories">
    <h2 class="titleProduct">DANH MỤC SẢN PHẨM</h2>
    <div class="category-list">
        <c:forEach items="${listCC}" var="c">
            <%-- Thay đổi: Dùng link trực tiếp hoặc hàm chuyển hướng chung --%>
            <div class="category-card"
                 onclick="location.href='CategoryController?cid=${c.id}'"
                 style="cursor:pointer;">

                <i class="fas fa-home"></i> <%-- Bạn có thể đổi icon tùy ý --%>
                <p>${c.categoryName.toUpperCase()}</p>
                <span>Khám phá ngay</span>
            </div>
        </c:forEach>
    </div>
</section>

<%-- XÓA BỎ ĐOẠN SCRIPT IF-ELSE CŨ ĐI --%>