package controller;

import dao.AddressDao;
import dao.PaymentDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;
import dao.OrderDao;

import java.io.IOException;
import java.util.List;
@WebServlet(name = "MyPageServlet", value = "/MyPageServlet")
public class MyPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("LOGGED_USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "ho-so";
        }

        request.setAttribute("activeTab", tab);

        if ("thanh-toan".equals(tab)) {
            PaymentDao paymentDao = new PaymentDao();
            List<Payment> listPayments = paymentDao.getPaymentsByUserId(user.getId());
            request.setAttribute("listPayments", listPayments);

         } else if ("don-hang".equals(tab)) {
        OrderDao orderDao = new OrderDao();
        List<Order> allOrders = orderDao.getOrdersByUserId(user.getId());

        String statusFilter = request.getParameter("status");
        List<Order> displayList = new java.util.ArrayList<>();

        int countQualifiedOrders = 0;
        double totalSpent = 0;

        for (Order o : allOrders) {
            o.setDetails(orderDao.getDetailsByOrderId(o.getId()));

            if ("Đã giao".equals(o.getStatus()) && "Đã thanh toán".equals(o.getPaymentStatus())) {
                countQualifiedOrders++;
                totalSpent += o.getTotalOrder();
            }

            if (statusFilter == null || statusFilter.isEmpty() || o.getStatus().equals(statusFilter)) {
                displayList.add(o);
            }
        }

        request.setAttribute("listO", displayList);
        request.setAttribute("countOrder", countQualifiedOrders);
        request.setAttribute("totalSpent", totalSpent);
        request.setAttribute("allOrders", allOrders);
    }
    else if ("dia-chi".equals(tab)) {
            AddressDao dao = new AddressDao();
            List<Address> addresses = dao.getAddressesByUserId(user.getId());
            request.setAttribute("addresses", addresses);
        }

        request.getRequestDispatcher("/mypage_user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String orderIdStr = request.getParameter("orderId");
        OrderDao orderDao = new OrderDao();

        if (orderIdStr != null) {
            try {
                int orderId = Integer.parseInt(orderIdStr);

                if ("cancelOrder".equals(action)) {
                    orderDao.cancelOrReturnOrder(orderId, "Đã hủy");
                } else if ("returnOrder".equals(action)) {
                    orderDao.cancelOrReturnOrder(orderId, "Hoàn hàng");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("MyPageServlet?tab=don-hang");
    }
}