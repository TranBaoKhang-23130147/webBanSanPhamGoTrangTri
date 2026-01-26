package controller;

import model.GooglePojo;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import utils.GoogleUtils;

import java.io.IOException;

@WebServlet(name = "LoginGoogleServlet", value = "/google-login")
public class LoginGoogleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accessToken = GoogleUtils.getToken(code);
        GooglePojo googlePojo = GoogleUtils.getUserInfo(accessToken);
        String email = googlePojo.getEmail();

        UserDao dao = new UserDao();
        User user = dao.checkLoginGoogle(email);

        if (user == null) {
            // Lần đầu đăng nhập -> Tạo mới
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(googlePojo.getName());
            newUser.setRole("User");
            newUser.setStatus("Active");
            newUser.setPassword("GOOGLE_" + System.currentTimeMillis());

            if (dao.insertUserFromGoogle(newUser)) {
                user = dao.checkLoginGoogle(email); // Lấy lại để có ID vừa tạo
            }

        }

        // BƯỚC 2: XỬ LÝ SESSION VÀ PHÂN QUYỀN (Dành cho cả User cũ và User vừa mới thêm)
        // Lưu ý: Đoạn này nằm NGOÀI khối if(user == null) ở trên
        if (user != null) {
            if (!"Active".equals(user.getStatus())) {
                request.setAttribute("ERROR_MESSAGE", "Tài khoản bị khóa!");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            // GÁN SESSION NHƯ BÌNH THƯỜNG
            HttpSession session = request.getSession();
            session.setAttribute("LOGGED_USER", user); // Bây giờ tác vụ khác sẽ nhận được session này

            // PHÂN QUYỀN
            if ("Admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin_homepage.jsp");
            } else if ("Staff".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect(request.getContextPath() + "/AdminProfileServlet");
            } else {
                response.sendRedirect(request.getContextPath() + "/HomeServlet");
            }
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}