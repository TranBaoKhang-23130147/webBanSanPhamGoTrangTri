package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/admin-edit-product")
public class AdminEditProductServlet extends HttpServlet {

    ProductDao productDao = new ProductDao();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = Integer.parseInt(req.getParameter("id"));

        Product product = productDao.getFullProductById(id);

        req.setAttribute("product", product);
        req.setAttribute("productInfo", product.getInformation());
        req.setAttribute("listCategories", productDao.getAllCategory());
        req.setAttribute("listColors", productDao.getAllColors());
        req.setAttribute("listSizes", productDao.getAllSizes());
        req.setAttribute("listSources", productDao.getAllSources());
        req.setAttribute("listTypes", productDao.getAllProductTypes());

        req.getRequestDispatcher("admin_edit_product.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int productId = Integer.parseInt(req.getParameter("productId"));
        int infoId = Integer.parseInt(req.getParameter("infoId"));
        int descId = Integer.parseInt(req.getParameter("descId"));

        Product p = new Product();
        p.setId(productId);
        p.setNameProduct(req.getParameter("productName"));
        p.setPrice(Double.parseDouble(req.getParameter("price")));
        p.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
        p.setSourceId(Integer.parseInt(req.getParameter("sourceId")));
        p.setProductTypeId(Integer.parseInt(req.getParameter("productTypeId")));
        p.setMfgDate(Date.valueOf(req.getParameter("mfgDate")));

        Information info = new Information();
        info.setId(infoId);
        info.setMaterial(req.getParameter("material"));
        info.setGuarantee(req.getParameter("guarantee"));

        Description desc = new Description();
        desc.setId(descId);
        desc.setIntroduce(req.getParameter("introduce"));
        desc.setHighlights(req.getParameter("highlights"));

        String imageRaw = req.getParameter("productImages");
        List<String> imagePaths = new ArrayList<>();

        if (imageRaw != null && !imageRaw.isEmpty()) {
            imagePaths = Arrays.asList(imageRaw.split(","));
        }

        String[] skus = req.getParameterValues("variantSKU[]");
        String[] colors = req.getParameterValues("variantColor[]");
        String[] sizes = req.getParameterValues("variantSize[]");
        String[] qty = req.getParameterValues("variantStock[]");
        String[] price = req.getParameterValues("variantPrice[]");

        List<ProductVariants> variants = new ArrayList<>();

        for (int i = 0; i < skus.length; i++) {
            ProductVariants v = new ProductVariants();
            v.setProduct_id(productId);
            v.setSku(skus[i]);
            v.setColor_id(Integer.parseInt(colors[i]));
            v.setSize_id(Integer.parseInt(sizes[i]));
            v.setInventory_quantity(Integer.parseInt(qty[i]));
            v.setVariant_price(new BigDecimal(price[i]));

            variants.add(v);
        }

        productDao.updateFullProduct(p, desc, info, variants, imagePaths);

        resp.sendRedirect("admin-products");
    }
}