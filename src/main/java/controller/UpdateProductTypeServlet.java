package controller;

import dao.ProductTypeDao; // Đảm bảo bạn đã import đúng package chứa ProductTypeDao
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateProductTypeServlet", value = "/update-product-type")
public class UpdateProductTypeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Thông thường không xử lý Update qua GET, chuyển hướng về trang danh sách
        response.sendRedirect("manage-product-types");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Thiết lập encoding để không bị lỗi tiếng Việt khi sửa tên
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        try {
            // 2. Lấy dữ liệu từ Form (ID và Tên mới)
            String idRaw = request.getParameter("id");
            String name = request.getParameter("productTypeName");

            if (idRaw != null && name != null) {
                int id = Integer.parseInt(idRaw);

                // 3. Gọi DAO để cập nhật vào Database
                ProductTypeDao dao = new ProductTypeDao();
                boolean success = dao.updateProductType(id, name);

                // 4. Thiết lập thông báo hiển thị bằng SweetAlert2 ở JSP
                HttpSession session = request.getSession();
                if (success) {
                    session.setAttribute("msg", "Cập nhật loại sản phẩm thành công!");
                    session.setAttribute("msgType", "success");
                } else {
                    session.setAttribute("msg", "Cập nhật thất bại. Vui lòng thử lại!");
                    session.setAttribute("msgType", "error");
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.getSession().setAttribute("msg", "ID không hợp lệ!");
            request.getSession().setAttribute("msgType", "error");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("msg", "Lỗi hệ thống: " + e.getMessage());
            request.getSession().setAttribute("msgType", "error");
        }

        // 5. Quay lại trang quản lý (Servlet hiển thị danh sách)
        // Lưu ý: Đảm bảo đường dẫn này khớp với Servlet hiển thị listPT của bạn
        response.sendRedirect("manage-product-types");
    }
}