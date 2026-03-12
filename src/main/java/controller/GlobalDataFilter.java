package controller;

import dao.CategoryDao;
import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import model.Category;
import model.ProductType;
import java.io.IOException;
import java.util.List;

@WebFilter("/*")
public class GlobalDataFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        CategoryDao cDao = new CategoryDao();
        ProductDao pDao = new ProductDao();

        List<Category> listCC = cDao.getAllCategory();

        List<ProductType> listType = pDao.getAllProductTypes();

        request.setAttribute("listCC", listCC);
        request.setAttribute("listType", listType);

        chain.doFilter(request, response);
    }
}