package com.miro.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WidgetDto {
    Integer zIndex;

    Integer x;

    Integer y;

    Integer width;

    Integer height;
}
