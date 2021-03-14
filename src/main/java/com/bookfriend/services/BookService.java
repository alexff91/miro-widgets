package com.bookfriend.services;

import com.bookfriend.model.Book;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BookService {
  Book save(Book book);

  void delete(UUID id);

  Optional<Book> get(UUID id);

  Page<Book> getAll(Integer page, Integer size);
}
