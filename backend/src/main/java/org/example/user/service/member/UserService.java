package org.example.user.service.member;

import org.example.image.storage.core.StorageType;
import org.example.image.storageManager.common.StorageSaveResult;
import org.example.image.storageManager.imageStorageManager.ImageStorageManager;
import org.example.user.domain.dto.UserDto;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.repository.member.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final ImageStorageManager imageStorageManager;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserDto.UserUpdateResponse updateUser(UserDto.UserUpdateRequest updateRequest, String email,
		MultipartFile profileImage) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("User not found"));

		if (profileImage != null && !profileImage.isEmpty()) {
			StorageSaveResult storageSaveResult = imageStorageManager.saveResource(profileImage,
				StorageType.LOCAL_FILE_SYSTEM);
			user.updateProfileImage(storageSaveResult.resourceLocationId());
		}

		user.updateDetails(updateRequest.birth(), updateRequest.instaId(), updateRequest.nickName(),
			updateRequest.gender());

		// UserEntity savedUser = userRepository.save(user);
		return UserDto.UserUpdateResponse.toDto(user);

	}

	public Long saveUser(UserDto.UserCreateRequest addUserRequest) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return userRepository.save(UserEntity.builder()
			.email(addUserRequest.email())
			.password(bCryptPasswordEncoder.encode(addUserRequest.password()))
			.build()).getUserId();
	}

	@Transactional(readOnly = true)
	public UserEntity findById(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
	}

	@Transactional(readOnly = true)
	public UserEntity findByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new IllegalArgumentException("unexpected user"));
	}

}
