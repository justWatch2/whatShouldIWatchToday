package avengers.waffle.configuration.querydsl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class QuerydslConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
////                .allowedOrigins("http://192.168.0.72:3000") // React 포트
////                .allowedOrigins("http://localhost:3000")
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                .allowedHeaders("*")
//                .allowedOriginPatterns("*")
//                .allowCredentials(true);
//    }
    @PersistenceContext
    EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }




}
