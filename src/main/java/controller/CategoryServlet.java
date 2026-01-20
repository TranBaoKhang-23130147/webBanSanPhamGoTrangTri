package controller;

import dao.CategoryDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Category;

import java.io.IOException;
import java.util.List;
@WebServlet(name = "CategoryServlet", urlPatterns = {"/category-manager", "/add-category", "/delete-category"})
public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        CategoryDao dao = new CategoryDao();

        if (action.equals("/delete-category")) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (dao.deleteCategory(id)) {
                request.getSession().setAttribute("msg", "Đã xóa danh mục thành công!");
                request.getSession().setAttribute("msgType", "success");
            } else {
                request.getSession().setAttribute("msg", "Không thể xóa! Danh mục này đang chứa sản phẩm.");
                request.getSession().setAttribute("msgType", "error");
            }
            response.sendRedirect("category-manager");
            return;
        }

        // Logic tìm kiếm và hiển thị danh sách (giữ nguyên)
        String keyword = request.getParameter("keyword");

        List<Category> listC = dao.getAllCategoriesWithTotalInventory(keyword);

        request.setAttribute("listC", listC);
        request.setAttribute("keyword", keyword != null ? keyword : "");


        request.setAttribute("listC", listC);
        request.setAttribute("keyword", keyword);
        request.setAttribute("activePage", "category"); // Để active menu
        request.getRequestDispatcher("/admin_category.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("categoryName");
        CategoryDao dao = new CategoryDao();

        if (dao.insertCategory(name)) {
            request.getSession().setAttribute("msg", "Đã thêm danh mục: " + name);
            request.getSession().setAttribute("msgType", "success"); // Quan trọng: Thêm type success
        } else {
            request.getSession().setAttribute("msg", "Thêm danh mục thất bại!");
            request.getSession().setAttribute("msgType", "error");
        }
        response.sendRedirect("category-manager");
    }
}