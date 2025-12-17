package controller;

import dao.CategoryDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Category;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryServlet", urlPatterns = {"/category-manager", "/add-category"})
public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("search");
        CategoryDao dao = new CategoryDao();
        List<Category> list;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Nếu có từ khóa, gọi hàm search
            list = dao.searchCategoryByName(keyword);
        } else {
            // Nếu không có, hiển thị tất cả
            list = dao.getAllCategory();
        }

        request.setAttribute("listC", list);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("admin_category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("categoryName");

        CategoryDao dao = new CategoryDao();
        if (dao.insertCategory(name)) {
            // Dùng Session để lưu thông báo (tránh mất khi redirect)
            request.getSession().setAttribute("msg", "Đã thêm danh mục: " + name);
        }

        // Sau khi thêm xong, quay về link hiển thị danh sách
        response.sendRedirect("category-manager");
    }
}