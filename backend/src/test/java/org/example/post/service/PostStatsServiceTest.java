package org.example.post.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.example.post.domain.entity.PostDailyStats;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.entity.PostTotalStats;
import org.example.post.repository.PostDailyStatsRepository;
import org.example.post.repository.PostTotalStatsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PostStatsServiceTest {

	@Mock
	private PostDailyStatsRepository postDailyStatsRepository;

	@Mock
	private PostTotalStatsRepository postTotalStatsRepository;

	@InjectMocks
	private PostStatsService postStatsService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetDailyStatsByPost() {
		PostEntity post = mock(PostEntity.class);
		PostDailyStats dailyStats1 = new PostDailyStats();
		PostDailyStats dailyStats2 = new PostDailyStats();
		List<PostDailyStats> dailyStatsList = Arrays.asList(dailyStats1, dailyStats2);

		when(postDailyStatsRepository.findByPost(post)).thenReturn(dailyStatsList);

		List<PostDailyStats> result = postStatsService.getDailyStatsByPost(post);

		verify(postDailyStatsRepository).findByPost(post);
		assertEquals(dailyStatsList, result);
	}

	@Test
	void testGetTotalStatsByPost() {
		PostEntity post = mock(PostEntity.class);
		PostTotalStats totalStats1 = new PostTotalStats();
		PostTotalStats totalStats2 = new PostTotalStats();
		List<PostTotalStats> totalStatsList = Arrays.asList(totalStats1, totalStats2);

		when(postTotalStatsRepository.findByPost(post)).thenReturn(totalStatsList);

		List<PostTotalStats> result = postStatsService.getTotalStatsByPost(post);

		verify(postTotalStatsRepository).findByPost(post);
		assertEquals(totalStatsList, result);
	}
}
