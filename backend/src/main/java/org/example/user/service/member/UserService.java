package org.example.user.service.member;

import static org.example.config.oauth.OAuth2SuccessHandler.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.image.imageStorageManager.ImageStorageManager;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.example.log.LogExecution;
import org.example.user.domain.dto.UserDto;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Role;
import org.example.user.domain.enums.UserStatus;
import org.example.user.repository.member.UserRepository;
import org.example.util.CookieUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final ImageStorageManager imageStorageManager;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@LogExecution
	public UserDto.UserUpdateResponse updateUser(
		UserDto.UserUpdateRequest updateRequest,
		String email,
		MultipartFile profileImage
	) throws IOException {
		UserEntity user = this.getUserByEmail(email);
		AssertThat_UserAccountIsActive(user);

		// TODO: image storage는 ApiException이 아닌 자체적 Exception을 던지도록
		//       변경될 예정입니다. 후추 try-catch로 변경해야 합니다.
		if (profileImage != null && !profileImage.isEmpty()) {
			StorageSaveResult storageSaveResult = imageStorageManager.saveImage(
				profileImage, StorageType.LOCAL_FILE_SYSTEM
			);

			user.updateProfileImage(storageSaveResult.imageLocationId());
		}

		if(updateRequest != null) {
			user.updateDetails(
				updateRequest.birth(),
				updateRequest.instaId(),
				updateRequest.nickName(),
				updateRequest.gender()
			);
		}

		return UserDto.UserUpdateResponse.toDto(user);
	}

	@LogExecution
	public void signupUser(UserDto.UserCreateRequest addUserRequest) {

		userRepository.save(UserEntity.builder()
			.email(addUserRequest.email())
			.password(bCryptPasswordEncoder.encode(addUserRequest.password()))
			.role(Role.ROLE_ADMIN)
			.build());
	}

	@LogExecution
	public UserDto.UserResponse loginUser(UserDto.UserLoginRequest loginRequest) {
			UserEntity user = getUserByEmail(loginRequest.email());

			if (user != null && bCryptPasswordEncoder.matches(loginRequest.password(), user.getPassword())) {
				// 로그인 성공 시 SecurityContextHolder에 권한 부여
				Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
				Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
				SecurityContext context = SecurityContextHolder.getContext();

				context.setAuthentication(authentication);

				System.out.println("context = " + context);



				return new UserDto.UserResponse(user.getEmail(), user.getRole().name());
			}

			return null; // 로그인 실패 시 null 반환
		}

	@LogExecution
	@Transactional(readOnly = true)
	public UserDto.UserGetInfoResponse getMyInfo(String email) {
		UserEntity user = this.getUserByEmail_internal(email);
		AssertThat_UserAccountIsActive(user);

		return UserDto.UserGetInfoResponse.toDto(user,  userRepository.postCount(user));
	}

	@LogExecution
	@Transactional(readOnly = true)
	public List<UserDto.UserGetPostsResponse> getMyPosts(String email) {
		UserEntity user = this.getUserByEmail_internal(email);
		AssertThat_UserAccountIsActive(user);

		return userRepository.postList(user);
	}

	@LogExecution
	public void resignUser(String email, HttpServletRequest request, HttpServletResponse response) {
		// 이미 탈퇴한 사용자에 대한 탈퇴 요청은 비정상 Request 입니다.
		UserEntity user = this.getUserByEmail_internal(email);
		AssertThat_UserAccountIsActive(user);

		user.updateUserStatus(UserStatus.USER_STATUS_DEACTIVATE);
		this.logoutUser(email, request, response);
	}

	@LogExecution
	public void logoutUser(String email, HttpServletRequest request, HttpServletResponse response) {
		// 이미 탈퇴한 사용자에 대한 로그아웃 요청은 비정상 Request 입니다.
		UserEntity user = this.getUserByEmail_internal(email);
		AssertThat_UserAccountIsActive(user);

		// 로그-아웃 처리를 위해, 클라이언트 브라우저에서 토큰 쿠키를 삭제합니다.
		CookieUtil.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
		CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
	}

	/**
	 * NOTE: 만약 비활성화된 사용자를 검색한 경우, ApiUserException을 발생시킵니다.
	 */
	@Transactional(readOnly = true)
	@LogExecution
	public UserEntity getUserById(Long userId) throws ApiUserException {
		UserEntity user = getUserById_internal(userId);
		AssertThat_UserAccountIsActive(user);

		return user;
	}

	/**
	 * NOTE: 만약 비활성화된 사용자를 검색한 경우, ApiUserException을 발생시킵니다.
	 */
	@Transactional(readOnly = true)
	@LogExecution
	public UserEntity getUserByEmail(String email) throws ApiUserException {
		UserEntity user = getUserByEmail_internal(email);
		AssertThat_UserAccountIsActive(user);

		return user;
	}

	// Private Helper Methods ----------------------------------------------------

	/**
	 * NOTE: DEACTIVATED 처리된 사용자도 검색에 포함됩니다.
	 */
	private UserEntity getUserById_internal(Long userId) throws ApiUserException {
		return userRepository.findById(userId)
							 .orElseThrow(() -> ApiUserException.builder()
								 .category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
								 .subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
								 .build());
	}

	/**
	 * NOTE: DEACTIVATED 처리된 사용자도 검색에 포함됩니다.
	 */
	private UserEntity getUserByEmail_internal(String email) throws ApiUserException {
		return userRepository.findByEmail(email)
							 .orElseThrow(() -> ApiUserException.builder()
								 .category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
								 .subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
								 .build()
							 );
	}

	private void AssertThat_UserAccountIsActive(UserEntity userEntity) throws ApiUserException {
		if (userEntity.getUserStatus() == UserStatus.USER_STATUS_DEACTIVATE) {
			throw ApiUserException.builder()
				.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
				.subCategory(ApiUserErrorSubCategory.USER_DEACTIVATE)
				.build();
		}
	}
}
