package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "AdminDeleteCustomerServlet", value = "/AdminDeleteCustomerServlet")
public class AdminDeleteCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User currentAdmin = (session != null) ? (User) session.getAttribute("LOGGED_USER") : null;

        if (currentAdmin == null || !"Admin".equals(currentAdmin.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String idRaw = request.getParameter("id");
        String type = request.getParameter("type");

        try {
            int userId = Integer.parseInt(idRaw);

            if (userId == currentAdmin.getId()) {
                session.setAttribute("msg", "Lỗi: Bạn không thể tự xóa tài khoản đang sử dụng!");
                session.setAttribute("msgType", "error");
                response.sendRedirect("admin-management"); // URL trang quản lý admin
                return;
            }

            UserDao dao = new UserDao();

            boolean success = dao.deleteUser(userId);

            if (success) {
                session.setAttribute("msg", "Đã xóa tài khoản thành công!");
                session.setAttribute("msgType", "success");
            } else {
                session.setAttribute("msg", "Xóa thất bại! Vui lòng thử lại sau.");
                session.setAttribute("msgType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("msg", "Lỗi hệ thống: " + e.getMessage());
            session.setAttribute("msgType", "error");
        }

        if ("admin".equalsIgnoreCase(type)) {
            response.sendRedirect("admin-management");
        } else {
            response.sendRedirect("admin-customers");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}