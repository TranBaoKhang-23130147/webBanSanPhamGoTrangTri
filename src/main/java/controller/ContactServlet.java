package controller;

import dao.ContactDao;
import dao.NotificationDao;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "ContactServlet", value = "/ContactServlet")
public class ContactServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {



        request.getRequestDispatcher("/contact_user.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String content = request.getParameter("content");

        User user = (User) request.getSession().getAttribute("LOGGED_USER");
        Integer userId = (user != null) ? user.getId() : null;

        ContactDao contactDAO = new ContactDao();
        NotificationDao notiDAO = new NotificationDao();

        int contactId = contactDAO.insertContactReturnId(
                userId, fname, lname, email, phone, content
        );


        UserDao userDao = new UserDao();
        Integer adminId = userDao.getFirstAdminId();

        if (contactId > 0 && adminId != null) {
            notiDAO.createContactNotification(
                    adminId,
                    contactId,
                    "Liên hệ mới từ " + fname + " " + lname
            );
        }
        response.sendRedirect(
                request.getContextPath() + "/ContactServlet?success=true"
        );
    }


}