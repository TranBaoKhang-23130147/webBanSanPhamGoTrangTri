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

            String categoryParam = request.getParameter("category");
            String page = request.getParameter("page");

            ProductDao dao = new ProductDao();
            Integer categoryId = null;
            if (categoryParam != null && !categoryParam.isBlank()) {
                categoryId =  dao.getCategoryIdByName(categoryParam);;
            }

            List<Product> listP;

            if (types == null && prices == null && ratings == null) {
                if (categoryId != null) {
                    listP = dao.getProductsByCategory(categoryId);
                } else {
                    listP = dao.getAllProducts();
                }
            } else {
                listP = dao.filterProducts(types, prices, ratings, categoryId);
            }

            request.setAttribute("listP", listP);

            String targetJsp = "product_all_user.jsp";
            if ("livingroom".equals(page)) {
                targetJsp = "decorate_livingroom_user.jsp";
            }
            if ("bedroom".equals(page)) {
                targetJsp = "decorate_bedroom_user.jsp";
            }
            request.getRequestDispatcher(targetJsp)
                    .forward(request, response);
        }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}