package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dao.UserDao;
import model.User;

import java.io.IOException;

@WebServlet("/AdminUpdateCustomerController")
public class AdminUpdateCustomerController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("LOGGED_USER");

        if (admin == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.isEmpty()) {
            response.sendRedirect("customers");
            return;
        }

        int customerId = Integer.parseInt(userIdStr);

        try {

            UserDao dao = new UserDao();

            User user = new User();
            user.setId(customerId);
            user.setUsername(request.getParameter("fullName"));
            user.setDisplayName(request.getParameter("displayName"));
            user.setPhone(request.getParameter("phone"));
            user.setGender(request.getParameter("gender"));

            String birthDate = request.getParameter("birthDate");
            if (birthDate != null && !birthDate.isEmpty()) {
                user.setBirthDate(java.sql.Date.valueOf(birthDate));
            }

            String avatarUrl = request.getParameter("avatar_id");

            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                int imageId = dao.getImageIdByUrl(avatarUrl);
                user.setAvatarId(imageId);
            } else {
                User oldUser = dao.getUserById(customerId);
                user.setAvatarId(oldUser.getAvatarId());
            }

            boolean success = dao.updateUserProfile(user);

            if (success) {
                response.sendRedirect("admin/customer-detail?id=" + customerId + "&msg=success");
            } else {
                response.sendRedirect("admin/customer-detail?id=" + customerId + "&msg=error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin/customer-detail?id=" + customerId + "&msg=exception");
        }
    }
}