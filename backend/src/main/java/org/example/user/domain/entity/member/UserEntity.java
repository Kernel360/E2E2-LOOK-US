package org.example.user.domain.entity.member;

import java.util.Collection;
import java.util.List;

import org.example.user.domain.entity.BaseEntity;
import org.example.user.domain.enums.Gender;
import org.example.user.domain.enums.Role;
import org.example.user.domain.enums.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class UserEntity extends BaseEntity implements UserDetails { // UserDetails를 상속받아 인증 객체로 사용

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", updatable = false)
	private Long userId;

	// 사용자 이름
	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private String birth;

	private String nickname;

	private String instaId;

	@Enumerated(EnumType.STRING)
	private Role role;

	private Long profileImageLocationId;

	private String provider;

	private String providerId;

	@Enumerated(EnumType.STRING)
	private UserStatus userStatus = UserStatus.USER_STATUS_ACTIVATE;

	@Builder
	public UserEntity(String username, String password, String email, Gender gender, String birth, String nickname,
		String instaId, Role role, Long profileImageLocationId, String provider, String providerId) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.gender = gender;
		this.birth = birth;
		this.nickname = nickname;
		this.instaId = instaId;
		this.role = role;
		this.profileImageLocationId = profileImageLocationId;
		this.provider = provider;
		this.providerId = providerId;
	}

	// 사용자 이름 변경
	public UserEntity updateUsername(String username) {
		this.username = username;
		return this;
	}

	// 사용자 상세 정보 업데이트
	public void updateDetails(String birth, String instaId, String nickname, Gender gender) {
		this.birth = birth;
		this.instaId = instaId;
		this.nickname = nickname;
		this.gender = gender;
	}

	// 사용자 계정 [ 활성화 | 비활성화 ] 업데이트
	public void updateUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	// 이미지도 업데이트
	public void updateProfileImage(Long profileImageLocationId) {
		this.profileImageLocationId = profileImageLocationId;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.role.name()));
	}

	// NOTE: security의 username을 email로 처리하도록 하기 위함입니다.
	@Override
	public String getUsername() {
		return email;
	}

	// 사용자의 패스워드 반환
	@Override
	public String getPassword() {
		return password;
	}

	// 계정 만료 여부 반환
	@Override
	public boolean isAccountNonExpired() {
		// 만료 되었는지 확인하는 로직
		return true; // true -> 만료되지 않았음
	}

	// 계정 잠금 여부 반환
	@Override
	public boolean isAccountNonLocked() {
		// 계정 잠금되었는지 확인하는 로직
		return true; // true -> 잠금되지 않았음
	}

	// 패스워드의 만료 여부 반환
	@Override
	public boolean isCredentialsNonExpired() {
		// 패스워드가 만료되었는지 확인하는 로직
		return true; // true -> 만료되지 않음
	}

	// 계정 사용 가능 여부 반환
	@Override
	public boolean isEnabled() {
		// 계정이 사용가능한지 확인하는 로직
		return true; // true -> 사용 가능
	}

	public boolean equals(UserEntity user) {
		return userId.equals(user.getUserId());
	}
}
