package controller;

import dao.CategoryDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "CategoryServlet", urlPatterns = {"/add-category"})
public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nếu người dùng cố tình truy cập link này bằng trình duyệt, đẩy về trang quản lý
        response.sendRedirect("admin_category.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("categoryName");

        if (name == null || name.trim().isEmpty()) {
            response.getWriter().println("Tên danh mục không được để trống");
            return;
        }

        CategoryDao dao = new CategoryDao();
        if (dao.insertCategory(name.trim())) {
            response.sendRedirect("admin_category.jsp");
        } else {
            response.getWriter().println("Thêm thất bại!");
        }
    }
}