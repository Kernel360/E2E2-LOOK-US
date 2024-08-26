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

		// 각 게시글에 대해 통계를 업데이트
		for (PostEntity post : posts) {
			PostStats postStats = new PostStats(
				post,
				post.getLikeCount(),
				post.getHits(),
				LocalDateTime.now()
			);
			postStatsRepository.save(postStats);
		}

		return RepeatStatus.FINISHED;
	}
}