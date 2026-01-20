package controller;

import jakarta.servlet.http.Part; // PHẢI dùng servlet.http.Part
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FileUtil {
    public static String saveFile(Part part, String uploadPath) throws IOException {
        // Lấy tên file gốc
        String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        // Tạo tên duy nhất bằng timestamp để tránh trùng
        String uniqueName = System.currentTimeMillis() + "_" + fileName;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        // Ghi file vào thư mục vật lý
        part.write(uploadPath + File.separator + uniqueName);

        // Trả về đường dẫn tương đối để lưu vào DB
        return "img/products/" + uniqueName;
    }
}