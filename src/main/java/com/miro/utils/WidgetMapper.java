package com.miro.utils;

import com.miro.model.Widget;
import com.miro.model.WidgetDto;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class WidgetMapper {

    @NotNull
    public static Widget convertToEntity(WidgetDto dto) {
        return Widget.builder().height(dto.getHeight())
                .width(dto.getWidth())
                .lastModificationTs(Instant.now())
                .x(dto.getX())
                .y(dto.getY())
                .zIndex(dto.getZIndex())
                .build();
    }
}
