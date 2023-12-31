package com.example.demo.dto;

import com.example.demo.models.Author;
import com.example.demo.models.Book;
import com.example.demo.models.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String authorName;
    @NotBlank
    private String authorEmail;
    @NotNull
    private Genre genre;

    public Book toBook(){
        Author author = Author.builder()
                        .name(authorName)
                        .email(authorEmail)
                        .build();

        return Book.builder()
                .name(this.name)
                .book_author(author)
                .genre(this.genre)
                .build();
    }
}
