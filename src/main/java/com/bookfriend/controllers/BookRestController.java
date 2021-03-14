package com.bookfriend.controllers;

import com.bookfriend.model.Book;
import com.bookfriend.model.BookDto;
import com.bookfriend.services.BookService;
import com.bookfriend.utils.BookMapper;
import lombok.SneakyThrows;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books")
public class BookRestController {

  private static final int MAX_PAGE_SIZE = 500;
  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int ZERO_PAGE_NUM = 0;

  final
  BookService bookService;

  public BookRestController(@Qualifier("BookService") BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping
  public ResponseEntity<Book> createBook(@RequestBody BookDto bookDto) {
    Book book = BookMapper.convertToEntity(bookDto);
    Book storedBook = bookService.save(book);
    return new ResponseEntity<>(storedBook, HttpStatus.CREATED);
  }


  @PostMapping("/uploadEpub")
  public ResponseEntity<Void> submit(
      @RequestPart(required = true) MultipartFile file, String login, ServletRequest request) {
    String filePath = request.getServletContext().getRealPath("/");

    try {
      file.transferTo(new File(filePath));
      EpubReader epubReader = new EpubReader();
      nl.siegmann.epublib.domain.Book book = epubReader.readEpub(new FileInputStream(filePath));

      Book bookRequest = new Book();
      bookRequest.setAuthorNames(book.getMetadata().getAuthors().stream()
          .map(author -> author.getFirstname() + " " + author.getLastname()).collect(Collectors.toList()));
      bookRequest.setTitle(book.getTitle());
      bookRequest.setUserLogin(login);
      bookRequest.setFileUrl(filePath);
      bookRequest.setCreatedTime(Instant.now());
      bookService.save(bookRequest);
    } catch (IOException e) {
      return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Void>(HttpStatus.CREATED);
  }

  @PutMapping("/{bookId}")
  public ResponseEntity<Book> updateBook(@RequestBody BookDto book, @PathVariable UUID bookId) {
    Optional<Book> optionalBook = bookService.get(bookId);
    if (optionalBook.isPresent()) {
      Book oldBook = optionalBook.get();
      oldBook.setTitle(book.getTitle());
      oldBook.setCreatedTime(Instant.now());
      return new ResponseEntity<>(bookService.save(oldBook), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }


  @DeleteMapping("/{bookId}")
  public ResponseEntity<String> deleteBook(@PathVariable UUID bookId) {
    bookService.delete(bookId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{bookId}")
  public ResponseEntity<Book> getBookById(@PathVariable UUID bookId) {
    Optional<Book> optionalBook = bookService.get(bookId);
    if (optionalBook.isPresent()) {
      Book book = optionalBook.get();
      return new ResponseEntity<>(book, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @SneakyThrows
  @GetMapping("/{bookId}/read")
  public ResponseEntity<String> readBook(@PathVariable UUID bookId, int resourseId) {
    Optional<Book> optionalBook = bookService.get(bookId);
    if (optionalBook.isPresent()) {
      Book book = optionalBook.get();
      EpubReader epubReader = new EpubReader();
      nl.siegmann.epublib.domain.Book bookReader = epubReader.readEpub(new FileInputStream(book.getFileUrl()));
      Resource resource = bookReader.getContents().get(resourseId);
      return new ResponseEntity<>(new String(resource.getData()), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping
  public ResponseEntity<Page<Book>> getBooks(@RequestParam(required = false, defaultValue = "0") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer pageSize
  ) {
    if (pageSize > MAX_PAGE_SIZE) {
      pageSize = MAX_PAGE_SIZE;
    }
    Page<Book> books = bookService.getAll(page, pageSize);

    PageRequest pageRequest = PageRequest.of(books.getNumber(), books.getSize());
    books = new PageImpl<>(books.toList(), pageRequest, books.getSize());

    return new ResponseEntity<>(books, HttpStatus.OK);
  }
}