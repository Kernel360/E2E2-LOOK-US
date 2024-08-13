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
		UserEntity userEntity = getUser(email);

		isUserAccountDeactivated(userEntity);

		if (profileImage != null && !profileImage.isEmpty()) {
			StorageSaveResult storageSaveResult = imageStorageManager.saveResource(profileImage,
				StorageType.LOCAL_FILE_SYSTEM);
			userEntity.updateProfileImage(storageSaveResult.resourceLocationId());
		}

		if(updateRequest != null) {
			userEntity.updateDetails(updateRequest.birth(), updateRequest.instaId(), updateRequest.nickName(),
				updateRequest.gender());
		}

		// UserEntity savedUser = userRepository.save(user);
		return UserDto.UserUpdateResponse.toDto(userEntity);

	}

	public Long saveUser(UserDto.UserCreateRequest addUserRequest) {
		UserEntity userEntity = getUser(addUserRequest.email());

		if (userEntity.getUserStatus() == UserStatus.USER_STATUS_DEACTIVATE) {
			userEntity.setUserStatus(UserStatus.USER_STATUS_ACTIVATE);
			userRepository.save(userEntity);

			return userEntity.getUserId();
		}

		return userRepository.save(UserEntity.builder()
			.email(addUserRequest.email())
			.password(bCryptPasswordEncoder.encode(addUserRequest.password()))
			.build()).getUserId();
	}

	@Transactional(readOnly = true)
	public UserDto.UserGetInfoResponse getMyInfo(String email) {
		UserEntity userEntity = getUser(email);

		isUserAccountDeactivated(userEntity);

		return UserDto.UserGetInfoResponse.toDto(userEntity,  userRepository.postCount(userEntity));
	}

	@Transactional(readOnly = true)
	public UserEntity findById(Long userId) {
		UserEntity userEntity = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Unexpected user"));

		isUserAccountDeactivated(userEntity);

		return userEntity;
	}

	@Transactional(readOnly = true)
	public UserEntity findByEmail(String email) {
		UserEntity userEntity = getUser(email);

		isUserAccountDeactivated(userEntity);

		return userEntity;
	}

	@Transactional(readOnly = true)
	public List<UserDto.UserGetPostsResponse> getMyPosts(String email) {
		UserEntity userEntity = getUser(email);

		return userRepository.postList(userEntity);
	}

	public void resignUser(String email, HttpServletRequest request, HttpServletResponse response) {
		UserEntity userEntity = findByEmail(email);
		userEntity.setUserStatus(UserStatus.USER_STATUS_DEACTIVATE);

		CookieUtil.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
		CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
	}


	private UserEntity getUser(String email){
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
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
