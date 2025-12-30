package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "UpdateSettingServlet", value = "/UpdateSettingServlet")
public class UpdateSettingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("activePage", "setting"); // Để active menu

        request.getRequestDispatcher("admin_setting.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lấy dữ liệu từ form
        String fullName = request.getParameter("full_name");
        String phone = request.getParameter("phone");

        // 2. Lấy User hiện tại từ session để biết ID cần cập nhật
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("LOGGED_USER");

        if (currentUser != null) {
            UserDao dao = new UserDao();
            boolean success = dao.updateUserInfo(currentUser.getId(), fullName, phone);

            if (success) {
                // 3. CỰC KỲ QUAN TRỌNG: Cập nhật lại object trong session để hiển thị mới ngay lập tức
                currentUser.setUsername(fullName); // Theo code của bạn setUsername là full_name
                currentUser.setPhone(phone);
                session.setAttribute("LOGGED_USER", currentUser);

                request.setAttribute("MESSAGE", "Cập nhật thành công!");
            } else {
                request.setAttribute("ERROR", "Cập nhật thất bại!");
            }
        }

        // 4. Quay lại trang setting
        request.getRequestDispatcher("/admin_setting.jsp").forward(request, response);
    }
        }


