package com.miro.services;

import com.miro.model.Widget;
import com.miro.repositories.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WidgetServiceImpl implements WidgetService {

    @Autowired
    private WidgetRepository repository;

    @Transactional()
    @Override
    public Widget save(Widget widget) {
        List<Widget> widgets = repository.findAll();
        checkNullZIndex(widget, widgets);
        checkZIndexCollision(widget, widgets);
        return repository.save(widget);
    }

    private void checkZIndexCollision(Widget widget, List<Widget> widgets) {
        if (widgets.stream()
                .anyMatch(widgetFiltered -> widgetFiltered.getZIndex().equals(widget.getZIndex()))) {
            List<Widget> higherWidgets = widgets.stream()
                    .filter(w -> w.getZIndex() >= widget.getZIndex())
                    .collect(Collectors.toList());

            higherWidgets.forEach(w -> {
                w.setZIndex(w.getZIndex() + 1);
            });
            repository.saveAll(higherWidgets);
        }
    }

    private void checkNullZIndex(Widget widget, List<Widget> widgets) {
        if (widget.getZIndex() == null) {
            int computedZIndex = 0;
            Optional<Widget> max = widgets.stream().max(Comparator.comparingInt(Widget::getZIndex));
            if (max.isPresent()) {
                computedZIndex = max.get().getZIndex() + 1;
            }
            widget.setZIndex(computedZIndex);
        }
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Widget> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<Widget> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "zIndex"));
        return repository.findAll(pageRequest);
    }
}
