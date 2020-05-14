package com.miro.repositories;

import com.miro.model.Widget;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WidgetRepository extends PagingAndSortingRepository<Widget, Long> {

    Widget findOneById(Long id);

    @Override
    List<Widget> findAll();
}