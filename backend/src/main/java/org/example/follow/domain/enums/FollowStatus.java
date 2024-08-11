package org.example.follow.domain.enums;

public enum FollowStatus {
	FOLLOW(1),
	UNFOLLOW(0);

	private final int status;

	FollowStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public static FollowStatus fromStatus(int status) {
		for (FollowStatus followStatus : FollowStatus.values()) {
			if (followStatus.getStatus() == status) {
				return followStatus;
			}
		}
		throw new IllegalArgumentException("Invalid status: " + status);
	}
}