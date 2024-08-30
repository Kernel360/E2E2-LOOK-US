package org.example.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.example.post.domain.entity.CategoryEntity;
import org.example.post.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class CategoryServiceTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testInitializeCategoriesWhenRepositoryIsEmpty() {
		// Given
		when(categoryRepository.count()).thenReturn(0L); // Simulate an empty repository

		// When
		categoryService.initializeCategories();

		// Then
		List<String> categories = List.of(
			// 상의
			"맨투맨/스웨트", "셔츠/블라우스", "후드 티셔츠", "니트/스웨터", "피케/카라 티셔츠",
			"긴소매 티셔츠", "반소매 티셔츠", "민소매 티셔츠", "스포츠 상의", "기타 상의",

			// 바지
			"데님 팬츠", "코튼 팬츠", "슈트 팬츠/슬랙스", "트레이닝/조거 팬츠", "숏 팬츠",
			"레깅스", "점프 슈트/오버올", "스포츠 하의", "기타 바지",

			// 원피스/스커트
			"원피스/스커트", "미니원피스", "미디원피스", "맥시원피스", "미니스커트", "미디스커트", "롱스커트",

			// 신발
			"스니커즈", "구두", "샌들/슬리퍼", "부츠/워커", "스포츠화", "신발용품",

			// 가방
			"백팩", "메신저/크로스 백", "숄더백", "토트백", "에코백", "보스턴/더플백", "웨이스트 백",
			"파우치 백", "브리프 케이스", "캐리어", "가방 소품", "지갑/머니클립", "클러치 백",
			"모자", "양말/레그웨어", "선글라스/안경테", "액세서리", "시계", "주얼리"
		);
		ArgumentCaptor<CategoryEntity> captor = ArgumentCaptor.forClass(CategoryEntity.class);

		// Verify that save was called for each category
		verify(categoryRepository, times(categories.size())).save(captor.capture());

		List<CategoryEntity> capturedEntities = captor.getAllValues();

		for (int i = 0; i < categories.size(); i++) {
			assertEquals(categories.get(i), capturedEntities.get(i).getCategoryContent());
		}
	}

	@Test
	void testInitializeCategoriesWhenRepositoryIsNotEmpty() {
		// Given
		when(categoryRepository.count()).thenReturn(1L); // Simulate a non-empty repository

		// When
		categoryService.initializeCategories();

		// Then
		// Verify that save was never called because the repository already has entries
		verify(categoryRepository, never()).save(any(CategoryEntity.class));
	}
}
