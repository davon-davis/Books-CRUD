package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.servlet.http.Cookie;
import javax.transaction.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    BookRepository repository;

    @Test
    @Transactional
    @Rollback
    public void testGetAllBooks() throws Exception{
        Book book = new Book();
        book.setName("Harry Potter");
        book.setPublishDate(new Date());

        repository.save(book);

        MockHttpServletRequestBuilder request = get("/books")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name", is("Harry Potter")));
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateBook() throws Exception{
        MockHttpServletRequestBuilder request = post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"The Raven\",\"publishDate\":\"1999-11-30\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("The Raven")));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetABook() throws Exception{
        Book book = new Book();
        book.setName("Alexander Hamilton");
        book.setPublishDate(new Date());

        Book book2 = new Book();
        book2.setName("To Kill A Mockingbird");
        book2.setPublishDate(new Date());

        repository.save(book);
        repository.save(book2);

        MockHttpServletRequestBuilder request = get("/books/2")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("To Kill A Mockingbird")));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteABook() throws Exception{
        Book book = new Book();
        book.setName("Clifford");
        book.setPublishDate(new Date());

        Book book2 = new Book();
        book2.setName("Percy Jackson");
        book2.setPublishDate(new Date());

        repository.save(book);
        repository.save(book2);

        MockHttpServletRequestBuilder request = delete("/books/1")
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("A book was deleted. Books remaining: 1"));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateABook() throws Exception{
        Book book = new Book();
        book.setName("Clifford");
        book.setPublishDate(new Date());

        repository.save(book);

        MockHttpServletRequestBuilder request = patch("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Cat in the Hat\"}");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cat in the Hat")));
    }
}
