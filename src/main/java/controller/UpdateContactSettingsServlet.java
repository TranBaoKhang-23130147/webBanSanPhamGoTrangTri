package controller;

import dao.ContactSettingsDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ContactSettings;

import java.io.IOException;
@WebServlet("/update-contact-settings")
public class UpdateContactSettingsServlet extends HttpServlet {

    private final ContactSettingsDao settingsDao = new ContactSettingsDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        try {
            ContactSettings settings = new ContactSettings();
            settings.setPhone(req.getParameter("phone"));
            settings.setEmail(req.getParameter("email"));
            settings.setAddress(req.getParameter("address"));
            settings.setFacebookUrl(req.getParameter("facebook_url"));
            settings.setInstagramUrl(req.getParameter("instagram_url"));
            settings.setTwitterUrl(req.getParameter("twitter_url"));
            settings.setGoogleUrl(req.getParameter("google_url"));

            boolean success = settingsDao.updateSettings(settings);

            // ✅ redirect có contextPath
            String redirectUrl = req.getContextPath() + "/admin-contact-settings";

            if (success) {
                resp.sendRedirect(redirectUrl + "?success=true");
            } else {
                resp.sendRedirect(redirectUrl + "?error=true");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(
                    req.getContextPath() + "/admin-contact-settings?error=system"
            );
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/admin-contact-settings");
    }

}