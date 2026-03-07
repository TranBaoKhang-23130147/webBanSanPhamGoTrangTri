package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
    public static Connection getConnection() throws Exception {
        String url = "jdbc:mysql://localhost:3306/webbansanphamgotrangtri_db";
        String user = "root";
        String password = "";

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        try {
            DBContext db = new DBContext();
            Connection conn = db.getConnection();
            if (conn != null) {
                System.out.println("Kết nối thành công");
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }


}
