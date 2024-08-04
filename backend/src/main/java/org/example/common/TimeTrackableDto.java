package org.example.common;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public abstract class TimeTrackableDto {

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
