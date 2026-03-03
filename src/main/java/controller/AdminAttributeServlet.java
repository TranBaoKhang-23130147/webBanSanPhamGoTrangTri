package controller;

import dao.*;
import model.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

import java.io.IOException;

@WebServlet("/admin-attribute")
public class AdminAttributeServlet extends HttpServlet {

    private ColorDao colorDao;
    private SizeDao sizeDao;

    @Override
    public void init() {
        try {
            colorDao = new ColorDao();
            sizeDao = new SizeDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String del = req.getParameter("delete");

        if(del != null){

            int id = Integer.parseInt(req.getParameter("id"));

            if(del.equals("color")){
                colorDao.delete(id);
            }

            if(del.equals("size")){
                sizeDao.delete(id);
            }

            resp.sendRedirect("admin-attribute");
            return;
        }

        req.setAttribute("listColors", colorDao.getAll());
        req.setAttribute("listSizes", sizeDao.getAll());

        req.getRequestDispatcher("/admin_attribute.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String type = req.getParameter("type");

        if ("color".equals(type)) {

            int id = req.getParameter("id").isEmpty()
                    ? 0
                    : Integer.parseInt(req.getParameter("id"));

            colorDao.save(new Color(
                    id,
                    req.getParameter("name"),
                    req.getParameter("code")
            ));

        } else if ("size".equals(type)) {

            int id = req.getParameter("id").isEmpty()
                    ? 0
                    : Integer.parseInt(req.getParameter("id"));

            sizeDao.save(new Size(
                    id,
                    req.getParameter("name")
            ));
        }

        resp.sendRedirect("admin-attribute");
    }
}
