package avengers.waffle.service.impl.mypage;

import avengers.waffle.dto.userDTO.MyProfileResponseDTO;
import avengers.waffle.dto.userDTO.ProfileUpdateRequestDTO;
import avengers.waffle.entity.Member;
import avengers.waffle.repository.mypageRepository.If_MypageService;
import avengers.waffle.repository.posts.MovieMemberRepository;
import avengers.waffle.utils.FileDataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageMember implements If_MypageService {
    private final MovieMemberRepository movieMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileDataUtil fileDataUtil;

    @Override
    @Transactional(readOnly = true)
    public MyProfileResponseDTO getMyProfile(String userId) {
        // 1. Repository를 사용해 DB에서 유저 정보를 찾습니다.
        Member member = movieMemberRepository.findByMemberId(userId);

        // 2. orElseThrow 대신, if문을 사용해 member가 null인지 직접 확인합니다.
        if (member == null) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다: " + userId);
        }

        // 3. 찾은 유저 정보를 DTO로 변환하여 반환합니다.
        return MyProfileResponseDTO.fromEntity(member);
    }

    @Override
    @Transactional
    public void updateMyProfile(String userId, ProfileUpdateRequestDTO requestDTO, MultipartFile imageFile) {

        Member member = movieMemberRepository.findByMemberId(userId);
        if (member == null) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다: " + userId);
        }

        if (!passwordEncoder.matches(requestDTO.getCurrentPassword(), member.getMemberPw())) {
            throw new BadCredentialsException("기존 비밀번호가 일치하지 않습니다.");
        }

        try {
            String newImageUrl = member.getImgUrl();
            if (imageFile != null && !imageFile.isEmpty()) {
                //이미있던 url
                String oldImageUrl = member.getImgUrl();

                String[] uploadedFiles = fileDataUtil.fileUpload(new MultipartFile[]{imageFile});
                if (uploadedFiles.length > 0 && uploadedFiles[0] != null) {
                    newImageUrl = uploadedFiles[0];
                }
                if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                    fileDataUtil.deleteFile(oldImageUrl);
                }
            }

            String newPassword = member.getMemberPw();
            if (requestDTO.getPassword() != null && !requestDTO.getPassword().isEmpty()) {
                newPassword = passwordEncoder.encode(requestDTO.getPassword());
            }

            member.updateProfile(
                    requestDTO.getMemberName(),
                    newPassword,
                    newImageUrl
            );

            movieMemberRepository.save(member);


        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 수정 중 파일 처리 오류가 발생했습니다.", e);
        }
    }
}

