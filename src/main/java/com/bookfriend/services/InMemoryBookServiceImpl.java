package com.bookfriend.services;

import com.bookfriend.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service("InMemory")
public class InMemoryBookServiceImpl implements BookService {

  ConcurrentHashMap<UUID, Book> booksStore = new ConcurrentHashMap<>();

  @Override
  public Book save(Book book) {
    UUID id = UUID.randomUUID();
    book.setId(id);
    booksStore.put(id, book);
    return book;
  }

  @Override
  public void delete(UUID id) {
    booksStore.remove(id);
  }

  @Override
  public Optional<Book> get(UUID id) {
    return Optional.ofNullable(booksStore.get(id));
  }

  @Override
  public Page<Book> getAll(Integer page, Integer size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return new PageImpl<>(Collections.list(booksStore.elements()).stream()
        .skip((long) page * size)
        .limit(size)
        .sorted(Comparator.comparing(Book::getCreatedTime))
        .collect(Collectors.toCollection(ArrayList::new)), pageRequest, booksStore.size());
  }
}
