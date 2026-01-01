package controller;

import dao.AddressDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.User;

import java.io.IOException;

@WebServlet(name = "UpdateAdminAddressServlet", value = "/UpdateAdminAddressServlet")
public class UpdateAdminAddressServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("LOGGED_USER");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Address addr = new Address();
        addr.setUserId(user.getId());
        addr.setName(req.getParameter("name"));
        addr.setPhone(req.getParameter("phone"));
        addr.setDetail(req.getParameter("detail"));
        addr.setCommune(req.getParameter("commune"));
        addr.setDistrict(req.getParameter("district"));
        addr.setProvince(req.getParameter("province"));

        AddressDao dao = new AddressDao();
        dao.saveOrUpdate(addr);

        // cập nhật lại user trong session
        user.setAddress(addr);
        req.getSession().setAttribute("LOGGED_USER", user);

        resp.sendRedirect("profile.jsp");
    }
}