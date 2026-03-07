package controller;

import jakarta.servlet.http.Part; // PHẢI dùng servlet.http.Part
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class FileUtil {
    public static String saveFile(Part part, String uploadPath) throws IOException {
        String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String uniqueName = System.currentTimeMillis() + "_" + fileName;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        part.write(uploadPath + File.separator + uniqueName);

        return "img/products/" + uniqueName;
    }
}