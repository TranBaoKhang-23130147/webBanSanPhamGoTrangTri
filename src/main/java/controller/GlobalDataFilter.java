package controller;

import dao.CategoryDao;
import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import model.Category;
import model.ProductType;
import java.io.IOException;
import java.util.List;

// WebFilter("/*") có nghĩa là nó sẽ chạy qua tất cả các trang
@WebFilter("/*")
public class GlobalDataFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        CategoryDao cDao = new CategoryDao();
        ProductDao pDao = new ProductDao();

        // 1. Lấy danh sách danh mục (cho Header)
        List<Category> listCC = cDao.getAllCategory();

        // 2. Lấy danh sách loại sản phẩm (cho Sidebar/Filter nếu cần)
        List<ProductType> listType = pDao.getAllProductTypes();

        // 3. Đẩy vào request
        // Bây giờ mọi trang JSP đều có thể gọi ${listCC} và ${listType}
        request.setAttribute("listCC", listCC);
        request.setAttribute("listType", listType);

        // Cho phép đi tiếp đến Servlet hoặc JSP mục tiêu
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}