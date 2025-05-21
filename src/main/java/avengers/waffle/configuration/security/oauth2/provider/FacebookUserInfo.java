package avengers.waffle.configuration.security.oauth2.provider;

import java.util.Map;

public  class FacebookUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;    //oauth2User.getAttributes() 여기 나오는 리턴값을 받기 위해

    public FacebookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
