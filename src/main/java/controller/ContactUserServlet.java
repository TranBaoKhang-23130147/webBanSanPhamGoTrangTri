package controller;

import dao.NotificationDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Notification;

import java.io.IOException;
import java.sql.Timestamp;

@WebServlet("/contact-user")
public class ContactUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String lastName = req.getParameter("lastName");
        String firstName = req.getParameter("firstName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String message = req.getParameter("message");
        String name = lastName + " " + firstName;
        JavaMailUtil mail = new JavaMailUtil();

        String adminEmail = "yourgmail@gmail.com";
        String subject = "Liên hệ từ khách hàng: " + name;
        String body =
                "Tên: " + name + "\n" +
                        "Email: " + email + "\n\n" +
                        "Số điện thoại: " + phone + "\n\n" +
                        "Nội dung:\n" + message;

        boolean sent = mail.sendEmail(adminEmail, subject, body);

        if (sent) {
            Notification noti = new Notification();
            noti.setAdminId(1);
            noti.setType("CONTACT");
            noti.setRelatedId(0);
            noti.setContent("Khách hàng " + name + " vừa gửi liên hệ");
            noti.setRead(false);

            NotificationDao notiDAO = new NotificationDao();
            notiDAO.insert(noti);
        }

        if (sent) {
            resp.sendRedirect(req.getContextPath() + "/contact_user.jsp?success=true");
        } else {
            resp.sendRedirect(req.getContextPath() + "/contact_user.jsp?error=true");
        }
    }

}
