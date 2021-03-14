package com.bookfriend.services;

import com.bookfriend.model.Book;
import com.bookfriend.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.transaction.annotation.Isolation.READ_COMMITTED;

@Service("H2Service")
@Transactional
public class H2BookServiceImpl implements BookService {

  @Autowired
  private BookRepository repository;

  @Transactional(isolation = READ_COMMITTED)
  @Override
  public Book save(Book book) {
    return repository.save(book);
  }

  @Override
  public void delete(UUID id) {
    repository.deleteById(id);
  }

  @Override
  public Optional<Book> get(UUID id) {
    return repository.findById(id);
  }

  @Override
  public Page<Book> getAll(Integer page, Integer size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return repository.findAll(pageRequest);
  }
}
