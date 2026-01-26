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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("LOGGED_USER");

        if (loggedUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserDao userDao = new UserDao();
        // 1. Load User từ DB (có LEFT JOIN images) để đảm bảo lấy được URL ảnh mới nhất
        User currentUser = userDao.getUserById(loggedUser.getId());

        if (currentUser == null) {
            currentUser = loggedUser;
        }

        // 2. Lấy danh sách đơn hàng để hiển thị Summary
        dao.OrderDao orderDao = new dao.OrderDao();
        java.util.List<model.Order> listOrders = orderDao.getOrdersByUserId(currentUser.getId());

        double totalSpent = 0;
        if (listOrders != null) {
            for (model.Order o : listOrders) {
                totalSpent += o.getTotalOrder();
            }
            request.setAttribute("listOrders", listOrders);
            request.setAttribute("totalOrders", listOrders.size());
        } else {
            request.setAttribute("totalOrders", 0);
        }

        // 3. Gửi dữ liệu currentUser sang JSP
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("totalSpent", totalSpent);

        request.getRequestDispatcher("mypage_user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("LOGGED_USER");

        if (loggedUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserDao userDao = new UserDao();
        try {
            // 1. Lấy dữ liệu từ form
            String fullName     = request.getParameter("fullName");
            String displayName  = request.getParameter("displayName");
            String phone        = request.getParameter("phone");
            String gender       = request.getParameter("gender");
            String birthDateStr = request.getParameter("birthDate");
            String avatarUrlRaw = request.getParameter("avatar_id"); // Đây là chuỗi URL từ CKFinder

            // 2. Chuẩn hóa Avatar URL (Xóa contextPath nếu có)
            String relativeAvatarUrl = null;
            if (avatarUrlRaw != null && !avatarUrlRaw.trim().isEmpty()) {
                relativeAvatarUrl = avatarUrlRaw.trim();
                String contextPath = request.getContextPath();
                if (relativeAvatarUrl.startsWith(contextPath)) {
                    relativeAvatarUrl = relativeAvatarUrl.substring(contextPath.length());
                }
            }

            // 3. Tạo đối tượng cập nhật
            User userToUpdate = new User();
            userToUpdate.setId(loggedUser.getId());
            userToUpdate.setUsername(fullName); // Giả sử username dùng lưu full_name
            userToUpdate.setDisplayName(displayName);
            userToUpdate.setPhone(phone);
            userToUpdate.setGender(gender);

            if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
                userToUpdate.setBirthDate(java.sql.Date.valueOf(birthDateStr));
            }

            // 4. Xử lý Khóa ngoại avatar_id: Từ URL tìm ra ID trong bảng images
            if (relativeAvatarUrl != null) {
                int realImageId = userDao.getImageIdByUrl(relativeAvatarUrl);
                userToUpdate.setAvatarId(realImageId);
                userToUpdate.setAvatarUrl(relativeAvatarUrl);
            } else {
                // Nếu không chọn ảnh mới, giữ lại ảnh cũ từ session
                userToUpdate.setAvatarId(loggedUser.getAvatarId());
                userToUpdate.setAvatarUrl(loggedUser.getAvatarUrl());
            }

            // 5. Thực hiện Update DB
            boolean success = userDao.updateUserProfile(userToUpdate);

            if (success) {
                // QUAN TRỌNG: Load lại User từ DB (có JOIN) để cập nhật Session
                User updatedUser = userDao.getUserById(loggedUser.getId());
                session.setAttribute("LOGGED_USER", updatedUser);
                request.setAttribute("currentUser", updatedUser);
                request.setAttribute("message", "Cập nhật hồ sơ thành công!");
            } else {
                request.setAttribute("error", "Lỗi: Không thể lưu vào Database.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
        }

        // Trở lại trang doGet để nạp lại dữ liệu đơn hàng và hiển thị
        doGet(request, response);
    }
}