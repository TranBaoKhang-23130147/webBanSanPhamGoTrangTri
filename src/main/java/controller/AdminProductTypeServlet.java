package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.ProductType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminProductTypeServlet", value = "/AdminProductTypeServlet")
public class AdminProductTypeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao productDao = new ProductDao();

        // Danh sách loại SP
        List<ProductType> listPT = productDao.getAllProductTypes();

        // Map<typeId, soLuongSP>
        Map<Integer, Integer> productCountMap = productDao.countProductByType();

        request.setAttribute("listPT", listPT);
        request.setAttribute("productCountMap", productCountMap);
        request.setAttribute("activePage", "productType");
        request.getRequestDispatcher("admin_products_type.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}