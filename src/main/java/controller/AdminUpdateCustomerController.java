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

        HttpSession session = request.getSession();
        User admin = (User) session.getAttribute("LOGGED_USER");
        if (admin == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserDao userDao = new UserDao();
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null || userIdStr.isEmpty()) {
            response.sendRedirect("customers");
            return;
        }
        int customerId = Integer.parseInt(userIdStr);

        try {
            String fullName     = request.getParameter("fullName");
            String displayName  = request.getParameter("displayName");
            String phone        = request.getParameter("phone");
            String gender       = request.getParameter("gender");
            String birthDateStr = request.getParameter("birthDate");
            String avatarUrlRaw = request.getParameter("avatar_id");

            String relativeAvatarUrl = null;
            if (avatarUrlRaw != null && !avatarUrlRaw.trim().isEmpty()) {
                relativeAvatarUrl = avatarUrlRaw.trim();
                String contextPath = request.getContextPath();
                if (relativeAvatarUrl.startsWith(contextPath)) {
                    relativeAvatarUrl = relativeAvatarUrl.substring(contextPath.length());
                }
            }

            User customerToUpdate = new User();
            customerToUpdate.setId(customerId);
            customerToUpdate.setUsername(fullName);
            customerToUpdate.setDisplayName(displayName);
            customerToUpdate.setPhone(phone);
            customerToUpdate.setGender(gender);

            if (birthDateStr != null && !birthDateStr.trim().isEmpty()) {
                customerToUpdate.setBirthDate(java.sql.Date.valueOf(birthDateStr));
            }

            if (relativeAvatarUrl != null) {
                int realImageId = userDao.getImageIdByUrl(relativeAvatarUrl);
                customerToUpdate.setAvatarId(realImageId);
            } else {
                User currentInfo = userDao.getUserById(customerId);
                customerToUpdate.setAvatarId(currentInfo.getAvatarId());
            }

            boolean success = userDao.updateUserProfile(customerToUpdate);

            if (success) {
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