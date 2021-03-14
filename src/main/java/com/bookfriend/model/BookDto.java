package com.bookfriend.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Builder
@Data
public class BookDto {
  String title;

  List<String> authorNames;

  Instant createdTime;
}
