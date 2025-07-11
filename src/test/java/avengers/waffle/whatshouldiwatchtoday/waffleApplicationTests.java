package avengers.waffle.whatshouldiwatchtoday;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@SpringBootTest
public class waffleApplicationTests {

    @Value("${file.uploadPath}")
    private String uploadPath;

    @Test
    void contextLoads() {
        String imageUrl = "https://lh3.googleusercontent.com/a/ACg8ocJl2uSf7REwxOnQc3egmEuoLP-NqfEeO9sfoZjDtS_3n6C7wsc=s96-c";
        UUID uid = UUID.randomUUID();
        String saveName = uid.toString();

        String OUTPUT_FILE_PATH = uploadPath + "/" +saveName+ ".png";
        String FILE_URL = imageUrl;

        try (InputStream in = new URL(FILE_URL).openStream()) {
            Path imagePath = Paths.get(OUTPUT_FILE_PATH);
            Files.createDirectories(imagePath.getParent()); // 폴더 없으면 생성
            Files.copy(in, imagePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("✅ 저장 완료: " + imagePath.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("/img/download?filename=" + saveName);
    }

}


