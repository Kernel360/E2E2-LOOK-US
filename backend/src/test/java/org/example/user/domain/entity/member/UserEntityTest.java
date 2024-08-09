package org.example.user.domain.entity.member;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;

import org.example.user.domain.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserEntityTest {

	private UserEntity user;

	@BeforeEach
	public void setUp() {
		user = UserEntity.builder()
			.username("testUser")
			.password("password")
			.email("test@example.com")
			.gender(Gender.GENDER_MAN)
			// .role(Role.ROLE_USER)
			.build();
	}

	@DisplayName("사용자가 가지고 있는 권한의 목록을 반환 ")
	@Test
	public void testGetAuthorities() {
		Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
		assertThat(authorities).hasSize(2);
		// 각 권한이 포함되어 있는지 검증
		assertThat(authorities).extracting(GrantedAuthority::getAuthority)
			.containsExactlyInAnyOrder("USER", "ADMIN");
	}

	@DisplayName("user update test")
	@Test
	public void testUpdateDetails() {
		user.updateDetails("2000-01-01", "instaId", "nickname", Gender.GENDER_WOMAN);
		assertThat(user.getBirth()).isEqualTo("2000-01-01");
		assertThat(user.getInstaId()).isEqualTo("instaId");
		assertThat(user.getNickname()).isEqualTo("nickname");
		assertThat(user.getGender()).isEqualTo(Gender.GENDER_WOMAN);
	}

	@DisplayName("update profile image test")
	@Test
	public void testUpdateProfileImage() {
		Long newProfileImageId = 12345L;
		user.updateProfileImage(newProfileImageId);
		assertThat(user.getProfileImageId()).isEqualTo(newProfileImageId);
	}

	@DisplayName("로그인 후 UserDetails에 저장된 user 정보가 맞는지 확인")
	@Test
	public void testUserDetailsMethods() {
		assertThat(user.getUsername()).isEqualTo("test@example.com");
		assertThat(user.getPassword()).isEqualTo("password");
		assertThat(user.isAccountNonExpired()).isTrue();
		assertThat(user.isAccountNonLocked()).isTrue();
		assertThat(user.isCredentialsNonExpired()).isTrue();
		assertThat(user.isEnabled()).isTrue();
	}
}
