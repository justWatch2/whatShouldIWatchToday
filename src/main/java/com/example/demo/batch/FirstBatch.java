package com.example.demo.batch;

import com.example.demo.entity.AfterEntity;
import com.example.demo.entity.BeforeEntity;
import com.example.demo.repository.AfterRepository;
import com.example.demo.repository.BeforeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class FirstBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager; // 배치 중 오류 발생 시 자동으로 롤백
    private final BeforeRepository beforeRepository;
    private final AfterRepository afterRepository;

    @Bean
    public Job firstjob() {
        return new JobBuilder("firstJob", jobRepository)
                .start(firstStep())
                .build();
    }

    @Bean
    public Step firstStep() {
        return new StepBuilder("keyStep", jobRepository)
                .<BeforeEntity, AfterEntity>chunk(40, transactionManager) // chunk: 한번에 읽어올 데이터 수량
                .reader(beforeReader())
                .processor(beforeProcessor())
                .writer(afterWriter())
                .taskExecutor(taskExecutor())
                .throttleLimit(3)
                .build();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("keytaskExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public RepositoryItemReader<BeforeEntity> beforeReader() {
        return new RepositoryItemReaderBuilder<BeforeEntity>()
                .name("beforeReader")
                .pageSize(40)
                .methodName("findAll")
                .repository(beforeRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<BeforeEntity, AfterEntity> beforeProcessor() {
        return new ItemProcessor<BeforeEntity, AfterEntity>() {
            @Override
            public AfterEntity process(BeforeEntity item) throws Exception {
                System.out.println("id: " + item.getId());
                String url = "https://api.themoviedb.org/3/movie/" + item.getId() + "?language=ko-KR";
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("accept", "application/json")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzMDE0MGMyMjgwZGE4MjlhMTg5YjIxYTc1MDZkNDgxZCIsIm5iZiI6MTc0ODQwNzE2MS4zNzIsInN1YiI6IjY4MzY5Mzc5NGZmOGNkMGZjNzczMDkzZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.mpNZFN2HTc2YWMebsbAdz5XLSD28dqhldxBaW8Zzgdo")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        long middleTime = System.currentTimeMillis();
                System.out.println(response.body());
                if (response.statusCode() != 200) {
                    System.out.println("상태 코드: " + response.statusCode());
                }
                JSONObject obj = new JSONObject(response.body());
                JSONArray obj2 = obj.optJSONArray("genres");
                StringBuilder genres = new StringBuilder();
                if(obj2 != null) {
                    for (int i = 0; i < obj2.length(); i++) {
                        genres.append(obj2.getJSONObject(i).optString("name")).append(", ");
                    }
                }
                LocalDate sampleDate;
                if (obj.optString("release_data") == null || obj.optString("release_date").trim().isEmpty()) {
                    sampleDate = LocalDate.parse("1111-11-11");
                }else{
                    sampleDate = LocalDate.parse(obj.optString("release_date"));
                }
                AfterEntity afterEntity = AfterEntity.builder()
                        .id(obj.optInt("id"))
                        .releaseDate(sampleDate)  // 기본값 필요해서 임의 날짜 지정
                        .runtime(obj.optInt("runtime"))
                        .backdropPath(obj.optString("backdrop_path", null))
                        .imdbId(obj.optString("imdb_id", null))
                        .originalLanguage(obj.optString("original_language", null))
                        .originalTitle(obj.optString("original_title", null))
                        .overview(obj.optString("overview", null))
                        .posterPath(obj.optString("poster_path", null))
                        .tagline(obj.optString("tagline", null))
                        .genres(genres.toString())
                        .koreanTitle(obj.optString("title", null))
                        .build();
                return afterEntity;
            }
        };
    }

    @Bean
    public RepositoryItemWriter<AfterEntity> afterWriter() {
        return new RepositoryItemWriterBuilder<AfterEntity>()
                .repository(afterRepository)
                .methodName("save")
                .build();
    }
}
