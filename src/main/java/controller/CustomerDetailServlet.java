package controller;

import dao.AddressDao;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/customer-detail")
public class CustomerDetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null) {
            response.sendRedirect("customers");
            return;
        }

        int userId = Integer.parseInt(idParam);

        UserDao userDao = new UserDao();
        AddressDao addressDao = new AddressDao();

        User customer = userDao.getById(userId);
        List<Address> addresses = addressDao.getByUserId(userId);

        if (customer != null) {

            request.setAttribute("customer", customer);
            request.setAttribute("addresses", addresses);

            RequestDispatcher rd = request.getRequestDispatcher("/admin_customer_detail.jsp");
            rd.forward(request, response);

        } else {

            response.sendRedirect("customers");

        }
    }
}