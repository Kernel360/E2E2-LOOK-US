package org.example.user.service.member;

import org.example.user.domain.dto.request.member.AddUserRequest;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	public Long saveUser(AddUserRequest addUserRequest) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return userRepository.save(UserEntity.builder()
			.username(addUserRequest.getUsername())
			.password(encoder.encode(addUserRequest.getPassword()))
			.build()).getUserId();
	}

	public UserEntity findById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
	}

	public UserEntity findByUsername(String username) {
		System.out.println("Searching for user: " + username);
		return userRepository.findByUsername(username)
			.orElseThrow(() -> new IllegalArgumentException("unexpected user"));
	}

}
//    // 이 메서드는 새 사용자를 생성하고 저장합니다. 실제 사용 시 비밀번호 등 추가 정보 처리가 필요할 수 있습니다.
//    private User createUser(String username) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        User newUser = User.builder()
//                .username(username)
//                // 임시 비밀번호 설정; 실제 상황에서는 보다 안전한 접근이 필요합니다.
//                .password(encoder.encode("defaultPassword"))
//                .avatarUrl("image")
//                .build();
//        return userRepository.save(newUser);
//    }
