package com.miro.model;

import lombok.*;

import javax.persistence.Entity;

@Builder
@Data
public class WidgetDto {
    Integer zIndex;

    Integer x;

    Integer y;

    Integer width;

    Integer height;
}
