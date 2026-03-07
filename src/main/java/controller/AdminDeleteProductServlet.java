package controller;

import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/admin/delete-product")
public class AdminDeleteProductServlet extends HttpServlet {

    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String idRaw = request.getParameter("id");
        if (idRaw == null || idRaw.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"Thiếu ID sản phẩm!\"}");
            out.flush();
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(idRaw.trim());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\": false, \"message\": \"ID sản phẩm không hợp lệ!\"}");
            out.flush();
            return;
        }

        try {
            boolean success = productDao.deleteFullProduct(productId);

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print("{\"success\": true, \"message\": \"Xóa sản phẩm thành công!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"success\": false, \"message\": \"Xóa thất bại! Sản phẩm không tồn tại hoặc đã bị xóa trước đó.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"success\": false, \"message\": \"Lỗi hệ thống khi xóa sản phẩm: " + e.getMessage() + "\"}");
        }

        out.flush();
        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        response.getWriter().print("{\"success\": false, \"message\": \"Phương thức GET không được hỗ trợ cho xóa sản phẩm. Vui lòng dùng POST.\"}");
    }
}