package com.backend.jobbit.dto.jobDto;

import com.backend.jobbit.dto.imageDto.ImageDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobWithImagesDto {

    private Long jobId;
    private List<ImageDto> jobImages;
}
