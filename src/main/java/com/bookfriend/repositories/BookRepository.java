package com.bookfriend.repositories;

import com.bookfriend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

  Book findOneById(UUID id);

  @Override
  List<Book> findAll();
}