    package controller;

    import dao.ProductTypeDao;
    import jakarta.servlet.*;
    import jakarta.servlet.http.*;
    import jakarta.servlet.annotation.*;
    import model.ProductType;

    import java.io.IOException;
    import java.util.List;

    @WebServlet(name = "ProductTypeServlet", urlPatterns = {"/product-type-manager", "/add-product-type", "/delete-product-type"})    public class ProductTypeServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            String action = request.getServletPath(); // Lấy đường dẫn đang gọi
            if (action.equals("/delete-product-type")) {
                int id = Integer.parseInt(request.getParameter("id"));
                ProductTypeDao dao = new ProductTypeDao();

                if (dao.deleteProductType(id)) {
                    request.getSession().setAttribute("msg", "Đã xóa loại sản phẩm thành công.");
                    request.getSession().setAttribute("msgType", "success"); // Màu xanh
                } else {
                    request.getSession().setAttribute("msg", "Không thể xóa! Loại sản phẩm này đang có sản phẩm tồn tại.");
                    request.getSession().setAttribute("msgType", "error"); // Màu đỏ
                }
                response.sendRedirect("product-type-manager");
                return;
            }
            String keyword = request.getParameter("keyword");
            ProductTypeDao dao = new ProductTypeDao();

            List<ProductType> listPT = dao.getAllProductTypesWithTotalInventory(keyword);

            request.setAttribute("listPT", listPT);
            request.setAttribute("keyword", keyword != null ? keyword : "");




            request.setAttribute("keyword", keyword);
            request.setAttribute("activePage", "productType"); // Để active menu

            request.getRequestDispatcher("admin_products_type.jsp").forward(request, response);
        }
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");

            String name = request.getParameter("productTypeName");
            ProductTypeDao dao = new ProductTypeDao();

            if (dao.insertProductType(name)) {
                // Cần có cả 2 dòng này
                request.getSession().setAttribute("msg", "Đã thêm loại sản phẩm: " + name);
                request.getSession().setAttribute("msgType", "success"); // <--- THÊM DÒNG NÀY
            } else {
                request.getSession().setAttribute("msg", "Thêm thất bại!");
                request.getSession().setAttribute("msgType", "error");
            }

            response.sendRedirect("product-type-manager");
        }}