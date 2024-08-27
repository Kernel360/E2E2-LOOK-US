package org.example.config.batch;

import org.example.post.repository.PostDailyStatsRepository;
import org.example.post.repository.PostRepository;
import org.example.post.repository.PostTotalStatsRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;
	private final PostRepository postRepository;
	private final PostDailyStatsRepository postDailyStatsRepository;
	private final PostTotalStatsRepository postTotalStatsRepository;

	public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager,
		PostRepository postRepository, PostDailyStatsRepository postDailyStatsRepository,
		PostTotalStatsRepository postTotalStatsRepository) {
		this.jobRepository = jobRepository;
		this.transactionManager = transactionManager;
		this.postRepository = postRepository;
		this.postDailyStatsRepository = postDailyStatsRepository;
		this.postTotalStatsRepository = postTotalStatsRepository;
	}

	@Bean
	public Job postStatsJob() {
		return new JobBuilder("postStatsJob", jobRepository)
			.start(postStatsStep())
			.build();
	}

	@Bean
	public Step postStatsStep() {
		return new StepBuilder("postStatsStep", jobRepository)
			.tasklet(updatePostStatsTasklet(), transactionManager)
			.build();
	}

	@Bean
	public UpdatePostStatsTasklet updatePostStatsTasklet() {
		return new UpdatePostStatsTasklet(postDailyStatsRepository, postTotalStatsRepository, postRepository);
	}

	@Bean
	public Step updatePostStatsStep() {
		return new StepBuilder("updatePostStatsStep", jobRepository)
			.tasklet(updatePostStatsTasklet(), transactionManager)
			.build();
	}

	@Bean
	public Job updatePostStatsJob() {
		return new JobBuilder("updatePostStatsJob", jobRepository)
			.start(updatePostStatsStep())
			.build();
	}

}
