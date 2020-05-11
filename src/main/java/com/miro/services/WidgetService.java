package com.miro.services;

import com.miro.model.Widget;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface WidgetService {
    Widget save(Widget widget);

    void delete(Integer id);

    Optional<Widget> get(Long id);

    Page<Widget> getAll(Integer page, Integer size);
}
