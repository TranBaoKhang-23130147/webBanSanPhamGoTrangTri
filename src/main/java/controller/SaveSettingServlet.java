package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "SaveSettingServlet", value = "/SaveSettingServlet")
public class SaveSettingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String newName = request.getParameter("username");
        String newPhone = request.getParameter("phone");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user != null) {
            UserDao dao = new UserDao();
            boolean isSaved = dao.updateUserInfo(user.getId(), newName, newPhone);

            if (isSaved) {
                user.setUsername(newName);
                user.setPhone(newPhone);
                session.setAttribute("LOGGED_USER", user);
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin_setting.jsp");

    }
}