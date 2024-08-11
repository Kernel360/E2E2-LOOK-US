package org.example.user.service.member;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.example.image.storage.core.StorageType;
import org.example.image.storageManager.common.StorageSaveResult;
import org.example.image.storageManager.imageStorageManager.ImageStorageManager;
import org.example.user.domain.dto.UserDto;
import org.example.user.domain.entity.member.UserEntity;
import org.example.user.domain.enums.Gender;
import org.example.user.repository.member.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private ImageStorageManager imageStorageManager;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@InjectMocks
	private UserService userService;

	private UserDto.UserCreateRequest getCreateUserRequest(
		String email, String password
	) {
		return new UserDto.UserCreateRequest(email, password);
	}

	private final UserEntity defaultUser =
		UserEntity.builder()
			.email("test@gmail.com")
			.password("password")
			.build();

	private UserDto.UserUpdateRequest getUpdateUserRequest(
		String birth,
		String instaId,
		String nickName,
		Gender gender
	) { return new UserDto.UserUpdateRequest(birth, instaId, nickName, gender);

	}

	@DisplayName("일반 회원가입을 하면 회원이 저장된다.")
	@Test
	public void 회원_저장() {
		given(bCryptPasswordEncoder.encode(anyString())).willReturn("encodedPassword");
		given(userRepository.save(any(UserEntity.class))).willReturn(defaultUser);

		ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);

		userService.saveUser(new UserDto.UserCreateRequest("test@gmail.com", "password"));

		verify(userRepository, times(1)).save(captor.capture());
		UserEntity savedUser = captor.getValue();

		// 검증
		assertEquals("test@gmail.com", savedUser.getEmail());
		assertEquals("encodedPassword", savedUser.getPassword());
	}

	@DisplayName("소셜로그인_성공_후_회원정보_업데이트")
	@Test
	public void testUpdateUser() {
		given(userRepository.findByEmail(anyString()))
			.willReturn(Optional.of(defaultUser));
		given(imageStorageManager.saveResource(any(MultipartFile.class), any()))
			.willReturn(new StorageSaveResult(StorageType.LOCAL_FILE_SYSTEM, 1L));


		UserDto.UserUpdateRequest updateUserRequest = getUpdateUserRequest("2000-01-01", "instaId", "nickname",
			Gender.GENDER_WOMAN);

		MultipartFile profileImage = Mockito.mock(MultipartFile.class);

		userService.updateUser(updateUserRequest, "test@example.com", profileImage);
		System.out.println(defaultUser);
		assertThat(defaultUser.getBirth()).isEqualTo("2000-01-01");
		assertThat(defaultUser.getInstaId()).isEqualTo("instaId");
		assertThat(defaultUser.getNickname()).isEqualTo("nickname");
		assertThat(defaultUser.getGender()).isEqualTo(Gender.GENDER_WOMAN);
		assertThat(defaultUser.getProfileImageId()).isEqualTo(1L);
	}

	// TODO : 나중에 다시 해보기
	@DisplayName("회원 아이디로 검색하기")
	@Test
	public void testFindById_UserExists() {
		//given
		given(userRepository.findById(anyLong()))
			.willReturn(Optional.of(defaultUser));

		ReflectionTestUtils.setField(defaultUser, "userId", 1L);

		System.out.println(defaultUser);
		//when
		UserEntity foundUser = userService.findById(1L);

		//then
		assertThat(foundUser.getUserId()).isEqualTo(1L);
	}

	@DisplayName("회원 아이디가 없을 때 예외발생")
	@Test
	public void testFindById_UserNotFound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			userService.findById(1L);
		});
	}


	@DisplayName("회원 이메일로 사용자 조회")
	@Test
	void findByEmail_Success() throws Exception {
		//given
		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(defaultUser));

		//when
		UserEntity foundUser = userService.findByEmail("test@gmail.com");

		//then
		assertThat(foundUser.getEmail()).isEqualTo("test@gmail.com");
	}

	@DisplayName("회원 이메일이 없을 때 예외")
	@Test
	public void testFindByEmail_UserNotFound() {
		when(userRepository.findByEmail("test2@gmail.com")).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> {
			userService.findByEmail("test2@gmail.com");
		});
	}




}