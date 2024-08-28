package org.example.user.service.member;

import org.example.config.log.LogExecution;
import org.example.user.repository.member.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
// 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	// 사용자 이름 (email)으로 사용자의 정보를 가져오는 메서드
	@Override
	@LogExecution
	public UserDetails loadUserByUsername(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException(email));
	}
}
