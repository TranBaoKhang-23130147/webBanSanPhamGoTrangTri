package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "AdminProfileServlet", value = "/AdminProfileServlet")
public class AdminProfileServlet extends HttpServlet {

    /**
     * BƯỚC 1: Xử lý hiển thị trang (Fix lỗi 405 Method Not Allowed)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("LOGGED_USER");

        // Nếu chưa đăng nhập hoặc không phải Admin, đá về trang login
        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Đẩy đối tượng user sang JSP để hiển thị
        request.setAttribute("admin", loggedUser);
        request.getRequestDispatcher("/admin_profile.jsp").forward(request, response);
    }

    /**
     * BƯỚC 2: Xử lý cập nhật thông tin/ảnh đại diện
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("LOGGED_USER");

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        UserDao userDao = new UserDao();
        int currentAdminId = loggedUser.getId();

        try {
            // Lấy URL ảnh từ input (đảm bảo file JSP có input name="adminAvatarUrl")
            String avatarUrlRaw = request.getParameter("adminAvatarUrl");

            String relativeAvatarUrl = null;
            if (avatarUrlRaw != null && !avatarUrlRaw.trim().isEmpty()) {
                relativeAvatarUrl = avatarUrlRaw.trim();
                String contextPath = request.getContextPath();

                // Chuẩn hóa đường dẫn tương đối để lưu vào DB
                if (relativeAvatarUrl.startsWith(contextPath)) {
                    relativeAvatarUrl = relativeAvatarUrl.substring(contextPath.length());
                }
            }

            if (relativeAvatarUrl != null) {
                // Tìm ID ảnh dựa trên URL
                int imageId = userDao.getImageIdByUrl(relativeAvatarUrl);

                // Cập nhật vào DB
                boolean success = userDao.updateUserAvatarId(currentAdminId, imageId);

                if (success) {
                    // Cập nhật lại session để giao diện đổi ảnh ngay lập tức
                    loggedUser.setAvatarId(imageId);
                    loggedUser.setAvatarUrl(relativeAvatarUrl);
                    session.setAttribute("LOGGED_USER", loggedUser);

                    response.sendRedirect("AdminProfileServlet?status=success");
                } else {
                    response.sendRedirect("AdminProfileServlet?status=error");
                }
            } else {
                response.sendRedirect("AdminProfileServlet");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminProfileServlet?status=exception");
        }
    }
}