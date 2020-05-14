package com.miro.utils;

import com.miro.model.Widget;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
}