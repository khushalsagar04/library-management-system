package com.example.demo.repository;

import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByName(String bookName);

    @Query("select b from Book b, Author a where b.book_author.id = a.id and a.name = ?1")
    List<Book> findByAuthorName(String authorName);

    List<Book> findByGenre(Genre genre);
}
