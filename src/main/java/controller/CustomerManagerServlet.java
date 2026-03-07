package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CustomerManagerServlet", value = "/admin/customers")
public class CustomerManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserDao dao = new UserDao();

        List<User> list = dao.getAllCustomers();

        request.setAttribute("listUsers", list);

        request.getRequestDispatcher("/admin_customer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}