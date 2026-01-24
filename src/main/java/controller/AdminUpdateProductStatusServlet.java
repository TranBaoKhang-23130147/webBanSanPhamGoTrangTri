package controller;

import dao.ProductDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
@WebServlet("/admin/update-product-status")
public class AdminUpdateProductStatusServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            int status    = Integer.parseInt(request.getParameter("status"));   // ← đổi thành status

            boolean success = productDao.updateProductStatus(productId, status);

            if (success) {
                out.print("{\"success\": true, \"message\": \"Cập nhật trạng thái thành công\"}");
            } else {
                out.print("{\"success\": false, \"message\": \"Không tìm thấy sản phẩm\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"success\": false, \"message\": \"Dữ liệu không hợp lệ (id hoặc status)\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\": false, \"message\": \"Lỗi hệ thống: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
            out.close();
        }
    }
}
