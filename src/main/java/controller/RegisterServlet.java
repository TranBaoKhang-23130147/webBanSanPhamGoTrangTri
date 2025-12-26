package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import utils.EmailUtil;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("re_password");

        UserDao dao = new UserDao();

        /* ================== VALIDATE ================== */

        if (!password.equals(rePassword)) {
            request.setAttribute("ERROR_MESSAGE", "Mật khẩu xác nhận không khớp!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        if (dao.checkEmailExist(email)) {
            request.setAttribute("ERROR_MESSAGE", "Email đã tồn tại!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        if (!password.matches(passwordRegex)) {
            request.setAttribute("ERROR_MESSAGE",
                    "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ in hoa, chữ thường, số và ký tự đặc biệt!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        /* ================== TẠO OTP ================== */
        String otp = String.valueOf((int) (Math.random() * 900000 + 100000));

        try {
            EmailUtil.sendOTP(email, otp);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("ERROR_MESSAGE", "Không gửi được OTP qua email!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        /* ================== LƯU SESSION TẠM ================== */
        HttpSession session = request.getSession();
        session.setAttribute("OTP", otp);
        session.setAttribute("OTP_TIME", System.currentTimeMillis());

        // Lưu user tạm (CHƯA ghi DB)
        User tempUser = new User(username, password, "Active", "User", email, 0);
        session.setAttribute("REG_USER", tempUser);

        /* ================== HIỆN FORM OTP ================== */
        request.setAttribute("SHOW_OTP", true);

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
