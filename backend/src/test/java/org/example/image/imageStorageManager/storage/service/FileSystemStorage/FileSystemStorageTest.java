/*
package org.example.image.imageStorageManager.storage.service.FileSystemStorage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.example.exception.common.ApiErrorCategory;
import org.example.exception.storage.ApiStorageErrorSubCategory;
import org.example.exception.storage.ApiStorageException;
import org.example.image.imageStorageManager.storage.service.core.StoragePacket;
import org.example.image.imageStorageManager.storage.service.core.StorageSaveResultInternal;
import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageLoadResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class FileSystemStorageTest {

	private FileSystemStorage storage;
	private Path mockRootLocation;

	@BeforeEach
	public void setUp() throws NoSuchFieldException, IllegalAccessException {
		// FileSystemStorage 객체 생성
		storage = mock(new FileSystemStorage());

		// rootLocation 필드 설정
		Field rootLocationField = FileSystemStorage.class.getDeclaredField("rootLocation");
		rootLocationField.setAccessible(true);
		// 실제 테스트를 위해 임의의 경로 설정
		Path testPath = Paths.get("data/images");
		rootLocationField.set(storage, testPath);

		// Path 객체 모킹
		mockRootLocation = Mockito.mock(Path.class);

		// 필요에 따라 mockRootLocation에 대한 동작을 정의
		when(mockRootLocation.resolve(anyString())).thenReturn(mockRootLocation);
		when(mockRootLocation.normalize()).thenReturn(mockRootLocation);
		when(mockRootLocation.toAbsolutePath()).thenReturn(mockRootLocation);
	}

	@Test
	public void test_save_non_empty_file() {
		// Arrange
		MultipartFile fileData = new MockMultipartFile("file", "test.txt", "text/plain", "file content".getBytes());
		StoragePacket packet = mock(StoragePacket.class);

		when(packet.isPayloadEmpty()).thenReturn(false);
		when(packet.getFileData()).thenReturn(fileData);
		when(packet.getDestinationPath()).thenReturn(Paths.get("testDir/test.txt"));

		Path destination = mock(Path.class);
		when(mockRootLocation.resolve(any(Path.class))).thenReturn(destination);
		when(destination.normalize()).thenReturn(destination);
		when(destination.toAbsolutePath()).thenReturn(destination);
		when(destination.getParent()).thenReturn(mockRootLocation);

		try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
			mockedFiles.when(() -> Files.copy((InputStream)any(), any(Path.class))).thenReturn(null);

			// Act
			StorageSaveResultInternal result = storage.save(packet);

			// Assert
			assertNotNull(result);
			assertEquals(StorageType.LOCAL_FILE_SYSTEM, result.storageType());
			verify(storage, times(1)).createDirectory(destination.getParent());
		}
	}

	@Test
	public void test_save_empty_file_throws_exception() {
		// Arrange
		StoragePacket packet = mock(StoragePacket.class);
		when(packet.isPayloadEmpty()).thenReturn(true);

		// Act & Assert
		ApiStorageException exception = assertThrows(ApiStorageException.class, () -> {
			storage.save(packet);
		});

		assertEquals(ApiErrorCategory.RESOURCE_INACCESSIBLE, exception.getErrorCategory());
		assertEquals(ApiStorageErrorSubCategory.FILE_IS_EMPTY, exception.getErrorSubCategory());
	}

	@Test
	public void test_load_existing_file() throws MalformedURLException {
		// Arrange
		String filePath = "testDir/test.txt";
		Path fullPath = mock(Path.class);

		when(mockRootLocation.resolve(filePath)).thenReturn(fullPath);
		when(fullPath.normalize()).thenReturn(fullPath);
		when(fullPath.toAbsolutePath()).thenReturn(fullPath);

		Resource resource = mock(Resource.class);
		when(resource.exists()).thenReturn(true);
		when(resource.isReadable()).thenReturn(true);
		// UrlResource 객체의 메서드를 직접 모킹합니다.
		try (MockedStatic<UrlResource> mockedUrlResource = mockStatic(UrlResource.class)) {
			mockedUrlResource.when(() -> new UrlResource(fullPath.toUri())).thenReturn(resource);

			// Act
			StorageLoadResult result = storage.load(filePath);

			// Assert
			assertNotNull(result);
			assertEquals(StorageType.LOCAL_FILE_SYSTEM, result.storageType());
			assertEquals(resource, result.resource());
		}
	}

	@Test
	public void test_load_non_existent_file_throws_exception() throws MalformedURLException {
		// Arrange
		String filePath = "nonexistent.txt";
		Path fullPath = mock(Path.class);

		when(mockRootLocation.resolve(filePath)).thenReturn(fullPath);
		when(fullPath.normalize()).thenReturn(fullPath);
		when(fullPath.toAbsolutePath()).thenReturn(fullPath);

		Resource resource = mock(Resource.class);
		when(resource.exists()).thenReturn(false);
		try (MockedStatic<UrlResource> mockedUrlResource = mockStatic(UrlResource.class)) {
			mockedUrlResource.when(() -> new UrlResource(fullPath.toUri())).thenReturn(resource);

			// Act & Assert
			ApiStorageException exception = assertThrows(ApiStorageException.class, () -> {
				storage.load(filePath);
			});

			assertEquals(ApiErrorCategory.RESOURCE_INACCESSIBLE, exception.getErrorCategory());
			assertEquals(ApiStorageErrorSubCategory.FILE_NOT_READABLE, exception.getErrorSubCategory());
		}
	}

	@Test
	public void test_load_file_with_malformed_url_throws_exception() throws MalformedURLException {
		// Arrange
		String filePath = "testDir/test.txt";
		Path fullPath = mock(Path.class);

		when(mockRootLocation.resolve(filePath)).thenReturn(fullPath);
		when(fullPath.normalize()).thenReturn(fullPath);
		when(fullPath.toAbsolutePath()).thenReturn(fullPath);

		try (MockedStatic<UrlResource> mockedUrlResource = mockStatic(UrlResource.class)) {
			mockedUrlResource.when(() -> new UrlResource(fullPath.toUri())).thenThrow(MalformedURLException.class);

			// Act & Assert
			ApiStorageException exception = assertThrows(ApiStorageException.class, () -> {
				storage.load(filePath);
			});

			assertEquals(ApiErrorCategory.RESOURCE_INACCESSIBLE, exception.getErrorCategory());
			assertEquals(ApiStorageErrorSubCategory.FILE_READ_IO_FAILURE, exception.getErrorSubCategory());
		}
	}

	@Test
	public void test_create_directory_success() throws IOException {
		// Arrange
		Path directoryPath = mock(Path.class);
		try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
			mockedFiles.when(() -> Files.createDirectories(directoryPath)).thenReturn(null);

			// Act
			storage.createDirectory(directoryPath);

			// Assert
			mockedFiles.verify(() -> Files.createDirectories(directoryPath));
		}
	}

	@Test
	public void test_create_directory_failure_throws_exception() throws IOException {
		// Arrange
		Path directoryPath = mock(Path.class);
		try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
			mockedFiles.when(() -> Files.createDirectories(directoryPath)).thenThrow(new IOException());

			// Act & Assert
			ApiStorageException exception = assertThrows(ApiStorageException.class, () -> {
				storage.createDirectory(directoryPath);
			});

			assertEquals(ApiErrorCategory.RESOURCE_INACCESSIBLE, exception.getErrorCategory());
			assertEquals(ApiStorageErrorSubCategory.DIRECTORY_NOT_ACCESSIBLE, exception.getErrorSubCategory());
		}
	}
	// Loading a file with an invalid path throws an ApiStorageException.
	@Test
	public void test_load_invalid_path_throws_exception() {
		// Arrange
		String filePath = "invalidPath/test.txt";
		Path fullPath = mock(Path.class);

		when(mockRootLocation.resolve(filePath)).thenReturn(fullPath);
		when(fullPath.normalize()).thenReturn(fullPath);
		when(fullPath.toAbsolutePath()).thenReturn(fullPath);

		Resource resource = mock(Resource.class);
	when(resource.exists()).thenReturn(false);

		// Act & Assert
		ApiStorageException exception = assertThrows(ApiStorageException.class, () -> {
			storage.load(filePath);
		});

		assertEquals(ApiErrorCategory.RESOURCE_INACCESSIBLE, exception.getErrorCategory());
		assertEquals(ApiStorageErrorSubCategory.FILE_NOT_FOUND, exception.getErrorSubCategory());
	}
}
*/
