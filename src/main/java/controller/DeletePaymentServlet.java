package controller;

import dao.PaymentDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeletePaymentServlet", value = "/DeletePaymentServlet")
public class DeletePaymentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);

                PaymentDao dao = new PaymentDao();
                boolean isDeleted = dao.deletePayment(id);

                if (isDeleted) {
                    System.out.println("Xóa thành công thẻ ID: " + id);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("MyPageServlet?tab=thanh-toan");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}