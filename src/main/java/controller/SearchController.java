package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchController", value = "/search") // Đổi tên URL cho ngắn gọn
public class SearchController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy dữ liệu từ các ô input (name="txtSearch" và name="category")
        String txtSearch = request.getParameter("txtSearch");
        String category = request.getParameter("category");

        // Xử lý trường hợp lần đầu vào trang (txtSearch bị null)
        if (txtSearch == null) {
            txtSearch = "";
        }
        if (category == null) {
            category = "all";
        }

        // 2. Gọi lớp DAO để lấy danh sách sản phẩm
        ProductDao dao = new ProductDao();
        List<Product> list = dao.searchProducts(txtSearch, category);

        // 3. Đẩy dữ liệu sang trang JSP
        request.setAttribute("listP", list);
        request.setAttribute("txtS", txtSearch); // Giữ lại từ khóa vừa search trên ô input
        request.setAttribute("catS", category);  // Giữ lại phân loại đã chọn

        // 4. Chuyển hướng về trang search.jsp
        request.getRequestDispatcher("search.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Cho phép cả phương thức POST cũng chạy như GET
    }
}