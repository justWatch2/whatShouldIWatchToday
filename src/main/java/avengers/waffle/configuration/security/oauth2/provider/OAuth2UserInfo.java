package avengers.waffle.configuration.security.oauth2.provider;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
