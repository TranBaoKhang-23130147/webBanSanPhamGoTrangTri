package controller;

import dao.ProductTypeDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.ProductType;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/product-type-manager", "/add-product-type", "/delete-product-type"})
public class ProductTypeServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getServletPath();
        ProductTypeDao dao = new ProductTypeDao();

        if (action.equals("/delete-product-type")) {

            String idRaw = request.getParameter("id");

            try {

                int id = Integer.parseInt(idRaw);

                if (dao.deleteProductType(id)) {
                    request.getSession().setAttribute("msg", "Đã xóa loại sản phẩm thành công.");
                    request.getSession().setAttribute("msgType", "success");
                } else {
                    request.getSession().setAttribute("msg", "Không thể xóa! Loại này đang có sản phẩm.");
                    request.getSession().setAttribute("msgType", "error");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            response.sendRedirect(request.getContextPath() + "/AdminProductTypeServlet");
            return;
        }

        String keyword = request.getParameter("keyword");

        List<ProductType> list = dao.getAllProductTypesWithTotalInventory(keyword);

        request.setAttribute("listPT", list);

        if (keyword == null) {
            keyword = "";
        }

        request.setAttribute("keyword", keyword);
        request.setAttribute("activePage", "productType");

        request.getRequestDispatcher("admin_products_type.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("productTypeName");

        ProductTypeDao dao = new ProductTypeDao();

        if (dao.insertProductType(name)) {

            request.getSession().setAttribute("msg", "Đã thêm loại sản phẩm: " + name);
            request.getSession().setAttribute("msgType", "success");

        } else {
            request.getSession().setAttribute("msg", "Thêm thất bại!");
            request.getSession().setAttribute("msgType", "error");
        }

        response.sendRedirect(request.getContextPath() + "/AdminProductTypeServlet");
    }
}