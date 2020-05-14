package com.miro.repositories;

import com.miro.model.Widget;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WidgetRepository extends PagingAndSortingRepository<Widget, Long> {
    Widget findOneById(Long id);

    Optional<Widget> findById(Long id);

    List<Widget> findAll();
}