package org.example.user.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Role;
import org.example.user.repository.member.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailService userDetailService;

	private final UserEntity defaultUser =
		UserEntity.builder()
			.email("test@example.com")
			.password("password")
			.role(Role.ROLE_USER)
			.build();

	@Test
	public void loadUserByUsername_Success() {
		//given
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(defaultUser));

		// when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(defaultUser));
		//when
		UserDetails userDetails = userDetailService.loadUserByUsername("test@example.com");

		//then

		assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
		assertThat(userDetails.getPassword()).isEqualTo("password");
		assertThat(userDetails.getAuthorities()).extracting("authority").contains("ROLE_USER");

	}

	@Test
	public void loadUserByUsername_Fail() {
		//given

		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.empty());

		//when, then
		assertThatThrownBy(() -> {
			userDetailService.loadUserByUsername("no@gmail.com");
		}).isInstanceOf(IllegalArgumentException.class);
	}
}