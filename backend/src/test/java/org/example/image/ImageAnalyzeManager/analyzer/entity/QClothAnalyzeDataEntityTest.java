package org.example.image.ImageAnalyzeManager.analyzer.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.core.types.dsl.StringPath;

class QClothAnalyzeDataEntityTest {
	@Test
	public void test_correct_mapping_to_cloth_analyze_data_entity() {
		QClothAnalyzeDataEntity qEntity = QClothAnalyzeDataEntity.clothAnalyzeDataEntity;

		assertEquals("clothAnalyzeDataEntity", qEntity.getMetadata().getName());
		assertEquals(ClothAnalyzeDataEntity.class, qEntity.getType());
		assertEquals(ListPath.class, qEntity.boundingBox.getClass());
		assertEquals(StringPath.class, qEntity.clothName.getClass());
		assertEquals(EnumPath.class, qEntity.clothType.getClass());
		assertEquals(NumberPath.class, qEntity.imageAnalyzeDataId.getClass());
		assertEquals(NumberPath.class, qEntity.imageLocationId.getClass());
		assertEquals(SimplePath.class, qEntity.labColor.getClass());
		assertEquals(SimplePath.class, qEntity.rgbColor.getClass());
	}

	@Test
	public void initializes_with_valid_pathmetadata() {
		PathMetadata metadata = PathMetadataFactory.forVariable("test");
		QClothAnalyzeDataEntity entity = new QClothAnalyzeDataEntity(metadata);
		assertNotNull(entity);
	}
	@Test
	public void test_constructor_with_path() {
		// Given: Path 객체를 생성
		PathBuilder<ClothAnalyzeDataEntity> pathBuilder = new PathBuilder<>(ClothAnalyzeDataEntity.class, "clothAnalyzeDataEntityPath");

		// When: 해당 Path를 사용해 QClothAnalyzeDataEntity 생성
		QClothAnalyzeDataEntity qEntity = new QClothAnalyzeDataEntity(pathBuilder);

		// Then: 생성된 qEntity의 타입과 메타데이터를 확인
		assertNotNull(qEntity);  // qEntity가 null이 아님을 확인
		assertEquals(ClothAnalyzeDataEntity.class, qEntity.getType());  // 타입이 올바른지 확인
		assertEquals(pathBuilder.getMetadata(), qEntity.getMetadata());  // 메타데이터가 올바른지 확인
	}

}