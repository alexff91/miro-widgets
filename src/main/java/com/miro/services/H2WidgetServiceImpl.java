package com.miro.services;

import com.miro.model.Widget;
import com.miro.repositories.WidgetRepository;
import com.miro.utils.WidgetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.miro.utils.WidgetUtils.Z_INDEX_COLUMN;
import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service("H2Service")
@Transactional
public class H2WidgetServiceImpl implements WidgetService {

    @Autowired
    private WidgetRepository repository;

    @Transactional(isolation = READ_COMMITTED)
    @Override
    public Widget save(Widget widget) {
        List<Widget> widgets = repository.findAll();
        WidgetUtils.checkNullZIndex(widget, widgets);
        List<Widget> shiftedWidgets = WidgetUtils.updateZIndex(widget, widgets);
        repository.saveAll(shiftedWidgets);
        return repository.save(widget);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Widget> get(Long id) {
        return repository.findById(id);
    }

    @Override
    public Page<Widget> getAll(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, Z_INDEX_COLUMN));
        return repository.findAll(pageRequest);
    }
}
