package org.example.image.imageStorageManager;

import java.io.IOException;

import org.example.image.imageStorageManager.storage.service.core.StorageType;
import org.example.image.imageStorageManager.type.StorageLoadResult;
import org.example.image.imageStorageManager.type.StorageSaveResult;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageManager {

	/**
	 * <p>지정된 {@link StorageType} 에 {@link MultipartFile}을 저장 합니다.</p>
	 * <p>현재는 LocalFileSystem 만 구현 되었습니다.</p>
	 *
	 * @param resource 스토리지에 저장할 리소스 파일 입니다.
	 * @param storageType 어떤 스토리지에 저장 할지 결정 합니다.
	 * @return {@link StorageSaveResult StorageSaveResult} 리소스 저장 결과를 담은 레코드 입니다.
	 * @throws org.example.exception.storage.ApiStorageException 저장 실패시 런타임 예외가 발생 합니다.
	 *
	 * @see StorageType
	 */
	StorageSaveResult saveImage(MultipartFile resource, StorageType storageType) throws IOException;

	/**
	 * <p> 리소스 식별자를 통해서 {@link MultipartFile}을 획득 합니다.</p>
	 * <p> {@link StorageLoadResult}를 참고 해주세요.</p>
	 *
	 * @param imageLocationId 저장된 리소스 파일에 대한 식별자 입니다.
	 * @return {@link StorageLoadResult StorageFindResult} 리소스 검색 결과를 담은 레코드 입니다.
	 * @throws org.example.exception.storage.ApiStorageException 파일 탐색에 실패할 경우 런타임 예외를 발생시킵니다.
	 */
	StorageLoadResult loadImageByLocationId(Long imageLocationId);

}
