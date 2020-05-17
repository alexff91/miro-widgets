package com.miro.utils;

import com.miro.model.Widget;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.miro.utils.WidgetUtils.Z_INDEX_COLUMN;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WidgetUtilsTest {

    @Test
    void testUpdateZIndexShiftWidgets() {
        Widget w = Widget.builder().zIndex(0).build();
        Widget widgetWithZeroIndex = Widget.builder().zIndex(0).build();
        Widget widgetWithOneIndex = Widget.builder().zIndex(1).build();
        ArrayList<Widget> widgets = new ArrayList<>();
        widgets.add(widgetWithZeroIndex);
        widgets.add(widgetWithOneIndex);
        WidgetUtils.updateZIndex(w, widgets);
        assertEquals(w.getZIndex(), 0);
        assertEquals(widgetWithZeroIndex.getZIndex(), 1);
        assertEquals(widgetWithOneIndex.getZIndex(), 2);
    }

    @Test
    void testUpdateZIndexUnchangedWidgets() {
        Widget w = Widget.builder().zIndex(3).build();
        Widget widgetWithZeroIndex = Widget.builder().zIndex(0).build();
        Widget widgetWithOneIndex = Widget.builder().zIndex(1).build();
        ArrayList<Widget> widgets = new ArrayList<>();
        widgets.add(widgetWithZeroIndex);
        widgets.add(widgetWithOneIndex);
        WidgetUtils.updateZIndex(w, widgets);
        assertEquals(w.getZIndex(), 3);
        assertEquals(widgetWithZeroIndex.getZIndex(), 0);
        assertEquals(widgetWithOneIndex.getZIndex(), 1);
    }

    @Test
    void testCheckNullZIndexForFilledWidgetsList() {
        Widget w = Widget.builder().zIndex(null).build();
        Widget widgetWithZeroIndex = Widget.builder().zIndex(0).build();
        Widget widgetWithOneIndex = Widget.builder().zIndex(1).build();
        ArrayList<Widget> widgets = new ArrayList<>();
        widgets.add(widgetWithZeroIndex);
        widgets.add(widgetWithOneIndex);
        WidgetUtils.checkNullZIndex(w, widgets);
        assertEquals(w.getZIndex(), 2);
        assertEquals(widgetWithZeroIndex.getZIndex(), 0);
        assertEquals(widgetWithOneIndex.getZIndex(), 1);
    }

    @Test
    void testCheckNullZIndexForEmptyWidgetsList() {
        Widget w = Widget.builder().zIndex(null).build();
        ArrayList<Widget> widgets = new ArrayList<>();
        WidgetUtils.checkNullZIndex(w, widgets);
        assertEquals(w.getZIndex(), 0);
    }

    @Test
    void testSelectedAreaContainsAllWidgets() {
        Widget w1 = Widget.builder().zIndex(0).x(0).y(0).height(1).width(1).build();
        Widget w2 = Widget.builder().zIndex(1).x(1).y(1).height(3).width(1).build();
        Widget w3 = Widget.builder().zIndex(2).x(3).y(0).height(1).width(2).build();

        List<Widget> widgets = Arrays.asList(w1, w2, w3);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, Z_INDEX_COLUMN));
        PageImpl<Widget> widgetPage = new PageImpl<>(widgets, pageRequest, widgets.size());

        List<Widget> filteredWidgets = WidgetUtils.filter(0, 0, 10, 10, widgetPage);

        assertArrayEquals(widgets.toArray(), filteredWidgets.toArray());
    }

    @Test
    void testSelectedAreaNotContainsAllWidgets() {
        Widget w1 = Widget.builder().zIndex(0).x(0).y(0).height(1).width(1).build();
        Widget w2 = Widget.builder().zIndex(1).x(1).y(1).height(3).width(1).build();
        Widget w3 = Widget.builder().zIndex(2).x(3).y(0).height(1).width(2).build();

        List<Widget> widgets = Arrays.asList(w1, w2, w3);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, Z_INDEX_COLUMN));
        PageImpl<Widget> widgetPage = new PageImpl<>(widgets, pageRequest, widgets.size());

        List<Widget> filteredWidgets = WidgetUtils.filter(0, 0, 1, 1, widgetPage);

        assertEquals(1, filteredWidgets.size());
    }
}