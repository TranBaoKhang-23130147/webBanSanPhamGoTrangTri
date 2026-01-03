package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "ProductFilterServlet", value = "/ProductFilterServlet")
public class ProductFilterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao dao = new ProductDao();

        // 1. Nạp lại dữ liệu Sidebar để không bị mất khi lọc
        request.setAttribute("listType", dao.getAllProductTypes());
        request.setAttribute("listColor", dao.getAllColors());

        // 2. Lấy tham số
        String[] types = request.getParameterValues("type");
        String[] prices = request.getParameterValues("price");
        String[] ratings = request.getParameterValues("rating");
        String colorParam = request.getParameter("color");
        String page = request.getParameter("page");

        // 🔥 Lấy CategoryId trực tiếp từ hidden field
        String categoryIdStr = request.getParameter("categoryId");
        Integer categoryId = (categoryIdStr != null && !categoryIdStr.isEmpty()) ? Integer.parseInt(categoryIdStr) : null;

        // 3. Thực hiện lọc
        List<Product> listP = dao.filterProductsWithColor(types, prices, ratings, categoryId, colorParam);

        // 4. Gửi dữ liệu về trang
        request.setAttribute("listP", listP);
        request.setAttribute("activeCategoryId", categoryId); // Trả lại ID để filter.jsp nhận diện



        // 5. Điều hướng đến trang tương ứng
        String targetJsp = "product_all_user.jsp";
        if ("livingroom".equals(page)) targetJsp = "decorate_livingroom_user.jsp";
        else if ("bedroom".equals(page)) targetJsp = "decorate_bedroom_user.jsp";
        else if ("kitchen".equals(page)) targetJsp = "decorate_kitchen_user.jsp";
        else if ("homeoffice".equals(page)) targetJsp = "decorate_homeoffice_user.jsp";
        else if ("miniitem".equals(page)) targetJsp = "decorate_miniitem_user.jsp";
        else if ("sourvenir".equals(page)) targetJsp = "sourvenirs_user.jsp";

        request.getRequestDispatcher(targetJsp).forward(request, response);
    }}