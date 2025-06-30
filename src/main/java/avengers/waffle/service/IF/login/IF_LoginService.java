package avengers.waffle.service.IF.login;

import avengers.waffle.VO.login.MemberDTO;

public interface IF_LoginService {
    public void saveMember(MemberDTO memberDTO, String s);

    String getImgUrl(String id);
}
