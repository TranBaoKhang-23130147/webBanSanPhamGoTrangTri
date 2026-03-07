package controller;

import dao.PaymentDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "EditPaymentServlet", value = "/EditPaymentServlet")
public class EditPaymentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("MyPageServlet?tab=thanh-toan");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String idStr = request.getParameter("id");
        String type = request.getParameter("type");
        String cardNumber = request.getParameter("cardNumber");
        String duration = request.getParameter("duration");

        try {
            int id = Integer.parseInt(idStr);

            PaymentDao dao = new PaymentDao();
            boolean success = dao.updatePayment(id, type, cardNumber, duration);

            if(success) {
                request.getSession().setAttribute("msg", "Cập nhật thẻ thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("MyPageServlet?tab=thanh-toan");
    }
}