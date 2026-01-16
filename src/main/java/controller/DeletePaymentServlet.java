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

        // 1. Lấy ID thẻ cần xóa từ URL
        String idStr = request.getParameter("id");

        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);

                // 2. Gọi DAO để thực hiện xóa
                PaymentDao dao = new PaymentDao();
                boolean isDeleted = dao.deletePayment(id);

                if (isDeleted) {
                    // Xóa thành công, có thể thêm message thông báo nếu muốn
                    System.out.println("Xóa thành công thẻ ID: " + id);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // 3. Quay trở lại trang MyPageServlet với tab thanh-toan để thấy kết quả
        response.sendRedirect("MyPageServlet?tab=thanh-toan");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}