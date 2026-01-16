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

        // Thiết lập tiếng Việt
        request.setCharacterEncoding("UTF-8");

        // 1. Lấy dữ liệu từ các input name trong Form Modal
        String idStr = request.getParameter("id");
        String type = request.getParameter("type");
        String cardNumber = request.getParameter("cardNumber");
        String duration = request.getParameter("duration");

        try {
            int id = Integer.parseInt(idStr);

            // 2. Gọi DAO cập nhật Database
            PaymentDao dao = new PaymentDao();
            boolean success = dao.updatePayment(id, type, cardNumber, duration);

            if(success) {
                // Có thể thêm thông báo thành công vào session nếu muốn
                request.getSession().setAttribute("msg", "Cập nhật thẻ thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. Quay trở lại đúng Tab thanh toán
        response.sendRedirect("MyPageServlet?tab=thanh-toan");
    }
}