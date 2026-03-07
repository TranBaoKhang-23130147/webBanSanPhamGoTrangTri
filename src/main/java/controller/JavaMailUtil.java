package controller; // hoặc util / service tùy bạn đặt

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class JavaMailUtil {

    private static final String FROM_EMAIL = "23130165@st.hcmuaf.edu.vn";

    private static final String APP_PASSWORD = "frko knur iyym zetn";

    public static boolean sendEmail(String toEmail, String subject, String body) {

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);

            message.setContent(body, "text/html; charset=UTF-8");

            Transport.send(message);

            System.out.println("Gửi email thành công tới: " + toEmail);
            return true;

        } catch (Exception e) {
            System.err.println("Lỗi gửi email");
            e.printStackTrace();
            return false;
        }
    }
}
