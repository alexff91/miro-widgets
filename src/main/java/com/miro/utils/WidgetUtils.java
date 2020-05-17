package com.miro.utils;

import com.miro.model.Widget;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WidgetUtils {
    public static final String Z_INDEX_COLUMN = "zIndex";

    public static List<Widget> updateZIndex(Widget widget, List<Widget> widgets) {
        if (widgets.stream()
                .anyMatch(widgetFiltered -> widgetFiltered.getZIndex().equals(widget.getZIndex()))) {
            List<Widget> shiftedWidgets = widgets.stream()
                    .filter(w -> w.getZIndex() >= widget.getZIndex())
                    .collect(Collectors.toList());

            shiftedWidgets.forEach(w -> {
                w.setZIndex(w.getZIndex() + 1);
                w.setLastModificationTs(Instant.now());
            });
            return shiftedWidgets;
        } else {
            return Collections.emptyList();
        }
    }

    public static void checkNullZIndex(Widget widget, List<Widget> widgets) {
        if (widget.getZIndex() == null) {
            int computedZIndex = 0;
            Optional<Widget> max = widgets.stream().max(Comparator.comparingInt(Widget::getZIndex));
            if (max.isPresent()) {
                computedZIndex = max.get().getZIndex() + 1;
            }
            widget.setZIndex(computedZIndex);
        }
    }

    public static List<Widget> filter(Integer lowerLeftX, Integer lowerLeftY, Integer upperRightX, Integer upperRightY, Page<Widget> widgets) {
        return widgets.get()
                .filter(widget -> {
                    Rectangle filterArea = Rectangle.builder()
                            .x(lowerLeftX)
                            .y(lowerLeftY)
                            .height(upperRightY - lowerLeftY)
                            .width(upperRightX - lowerLeftX)
                            .build();
                    Rectangle widgetArea = Rectangle.builder()
                            .x(widget.getX())
                            .y(widget.getY())
                            .height(widget.getHeight())
                            .width(widget.getWidth())
                            .build();
                    return filterArea.contains(widgetArea);
                })
                .sorted(Comparator.comparingInt(Widget::getZIndex))
                .collect(Collectors.toList());
    }

    @Data
    @Builder
    public static class Rectangle {
        private int x;
        private int y;
        private int width;
        private int height;

        public boolean contains(Rectangle r) {
            return contains(r.x, r.y, r.width, r.height);
        }

        private boolean contains(int X, int Y, int W, int H) {
            int w = this.width;
            int h = this.height;
            if ((w | h | W | H) < 0) {
                return false;
            }
            int x = this.x;
            int y = this.y;
            if (X < x || Y < y) {
                return false;
            }
            w += x;
            W += X;
            if (W <= X) {
                if (w >= x || W > w) return false;
            } else {
                if (w >= x && W > w) return false;
            }
            h += y;
            H += Y;
            if (H <= Y) {
                return h < y && H <= h;
            } else {
                return h < y || H <= h;
            }
        }
    }
}