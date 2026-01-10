package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminManagementServlet", value = "/admin-management")
public class AdminManagementServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Gọi DAO
        UserDao dao = new UserDao();
        List<User> list = dao.getAllAdmins();

        // 2. Đẩy dữ liệu lên request với tên 'listAdmins' (khớp với file JSP)
        request.setAttribute("listAdmins", list);

        // 3. Chuyển hướng sang trang JSP dành riêng cho Admin
        request.getRequestDispatcher("admin_management.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}