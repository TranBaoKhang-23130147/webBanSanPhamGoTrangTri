
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

        String cid = request.getParameter("cid");
        String pageIndex = request.getParameter("page");

        if (cid == null) {
            response.sendRedirect("ProductAllServlet");
            return;
        }

        int currentPage = (pageIndex == null || pageIndex.isEmpty()) ? 1 : Integer.parseInt(pageIndex);
        int pageSize = 12;

        ProductDao pDao = new ProductDao();
        CategoryDao cDao = new CategoryDao();

        int categoryId = Integer.parseInt(cid);

        List<Product> listP = pDao.getProductsByCategoryPaging(categoryId, currentPage, pageSize);

        List<ProductType> listType = pDao.getAllProductTypes();
        List<model.ProductColor> listColor = pDao.getAllColors();

        List<Category> listCC = cDao.getAllCategory();

        int totalProducts = pDao.countProductsByCategory(categoryId);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        String name = "BỘ SƯU TẬP";
        String banner = "https://i.pinimg.com/1200x/4d/16/07/4d16076bd71f77a7b5f69963e875cac6.jpg";

        for (Category c : listCC) {
            if (c.getId() == categoryId) {
                name = c.getCategoryName();
                break;
            }
        }

        request.setAttribute("listP", listP);
        request.setAttribute("listType", listType);
        request.setAttribute("listColor", listColor);
        request.setAttribute("listCC", listCC);
        request.setAttribute("categoryName", name);
        request.setAttribute("categoryBanner", banner);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("product_category.jsp").forward(request, response);
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}