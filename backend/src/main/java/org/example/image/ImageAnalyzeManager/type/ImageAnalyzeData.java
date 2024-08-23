package org.example.image.ImageAnalyzeManager.type;

import java.util.List;
import org.example.image.ImageAnalyzeManager.analyzer.type.ClothAnalyzeData;
import lombok.Builder;

@Builder
public record ImageAnalyzeData(
	// NOTE: 현재는 단일 이미지 내 인물이 입고 있는 [의류들] 에 대한 분석 정보가 제공 됩니다.
	//       Image 1개 --> List<사진 속 인물이 입고 있는 옷들>
	List<ClothAnalyzeData> clothAnalyzeDataList
) {}


