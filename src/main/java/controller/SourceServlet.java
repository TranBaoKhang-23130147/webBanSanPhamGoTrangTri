package controller;

import dao.SourceDao;
import model.Source;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;
@WebServlet(name = "SourceServlet", urlPatterns = {"/source-manager", "/add-source", "/delete-source", "/edit-source"})
public class SourceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        SourceDao dao = new SourceDao();

//       1. delete

        String keyword = request.getParameter("search");
        List<Source> list = (keyword != null && !keyword.trim().isEmpty())
                ? dao.searchSourceByName(keyword)
                : dao.getAllSources();

        request.setAttribute("listS", list);
        request.setAttribute("activePage", "source"); // Để active menu
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("admin_source.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();
        String name = request.getParameter("sourceName"); // Khớp với name="sourceName" trong JSP
        SourceDao dao = new SourceDao();

        if (action.equals("/add-source")) {
            if (dao.insertSource(name)) {
                request.getSession().setAttribute("msg", "Thêm nguồn hàng thành công!");
                request.getSession().setAttribute("msgType", "success");
            } else {
                request.getSession().setAttribute("msg", "Thêm thất bại!");
                request.getSession().setAttribute("msgType", "error");
            }
        }
//        else if (action.equals("/edit-source")) {
//            int id = Integer.parseInt(request.getParameter("id"));
//            if (dao.updateSource(id, name)) {
//                request.getSession().setAttribute("msg", "Cập nhật thành công!");
//                request.getSession().setAttribute("msgType", "success");
//            } else {
//                request.getSession().setAttribute("msg", "Cập nhật thất bại!");
//                request.getSession().setAttribute("msgType", "error");
//            }
//        }
        response.sendRedirect("source-manager");
    }
}