package controller;

import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet(name = "AddProductServlet", value = "/admin_add_products")

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class AddProductServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        ProductDao dao = new ProductDao();

        try {
            // 1. LẤY THÔNG TIN CƠ BẢN
            String name = request.getParameter("productName");
            int catId = Integer.parseInt(request.getParameter("categoryId"));
            int typeId = Integer.parseInt(request.getParameter("typeId"));
            int sourceId = Integer.parseInt(request.getParameter("sourceId"));
            Date mfgDate = Date.valueOf(request.getParameter("mfgDate"));
            double priceBase = Double.parseDouble(request.getParameter("variantPrice[]")); // Lấy giá đầu tiên làm giá gốc

            // 2. LẤY THÔNG TIN MÔ TẢ & CHI TIẾT
            String introduce = request.getParameter("introduce");
            String highlights = request.getParameter("highlights");
            String material = request.getParameter("material");
            String infoSize = request.getParameter("infoSize");
            String infoColor = request.getParameter("infoColor");
            String guarantee = request.getParameter("guarantee");

            // --- BƯỚC THỰC THI DATABASE 1: Lưu Description & Information ---
            // Bạn cần viết hàm này trong DAO để trả về ID vừa tạo
            int descId = dao.insertFullDescription(introduce, highlights, material, infoSize, infoColor, guarantee);

            // --- BƯỚC THỰC THI DATABASE 2: Lưu Product ---
            Product p = new Product();
            p.setNameProduct(name);
            p.setCategoryId(catId);
            p.setProductTypeId(typeId);
            p.setSourceId(sourceId);
            p.setMfgDate(mfgDate);
            p.setPrice(priceBase);
            p.setDescription(descId);
            p.setIsActive(1);

            int productId = dao.insertProductAndGetId(p); // Hàm insert trả về ID sản phẩm

            // 3. XỬ LÝ UPLOAD NHIỀU ẢNH
            String uploadPath = getServletContext().getRealPath("") + File.separator + "img/product";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();

            Collection<Part> parts = request.getParts();
            int firstImageId = 0;
            boolean isFirst = true;

            for (Part part : parts) {
                if (part.getName().equals("productImages") && part.getSize() > 0) {
                    String fileName = System.currentTimeMillis() + "_" + part.getSubmittedFileName();
                    part.write(uploadPath + File.separator + fileName);
                    String urlImage = "img/product/" + fileName;

                    // Lưu vào bảng images và lấy ID
                    int imgId = dao.insertImageAndGetId(urlImage, productId);

                    // Logic: Lấy ảnh đầu tiên làm ảnh chính
                    if (isFirst) {
                        firstImageId = imgId;
                        isFirst = false;
                    }
                }
            }
            // Cập nhật ảnh chính cho sản phẩm
            dao.updatePrimaryImage(productId, firstImageId);

            // 4. XỬ LÝ BIẾN THỂ (Mảng dữ liệu)
            String[] colorIds = request.getParameterValues("colorId[]");
            String[] sizeIds = request.getParameterValues("sizeId[]");
            String[] prices = request.getParameterValues("variantPrice[]");
            String[] stocks = request.getParameterValues("variantStock[]");

            if (colorIds != null) {
                // Sửa .size() thành .length
                for (int i = 0; i < colorIds.length; i++) {
                    int cId = Integer.parseInt(colorIds[i]);
                    int sId = Integer.parseInt(sizeIds[i]);
                    double vPrice = Double.parseDouble(prices[i]);
                    int vStock = Integer.parseInt(stocks[i]);

                    dao.insertVariant(productId, cId, sId, vPrice, vStock);
                }
            }

            response.sendRedirect("admin-list-products"); // Thành công chuyển hướng về danh sách

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("admin_add_product.jsp").forward(request, response);

        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển hướng người dùng đến file JSP chứa form thêm sản phẩm
        request.getRequestDispatcher("admin/add_product_form.jsp").forward(request, response);
    }
}