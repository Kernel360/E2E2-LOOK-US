// package org.example.post.domain.entity;
//
// import org.example.user.domain.entity.member.UserEntity;
//
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.AccessLevel;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
//
// @Entity
// @Table(name = "user_post_likes")
// @Getter
// @Setter
// @NoArgsConstructor(access = AccessLevel.PROTECTED)
// @AllArgsConstructor
// public class UserPostLikesEntity {
//
// 	@Id
// 	@GeneratedValue(strategy = GenerationType.IDENTITY)
// 	@Column(name = "user_post_likes_id")
// 	private Long userPostLikesId;
//
// 	@ManyToOne(fetch = FetchType.LAZY)
// 	@JoinColumn(name = "user_id")
// 	private UserEntity user;
//
// 	@ManyToOne(fetch = FetchType.LAZY)
// 	@JoinColumn(name = "post_id")
// 	private PostEntity post;
// }
