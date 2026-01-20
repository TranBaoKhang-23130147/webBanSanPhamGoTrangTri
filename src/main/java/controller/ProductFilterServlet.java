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

        ProductDao dao = new ProductDao();

        // Sidebar
        request.setAttribute("listType", dao.getAllProductTypes());
        request.setAttribute("listColor", dao.getAllColors());

        // Params
        String[] types = request.getParameterValues("type");
        String[] prices = request.getParameterValues("price");
        String[] ratings = request.getParameterValues("rating");
        String colorParam = request.getParameter("color");

        String cidStr = request.getParameter("cid");
        Integer categoryId = (cidStr != null && !cidStr.isEmpty())
                ? Integer.parseInt(cidStr)
                : null;

        // Filter
        List<Product> listP = dao.filterProductsWithColor(
                types, prices, ratings, categoryId, colorParam
        );

        request.setAttribute("listP", listP);
        request.setAttribute("activeCategoryId", categoryId);

        String returnPage = request.getParameter("returnPage");

        if (returnPage == null || returnPage.isEmpty()) {
            returnPage = "product_all_user.jsp"; // fallback
        }

        request.getRequestDispatcher(returnPage)
                .forward(request, response);;
    }}