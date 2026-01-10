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

        // 1. Kiểm tra bảo mật: Chỉ Admin đã đăng nhập mới được thực hiện xóa
        HttpSession session = request.getSession(false);
        User currentAdmin = (session != null) ? (User) session.getAttribute("LOGGED_USER") : null;

        if (currentAdmin == null || !"Admin".equals(currentAdmin.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Lấy tham số id người cần xóa và type (admin/user) từ URL
        String idRaw = request.getParameter("id");
        String type = request.getParameter("type");

        try {
            int userId = Integer.parseInt(idRaw);

            // Chặn trường hợp Admin tự xóa chính mình
            if (userId == currentAdmin.getId()) {
                session.setAttribute("msg", "Lỗi: Bạn không thể tự xóa tài khoản đang sử dụng!");
                session.setAttribute("msgType", "error");
                response.sendRedirect("admin-management"); // URL trang quản lý admin
                return;
            }

            UserDao dao = new UserDao();

            // 3. Gọi hàm DAO xóa (đã có Transaction xử lý Address & Avatar)
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

        // 4. Điều hướng quay lại trang danh sách phù hợp
        // Lưu ý: Thay đổi các đường dẫn "admin-management" hay "admin-customers"
        // cho khớp với cấu hình Servlet hiển thị danh sách của bạn.
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