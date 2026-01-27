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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 1. Lấy thông tin cơ bản
            int productId = Integer.parseInt(req.getParameter("productId"));
            int infoId = Integer.parseInt(req.getParameter("infoId"));
            int descId = Integer.parseInt(req.getParameter("descId"));

            String name = req.getParameter("productName");
            double price = Double.parseDouble(req.getParameter("price"));
            int categoryId = Integer.parseInt(req.getParameter("categoryId"));
            int sourceId = Integer.parseInt(req.getParameter("sourceId"));
            int typeId = Integer.parseInt(req.getParameter("productTypeId"));
            Date mfgDate = Date.valueOf(req.getParameter("mfgDate"));

            // 2. Tạo đối tượng Information & Description
            Information info = new Information();
            info.setId(infoId);
            info.setMaterial(req.getParameter("material"));
            info.setGuarantee(req.getParameter("guarantee"));

            Description desc = new Description();
            desc.setId(descId);
            desc.setIntroduce(req.getParameter("introduce"));
            desc.setHighlights(req.getParameter("highlights"));

            // 3. Tạo đối tượng Product
            Product p = new Product();
            p.setId(productId);
            p.setNameProduct(name);
            p.setPrice(price);
            p.setCategoryId(categoryId);
            p.setSourceId(sourceId);
            p.setProductTypeId(typeId);
            p.setMfgDate(mfgDate);

            // 4. Xử lý Ảnh
            String imageRaw = req.getParameter("productImages");
            List<String> imagePaths;
            if (imageRaw != null && !imageRaw.trim().isEmpty()) {
                imagePaths = Arrays.asList(imageRaw.split(","));
            } else {
                Product oldProduct = productDao.getFullProductById(productId);
                imagePaths = oldProduct != null ? oldProduct.getListImages() : new ArrayList<>();
            }

            // 5. Thu thập danh sách Biến thể (đã thêm variantId[])
            String[] variantIds   = req.getParameterValues("variantId[]");
            String[] colorIds     = req.getParameterValues("variantColor[]");
            String[] sizeIds      = req.getParameterValues("variantSize[]");
            String[] skus         = req.getParameterValues("variantSKU[]");
            String[] quantities   = req.getParameterValues("variantStock[]");
            String[] vPrices      = req.getParameterValues("variantPrice[]");

            List<ProductVariants> variants = new ArrayList<>();
            if (colorIds != null && colorIds.length > 0) {
                for (int i = 0; i < colorIds.length; i++) {
                    ProductVariants v = new ProductVariants();
                    v.setProduct_id(productId);

                    // Xử lý id biến thể (cũ hoặc mới)
                    String idStr = (variantIds != null && i < variantIds.length) ? variantIds[i].trim() : "";
                    if (!idStr.isEmpty() && !idStr.equals("0")) {
                        v.setId(Integer.parseInt(idStr));
                    } // else: biến thể mới → id = 0 hoặc null

                    v.setColor_id(Integer.parseInt(colorIds[i]));
                    v.setSize_id(Integer.parseInt(sizeIds[i]));
                    v.setSku(skus[i].trim());
                    v.setInventory_quantity(Integer.parseInt(quantities[i]));
                    v.setVariant_price(new BigDecimal(vPrices[i].trim()));

                    variants.add(v);
                }
            }

            // 6. Gọi DAO update
            boolean success = productDao.updateFullProduct(p, desc, info, variants, imagePaths);

            if (success) {
                resp.sendRedirect("products?status=success");
            } else {
                req.setAttribute("message", "Lỗi: Không thể cập nhật cơ sở dữ liệu. Vui lòng kiểm tra SKU có trùng không.");
                doGet(req, resp);
            }
        } catch (NumberFormatException | NullPointerException e) {
            e.printStackTrace();
            req.setAttribute("message", "Lỗi dữ liệu đầu vào: " + e.getMessage());
            doGet(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("message", "Lỗi hệ thống: " + e.getMessage());
            doGet(req, resp);
        }
    }
}