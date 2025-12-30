package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "ProductFilterServlet", value = "/ProductFilterServlet")
public class ProductFilterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] types = request.getParameterValues("type");
        String[] prices = request.getParameterValues("price");
        String[] ratings = request.getParameterValues("rating");

        ProductDao dao = new ProductDao();

        List<Product> listP;

        // 🔥 NẾU CHƯA LỌC → LOAD TẤT CẢ
        if (types == null && prices == null && ratings == null) {
            listP = dao.getAllProducts();
        } else {
            listP = dao.filterProducts(types, prices, ratings);
        }

        request.setAttribute("listP", listP);
        request.getRequestDispatcher("product_all_user.jsp")
                .forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}