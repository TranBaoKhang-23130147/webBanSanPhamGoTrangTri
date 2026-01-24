package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "AdminProfileServlet", value = "/AdminProfileServlet")
public class AdminProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Giả sử Admin ID là 1 (hoặc lấy từ session)
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("LOGGED_USER");

        // 2. Kiểm tra nếu chưa đăng nhập hoặc session hết hạn
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 3. Lấy ID của người dùng hiện tại (Ví dụ trong ảnh DB của bạn là ID = 2)
        int currentId = loggedUser.getId();

        UserDao dao = new UserDao();
        User adminData = dao.getAdminProfile(currentId);

        // 4. Gửi dữ liệu sang JSP
        request.setAttribute("admin", adminData);
        request.getRequestDispatcher("admin_profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}