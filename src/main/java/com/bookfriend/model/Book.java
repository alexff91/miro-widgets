package com.bookfriend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book {
  private static final String SEQUENCE_NAME = "books_id_seq";

  @Id
  @GeneratedValue(generator = SEQUENCE_NAME, strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
  @Column(name = "id", nullable = false)
  UUID id;

  String title;

  @ElementCollection
  List<String> authorNames;

  String fileUrl;

  String userLogin;

  Instant createdTime;
}
