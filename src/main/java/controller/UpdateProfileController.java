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
        // Thêm dòng này để xử lý tiếng Việt từ Form gửi lên
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Lấy dữ liệu người dùng nhập từ form (dựa vào thuộc tính 'name' của input)
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        // 3. Cập nhật thông tin vào đối tượng user hiện tại
        user.setFullname(fullName);
        user.setPhone(phone);
        user.setEmail(email);

        // 4. Gọi DAO để cập nhật vào Database
        UserDao userDao = new UserDao();
        boolean isSuccess = userDao.updateUser(user);

        if (isSuccess) {
            // Cập nhật lại session để JSP hiển thị thông tin mới nhất ngay lập tức
            session.setAttribute("LOGGED_USER", user);
            request.setAttribute("message", "Cập nhật thành công!");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại.");
        }

        // 5. Quay trở lại trang hồ sơ
        request.getRequestDispatcher("mypage.jsp").forward(request, response);
    }
}