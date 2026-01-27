package controller; // hoặc util / service tùy bạn đặt

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class JavaMailUtil {

    // 🔴 PHẢI là Gmail đã tạo App Password
    private static final String FROM_EMAIL = "23130165@st.hcmuaf.edu.vn";

    // 🔴 App Password 16 ký tự – KHÔNG CÓ DẤU CÁCH
    private static final String APP_PASSWORD = "frko knur iyym zetn";

    /**
     * Gửi email (HTML)
     *
     * @param toEmail email người nhận
     * @param subject tiêu đề
     * @param body nội dung (HTML hoặc text)
     * @return true nếu gửi thành công
     */
    public static boolean sendEmail(String toEmail, String subject, String body) {

        try {
            // 1️⃣ Cấu hình SMTP Gmail
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // ⚠️ Fix lỗi SMTPTransport.protocolConnect
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            // 2️⃣ Tạo session có xác thực
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            // (Optional) bật debug nếu cần
            // session.setDebug(true);

            // 3️⃣ Tạo email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);

            // ✅ Gửi HTML + UTF-8
            message.setContent(body, "text/html; charset=UTF-8");

            // 4️⃣ Gửi
            Transport.send(message);

            System.out.println("✅ Gửi email thành công tới: " + toEmail);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Lỗi gửi email");
            e.printStackTrace();
            return false;
        }
    }
}
