package controller;

import dao.ContactSettingsDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.ContactSettings;

import java.io.IOException;

@WebServlet("/send-contact")
public class SendContactServlet extends HttpServlet {

    private ContactSettingsDao settingsDao = new ContactSettingsDao();
    private JavaMailUtil mailUtil = new JavaMailUtil();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String lastName = req.getParameter("lastName");
        String firstName = req.getParameter("firstName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String message = req.getParameter("message");
        if (isEmpty(lastName) || isEmpty(firstName) ||
                isEmpty(email) || isEmpty(phone) || isEmpty(message)) {

            req.setAttribute("error", "Vui lòng nhập đầy đủ thông tin!");
            req.getRequestDispatcher("contact_user.jsp").forward(req, resp);
            return;
        }
        ContactSettings settings = settingsDao.getSettings();
        String adminEmail = settings.getEmail();

        String fullName = lastName + " " + firstName;

        String subject = "Liên hệ từ khách hàng: " + fullName;

        String body =
                "Họ tên: " + fullName + "\n" +
                        "Email: " + email + "\n" +
                        "SĐT: " + phone + "\n\n" +
                        "Nội dung:\n" + message;

        boolean sent = mailUtil.sendEmail(adminEmail, subject, body);

        if (sent) {
            resp.sendRedirect("contact_user?success=true");
        } else {
            req.setAttribute("error", "Gửi email thất bại!");
            req.getRequestDispatcher("contact_user.jsp").forward(req, resp);
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.sendRedirect("contact_user");
    }
}