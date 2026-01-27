<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<form action="ProductFilterServlet" method="get">
    <input type="hidden" name="cid" value="${param.cid}">
    <input type="hidden" name="returnPage"
           value="${pageContext.request.servletPath}">
    <aside class="filter-sidebar">
        <h3>Bộ lọc</h3>

        <div class="filter-group">
            <h4>Loại</h4>
            <c:forEach items="${listType}" var="t">
                <label>
                    <input type="checkbox" name="type" value="${t.id}"
                    <c:forEach items="${paramValues.type}" var="selectedId">
                        ${selectedId == t.id ? 'checked' : ''}
                    </c:forEach>
                    > ${t.productTypeName} </label>
            </c:forEach>
        </div>

        <div class="filter-group">
            <h4>Màu sắc</h4>
            <select name="color" class="filter-select">
                <option value="">-- Chọn màu --</option>
                <c:forEach items="${listColor}" var="c">
                    <option value="${c.id}" ${param.color == c.id ? 'selected' : ''}>
                            ${c.colorName}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="filter-group">
            <h4>Giá tiền</h4>
<%--            <label><input type="checkbox" name="price" value="1"> Dưới 1 triệu</label>--%>
<%--            <label><input type="checkbox" name="price" value="2"> 1 - 3 triệu</label>--%>
<%--            <label><input type="checkbox" name="price" value="3"> 3 - 5 triệu</label>--%>
<%--            <label><input type="checkbox" name="price" value="4"> 5 - 10 triệu</label>--%>
<%--            <label><input type="checkbox" name="price" value="5"> Trên 10 triệu</label>--%>
            <c:set var="prices" value="${fn:join(paramValues.price, ',')}" />

            <label>
                <input type="checkbox" name="price" value="1"
                       <c:if test="${fn:contains(prices, '1')}">checked</c:if>>
                Dưới 1 triệu
            </label>

            <label>
                <input type="checkbox" name="price" value="2"
                       <c:if test="${fn:contains(prices, '2')}">checked</c:if>>
                1 - 3 triệu
            </label>

            <label>
                <input type="checkbox" name="price" value="3"
                       <c:if test="${fn:contains(prices, '3')}">checked</c:if>>
                3 - 5 triệu
            </label>

            <label>
                <input type="checkbox" name="price" value="4"
                       <c:if test="${fn:contains(prices, '4')}">checked</c:if>>
                5 - 10 triệu
            </label>

            <label>
                <input type="checkbox" name="price" value="5"
                       <c:if test="${fn:contains(prices, '5')}">checked</c:if>>
                Trên 10 triệu
            </label>

        </div>

        <div class="filter-group">
            <h4>Đánh giá</h4>
            <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

            <c:set var="ratings" value="${fn:join(paramValues.rating, ',')}" />

            <label>
                <input type="checkbox" name="rating" value="5"
                       <c:if test="${fn:contains(ratings, '5')}">checked</c:if>>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
            </label>

            <label>
                <input type="checkbox" name="rating" value="4"
                       <c:if test="${fn:contains(ratings, '4')}">checked</c:if>>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
            </label>

            <label>
                <input type="checkbox" name="rating" value="3"
                       <c:if test="${fn:contains(ratings, '3')}">checked</c:if>>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
            </label>

            <label>
                <input type="checkbox" name="rating" value="2"
                       <c:if test="${fn:contains(ratings, '2')}">checked</c:if>>
                <i class="ri-star-s-fill rating"></i>
                <i class="ri-star-s-fill rating"></i>
            </label>

            <label>
                <input type="checkbox" name="rating" value="1"
                       <c:if test="${fn:contains(ratings, '1')}">checked</c:if>>
                <i class="ri-star-s-fill rating"></i>
            </label>

        </div>

        <br><br>
        <button type="submit">LỌC SẢN PHẨM</button>
        <style>
            aside.filter-sidebar button[type="submit"] {
                width: 100%;
                padding: 12px;
                background-color: #8b5e3c !important;
                color: #ffffff !important;
                border: none;
                border-radius: 5px;
                font-weight: bold;
                text-transform: uppercase;
                cursor: pointer;
                transition: background 0.3s ease;
            }
            aside.filter-sidebar button[type="submit"]:hover {
                background-color: #333333 !important;
            }
        </style>
    </aside>
</form>