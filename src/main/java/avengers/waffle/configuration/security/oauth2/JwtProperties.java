package avengers.waffle.configuration.security.oauth2;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret;
    private int expirationTime;
    private String header;

    // Getter/Setter
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getExpirationTime() {
        return expirationTime;
    }
    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getHeader() {
        return header;
    }
    public void setHeader(String header) {
        this.header = header;
    }
}
