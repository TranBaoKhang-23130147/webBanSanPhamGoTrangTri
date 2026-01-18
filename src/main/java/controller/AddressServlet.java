package controller;

import dao.AddressDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.User;

import java.io.IOException;

@WebServlet(name = "AddressServlet", value = "/AddressServlet")
public class AddressServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("LOGGED_USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = user.getId();
        String action = request.getParameter("action");

        AddressDao dao = new AddressDao();

        if ("add".equals(action)) {
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
        else if ("update".equals(action)) {
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
        else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.delete(id, userId);
        }
        else if ("default".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.setDefault(id, userId);
        }

        response.sendRedirect("MyPageServlet?tab=dia-chi");
    }

}