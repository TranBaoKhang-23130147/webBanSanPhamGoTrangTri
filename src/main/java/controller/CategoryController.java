
package controller;

import dao.CategoryDao;
import dao.ProductDao;
import jakarta.servlet.*;
        import jakarta.servlet.http.*;
        import jakarta.servlet.annotation.*;
import model.Category;
import model.Product;
import model.ProductType;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryController", value = "/CategoryController")
public class CategoryController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy ID danh mục và Số trang
        String cid = request.getParameter("cid");
        String pageIndex = request.getParameter("page");

        if (cid == null) {
            response.sendRedirect("ProductAllServlet");
            return;
        }

        // Mặc định trang 1 nếu không có tham số page
        int currentPage = (pageIndex == null || pageIndex.isEmpty()) ? 1 : Integer.parseInt(pageIndex);
        int pageSize = 12; // Số sản phẩm mỗi trang

        ProductDao pDao = new ProductDao();
        CategoryDao cDao = new CategoryDao();

        // 2. Lấy dữ liệu
        int categoryId = Integer.parseInt(cid);

        // Lấy sản phẩm theo Category + Phân trang
        List<Product> listP = pDao.getProductsByCategoryPaging(categoryId, currentPage, pageSize);

        // Lấy dữ liệu cho SIDEBAR (Quan trọng để hiện lọc)
        List<ProductType> listType = pDao.getAllProductTypes(); // Lấy loại SP
        List<model.ProductColor> listColor = pDao.getAllColors(); // Lấy danh sách MÀU SẮC

        List<Category> listCC = cDao.getAllCategory(); // Dùng cho Header/Menu

        // Tính toán tổng số trang
        int totalProducts = pDao.countProductsByCategory(categoryId);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        // 3. Xử lý Banner và Tên (Giữ nguyên logic của bạn)
        String name = "BỘ SƯU TẬP";
        String banner = "https://i.pinimg.com/1200x/4d/16/07/4d16076bd71f77a7b5f69963e875cac6.jpg";

        for (Category c : listCC) {
            if (c.getId() == categoryId) {
                name = c.getCategoryName();
                break;
            }
        }

        // 4. Đẩy dữ liệu sang JSP
        request.setAttribute("listP", listP);
        request.setAttribute("listType", listType);   // Cho checkbox Loại SP
        request.setAttribute("listColor", listColor); // Cho checkbox Màu sắc
        request.setAttribute("listCC", listCC);
        request.setAttribute("categoryName", name);
        request.setAttribute("categoryBanner", banner);

        // Dữ liệu phân trang
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("product_category.jsp").forward(request, response);
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}