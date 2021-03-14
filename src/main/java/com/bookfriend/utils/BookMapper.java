package com.bookfriend.utils;

import com.bookfriend.model.Book;
import com.bookfriend.model.BookDto;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class BookMapper {

  @NotNull
  public static Book convertToEntity(BookDto dto) {
    return Book.builder()
        .createdTime(Instant.now())
        .authorNames(dto.getAuthorNames())
        .title(dto.getTitle())
        .build();
  }
}
