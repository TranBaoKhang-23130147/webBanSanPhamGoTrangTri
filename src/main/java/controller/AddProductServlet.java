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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            String basePriceStr = request.getParameter("basePrice");
            if (basePriceStr != null && !basePriceStr.isEmpty()) {
                p.setPrice(Double.parseDouble(basePriceStr));
            }
            p.setCategoryId(parseInt(request.getParameter("categoryId")));   // nếu "" → 0 → DAO sẽ set NULL nếu cần
            p.setProductTypeId(parseInt(request.getParameter("typeId")));
            p.setSourceId(parseInt(request.getParameter("sourceId")));
            // có thể là 0 hoặc null → sẽ xử lý ở DAO

            // Xử lý ngày sản xuất
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
                    String trimmed = img.trim();
                    if (!trimmed.isEmpty()) {
                        imagePaths.add(trimmed);
                    }
                }
            }

            // 5. Thu thập danh sách biến thể (Variants)
            String[] skus = request.getParameterValues("variantSKU[]");
            String[] colorIds = request.getParameterValues("colorId[]");
            String[] sizeIds = request.getParameterValues("sizeId[]");
            String[] vPrices = request.getParameterValues("variantPrice[]");
            String[] stocks = request.getParameterValues("variantStock[]");

            List<ProductVariant> variants = new ArrayList<>();
            Set<String> variantKeySet = new HashSet<>(); // key: colorId-sizeId
            boolean hasDuplicateVariant = false;
            String duplicateErrorDetail = "";

            // Lấy tên sản phẩm để generate SKU (nếu cần)
            String productName = request.getParameter("productName");
            String shortProductName = (productName != null && !productName.isEmpty())
                    ? productName.replaceAll("\\s+", "").substring(0, Math.min(6, productName.length())).toUpperCase()
                    : "SP";

// Danh sách màu và size để lấy tên ngắn (nếu bạn có getter getShortName hoặc tương tự, dùng; nếu không thì dùng ID)
            ProductDao dao = new ProductDao(); // để lấy tên màu/size nếu cần chi tiết hơn

            if (skus != null && skus.length > 0) {
                for (int i = 0; i < skus.length; i++) {
                    if (colorIds[i] == null || sizeIds[i] == null || vPrices[i] == null || stocks[i] == null) {
                        continue;
                    }

                    int colorId = parseInt(colorIds[i]);
                    int sizeId = parseInt(sizeIds[i]);

                    if (colorId > 0 && sizeId > 0) {
                        ProductVariant v = new ProductVariant();
                        v.setColorId(colorId);
                        v.setSizeId(sizeId);
                        v.setVariantPrice(parseDouble(vPrices[i]));
                        v.setInventoryQuantity(parseInt(stocks[i]));

                        // Generate SKU nếu trống
                        String skuInput = (skus[i] != null) ? skus[i].trim() : "";
                        if (skuInput.isEmpty()) {
                            // Ví dụ: SP-GIUONG-RED-L-AB12
                            String colorShort = "C" + colorId;   // Có thể cải tiến: lấy tên màu ngắn từ DB
                            String sizeShort = "S" + sizeId;
                            String randomPart = String.format("%04d", (int)(Math.random() * 10000)); // 0000-9999
                            skuInput = shortProductName + "-" + colorShort + "-" + sizeShort + "-" + randomPart;
                        }
                        v.setSku(skuInput);

                        // Check trùng color-size
                        String variantKey = colorId + "-" + sizeId;
                        if (variantKeySet.contains(variantKey)) {
                            hasDuplicateVariant = true;
                            duplicateErrorDetail = " (màu ID: " + colorId + ", size ID: " + sizeId + ")";
                            break;
                        }
                        variantKeySet.add(variantKey);

                        variants.add(v);
                    }
                }
            }

// Validation duplicate & required variants
            if (hasDuplicateVariant) {
                request.setAttribute("message", "LỖI: Có biến thể trùng màu sắc + kích thước!" + duplicateErrorDetail +
                        "<br>Mỗi cặp (màu, kích thước) chỉ được xuất hiện 1 lần.");
                forwardWithData(request, response);
                return;
            }

            if (variants.isEmpty()) {
                request.setAttribute("message", "LỖI: Phải có ít nhất 1 biến thể hợp lệ (chọn cả màu sắc và kích thước)!");
                forwardWithData(request, response);
                return;
            }

            // 6. Gọi DAO để insert toàn bộ
            boolean success = dao.insertFullProduct(p, desc, info, variants, imagePaths);

            if (success) {
                response.sendRedirect("admin-add-product?status=success");
            } else {
                request.setAttribute("message", "Thêm sản phẩm thất bại vào Database! Vui lòng kiểm tra lại dữ liệu.");
                forwardWithData(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Có lỗi xảy ra: " + e.getMessage());
            forwardWithData(request, response);
        }
    }

    // Hàm helper: load lại dropdowns và forward về JSP khi lỗi
    private void forwardWithData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDao dao = new ProductDao();
        request.setAttribute("listCategories", dao.getAllCategory());
        request.setAttribute("listTypes", dao.getAllProductTypes());
        request.setAttribute("listSources", dao.getAllSources());
        request.setAttribute("listColors", dao.getAllColors());
        request.setAttribute("listSizes", dao.getAllSizes());

        // (Tùy chọn) Bạn có thể set lại các giá trị đã nhập để giữ form không bị mất
        // Ví dụ: request.setAttribute("productName", request.getParameter("productName")); ...

        request.getRequestDispatcher("admin_add_products.jsp").forward(request, response);
    }

    // Hàm parseInt an toàn (đã có trong code cũ của bạn)
    private int parseInt(String v) {
        try {
            return (v == null || v.isEmpty()) ? 0 : Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}