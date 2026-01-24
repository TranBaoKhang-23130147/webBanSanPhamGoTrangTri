package controller;

import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/admin/edit-product")
public class AdminEditProductServlet extends HttpServlet {

    private final ProductDao dao = new ProductDao();

    // ===================== GET =====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int productId = Integer.parseInt(request.getParameter("id"));

        Product p = dao.getProductById(productId);
        if (p == null) {
            response.sendRedirect("products");
            return;
        }

        // Load full data
        p.setVariants(dao.getProductVariants(productId));
        p.setSubImages(dao.getProductImages(productId));

        Information info = dao.getInformationByProductId(productId);
        Description desc = dao.getDescriptionByProductId(productId);

        request.setAttribute("p", p);
        request.setAttribute("productInfo", info);
        request.setAttribute("productDesc", desc);

        // dropdowns
        request.setAttribute("listCategories", dao.getAllCategory());
        request.setAttribute("listTypes", dao.getAllProductTypes());
        request.setAttribute("listSources", dao.getAllSources());
        request.setAttribute("listColors", dao.getAllColors());
        request.setAttribute("listSizes", dao.getAllSizes());

        request.getRequestDispatcher("/admin_edit_product.jsp").forward(request, response);
    }

    // ===================== POST =====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int productId = Integer.parseInt(request.getParameter("productId"));
        int descriptionId = Integer.parseInt(request.getParameter("descriptionId"));
        int informationId = Integer.parseInt(request.getParameter("informationId"));

        // -------- PRODUCT --------
        Product p = new Product();
        p.setId(productId);
        p.setNameProduct(request.getParameter("productName"));
        p.setPrice(Double.parseDouble(request.getParameter("basePrice")));
        p.setCategoryId(parseInt(request.getParameter("categoryId")));
        p.setProductTypeId(parseInt(request.getParameter("typeId")));
        p.setSourceId(parseInt(request.getParameter("sourceId")));

        // -------- INFORMATION --------
        Information info = new Information();
        info.setId(informationId);
        info.setMaterial(request.getParameter("material"));
        info.setColor(request.getParameter("colorInfo"));
        info.setSize(request.getParameter("sizeInfo"));
        info.setGuarantee(request.getParameter("guarantee"));

        // -------- DESCRIPTION --------
        Description desc = new Description();
        desc.setId(descriptionId);
        desc.setIntroduce(request.getParameter("introduce"));
        desc.setHighlights(request.getParameter("highlights"));

        // -------- VARIANTS --------
        String[] variantIds = request.getParameterValues("variantId[]");
        String[] prices = request.getParameterValues("variantPrice[]");
        String[] stocks = request.getParameterValues("variantStock[]");

        // Variant cũ
        List<Integer> oldVariantIds = dao.getVariantIdsByProduct(productId);
        Set<Integer> newVariantIds = new HashSet<>();

        if (variantIds != null) {
            for (int i = 0; i < variantIds.length; i++) {
                int vid = Integer.parseInt(variantIds[i]);
                newVariantIds.add(vid);

                dao.updateVariant(
                        vid,
                        Double.parseDouble(prices[i]),
                        Integer.parseInt(stocks[i])
                );
            }
        }

        // -------- DELETE VARIANT ĐÃ XÓA --------
        for (int oldId : oldVariantIds) {
            if (!newVariantIds.contains(oldId)) {
                dao.deleteVariant(oldId);
            }
        }

        // -------- UPDATE TỔNG --------
        dao.updateFullProduct(p, info, desc);

        response.sendRedirect("admin/product-detail?id=" + productId);
    }

    private int parseInt(String v) {
        try {
            return (v == null || v.isEmpty()) ? 0 : Integer.parseInt(v);
        } catch (Exception e) {
            return 0;
        }
    }
}
