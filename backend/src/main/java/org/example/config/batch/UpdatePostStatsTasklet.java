package org.example.config.batch;

import org.example.post.domain.entity.PostEntity;
import org.example.post.domain.entity.PostStats;
import org.example.post.repository.PostRepository;
import org.example.post.repository.PostStatsRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class UpdatePostStatsTasklet implements Tasklet {

	private final PostRepository postRepository;
	private final PostStatsRepository postStatsRepository;

	public UpdatePostStatsTasklet(PostRepository postRepository, PostStatsRepository postStatsRepository) {
		this.postRepository = postRepository;
		this.postStatsRepository = postStatsRepository;
	}

	@Override
	@Transactional
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		// 모든 게시글을 가져옴
		List<PostEntity> posts = postRepository.findAll();
		LocalDate now = LocalDate.from(LocalDateTime.now());

		// 각 게시글에 대해 통계를 업데이트
		for (PostEntity post : posts) {
			// 오늘의 조회수와 좋아요 수를 계산
			int todayHits = post.getTodayHits(); // 예: 하루 동안의 조회수 누적
			int todayLikes = post.getTodayLikes(); // 예: 하루 동안의 좋아요 수 누적

			PostStats postStats = new PostStats(
				post,
				post.getLikeCount(),
				post.getHits(),
				todayLikes,
				todayHits,
				now.atStartOfDay()
			);
			postStatsRepository.save(postStats);

			// 오늘의 조회수와 좋아요 수 초기화
			post.resetTodayStats();  // 이 메서드는 하루가 끝나면 오늘의 통계를 초기화하는 역할을 합니다.
		}

		return RepeatStatus.FINISHED;
	}
}
