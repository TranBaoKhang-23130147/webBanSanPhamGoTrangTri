package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dao.UserDao;
import java.io.IOException;
import model.User;

@WebServlet(name = "AdminUpdateCustomerController", value = "/AdminUpdateCustomerController")
public class AdminUpdateCustomerController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 1. Kiểm tra quyền Admin (Tùy chọn: Bạn nên check session role admin ở đây)
        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("LOGGED_USER");
        if (admin == null) { // Thêm điều kiện check role Admin của bạn vào đây
            response.sendRedirect("login.jsp");
            return;
        }

        UserDao userDao = new UserDao();
        // Lấy ID khách hàng cần sửa từ input hidden
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.isEmpty()) {
            response.sendRedirect("customers"); // Nếu không có ID thì quay về danh sách
            return;
        }
        int customerId = Integer.parseInt(userIdStr);

        try {
            // 2. Lấy dữ liệu từ form modal
            String fullName     = request.getParameter("fullName");
            String displayName  = request.getParameter("displayName");
            String phone        = request.getParameter("phone");
            String gender       = request.getParameter("gender");
            String birthDateStr = request.getParameter("birthDate");
            String avatarUrlRaw = request.getParameter("avatar_id");

            // 3. Xử lý chuẩn hóa URL ảnh
            String relativeAvatarUrl = null;
            if (avatarUrlRaw != null && !avatarUrlRaw.trim().isEmpty()) {
                relativeAvatarUrl = avatarUrlRaw.trim();
                String contextPath = request.getContextPath();
                if (relativeAvatarUrl.startsWith(contextPath)) {
                    relativeAvatarUrl = relativeAvatarUrl.substring(contextPath.length());
                }
            }

            // 4. Tạo đối tượng cập nhật khách hàng
            User customerToUpdate = new User();
            customerToUpdate.setId(customerId);
            customerToUpdate.setUsername(fullName);
            customerToUpdate.setDisplayName(displayName);
            customerToUpdate.setPhone(phone);
            customerToUpdate.setGender(gender);

            if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
                customerToUpdate.setBirthDate(java.sql.Date.valueOf(birthDateStr));
            }

            // Xử lý avatar_id
            if (relativeAvatarUrl != null) {
                int realImageId = userDao.getImageIdByUrl(relativeAvatarUrl);
                customerToUpdate.setAvatarId(realImageId);
            } else {
                // Nếu không đổi ảnh, lấy lại ảnh hiện tại của khách đó trong DB
                User currentInfo = userDao.getUserById(customerId);
                customerToUpdate.setAvatarId(currentInfo.getAvatarId());
            }

            // 5. Cập nhật Database
            boolean success = userDao.updateUserProfile(customerToUpdate);

            // Trong file AdminUpdateCustomerController.java
            if (success) {
                // Sửa đường dẫn này cho đúng với mapping của CustomerDetailServlet
                response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + customerId + "&msg=success");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/customer-detail?id=" + customerId + "&msg=error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CustomerDetailController?id=" + customerId + "&msg=exception");
        }
    }
}