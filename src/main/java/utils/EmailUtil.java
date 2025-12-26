package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {

    public static void sendOTP(String toEmail, String otp) throws Exception {

        final String fromEmail = "23130082@st.hcmuaf.edu.vn";
        final String password = "umcskoycqyflcmsd"; // ❗ KHÔNG DẤU CÁCH

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmail));
        msg.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail)
        );
        msg.setSubject("Mã OTP đăng ký HOME DECOR");
        msg.setText(
                "Mã OTP của bạn là: " + otp +
                        "\nHiệu lực trong 5 phút."
        );

        Transport.send(msg);
    }
}
