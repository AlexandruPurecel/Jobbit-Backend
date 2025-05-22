package com.backend.jobbit.dto.imageDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Long id;
    private String name;
    private String type;
    private byte[] imageData;
}
