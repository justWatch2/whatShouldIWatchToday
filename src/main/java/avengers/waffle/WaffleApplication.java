package avengers.waffle;

<<<<<<< HEAD
import lombok.extern.slf4j.Slf4j;
=======
import org.mybatis.spring.annotation.MapperScan;
>>>>>>> 717ac2530a1f92c433767ce7361f6046b03b8ead
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@SpringBootApplication
<<<<<<< HEAD
@EnableJpaAuditing
=======
@MapperScan("avengers.waffle.mapper")
>>>>>>> 717ac2530a1f92c433767ce7361f6046b03b8ead
public class WaffleApplication {

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(WaffleApplication.class, args);
    }

}
