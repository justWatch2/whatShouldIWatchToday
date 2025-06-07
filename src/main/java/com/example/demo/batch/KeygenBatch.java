package com.example.demo.batch;

import com.example.demo.entity.AfterEntity;
import com.example.demo.entity.Keywords;
import com.example.demo.entity.NewMovies;
import com.example.demo.repository.AfterRepository;
import com.example.demo.repository.BeforeRepository;
import com.example.demo.repository.KeywordsRepository;
import com.example.demo.repository.NewMoviesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.*;

@Configuration
@RequiredArgsConstructor
public class KeygenBatch {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager; // 배치 중 오류 발생 시 자동으로 롤백
    private final NewMoviesRepository newMoviesRepository;
    private final KeywordsRepository keywordsRepository;

    @Bean
    public Job keyjob() {
        return new JobBuilder("keyJob", jobRepository)
                .start(keyStep())
                .build();
    }

    @Bean
    public Step keyStep() {
        return new StepBuilder("keyStep", jobRepository)
                .<NewMovies, List<Keywords>>chunk(100, transactionManager) // chunk: 한번에 읽어올 데이터 수량
                .reader(keyReader())
                .processor(keyProcessor())
                .writer(keyWriter())
                .taskExecutor(keytaskExecutor())
                .throttleLimit(3)
                .build();
    }

    @Bean
    public ThreadPoolTaskExecutor keytaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("keytaskExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public RepositoryItemReader<NewMovies> keyReader() {
        return new RepositoryItemReaderBuilder<NewMovies>()
                .name("keyReader")
                .pageSize(100)
                .methodName("findAll")
                .repository(newMoviesRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<NewMovies, List<Keywords>> keyProcessor() {
        return new ItemProcessor<NewMovies, List<Keywords>>() {
            @Override
            public List<Keywords> process(NewMovies item) throws Exception {
                List<Keywords> keywords = new ArrayList<>();
                if (!Objects.equals(item.getKeywords(), "")) {
                    StringTokenizer tokenizer = new StringTokenizer(item.getKeywords(), ",");
                    while (tokenizer.hasMoreTokens()) {
                        String keyW = tokenizer.nextToken();
                        if (!keyW.isEmpty()) {
                            Keywords keyword = Keywords.builder()
                                    .id(item.getId())
                                    .keyword(tokenizer.nextToken())
                                    .build();
                            keywords.add(keyword);
                        }
                    }
                }
                return keywords;
            }
        };
    }

    @Bean
    public ItemWriter<List<Keywords>> keyWriter() {
        return new ItemWriter<List<Keywords>>() {

            @Override
            public void write(Chunk<? extends List<Keywords>> chunk) throws Exception {
                for (List<Keywords> keyword : chunk) {
                    keywordsRepository.saveAll(keyword);
                }
            }
        };
    }
}
