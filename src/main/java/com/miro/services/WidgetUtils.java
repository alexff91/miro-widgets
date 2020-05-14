package com.miro.services;

import com.miro.model.Widget;

import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WidgetUtils {
    public static final String Z_INDEX_COLUMN = "zIndex";

    static List<Widget> updateZIndex(Widget widget, List<Widget> widgets) {
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

    static void checkNullZIndex(Widget widget, List<Widget> widgets) {
        if (widget.getZIndex() == null) {
            int computedZIndex = 0;
            Optional<Widget> max = widgets.stream().max(Comparator.comparingInt(Widget::getZIndex));
            if (max.isPresent()) {
                computedZIndex = max.get().getZIndex() + 1;
            }
            widget.setZIndex(computedZIndex);
        }
    }
}