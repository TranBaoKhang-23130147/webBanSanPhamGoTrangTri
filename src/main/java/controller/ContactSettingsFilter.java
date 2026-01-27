package  controller;

import dao.ContactSettingsDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import model.ContactSettings;

import java.io.IOException;

@WebFilter("/*")
public class ContactSettingsFilter implements Filter {

    private ContactSettingsDao dao;

    @Override
    public void init(FilterConfig filterConfig) {
        dao = new ContactSettingsDao();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        // Chỉ áp dụng cho user (tránh admin nếu muốn)
        if (!req.getRequestURI().contains("/admin")) {
            ContactSettings settings = dao.getSettings();
            req.setAttribute("contactSettings", settings);
        }

        chain.doFilter(request, response);
    }
}
