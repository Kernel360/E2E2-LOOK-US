DROP TABLE IF EXISTS userpostscrap;
DROP TABLE IF EXISTS userpostlike;
DROP TABLE IF EXISTS posthashtag;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS image;
DROP TABLE IF EXISTS follow;
DROP TABLE IF EXISTS hashtag;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS user;

create table user
(
    user_id         BIGINT(32)  NOT NULL AUTO_INCREMENT,
    email           VARCHAR(50) NOT NULL UNIQUE,
    password        VARCHAR(8)  NOT NULL,
    gender          INT         NOT NULL,
    birth           VARCHAR(50) NOT NULL,
    nickname        VARCHAR(20) NOT NULL UNIQUE,
    insta_id        VARCHAR(50) NOT NULL UNIQUE,
    role            INT         NOT NULL,
    profile_image   TEXT        NULL,
    user_status     INT         NOT NULL,
    user_created_at TIMESTAMP   NOT NULL,
    user_updated_at TIMESTAMP   NOT NULL,

    PRIMARY KEY (user_id)
);

create table post
(
    post_id         BIGINT(32)   NOT NULL AUTO_INCREMENT,
    user_id         BIGINT(32)   NOT NULL,
    post_content    VARCHAR(255) NULL,
    post_status     INT          NOT NULL,
    post_created_at TIMESTAMP    NOT NULL,
    user_updated_at TIMESTAMP    NOT NULL,
    like_count      INT          NOT NULL,
    FOREIGN KEY (user_id) references user (user_id),
    PRIMARY KEY (post_id)

);

create table comment
(
    comment_id         BIGINT(32) NOT NULL AUTO_INCREMENT,
    user_id            BIGINT(32) NOT NULL,
    post_id            BIGINT(32) NOT NULL,
    comment_content    TEXT       NOT NULL,
    comment_created_at TIMESTAMP  NOT NULL,
    comment_updated_at TIMESTAMP  NOT NULL,
    FOREIGN KEY (user_id) references user (user_id),
    FOREIGN KEY (post_id) references post (post_id),
    PRIMARY KEY (comment_id)
);

create table image
(
    image_id   BIGINT(32)   NOT NULL AUTO_INCREMENT,
    post_id    BIGINT(32)   NOT NULL,
    image_ling VARCHAR(255) NOT NULL,
    FOREIGN KEY (post_id) references post (post_id),
    PRIMARY KEY (image_id)
);

create table follow
(
    follow_id    BIGINT(32) NOT NULL AUTO_INCREMENT,
    follower_id  BIGINT(32) NOT NULL,
    following_id BIGINT(32) NOT NULL,
    FOREIGN KEY (follower_id) references user (user_id),
    FOREIGN KEY (following_id) references user (user_id),
    PRIMARY KEY (follow_id)
);

create table hashtag
(
    hashtag_id BIGINT(32)  NOT NULL AUTO_INCREMENT,
    hashtag    VARCHAR(30) NOT NULL,
    PRIMARY KEY (hashtag_id)
);

create table userpostscrap
(
    user_post_scrap_id BIGINT(32) NOT NULL AUTO_INCREMENT,
    user_id            BIGINT(32) NOT NULL,
    post_id            BIGINT(32) NOT NULL,
    FOREIGN KEY (user_id) references user (user_id),
    FOREIGN KEY (post_id) references post (post_id),
    PRIMARY KEY (user_post_scrap_id)

);

create table userpostlike
(
    user_post_like_id BIGINT(32) NOT NULL AUTO_INCREMENT,
    user_id           BIGINT(32) NOT NULL,
    post_id           BIGINT(32) NOT NULL,
    post_likes        BIGINT(32) NOT NULL,
    FOREIGN KEY (user_id) references user (user_id),
    FOREIGN KEY (post_id) references post (post_id),
    PRIMARY KEY (user_post_like_id)
);

create table posthashtag
(
    post_hashtag_id BIGINT(32) NOT NULL AUTO_INCREMENT,
    hashtag_id      BIGINT(32) NOT NULL,
    post_id         BIGINT(32) NOT NULL,
    FOREIGN KEY (hashtag_id) references hashtag (hashtag_id),
    FOREIGN KEY (post_id) references post (post_id),
    PRIMARY KEY (post_hashtag_id)
);