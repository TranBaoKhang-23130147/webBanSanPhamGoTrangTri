package controller;

import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;
import model.Product;
import model.ProductType;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class AdminProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy dữ liệu từ form (nếu có)
        // Lấy các tham số lọc từ JSP gửi lên
        String keyword = request.getParameter("keyword");
        String typeId = request.getParameter("typeId");
        String categoryId = request.getParameter("categoryId");

        ProductDao dao = new ProductDao();

// CHỈ DÙNG hàm searchProducts này thôi (Hàm này phải xử lý được cả khi tham số null)
        List<Product> list = dao.searchProducts(keyword, typeId, categoryId);

        request.setAttribute("productList", list);
        request.setAttribute("typeList", dao.getAllProductTypes());
        request.setAttribute("categoryList", dao.getAllCategory());

        request.getRequestDispatcher("admin_products.jsp").forward(request, response);
    }
}

