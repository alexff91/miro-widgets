package com.miro.services;

import com.miro.model.Widget;
import com.miro.utils.WidgetUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.miro.utils.WidgetUtils.Z_INDEX_COLUMN;

@Service("InMemory")
public class InMemoryWidgetServiceImpl implements WidgetService {

    ConcurrentHashMap<Long, Widget> widgetsStore = new ConcurrentHashMap<>();

    @Override
    public Widget save(Widget widget) {
        List<Widget> widgets = Collections.list(widgetsStore.elements());
        WidgetUtils.checkNullZIndex(widget, widgets);
        List<Widget> shiftedWidgets = WidgetUtils.updateZIndex(widget, widgets);
        long computedId;
        if (widgets.size() == 0) {
            computedId = 0L;
        } else {
            computedId = widgets.stream().max(Comparator.comparingInt(o -> o.getId().intValue())).get().getId() + 1L;
        }
        widget.setId(computedId);
        widgetsStore.put(computedId, widget);
        return widget;
    }

    @Override
    public void delete(Long id) {
        widgetsStore.remove(id);
    }

    @Override
    public Optional<Widget> get(Long id) {
        return Optional.ofNullable(widgetsStore.get(id));
    }

    @Override
    public Page<Widget> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, Z_INDEX_COLUMN));
        return new PageImpl<>(Collections.list(widgetsStore.elements()).stream()
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toCollection(ArrayList::new)), pageRequest, widgetsStore.size());
    }
}
