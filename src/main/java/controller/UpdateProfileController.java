package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import dao.UserDao;
import java.io.IOException;
import model.User;



@WebServlet(name = "UpdateProfileController", value = "/UpdateProfileController")
public class UpdateProfileController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // --- PHẦN CODE LẤY ĐƠN HÀNG Ở ĐÂY ---
        dao.OrderDao orderDao = new dao.OrderDao();
        java.util.List<model.Order> listOrders = orderDao.getOrdersByUserId(user.getId());

        // Tính toán sơ bộ cho phần Summary (Số lượng đơn và tổng tiền)
        double totalSpent = 0;
        for (model.Order o : listOrders) {
            totalSpent += o.getTotalOrder();
        }

        // Gửi dữ liệu sang JSP
        request.setAttribute("listOrders", listOrders);
        request.setAttribute("totalOrders", listOrders.size());
        request.setAttribute("totalSpent", totalSpent);

        // Chuyển hướng tới trang JSP để hiển thị
        request.getRequestDispatcher("mypage_user.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        // Kiểm tra đúng tên biến session của bạn (LOGGED_USER)
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 1. Lấy dữ liệu từ JSP
            String fullName = request.getParameter("fullName");       // Họ và tên thật
            String displayName = request.getParameter("displayName"); // Tên khác/Biệt danh
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");           // Nam/Nữ
            String birthDateStr = request.getParameter("birthDate"); // Định dạng yyyy-MM-dd

            // 2. Cập nhật vào đối tượng User (RAM)
            user.setUsername(fullName); // Giả sử username của bạn dùng lưu full_name
            user.setDisplayName(displayName);
            user.setPhone(phone);
            user.setGender(gender);

            // Xử lý ngày sinh: Chuyển từ String sang java.sql.Date
            if (birthDateStr != null && !birthDateStr.isEmpty()) {
                user.setBirthDate(java.sql.Date.valueOf(birthDateStr));
            }

            // 3. Gọi DAO cập nhật
            UserDao userDao = new UserDao();
            // Bạn nên dùng hàm updateUserProfile (hàm này có đủ các trường bạn cần)
            boolean isSuccess = userDao.updateUserProfile(user);

            if (isSuccess) {
                session.setAttribute("LOGGED_USER", user);
                request.setAttribute("message", "Cập nhật hồ sơ thành công!");
            } else {
                request.setAttribute("error", "Lỗi: Không thể cập nhật dữ liệu.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi định dạng dữ liệu: " + e.getMessage());
        }

        // 4. Trả về trang cá nhân
        request.getRequestDispatcher("mypage_user.jsp").forward(request, response);
    }

}