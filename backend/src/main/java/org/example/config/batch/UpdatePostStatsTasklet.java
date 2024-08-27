package org.example.config.batch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.example.post.domain.entity.PostDailyStats;
import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.entity.PostTotalStats;
import org.example.post.repository.PostDailyStatsRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.PostTotalStatsRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdatePostStatsTasklet implements Tasklet {

	private final PostDailyStatsRepository postDailyStatsRepository;
	private final PostTotalStatsRepository postTotalStatsRepository;
	private final PostRepository postRepository;

	@Override
	@Transactional
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		// 모든 게시글을 가져옴
		List<PostEntity> posts = postRepository.findAll();
		LocalDate now = LocalDate.from(LocalDateTime.now());

		// 각 게시글에 대해 통계를 업데이트
		for (PostEntity post : posts) {

			PostTotalStats postTotalStats = new PostTotalStats(
				post,
				post.getLikeCount(),
				post.getHits(),
				now.atStartOfDay()
			);

			PostDailyStats postDailyStats = new PostDailyStats(
				post,
				post.getTodayLikes(),
				post.getTodayHits(),
				now.atStartOfDay()
			);

			postTotalStatsRepository.save(postTotalStats);
			postDailyStatsRepository.save(postDailyStats);

			// 오늘의 조회수와 좋아요 수 초기화
			post.resetTodayStats();  // 이 메서드는 하루가 끝나면 오늘의 통계를 초기화하는 역할을 합니다.
		}

		return RepeatStatus.FINISHED;
	}
}
