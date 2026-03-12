package controller;

import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin-add-product")
public class AddProductServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao dao = new ProductDao();

        request.setAttribute("listCategories", dao.getAllCategory());
        request.setAttribute("listTypes", dao.getAllProductTypes());
        request.setAttribute("listSources", dao.getAllSources());
        request.setAttribute("listColors", dao.getAllColors());
        request.setAttribute("listSizes", dao.getAllSizes());

        request.getRequestDispatcher("admin_add_products.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        ProductDao dao = new ProductDao();
        try {
            Information info = new Information();
            info.setMaterial(request.getParameter("material"));
            info.setColor(request.getParameter("colorInfo"));
            info.setSize(request.getParameter("sizeInfo"));
            info.setGuarantee(request.getParameter("guarantee"));

            Description desc = new Description();
            desc.setIntroduce(request.getParameter("introduce"));
            desc.setHighlights(request.getParameter("highlights"));

            Product p = new Product();
            p.setNameProduct(request.getParameter("productName"));

            String price = request.getParameter("basePrice");
            if(price != null && !price.isEmpty()){
                p.setPrice(Double.parseDouble(price));
            }

            p.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
            p.setProductTypeId(Integer.parseInt(request.getParameter("typeId")));
            p.setSourceId(Integer.parseInt(request.getParameter("sourceId")));

            String mfgDate = request.getParameter("mfgDate");
            if(mfgDate != null && !mfgDate.isEmpty()){
                p.setMfgDate(java.sql.Date.valueOf(mfgDate));
            }
            List<String> images = new ArrayList<>();
            String imgStr = request.getParameter("productImages");

            if(imgStr != null && !imgStr.isEmpty()){
                String[] arr = imgStr.split(",");
                for(String s : arr){
                    images.add(s.trim());
                }
            }
            String[] skus = request.getParameterValues("variantSKU[]");
            String[] colors = request.getParameterValues("colorId[]");
            String[] sizes = request.getParameterValues("sizeId[]");
            String[] prices = request.getParameterValues("variantPrice[]");
            String[] stocks = request.getParameterValues("variantStock[]");

            List<ProductVariants> listVariant = new ArrayList<>();

            if(skus != null){
                for(int i = 0; i < skus.length; i++){

                    ProductVariants v = new ProductVariants();

                    v.setSku(skus[i]);

                    v.setColor_id(Integer.parseInt(colors[i]));
                    v.setSize_id(Integer.parseInt(sizes[i]));

                    if(prices[i] != null && !prices[i].isEmpty()){
                        v.setVariant_price(new BigDecimal(prices[i]));
                    }

                    if(stocks[i] != null && !stocks[i].isEmpty()){
                        v.setInventory_quantity(Integer.parseInt(stocks[i]));
                    }

                    listVariant.add(v);
                }
            }

            if(listVariant.isEmpty()){
                request.setAttribute("message","Phải có ít nhất 1 biến thể!");
                loadData(request,response);
                return;
            }
            boolean check = dao.insertFullProduct(p, desc, info, listVariant, images);

            if(check){
                response.sendRedirect("admin-add-product?status=success");
            }else{
                request.setAttribute("message","Thêm sản phẩm thất bại!");
                loadData(request,response);
            }

        } catch (Exception e) {

            e.printStackTrace();
            request.setAttribute("message","Lỗi!");
            loadData(request,response);

        }
    }

    private void loadData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductDao dao = new ProductDao();

        request.setAttribute("listCategories", dao.getAllCategory());
        request.setAttribute("listTypes", dao.getAllProductTypes());
        request.setAttribute("listSources", dao.getAllSources());
        request.setAttribute("listColors", dao.getAllColors());
        request.setAttribute("listSizes", dao.getAllSizes());

        request.getRequestDispatcher("admin_add_products.jsp").forward(request, response);
    }
}