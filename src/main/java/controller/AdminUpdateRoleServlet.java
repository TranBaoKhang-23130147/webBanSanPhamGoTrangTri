package controller;

import dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "AdminUpdateRoleServlet", value = "/AdminUpdateRoleServlet")
public class AdminUpdateRoleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Thiết lập kiểu dữ liệu trả về là JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // 1. Lấy dữ liệu từ Request
            String idStr = request.getParameter("id");
            String newRole = request.getParameter("role");

            if (idStr == null || newRole == null) {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Thiếu dữ liệu\"}");
                return;
            }

            int adminId = Integer.parseInt(idStr);
            UserDao dao = new UserDao();

            // 2. Gọi DAO cập nhật
            boolean success = dao.updateAdminRole(adminId, newRole);

            // 3. Trả về kết quả cho AJAX
            if (success) {
                response.getWriter().write("{\"status\":\"success\"}");
            } else {
                response.getWriter().write("{\"status\":\"error\", \"message\":\"Cập nhật thất bại\"}");
            }

        } catch (Exception e) {
            response.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
    }
}