//package controller;
//
//import dao.ProductDao;
//import jakarta.servlet.*;
//import jakarta.servlet.http.*;
//import jakarta.servlet.annotation.*;
//import model.Product;
//import java.io.IOException;
//import java.sql.Date;
//
//@WebServlet(name = "AddProductServlet", value = "/add-product")
//public class AddProductServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//
//        try {
//            // 1. Lấy dữ liệu từ các trường input trong form
//            String name = request.getParameter("productName");
//            String description = request.getParameter("description");
//            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
//            int productTypeId = Integer.parseInt(request.getParameter("productTypeId"));
//            double price = Double.parseDouble(request.getParameter("price"));
//            int sourceId = Integer.parseInt(request.getParameter("sourceId"));
//            int isActive = Integer.parseInt(request.getParameter("isActive"));
//            String mfgDateStr = request.getParameter("mfgDate");
//
//            // 2. Tạo đối tượng Product và gán giá trị
//            Product p = new Product();
//            p.setNameProduct(name);
//            p.setDescription(description);
//            p.setCategoryId(categoryId);
//            p.setProductTypeId(productTypeId);
//            p.setPrice(price);
//            p.setSourceId(sourceId);
//            p.setIsActive(isActive);
//            p.setMfgDate(Date.valueOf(mfgDateStr)); // Chuyển String sang SQL Date
//            p.setPrimaryImageId(1); // Giả sử mặc định ID ảnh là 1 nếu chưa xử lý upload
//
//            // 3. Gọi DAO lưu vào database
//            ProductDao dao = new ProductDao();
//            if (dao.insertProduct(p)) {
//                request.getSession().setAttribute("msg", "Thêm sản phẩm thành công!");
//            } else {
//                request.getSession().setAttribute("msg", "Lỗi: Không thể thêm sản phẩm.");
//            }
//        } catch (Exception e) {
//            request.getSession().setAttribute("msg", "Lỗi dữ liệu: " + e.getMessage());
//        }
//
//        // 4. Quay lại trang danh sách sản phẩm hoặc trang thêm
//        request.getRequestDispatcher("admin/admin_add_products.jsp").forward(request, response);    }
//}