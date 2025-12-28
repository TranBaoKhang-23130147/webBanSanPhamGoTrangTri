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

    /**
     * Phương thức doGet: Xử lý khi Admin truy cập vào trang quản lý khách hàng.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Khởi tạo DAO để làm việc với Database
        UserDao dao = new UserDao();

        // 2. Lấy danh sách tất cả người dùng có role là 'User'
        List<User> list = dao.getAllCustomers();

        // 3. Đẩy danh sách khách hàng vào request attribute để JSP có thể lấy ra dùng
        request.setAttribute("listUsers", list);

        // 4. Chuyển hướng (Forward) dữ liệu sang trang JSP để hiển thị giao diện
        request.getRequestDispatcher("/admin_customer.jsp").forward(request, response);
    }

    /**
     * Phương thức doPost: Xử lý các yêu cầu gửi từ form (nếu có).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Nếu bạn muốn thực hiện tìm kiếm hoặc lọc khách hàng qua form, bạn sẽ code ở đây.
        // Hiện tại chúng ta chỉ cần hiển thị danh sách nên để trống hoặc gọi lại doGet.
        doGet(request, response);
    }
}