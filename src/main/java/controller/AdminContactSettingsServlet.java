package controller;

import dao.ContactSettingsDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.ContactSettings;

import java.io.IOException;
@WebServlet("/admin-contact-settings")
public class AdminContactSettingsServlet extends HttpServlet {

    private final ContactSettingsDao settingsDao = new ContactSettingsDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ContactSettings settings = settingsDao.getSettings();
        if (settings == null) {
            settings = new ContactSettings();
        }

        req.setAttribute("settings", settings);
        req.getRequestDispatcher("/admin-contact-settings.jsp")
                .forward(req, resp);

    }
}
