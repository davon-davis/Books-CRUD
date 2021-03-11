package com.example.demo;


import org.springframework.data.web.JsonPath;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    final private BookRepository repository;

    public BookController(BookRepository repository) {
        this.repository = repository;
    }

    @GetMapping("")
    public Iterable<Book> getAll(){
        return this.repository.findAll();
    }

    @PostMapping("")
    public Book createBook(@RequestBody Book book){
        return this.repository.save(book);
    }

    @GetMapping("/{id}")
    public Object getBook(@PathVariable Long id){
        return this.repository.existsById(id) ?
                this.repository.findById(id) :
                "This book does not exist";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id){
        this.repository.deleteById(id);
        return "A book was deleted. Books remaining: " + this.repository.count();
    }

    @PatchMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id){
        if (this.repository.existsById(id)){
            Book oldBook = this.repository.findById(id).get();
            oldBook.setName(book.getName());
            oldBook.setPublishDate((book.getPublishDate()));
            return this.repository.save(oldBook);
        }else{
            return this.repository.save(book);
        }
    }
}
