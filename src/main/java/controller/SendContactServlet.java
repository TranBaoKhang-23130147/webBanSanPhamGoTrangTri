package controller;

import dao.ContactSettingsDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ContactSettings;

import java.io.IOException;

@WebServlet("/send-contact")
public class SendContactServlet extends HttpServlet {

    private final ContactSettingsDao settingsDao = new ContactSettingsDao();
    private final JavaMailUtil mailUtil = new JavaMailUtil();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        try {
            String lastName  = req.getParameter("lastName");
            String firstName = req.getParameter("firstName");
            String email     = req.getParameter("email");
            String phone     = req.getParameter("phone");
            String message   = req.getParameter("message");

            // Validation
            if (isEmpty(lastName) || isEmpty(firstName) ||
                    isEmpty(email) || isEmpty(phone) || isEmpty(message)) {
                req.setAttribute("error", "Vui lòng điền đầy đủ thông tin liên hệ!");
                req.getRequestDispatcher("/contact_user.jsp").forward(req, resp);
                return;
            }

            // Lấy email admin từ DB
            ContactSettings settings = settingsDao.getSettings();
            if (settings == null || isEmpty(settings.getEmail())) {
                throw new Exception("Hệ thống chưa cấu hình email nhận liên hệ!");
            }
            String adminEmail = settings.getEmail().trim();

            // Nội dung email
            String fullName = (lastName.trim() + " " + firstName.trim()).trim();
            String subject = "Liên hệ mới từ khách hàng: " + fullName;

            StringBuilder body = new StringBuilder();
            body.append("=== THÔNG TIN LIÊN HỆ MỚI ===\n\n");
            body.append("Họ và tên: ").append(fullName).append("\n");
            body.append("Email: ").append(email.trim()).append("\n");
            body.append("SĐT: ").append(phone.trim()).append("\n");
            body.append("Nội dung:\n").append(message.trim()).append("\n\n");
            body.append("Thời gian: ").append(new java.util.Date());

            // Gửi mail
            boolean sent = mailUtil.sendEmail(adminEmail, subject, body.toString());

            if (sent) {
                // Thành công: redirect về trang contact_user (không cần .jsp)
                resp.sendRedirect("contact_user?success=true");
            } else {
                req.setAttribute("error", "Không thể gửi email. Vui lòng thử lại!");
                req.getRequestDispatcher("/contact_user.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("/contact_user.jsp").forward(req, resp);
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect("contact_user");
    }
}