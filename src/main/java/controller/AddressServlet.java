package controller;

import dao.AddressDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.User;

import java.io.IOException;

@WebServlet("/AddressServlet")
public class AddressServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        int userId = user.getId();
        String action = request.getParameter("action");
        AddressDao dao = new AddressDao();
        if (action.equals("add")) {
            Address a = new Address();
            a.setUserId(userId);
            a.setName(request.getParameter("name"));
            a.setPhone(request.getParameter("phone"));
            a.setDetail(request.getParameter("detail"));
            a.setCommune(request.getParameter("commune"));
            a.setDistrict(request.getParameter("district"));
            a.setProvince(request.getParameter("province"));
            a.setIsDefault(0);

            dao.insert(a);
        }
        if (action.equals("update")) {
            Address a = new Address();
            a.setId(Integer.parseInt(request.getParameter("id")));
            a.setUserId(userId);
            a.setName(request.getParameter("name"));
            a.setPhone(request.getParameter("phone"));
            a.setDetail(request.getParameter("detail"));
            a.setCommune(request.getParameter("commune"));
            a.setDistrict(request.getParameter("district"));
            a.setProvince(request.getParameter("province"));

            dao.update(a);
        }
        if (action.equals("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id, userId);
        }
        if (action.equals("default")) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.setDefault(id, userId);
        }

        response.sendRedirect("MyPageServlet?tab=dia-chi");
    }
}