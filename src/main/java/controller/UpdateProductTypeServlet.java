package controller;

import dao.ProductTypeDao; 
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateProductTypeServlet", value = "/update-product-type")
public class UpdateProductTypeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("manage-product-types");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        try {
            String idRaw = request.getParameter("id");
            String name = request.getParameter("productTypeName");

            if (idRaw != null && name != null) {
                int id = Integer.parseInt(idRaw);

                ProductTypeDao dao = new ProductTypeDao();
                boolean success = dao.updateProductType(id, name);

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

        response.sendRedirect("manage-product-types");
    }
}