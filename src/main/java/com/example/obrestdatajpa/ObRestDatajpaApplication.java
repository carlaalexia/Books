package com.example.obrestdatajpa;

import com.example.obrestdatajpa.entities.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import com.example.obrestdatajpa.repository.BookRepository;

import java.time.LocalDate;


//siempre crear paquetes dentro de la carpeta que
//se crea para que Spring pueda leertodo
@SpringBootApplication
public class ObRestDatajpaApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ObRestDatajpaApplication.class, args);
		BookRepository repository =  (BookRepository) context.getBean(BookRepository.class);


		//CRUD

		//crear libro
		Book book1 = new Book(null, "Homo Deus", "Yuval Noah", 450, 29.99, LocalDate.of(2018, 12, 1), true);
		Book book2 = new Book(null, "Homo Sapiens", "Yuval Noah", 450, 19.99, LocalDate.of(2013, 12, 1), true);

		//almacecer un libro
		System.out.println("Num libros en base de datos: " + repository.findAll().size());

		repository.save(book1);
		repository.save(book2);

		// recuperar todos los libros
		System.out.println("Num libros en base de datos: " + repository.findAll().size());

		// borrar un libro
		//repository.deleteById(1L);

		System.out.println("Num libros en base de datos: " + repository.findAll().size());
	}

}
