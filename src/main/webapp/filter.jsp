<%--<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>--%>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>

<%--<form action="ProductFilterServlet" method="get">--%>
<%--    <input type="hidden" name="categoryId" value="${activeCategoryId}">--%>
<%--    <input type="hidden" name="page" value="${param.page}">--%>
<%--    <aside class="filter-sidebar">--%>
<%--        <h3>Bộ lọc</h3>--%>

<%--        <div class="filter-group">--%>
<%--            <h4>Loại</h4>--%>
<%--            <c:forEach items="${listType}" var="t">--%>
<%--                <label>--%>
<%--                    <input type="checkbox" name="type" value="${t.id}"--%>
<%--                    <c:forEach items="${paramValues.type}" var="selectedId">--%>
<%--                        ${selectedId == t.id ? 'checked' : ''}--%>
<%--                    </c:forEach>--%>
<%--                    > ${t.productTypeName} </label>--%>
<%--            </c:forEach>--%>
<%--        </div>--%>

<%--        <div class="filter-group">--%>
<%--            <h4>Màu sắc</h4>--%>
<%--            <select name="color" class="filter-select">--%>
<%--                <option value="">-- Chọn màu --</option>--%>
<%--                <c:forEach items="${listColor}" var="c">--%>
<%--                    <option value="${c.id}" ${param.color == c.id ? 'selected' : ''}>--%>
<%--                            ${c.colorName}--%>
<%--                    </option>--%>
<%--                </c:forEach>--%>
<%--            </select>--%>
<%--        </div>--%>

<%--        <div class="filter-group">--%>
<%--            <h4>Giá tiền</h4>--%>
<%--            <label><input type="checkbox" name="price" value="1"> Dưới 1 triệu</label>--%>
<%--            <label><input type="checkbox" name="price" value="2"> 1 - 3 triệu</label>--%>
<%--            <label><input type="checkbox" name="price" value="3"> 3 - 5 triệu</label>--%>
<%--            <label><input type="checkbox" name="price" value="4"> 5 - 10 triệu</label>--%>
<%--            <label><input type="checkbox" name="price" value="5"> Trên 10 triệu</label>--%>
<%--        </div>--%>

<%--        <div class="filter-group">--%>
<%--            <h4>Đánh giá</h4>--%>
<%--            <label><input type="checkbox" name="rating" value="5">--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--            </label>--%>
<%--            <label><input type="checkbox" name="rating" value="4">--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--            </label>--%>
<%--            <label><input type="checkbox" name="rating" value="3">--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--            </label>--%>
<%--            <label><input type="checkbox" name="rating" value="2">--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--            </label>--%>
<%--            <label><input type="checkbox" name="rating" value="1">--%>
<%--                <i class="ri-star-s-fill rating"></i>--%>
<%--            </label>--%>
<%--        </div>--%>

<%--        <br><br>--%>
<%--        <!-- 🔥 NÚT LỌC BẮT BUỘC -->--%>
<%--        <button type="submit">LỌC SẢN PHẨM</button>--%>
<%--    </aside>--%>
<%--</form>--%>