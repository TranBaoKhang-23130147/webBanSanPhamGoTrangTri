package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "DeleteCustomerServlet", value = "/DeleteCustomerServlet")
public class DeleteCustomerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String idRaw = request.getParameter("id");
        String type = request.getParameter("type");

        try {
            int userId = Integer.parseInt(idRaw);
            UserDao dao = new UserDao();

            // Thực hiện xóa
            boolean success = dao.deleteUser(userId);

            if (success) {
                session.setAttribute("msg", "Đã xóa khách hàng thành công!");
                session.setAttribute("msgType", "success");
            } else {
                session.setAttribute("msg", "Không thể xóa khách hàng này!");
                session.setAttribute("msgType", "error");
            }
        } catch (Exception e) {
            session.setAttribute("msg", "Lỗi: " + e.getMessage());
            session.setAttribute("msgType", "error");
        }

        // ĐIỀU HƯỚNG QUAN TRỌNG:
        // Quay về đúng URL của CustomerManagerServlet để load lại danh sách mới
        if ("admin".equalsIgnoreCase(type)) {
            response.sendRedirect(request.getContextPath() + "/admin-management");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/customers");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}