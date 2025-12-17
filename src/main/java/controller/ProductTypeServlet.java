    package controller;

    import dao.ProductTypeDao;
    import jakarta.servlet.*;
    import jakarta.servlet.http.*;
    import jakarta.servlet.annotation.*;
    import model.ProductType;

    import java.io.IOException;
    import java.util.List;

    @WebServlet(name = "ProductTypeServlet", urlPatterns = {"/product-type-manager", "/add-product-type"})
    public class ProductTypeServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            // Lấy từ khóa search từ thanh URL (nếu có)
            String keyword = request.getParameter("search");

            ProductTypeDao dao = new ProductTypeDao();
            List<ProductType> list;

            if (keyword != null && !keyword.trim().isEmpty()) {
                // Thực hiện tìm kiếm
                list = dao.searchProductTypeByName(keyword);
            } else {
                // Lấy toàn bộ danh sách
                list = dao.getAllProductType();
            }

            // Đẩy dữ liệu lên request để trang JSP nhận được
            request.setAttribute("listPT", list);
            request.setAttribute("keyword", keyword); // Để giữ lại chữ trong ô tìm kiếm

            // Forward sang trang admin_product_type.jsp
            request.getRequestDispatcher("admin_products_type.jsp").forward(request, response);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");

            // Lấy dữ liệu từ form Modal
            String name = request.getParameter("productTypeName");
            String categoryIdStr = request.getParameter("categoryId");

            try {
                int categoryId = Integer.parseInt(categoryIdStr);
                ProductTypeDao dao = new ProductTypeDao();

                if (dao.insertProductType(name, categoryId)) {
                    request.getSession().setAttribute("msg", "Đã thêm loại sản phẩm: " + name);
                } else {
                    request.getSession().setAttribute("msg", "Thêm thất bại!");
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("msg", "ID danh mục không hợp lệ!");
            }

            // Quay lại trang quản lý sau khi thêm
            response.sendRedirect("product-type-manager");
        }
    }