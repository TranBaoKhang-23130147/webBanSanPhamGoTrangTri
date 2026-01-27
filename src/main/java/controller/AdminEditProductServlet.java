package controller;

import dao.CategoryDao;
import dao.ProductDao;
import jakarta.servlet.ServletException;
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
    private ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idRaw = req.getParameter("id");
        if (idRaw != null && !idRaw.isEmpty()) {
            try {
                int id = Integer.parseInt(idRaw);
                Product product = productDao.getFullProductById(id);

                if (product != null) {
                    req.setAttribute("listCategories", productDao.getAllCategory());
                    req.setAttribute("listColors", productDao.getAllColors());
                    req.setAttribute("listSizes", productDao.getAllSizes());
                    req.setAttribute("listSources", productDao.getAllSources());
                    req.setAttribute("listTypes", productDao.getAllProductTypes());
                    req.setAttribute("product", product);
                    req.setAttribute("productInfo", product.getInformation());
                    req.getRequestDispatcher("/admin_edit_product.jsp").forward(req, resp);
                    return;
                } else {
                    req.setAttribute("errorMessage", "Không tìm thấy sản phẩm (ID: " + id + ")");
                }
            } catch (NumberFormatException e) {
                req.setAttribute("errorMessage", "ID sản phẩm không hợp lệ.");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("errorMessage", "Lỗi hệ thống khi tải thông tin sản phẩm.");
            }
        } else {
            req.setAttribute("errorMessage", "Thiếu tham số ID sản phẩm.");
        }

        req.getRequestDispatcher("/admin_edit_product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // ================== 1. ID ==================
            int productId = Integer.parseInt(req.getParameter("productId"));
            int infoId    = Integer.parseInt(req.getParameter("infoId"));
            int descId    = Integer.parseInt(req.getParameter("descId"));

            // ================== 2. PRODUCT ==================
            Product p = new Product();
            p.setId(productId);
            p.setNameProduct(req.getParameter("productName"));
            p.setPrice(Double.parseDouble(req.getParameter("price")));
            p.setCategoryId(Integer.parseInt(req.getParameter("categoryId")));
            p.setSourceId(Integer.parseInt(req.getParameter("sourceId")));
            p.setProductTypeId(Integer.parseInt(req.getParameter("productTypeId")));
            p.setMfgDate(Date.valueOf(req.getParameter("mfgDate")));

            // ================== 3. INFORMATION ==================
            Information info = new Information();
            info.setId(infoId);
            info.setMaterial(req.getParameter("material"));
            info.setGuarantee(req.getParameter("guarantee"));

            // ================== 4. DESCRIPTION ==================
            Description desc = new Description();
            desc.setId(descId);
            desc.setIntroduce(req.getParameter("introduce"));
            desc.setHighlights(req.getParameter("highlights"));

            // ================== 5. IMAGES ==================
            String imageRaw = req.getParameter("productImages");
            List<String> imagePaths = new ArrayList<>();

            if (imageRaw != null && !imageRaw.trim().isEmpty()) {
                imagePaths = Arrays.asList(imageRaw.split(","));
            } else {
                Product old = productDao.getFullProductById(productId);
                if (old != null) imagePaths = old.getListImages();
            }

            // ================== 6. VARIANTS ==================
            String[] variantIds   = req.getParameterValues("variantId[]");
            String[] skus         = req.getParameterValues("variantSKU[]");
            String[] colorIds     = req.getParameterValues("variantColor[]");
            String[] sizeIds      = req.getParameterValues("variantSize[]");
            String[] quantities   = req.getParameterValues("variantStock[]");
            String[] prices       = req.getParameterValues("variantPrice[]");

            if (skus == null || skus.length == 0) {
                throw new RuntimeException("Phải có ít nhất 1 biến thể");
            }

            List<ProductVariants> variants = new ArrayList<>();

            int n = skus.length;   // 🔥 MẢNG CHUẨN

            for (int i = 0; i < n; i++) {
                ProductVariants v = new ProductVariants();
                v.setProduct_id(productId);

                // ---- ID biến thể ----
                String idStr = (variantIds != null && i < variantIds.length)
                        ? variantIds[i].trim()
                        : "0";

                v.setId(Integer.parseInt(idStr)); // 0 = biến thể mới

                // ---- Validate ----
                if (skus[i] == null || skus[i].trim().isEmpty()) {
                    throw new RuntimeException("SKU không được rỗng (biến thể #" + (i + 1) + ")");
                }

                // ---- Set dữ liệu ----
                v.setSku(skus[i].trim());
                v.setColor_id(Integer.parseInt(colorIds[i]));
                v.setSize_id(Integer.parseInt(sizeIds[i]));
                v.setInventory_quantity(Integer.parseInt(quantities[i]));
                v.setVariant_price(new BigDecimal(prices[i]));

                variants.add(v);
            }

            // ================== 7. UPDATE ==================
            boolean success = productDao.updateFullProduct(
                    p, desc, info, variants, imagePaths
            );

            if (success) {
                resp.sendRedirect("products?update=success");
            } else {
                req.setAttribute("message", "Cập nhật thất bại (SKU trùng hoặc lỗi dữ liệu)");
                doGet(req, resp);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("message", "Lỗi: " + e.getMessage());
            doGet(req, resp);
        }
    }

}