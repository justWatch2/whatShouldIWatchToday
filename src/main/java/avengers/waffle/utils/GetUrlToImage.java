package avengers.waffle.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class GetUrlToImage {

    @Value("${file.uploadPath}")
    private String uploadPath;

    public String getImageUrl(String imageUrl) throws IOException {

        UUID uid = UUID.randomUUID();
        String saveName = uid.toString()+".png";

        String OUTPUT_FILE_PATH = uploadPath + "/" +saveName;
        String FILE_URL = imageUrl;

        try (InputStream in = new URL(FILE_URL).openStream()) {
            Path imagePath = Paths.get(OUTPUT_FILE_PATH);
            Files.createDirectories(imagePath.getParent()); // 폴더 없으면 생성
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ 저장 완료: " + imagePath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/img/download?filename=" + saveName;
    }
}