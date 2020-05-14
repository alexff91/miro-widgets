package com.miro.services;

import com.miro.model.Widget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.miro.services.WidgetUtils.Z_INDEX_COLUMN;

@Service
public class InMemoryWidgetServiceImpl implements WidgetService {

    ConcurrentHashMap<Long, Widget> widgetsStore = new ConcurrentHashMap<>();

    @Override
    public Widget save(Widget widget) {
        List<Widget> widgets = Collections.list(widgetsStore.elements());
        WidgetUtils.checkNullZIndex(widget, widgets);
        List<Widget> shiftedWidgets = WidgetUtils.updateZIndex(widget, widgets);
        Long computedId;
        if (widgets.size() == 0) {
            computedId = 0L;
        } else {
            computedId = widgets.stream().max(Comparator.comparingInt(o -> o.getId().intValue())).get().getId();
        }
        widget.setId(computedId);
        return widgetsStore.put(computedId, widget);
    }

    @Override
    public void delete(Long id) {
        widgetsStore.remove(id);
    }

    @Override
    public Optional<Widget> get(Long id) {
        return Optional.of(widgetsStore.get(id));
    }

    @Override
    public Page<Widget> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, Z_INDEX_COLUMN));
        return new PageImpl<>(Collections.list(widgetsStore.elements()), pageRequest, widgetsStore.size());
    }
}
