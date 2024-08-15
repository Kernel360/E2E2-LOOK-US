package org.example.config.oauth;

import org.example.user.common.RandomName;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Role;
import org.example.user.repository.member.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomName randomName;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest); // ❶ 요청을 바탕으로 유저 정보를 담은 객체 반환
        saveOrUpdate(user);

        return user; //사용자 객체는 식별자, 이름, 이메일, 프로필 사진 링크 등의 정보를 담고 있다
    }

    // ❷ 유저가 있으면 업데이트, 없으면 유저 생성
    private UserEntity saveOrUpdate(OAuth2User oAuth2User) {
        System.out.println("getAttributes:" + oAuth2User.getAttributes());
        OAuth2UserInfo oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());

        String provider = oAuth2UserInfo.getProvider(); //google
        String providerId = oAuth2UserInfo.getProviderId(); //googleId
        String username = oAuth2UserInfo.getUsername();
        String email = oAuth2UserInfo.getEmail();
        String password = passwordEncoder.encode("겟인데어");
        String nickname = randomName.setsNickName();

        UserEntity user = userRepository.findByEmail(email)
                .map(entity -> entity.updateUsername(username))
                .orElse(UserEntity.builder()
                        .username(username)
                        .password(password)
                        .email(email)
                        .nickname(nickname)
                        .provider(provider)
                        .providerId(providerId)
						.role(Role.ROLE_USER)
                        .build());

        return userRepository.save(user);
    }

}
