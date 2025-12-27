package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dao.UserDao;
import java.io.IOException;
import model.User;



@WebServlet(name = "UpdateProfileController", value = "/UpdateProfileController")
public class UpdateProfileController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 1. Lấy dữ liệu từ các thẻ <input name="..."> trong JSP
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        // 2. Cập nhật dữ liệu vào đối tượng user hiện tại (trong bộ nhớ RAM)
        user.setFullname(fullName);
        user.setPhone(phone);
        user.setEmail(email);

        // 3. Gọi DAO để lưu vào Database
        UserDao userDao = new UserDao();
        boolean isSuccess = userDao.updateUser(user);

        if (isSuccess) {
            // Cập nhật lại session để các trang khác thấy thay đổi
            session.setAttribute("LOGGED_USER", user);
            request.setAttribute("message", "Cập nhật hồ sơ thành công!");
        } else {
            request.setAttribute("error", "Lỗi: Không thể cập nhật dữ liệu vào database.");
        }

        // 4. Trả về trang cũ
        request.getRequestDispatcher("mypage.jsp").forward(request, response);
    }
}