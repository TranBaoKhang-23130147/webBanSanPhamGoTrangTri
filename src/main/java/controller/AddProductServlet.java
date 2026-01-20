package controller;

import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Description;
import model.Information;
import model.Product;
import model.ProductVariant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;

@WebServlet("/admin-add-product")
public class AddProductServlet extends HttpServlet {

    // ===================== GET: LOAD FORM =====================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao dao = new ProductDao();

        // Load dữ liệu lên các dropdown list trong JSP
        request.setAttribute("listCategories", dao.getAllCategory());
        request.setAttribute("listTypes", dao.getAllProductTypes());
        request.setAttribute("listSources", dao.getAllSources());
        request.setAttribute("listColors", dao.getAllColors());
        request.setAttribute("listSizes", dao.getAllSizes());

        request.getRequestDispatcher("admin_add_products.jsp").forward(request, response);
    }

    // ===================== POST: XỬ LÝ LƯU DỮ LIỆU =====================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            // 1. Thu thập dữ liệu cho Information
            Information info = new Information();
            info.setMaterial(request.getParameter("material"));
            info.setColor(request.getParameter("colorInfo"));
            info.setSize(request.getParameter("sizeInfo"));
            info.setGuarantee(request.getParameter("guarantee"));

            // 2. Thu thập dữ liệu cho Description
            Description desc = new Description();
            desc.setIntroduce(request.getParameter("introduce"));
            desc.setHighlights(request.getParameter("highlights"));

            // 3. Thu thập dữ liệu cho Product chính
            Product p = new Product();
            p.setNameProduct(request.getParameter("productName"));
            p.setPrice(parseDouble(request.getParameter("basePrice")));
            p.setCategoryId(parseInt(request.getParameter("categoryId")));
            p.setTypeId(parseInt(request.getParameter("typeId")));
            p.setSourceId(parseInt(request.getParameter("sourceId")));

            // Xử lý ngày tháng (Chuyển String từ form sang java.sql.Date)
            String mfgDateStr = request.getParameter("mfgDate");
            if (mfgDateStr != null && !mfgDateStr.isEmpty()) {
                p.setMfgDate(java.sql.Date.valueOf(mfgDateStr));
            }

            // 4. Xử lý danh sách ảnh
            String productImages = request.getParameter("productImages");
            List<String> imagePaths = new ArrayList<>();
            if (productImages != null && !productImages.isEmpty()) {
                String[] imgs = productImages.split(",");
                for (String img : imgs) {
                    imagePaths.add(img.trim());
                }
            }

            // 5. Thu thập danh sách biến thể (Variants)
            String[] skus = request.getParameterValues("variantSKU[]");
            String[] colorIds = request.getParameterValues("colorId[]");
            String[] sizeIds = request.getParameterValues("sizeId[]");
            String[] vPrices = request.getParameterValues("variantPrice[]");
            String[] stocks = request.getParameterValues("variantStock[]");

            List<ProductVariant> variants = new ArrayList<>();
            if (skus != null) {
                for (int i = 0; i < skus.length; i++) {
                    ProductVariant v = new ProductVariant();
                    v.setSku(skus[i]);
                    v.setColorId(parseInt(colorIds[i]));
                    v.setSizeId(parseInt(sizeIds[i]));
                    v.setVariantPrice(parseDouble(vPrices[i]));
                    v.setInventoryQuantity(parseInt(stocks[i]));
                    variants.add(v);
                }
            }

            // 6. Gọi DAO với các đối tượng đã đóng gói
            ProductDao dao = new ProductDao();
            boolean success = dao.insertFullProduct(p, desc, info, variants, imagePaths);

            if (success) {
                response.sendRedirect("admin-add-product?status=success");
            } else {
                request.setAttribute("message", "Thêm thất bại vào Database!");
                doGet(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin-add-product?status=error");
        }
    }

    // ===================== CÁC HÀM HỖ TRỢ (UTIL) =====================
    private int parseInt(String v) {
        try {
            return (v == null || v.isEmpty()) ? 0 : Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}