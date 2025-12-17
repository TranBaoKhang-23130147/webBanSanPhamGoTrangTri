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

            // 1. Lấy danh sách Category để đổ vào Dropdown
            dao.CategoryDao cDao = new dao.CategoryDao();
            List<model.Category> listC = cDao.getAllCategory();
            request.setAttribute("listC", listC); // Tên "listC" phải khớp với JSP

            // 2. Logic lấy danh sách ProductType hiện tại
            String keyword = request.getParameter("search");
            ProductTypeDao ptDao = new ProductTypeDao();
            List<model.ProductType> listPT = (keyword != null && !keyword.trim().isEmpty())
                    ? ptDao.searchProductTypeByName(keyword)
                    : ptDao.getAllProductType();

            request.setAttribute("listPT", listPT);
            request.setAttribute("keyword", keyword);

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