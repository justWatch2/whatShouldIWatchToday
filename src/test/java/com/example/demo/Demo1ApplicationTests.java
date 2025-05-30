package com.example.demo;

import com.example.demo.entity.AfterEntity;
import com.example.demo.repository.AfterRepository;
import com.example.demo.repository.BeforeRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@SpringBootTest
class Demo1ApplicationTests {

    @Test
    void contextLoads() throws IOException, InterruptedException, JSONException {
//        long startTime = System.currentTimeMillis();
//        String url = "https://api.themoviedb.org/3/movie/" + 9502 + "?language=ko-KR";
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(url))
//                .header("accept", "application/json")
//                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzMDE0MGMyMjgwZGE4MjlhMTg5YjIxYTc1MDZkNDgxZCIsIm5iZiI6MTc0ODQwNzE2MS4zNzIsInN1YiI6IjY4MzY5Mzc5NGZmOGNkMGZjNzczMDkzZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.mpNZFN2HTc2YWMebsbAdz5XLSD28dqhldxBaW8Zzgdo")
//                .method("GET", HttpRequest.BodyPublishers.noBody())
//                .build();
//        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
////        long middleTime = System.currentTimeMillis();
//        System.out.println(response.body());
//        JSONObject obj = new JSONObject(response.body());
//        JSONArray obj2 = new JSONArray(obj.getString("genres"));
//        StringBuilder genres = new StringBuilder();
//
//        for (int i = 0; i < obj2.length(); i++) {
//            genres.append(obj2.getJSONObject(i).getString("name")).append(", ");
//        }
//        AfterEntity afterEntity = AfterEntity.builder()
//                .id(obj.getInt("id"))
//                .releaseDate(LocalDate.parse(obj.getString("release_date")))
//                .runtime(obj.getInt("runtime"))
//                .backdropPath(obj.getString("backdrop_path"))
//                .imdbId(obj.getString("imdb_id"))
//                .originalLanguage(obj.getString("original_language"))
//                .originalTitle(obj.getString("original_title"))
//                .overview(obj.getString("overview"))
//                .posterPath(obj.getString("poster_path"))
//                .tagline(obj.getString("tagline"))
//                .genres(genres.toString())
//                .koreanTitle(obj.getString("title"))
//                .build();
//        System.out.println(print(afterEntity));
//        long endTime = System.currentTimeMillis();
//        System.out.println(middleTime - startTime);
//        System.out.println(endTime - startTime);
        LocalDate sampleDate = LocalDate.parse("2222-12-11");
        System.out.println(sampleDate);
        JSONObject obj = new JSONObject();
        obj.put("release_date", " ");
        if (obj.optString("release_data") == null || obj.optString("release_date").trim().isEmpty()) {
            sampleDate = LocalDate.parse("1111-11-11");
        }else{
            sampleDate = LocalDate.parse(obj.optString("release_date"));
        }
        System.out.println(sampleDate);

    }

    public String print(AfterEntity afterEntity) {
        StringBuilder result = new StringBuilder();
        result.append(afterEntity.getId()).append("\n");
        result.append(afterEntity.getTitle()).append("\n");
        result.append(afterEntity.getReleaseDate()).append("\n");
        result.append(afterEntity.getRuntime()).append("\n");
        result.append(afterEntity.getBackdropPath()).append("\n");
        result.append(afterEntity.getImdbId()).append("\n");
        result.append(afterEntity.getOriginalLanguage()).append("\n");
        result.append(afterEntity.getOriginalTitle()).append("\n");
        result.append(afterEntity.getOverview()).append("\n");
        result.append(afterEntity.getPosterPath()).append("\n");
        result.append(afterEntity.getTagline()).append("\n");
        result.append(afterEntity.getGenres()).append("\n");
        result.append(afterEntity.getKoreanTitle()).append("\n");
        return result.toString();
    }

}
