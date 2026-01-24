package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;

import java.io.IOException;
@WebFilter(urlPatterns = {"/AdminDeleteCustomerServlet","/admin/delete-product", "/DeleteCustomerServlet","/add-product-type", "/delete-product-type", "/admin-add-product"})
public class AdminSecurityFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User user = (session != null) ? (User) session.getAttribute("LOGGED_USER") : null;

        // Nếu là Admin thì cho đi tiếp (chain.doFilter)
        if (user != null && "Admin".equalsIgnoreCase(user.getRole())) {
            chain.doFilter(request, response);
        } else {
            // Nếu là Staff hoặc khách: Đuổi ra hoặc báo lỗi
            res.sendRedirect(req.getContextPath() + "/access-denied.jsp");
        }
    }

}