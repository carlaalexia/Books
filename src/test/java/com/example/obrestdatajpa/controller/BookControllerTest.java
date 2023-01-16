package com.example.obrestdatajpa.controller;

import com.example.obrestdatajpa.entities.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//parametro webEnviroment para asignar un puerto aleatorio
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    private TestRestTemplate testRestTemplate; //nos permite lanzar peticiones http

    @Autowired //le dice que nos inyecte un builder. Lo utilizamos para generar el objeto anterior
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port; //agregamos el port como una variable. La inyecta el propio spring

    @BeforeEach
    void setUp() {
        restTemplateBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Test
    void findAll() {
        ResponseEntity<Book[]> response  =
                testRestTemplate.getForEntity("/api/books", Book[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getStatusCodeValue());

        List<Book> books = Arrays.asList(response.getBody());
        System.out.println(books.size());

    }

    @Test
    void findOneById() {

        ResponseEntity<Book> response  =
                testRestTemplate.getForEntity("/api/books/1", Book.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void create() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        //esto nos permite dcir que se va a enviar un json

        String json = """
                {
                    "title": "One Piece",
                    "author": "Eiichirou Oda",
                    "pages": 250,
                    "price": 29.99,
                    "date": "2018-12-01",
                    "online": true
                }
                """;

        HttpEntity<String> request = new HttpEntity<>(json,headers);

        ResponseEntity<Book> response = testRestTemplate.exchange("/api/books", HttpMethod.POST, request, Book.class);

        Book result = response.getBody();

        assertEquals(1L, result.getId());
        assertEquals("One Piece", result.getTitle());
    }

    @Test
    void delete() {
        ResponseEntity<Book> response = testRestTemplate.getForEntity("/api/books/1", Book.class);
        Book book = response.getBody();
        testRestTemplate.delete("/api/books/1", book, "/api/books/{id}");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void update() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String json = """
              {
                    "id": 2,
                    "title": "Harry piter",
                    "author": "JK Rowling",
                    "pages": 625,
                    "price": 13.5,
                    "releaseDate": "2002-02-10",
                    "online": true
               }
                """;
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<Book> response = testRestTemplate.exchange("/api/books", HttpMethod.PUT, request, Book.class);
        Book resultado = response.getBody();
        System.out.println(resultado.getId());
//        assertEquals(2l, resultado.getId());

//        assertEquals("Harry piter", resultado.getTitle());

    }

}