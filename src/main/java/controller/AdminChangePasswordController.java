package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "AdminChangePasswordController", value = "/AdminChangePasswordController")
public class AdminChangePasswordController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy ID khách hàng từ input hidden
        String userIdStr = request.getParameter("userId");
        String password = request.getParameter("newPassword");
        String rePassword = request.getParameter("confirmPassword");

        if (userIdStr == null || userIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin/customers");
            return;
        }

        // 1️⃣ Kiểm tra khớp mật khẩu
        if (!password.equals(rePassword)) {
            response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + userIdStr + "&msg=pass_not_match");
            return;
        }

        // 2️⃣ Validate mật khẩu mạnh (Copy từ logic của bạn)
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!password.matches(regex)) {
            response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + userIdStr + "&msg=pass_weak");
            return;
        }

        // 3️⃣ Cập nhật mật khẩu qua DAO
        UserDao dao = new UserDao();
        // Bạn cần đảm bảo UserDao có hàm updatePasswordById(int id, String newPass)
        boolean success = dao.updatePasswordById(Integer.parseInt(userIdStr), password);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + userIdStr + "&msg=pass_success");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + userIdStr + "&msg=pass_error");
        }
    }
}