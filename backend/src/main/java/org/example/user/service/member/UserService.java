package org.example.user.service.member;

import static org.example.config.oauth.OAuth2SuccessHandler.*;

import java.util.List;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.user.ApiUserErrorSubCategory;
import org.example.exception.user.ApiUserException;
import org.example.image.storage.core.StorageType;
import org.example.image.storageManager.common.StorageSaveResult;
import org.example.image.storageManager.imageStorageManager.ImageStorageManager;
import org.example.user.domain.dto.UserDto;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.UserStatus;
import org.example.user.repository.member.UserRepository;
import org.example.util.CookieUtil;
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

	public UserDto.UserUpdateResponse updateUser(
		UserDto.UserUpdateRequest updateRequest,
		String email,
		MultipartFile profileImage
	) {
		UserEntity user = getUser(email);

		isUserAccountDeactivated(user);

		if (profileImage != null && !profileImage.isEmpty()) {
			StorageSaveResult storageSaveResult = imageStorageManager.saveResource(profileImage,
				StorageType.LOCAL_FILE_SYSTEM);
			user.updateProfileImage(storageSaveResult.resourceLocationId());
		}

		if(updateRequest != null) {
			user.updateDetails(updateRequest.birth(), updateRequest.instaId(), updateRequest.nickName(),
				updateRequest.gender());
		}

		// UserEntity savedUser = userRepository.save(user);
		return UserDto.UserUpdateResponse.toDto(user);

	}

	public Long saveUser(UserDto.UserCreateRequest addUserRequest) {
		UserEntity user = getUser(addUserRequest.email());

		if (user.getUserStatus() == UserStatus.USER_STATUS_DEACTIVATE) {
			user.setUserStatus(UserStatus.USER_STATUS_ACTIVATE);
			userRepository.save(user);

			return user.getUserId();
		}

		return userRepository.save(UserEntity.builder()
			.email(addUserRequest.email())
			.password(bCryptPasswordEncoder.encode(addUserRequest.password()))
			.build()).getUserId();
	}

	@Transactional(readOnly = true)
	public UserDto.UserGetInfoResponse getMyInfo(String email) {
		UserEntity user = getUser(email);

		isUserAccountDeactivated(user);

		return UserDto.UserGetInfoResponse.toDto(user,  userRepository.postCount(user));
	}

	@Transactional(readOnly = true)
	public UserEntity findById(Long userId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> ApiUserException.builder()
				.category(ApiErrorCategory.RESOURCE_BAD_REQUEST)
				.subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
				.setErrorData(() -> "찾을 수 없는 유저입니다.")
				.build());

		isUserAccountDeactivated(user);

		return user;
	}

	@Transactional(readOnly = true)
	public UserEntity findByEmail(String email) {
		UserEntity user = getUser(email);

		isUserAccountDeactivated(user);

		return user;
	}

	@Transactional(readOnly = true)
	public List<UserDto.UserGetPostsResponse> getMyPosts(String email) {
		UserEntity user = getUser(email);

		return userRepository.postList(user);
	}

	public void resignUser(String email, HttpServletRequest request, HttpServletResponse response) {
		UserEntity user = findByEmail(email);
		user.setUserStatus(UserStatus.USER_STATUS_DEACTIVATE);

		CookieUtil.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
		CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
	}


	private UserEntity getUser(String email){
		return userRepository.findByEmail(email)
							 .orElseThrow(
								 () -> ApiUserException.builder()
									 .category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
									 .subCategory(ApiUserErrorSubCategory.USER_NOT_FOUND)
									 .build()
							 );
	}

	private void isUserAccountDeactivated(UserEntity userEntity) {
		if (userEntity.getUserStatus() == UserStatus.USER_STATUS_DEACTIVATE) {
			throw ApiUserException.builder()
				.category(ApiErrorCategory.RESOURCE_INACCESSIBLE)
				.subCategory(ApiUserErrorSubCategory.USER_DEACTIVATE)
				.setErrorData(() -> ("입력된 이메일을 다시 확인하세요" + userEntity.getEmail()))
				.build();
		}
	}
}
