package org.example.bookstore.repository.book;

import java.util.List;
import org.example.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book>,
        PagingAndSortingRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN FETCH b.categories c"
            + " WHERE c.id = :id AND b.isDeleted = false")
    List<Book> findAllByCategoriesId(Long id);
}
