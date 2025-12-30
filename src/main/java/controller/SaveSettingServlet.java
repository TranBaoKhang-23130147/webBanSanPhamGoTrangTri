package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "SaveSettingServlet", value = "/SaveSettingServlet")
public class SaveSettingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("activePage", "setting"); // Để active menu

        request.getRequestDispatcher("admin_setting.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lấy dữ liệu từ JSP gửi lên
        String newName = request.getParameter("username"); // Lấy theo 'name' của input
        String newPhone = request.getParameter("phone");

        // 2. Lấy User từ Session để có ID
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user != null) {
            UserDao dao = new UserDao();
            // 3. Thực hiện lưu vào Database
            boolean isSaved = dao.updateUserInfo(user.getId(), newName, newPhone);

            if (isSaved) {
                // 4. Cập nhật lại Session để hiển thị ngay lập tức
                user.setUsername(newName);
                user.setPhone(newPhone);
                session.setAttribute("LOGGED_USER", user);
            }
        }
        // 5. Chuyển hướng về lại trang setting
        response.sendRedirect(request.getContextPath() + "/admin_setting.jsp");

    }
}