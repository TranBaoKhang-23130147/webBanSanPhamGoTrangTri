    package controller;

    import dao.UserDao;
    import jakarta.servlet.*;
    import jakarta.servlet.http.*;
    import jakarta.servlet.annotation.*;

    import java.io.IOException;

    @WebServlet(name = "ResetPasswordServlet", value = "/ResetPasswordServlet")
    public class ResetPasswordServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        }
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            HttpSession session = request.getSession();
            String email = (String) session.getAttribute("RESET_EMAIL");

            if (email == null) {
                request.setAttribute("ERROR_MESSAGE", "Phiên đổi mật khẩu đã hết hạn!");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }

            String password = request.getParameter("password");
            String rePassword = request.getParameter("re_password");

            // 1️⃣ Kiểm tra khớp mật khẩu
            if (!password.equals(rePassword)) {
                request.setAttribute("ERROR_MESSAGE", "Mật khẩu không khớp!");
                request.getRequestDispatcher("/login_pass_new.jsp").forward(request, response);
                return;
            }

            // 2️⃣ Validate mật khẩu mạnh
            String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

            if (!password.matches(regex)) {
                request.setAttribute("ERROR_MESSAGE",
                        "Mật khẩu phải có ít nhất 8 ký tự, gồm chữ in hoa, số và ký tự đặc biệt!");
                request.getRequestDispatcher("/login_pass_new.jsp").forward(request, response);
                return;
            }

            // 3️⃣ Update mật khẩu
            UserDao dao = new UserDao();
            boolean success = dao.updatePasswordByEmail(email, password);

            if (!success) {
                request.setAttribute("ERROR_MESSAGE", "Không thể đổi mật khẩu!");
                request.getRequestDispatcher("/login_pass_new.jsp").forward(request, response);
                return;
            }

            // 4️⃣ Xóa session
            session.invalidate();

            request.setAttribute("MESS_SUCCESS", "Đổi mật khẩu thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }