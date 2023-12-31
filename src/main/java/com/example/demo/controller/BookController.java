package com.example.demo.controller;

import com.example.demo.dto.CreateAdminRequest;
import com.example.demo.dto.CreateBookRequest;
import com.example.demo.dto.SearchBookRequest;
import com.example.demo.models.Book;
import com.example.demo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping("/book")
    public void createBook(@RequestBody @Valid CreateBookRequest createBookRequest){
        bookService.createBook(createBookRequest.toBook());
    }

    @GetMapping("/getBooks")
    public List<Book> getBooks(@RequestBody @Valid SearchBookRequest searchBookRequest) throws Exception {
        List<Book> list = bookService.findBook(searchBookRequest.getSearchKey(), searchBookRequest.getSearchValue());
        return list;
    }


}
