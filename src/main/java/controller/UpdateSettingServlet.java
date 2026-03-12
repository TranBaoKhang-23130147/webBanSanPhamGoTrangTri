package controller;

import dao.AddressDao;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.User;

import java.io.IOException;
import java.sql.Date;

@WebServlet(name = "UpdateSettingServlet", value = "/UpdateSettingServlet")
public class UpdateSettingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("LOGGED_USER");

        if (sessionUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserDao dao = new UserDao();
        User freshUser = dao.getById(sessionUser.getId());

        session.setAttribute("LOGGED_USER", freshUser);

        request.setAttribute("activePage", "setting");
        request.getRequestDispatcher("admin_setting.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("LOGGED_USER");

        if (sessionUser == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String fullName = req.getParameter("full_name");
        String displayName = req.getParameter("display_name");
        String phone = req.getParameter("phone");
        String gender = req.getParameter("gender");
        String birthDateStr = req.getParameter("birth_date");
        String avatarIdStr = req.getParameter("avatar_id");

        Date birthDate = null;
        if (birthDateStr != null && !birthDateStr.isEmpty()) {
            birthDate = Date.valueOf(birthDateStr);
        }

        User u = new User();
        u.setId(sessionUser.getId());
        u.setUsername(fullName);
        u.setDisplayName(displayName);
        u.setPhone(phone);
        u.setGender(gender);
        u.setBirthDate(birthDate);

        if (avatarIdStr != null && !avatarIdStr.isEmpty()) {
            u.setAvatarId(Integer.parseInt(avatarIdStr));
        } else {
            u.setAvatarId(sessionUser.getAvatarId());
        }

        UserDao userDao = new UserDao();
        try {
            userDao.updateUserProfile(u);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        String detail = req.getParameter("detail");
        String commune = req.getParameter("commune");
        String district = req.getParameter("district");
        String province = req.getParameter("province");

        if (detail != null || commune != null || district != null || province != null) {
            Address address = new Address();
            address.setUserId(sessionUser.getId());
            address.setDetail(detail);
            address.setCommune(commune);
            address.setDistrict(district);
            address.setProvince(province);

            AddressDao addressDao = new AddressDao();
            addressDao.saveOrUpdate(address);
        }

        User updatedUser = userDao.getById(u.getId());
        session.setAttribute("LOGGED_USER", updatedUser);

        String source = req.getParameter("source");

        if ("admin_profile".equals(source)) {
            resp.sendRedirect(req.getContextPath() + "/AdminProfileServlet");
        } else {
            resp.sendRedirect(req.getContextPath() + "/UpdateSettingServlet");
        }
    }
}