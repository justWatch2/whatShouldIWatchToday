package avengers.waffle.configuration.security.oauth2.provider;

import java.util.Map;

public  class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;    //oauth2User.getAttributes() 여기 나오는 리턴값을 받기 위해


    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProfile_image(){
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        if (properties != null && properties.get("profile_image") != null) {
            return properties.get("profile_image").toString();
        }
        return null;
    }

    @Override
    public String getProviderId() {
        return  attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
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
