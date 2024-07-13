package org.example.bookstore;

import java.math.BigDecimal;
import org.example.bookstore.model.Book;
import org.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book1 = new Book("Book1", "Author1", "ISBN1", new BigDecimal(100),
                    "Description1", "Image1");
            Book book2 = new Book("Book2", "Author2", "ISBN2", new BigDecimal(200),
                    "Description2", "Image2");
            bookService.save(book1);
            bookService.save(book2);
            System.out.println(bookService.findAll());
        };
    }
}
