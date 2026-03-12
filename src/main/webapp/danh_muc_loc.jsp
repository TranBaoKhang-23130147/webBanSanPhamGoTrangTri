<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<section class="categories">
    <h2 class="titleProduct">DANH MỤC SẢN PHẨM</h2>
    <div class="category-list">
        <c:forEach items="${listCC}" var="c">
            <div class="category-card"
                 onclick="location.href='CategoryController?cid=${c.id}'"
                 style="cursor:pointer;">

                <i class="fas fa-home"></i>
                <p>${c.categoryName.toUpperCase()}</p>
            </div>
        </c:forEach>
    </div>
</section>

