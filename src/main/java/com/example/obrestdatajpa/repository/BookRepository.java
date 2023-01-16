package com.example.obrestdatajpa.repository;

import com.example.obrestdatajpa.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//para que spring sepa reconocer
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
