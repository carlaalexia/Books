package com.example.obrestdatajpa.controller;

import com.example.obrestdatajpa.entities.Book;
import com.example.obrestdatajpa.repository.BookRepository;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


//Gestiona peticiones HTTP
@RestController
public class BookController {

    private final Logger log = LoggerFactory.getLogger(BookController.class);
    //este bean para ser creado necesita un bookrepository y lo inyecta
    //Atributos
    private BookRepository bookRepository;

    //construciores

    public BookController(BookRepository bookRepository) {

        this.bookRepository = bookRepository;
    }



//CRUD sobre la entidad book

    // buscar todos los libros (devuelvo lista de libros)

    @GetMapping("/api/books")
    public List<Book> findAll(){
        // recuperar y devolver los libros

        return bookRepository.findAll();

    }

    // buscar un solo libro en en case de datos segun id

    // se pone las llaves para buscar por id

    //pathVariable --> vincula el parametro con el parametro de url
    @GetMapping("/api/books/{id}")
    public ResponseEntity<Book> findOneById(@Parameter(description = "hola") @PathVariable Long id){

     Optional <Book> bookOp = bookRepository.findById(id);
     // devuelve un objeto opcional. Permite envolver un libro

        if(bookOp.isPresent())
            return ResponseEntity.ok(bookOp.get());
        else
            return ResponseEntity.notFound().build(); // 404

       // return bookOp.orElse(null);
    }

    // crear un nuevo libro en base de datos


    //RequestBody --> extrae la info de la peticion
    @PostMapping("/api/books")
    public ResponseEntity<Book> create(@RequestBody Book book, @RequestHeader HttpHeaders headers){
        System.out.println(headers.get("User-Agent"));
        // guardar el libro recibido por parámetro en la base de datos
        if(book.getId() != null){ // quiere decir que existe el id y por tanto no es una creación
            log.warn("trying to create a book with id");
            return ResponseEntity.badRequest().build();
        }
        Book result = bookRepository.save(book);
        return ResponseEntity.ok(result); // el libro devuelto tiene una clave primaria
    }


    // actualizar un libro existente en base de datos

    @PutMapping("/api/books")
    public ResponseEntity<Book> update(@RequestBody Book book){
        if(book.getId() == null ){ // si no tiene id quiere decir que sí es una creación
            log.warn("Trying to update a non existent book");
            return ResponseEntity.badRequest().build();
        }
        if(!bookRepository.existsById(book.getId())){ //verificar si existe en la base de datos
            log.warn("Trying to update a non existent book");
            return ResponseEntity.notFound().build();
        }

        // El proceso de actualización
        Book result = bookRepository.save(book);
        return ResponseEntity.ok(result); // el libro devuelto tiene una clave primaria
    }


    // borrar un libro en base de datos

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<Book> delete(@PathVariable Long id){

        if(!bookRepository.existsById(id)){
            log.warn("Trying to delete a non existent book");
            return ResponseEntity.notFound().build();
        }

        bookRepository.deleteById(id);

        return ResponseEntity.noContent().build(); //respuesta que se ha borrado
    }

    // borrar todos los libros de la base de datos


    @DeleteMapping("/api/books")
    public ResponseEntity<Book> deleteAll(){
        log.info("REST Request for delete all books");
        bookRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }


}
